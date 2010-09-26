/*
	Webscripter - a simple html creator
    Copyright (C) 2010 Davide Inglima
	
	This unit test is in the public domain.

	Contact: limacat@gmail.com
 */
package net.limacat.webscripter

trait Html {

  protected def openTag(tag: String): String = {
    "<" + tag + ">"
  }

  protected def closeTag(tag: String): String = {
    "</" + tag + ">"
  }

  protected def encap(tag: String, value: Html): String = {
    if (value == null) {
      encap(tag, "")
    } else {
      encap(tag, value.toHtml())
    }
  }

  protected def encap(tag: String, value: Object): String = {
    if (value == null) {
      openTag(tag) + closeTag(tag)
    } else {
      openTag(tag) + value + closeTag(tag)
    }
  }

  def toHtml(): String;
}

class Link(nome: String, destinazione: String) extends Html {
  def toHtml(): String = {
	  if (destinazione == null || "".equals(destinazione.trim())) {
	 	  ""
	  } else {
		  "<a href=\"" + destinazione + "\">" + nome + "</a>"
	  }
  }
}

object Link {
  def Metacritic(pagina: String): Link = {
    new Link("metacritic", pagina)
  }
  def Trailer(pagina: String): Link = {
    new Link("trailer", pagina)
  }
}


