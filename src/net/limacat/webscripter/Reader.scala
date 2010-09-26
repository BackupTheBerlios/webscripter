package net.limacat.webscripter
import java.util.List
import java.util.LinkedList
import java.util.NoSuchElementException

abstract class Mapper[T] {

  protected def getValueFrom(line: String): String = {
    if (line.indexOf(";") > -1) {
      line.substring(0, line.indexOf(";"))
    } else {
      line
    }
  }

  def readFrom(field: String): T

  def isBlank(read: T): Boolean = {
    read == null;
  }

}

object StringMapper extends Mapper[String] {

  def readFrom(field: String): String = {
    getValueFrom(field).trim()
  }

  override def isBlank(read: String): Boolean = {
    read == null || "".equals(read.trim());
  }

}

case object CategoriaMapper extends MadMapper[Categoria](Map("C" -> CASUAL, "H" -> HARDCORE, "P" -> PSN, "U" -> SCONOSCIUTO, "Z" -> SHOVELWARE))
case object ControllerMapper extends MadMapper[Controller](Map("1" -> C1, "2" -> C2, "N" -> C1N, "U" -> CNA))
case object SupportoMapper extends MadMapper[Supporto](Map("C" -> SC, "P" -> SP, "F" -> SF, "U" -> SU))

class MadMapper[T >: Null <: AnyRef](collection : Map[String, T]) extends Mapper[T] {

  def readFrom(field: String): T = {
    try {
      collection(getValueFrom(field).trim)
    } catch {
      case exc: NoSuchElementException => null
    }
  }
	
}

case object MetacriticLinkMapper extends LinkMapper("metacritic");
case object TrailerLinkMapper extends LinkMapper("trailer");

class LinkMapper(val linkName: String) extends Mapper[Link] {

  def readFrom(field: String): Link = {
    new Link(linkName, getValueFrom(field).trim())
  }

}

object ArrayLinkMapper extends Mapper[Array[Link]] {

  def readNextLink(index: Int, field: String): scala.List[Link] = {
    var tail: scala.List[Link] = Nil
    var xB: String = field;
    if (field.indexOf(",") > -1) {
      val subString = field.substring(field.indexOf(",") + 1)
      tail = readNextLink(index + 1, subString)
      xB = field.substring(0, field.indexOf(","))
    }
    xB = xB.trim
    if (xB.length == 0) {
      Nil
    } else {
      new Link(index.toString(), xB.trim()) :: tail;
    }
  }

  def mySubString(theField: String): String = {
    var myField = theField
    if (myField.indexOf(";") > -1) {
      myField = myField.substring(0, myField.indexOf(";"));
    }
    myField
  }

  def readFrom(field: String): Array[Link] = {
    val x: scala.List[Link] = readNextLink(1, mySubString(field));
    val size = x.length;
    val y: Array[Link] = new Array[Link](size);
    for (i <- 0 until size) {
      y(i) = x(i)
    }
    y
  }

}

class GameLineReader() {

  private def readField[T](line: String, mapper: Mapper[T]): (T, String) = {
    readField(line, mapper, "", null);
  }

  private def getNext(line: String): String = {
    if (line.indexOf(";") > -1) {
      line.substring(line.indexOf(";") + 1);
    } else {
      line;
    }

  }

  private def readField[T](line: String, mapper: Mapper[T], name: String, fieldInErrors: List[String]): (T, String) = {
    var next = getNext(line);
    var value = mapper.readFrom(line);
    if (fieldInErrors != null && mapper.isBlank(value)) {
      fieldInErrors.add(name);
    }
    (value, next);
  }

  def readLine(line: String): Game = {
    val fieldInErrors: List[String] = new LinkedList[String]
    val title = readField(line, StringMapper, "title", fieldInErrors)
    val cathegory = readField(title._2, CategoriaMapper, "cathegory", fieldInErrors)
    val exit = readField(cathegory._2, StringMapper, "exitDate", fieldInErrors)
    val controller = readField(exit._2, ControllerMapper, "controller", fieldInErrors)
    val supporto = readField(controller._2, SupportoMapper, "supporto", fieldInErrors)
    val metacritic = readField(supporto._2, MetacriticLinkMapper);
    val trailer = readField(metacritic._2, TrailerLinkMapper);
    val links = readField(trailer._2, ArrayLinkMapper);
    val note = readField(links._2, StringMapper);
    if (fieldInErrors.size > 0) {
      throw new FormatException(fieldInErrors);
    }
    new Game(title._1, cathegory._1, exit._1, controller._1, supporto._1, metacritic._1, trailer._1, links._1, note._1)
  }

}

class FormatException(val fieldsInError: List[String]) extends RuntimeException() {

}
