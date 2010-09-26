/*
	Webscripter - a simple html creator
    Copyright (C) 2010 Davide Inglima

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

	Contact: limacat@gmail.com
 */
package net.limacat.webscripter
import scala.collection.immutable.TreeSet

sealed case class Categoria(val cathegoryType: String) extends Html {
  def toHtml(): String = {
    cathegoryType
  }
}

case object CASUAL extends Categoria("casual")
case object HARDCORE extends Categoria("hardcore")
case object PSN extends Categoria("psn")
case object SCONOSCIUTO extends Categoria("sconosciuto")
case object SHOVELWARE extends Categoria("shovelware")

sealed case class Controller(val controllerType: String) extends Html {
  def toHtml(): String = {
    controllerType
  }
}

case object C1 extends Controller("1")
case object C2 extends Controller("2")
case object C1N extends Controller("1+N")
case object CNA extends Controller("N/A")

sealed case class Supporto(val supportoType: String) extends Html {
  def toHtml(): String = {
    supportoType
  }
}

case object SC extends Supporto("completo")
case object SP extends Supporto("parziale")
case object SF extends Supporto("facoltativo")
case object SU extends Supporto("sconosciuto")

class Game(val title: String, val categoria: Categoria, val uscita: String, val controller: Controller, val supporto: Supporto, val metacritic: Link, val trailer: Link, val links: Array[Link], note: String) extends Html with Ordered[Game] {

  private def linksToString: String = {
    if (links == null) {
      ""
    } else {
      var x = ""
      links.foreach(link => {
        x += (link.toHtml() + ", ")
      })
      return x;
    }
  }

  private def allToString = {
    encap("td", title) + encap("td", categoria) + encap("td", uscita) + encap("td", controller) + encap("td", supporto) + encap("td", metacritic) + encap("td", trailer) + encap("td", linksToString) + encap("td", note)
  }

  def toHtml(): String = {
    encap("tr", allToString);
  }

  def compare(that: Game): Int = {
    this.title.compare(that.title)
  }

}

class License(val content : String) extends Html {

  def toHtml(): String = {
    comment(content)
  }

}

class Script {
  var stop = false;

  def execute(): Unit = {

    val reader = new GameLineReader();
    val licenseReader = new LicenseReader();

    var myLicense : List[License] = List.empty[License]
    var mySet: Set[Game] = TreeSet.empty[Game]

    def pd(line: String): Unit = {
    	
      try {
        if (!stop) {
          line match {
            case line if (line.startsWith("@LICENSE")) => myLicense =  licenseReader.readLine(line) :: myLicense
            case line if (line.startsWith("@STOP")) => stop = true
            case line if (line.startsWith("#")) => return
            case line if (line.trim().equals("")) => return
            case _ => mySet = mySet + reader.readLine(line)
          }
        }
      } catch {
        case flx: FormatException => Console.println(line + ": " + flx.fieldsInError)
      }
    }

    scala.io.Source.fromFile("D:\\Dave\\Work\\Eclipse\\Scala-Web\\Webscripter\\src\\net\\limacat\\webscripter\\games.txt").getLines.foreach { line =>
      pd(line)
    }

    myLicense.reverse.foreach { line => Console.println(line.toHtml()) }
    val header = "<tr><th>titolo</th><th>categoria</th><th>uscita</th><th>controller</th><th>supporto</th><th>metacritic</th><th colspan=\"2\">video</th><th>note</th></tr>"
    Console.println("<table>");
    Console.println(header);
    mySet.foreach { game => Console.println(game.toHtml()) }
    Console.println("</table>");
  }
}