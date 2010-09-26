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

class Script {

  def execute(): Unit = {
    var stop = false;

    val reader = new GameLineReader();

    var mySet: Set[Game] = TreeSet.empty[Game]

    def pd(line: String): Unit = {
      if (line.startsWith("STOP")) {
        stop = true
      }
      if (!stop && !line.startsWith("#")) {
        try {
          mySet = mySet + reader.readLine(line)
        } catch {
          case flx: FormatException => Console.println(flx.fieldsInError)
        }
      }
    }

    val header = "<tr><th>titolo</th><th>categoria</th><th>uscita</th><th>controller</th><th>supporto</th><th>metacritic</th><th colspan=\"2\">video</th><th>note</th></tr>"
    Console.println("<table>");
    Console.println(header);
    scala.io.Source.fromFile("D:\\Dave\\Work\\Eclipse\\Scala-Web\\Webscripter\\src\\net\\limacat\\webscripter\\games.txt").getLines.foreach { line =>
      pd(line)
    }
    mySet.foreach { game => Console.println(game.toHtml()) }
       Console.println("</table>");    
  }
}