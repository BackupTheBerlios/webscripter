package net.limacat.webscripter;

import junit.framework.TestCase;

public class GameTest extends TestCase {

	// private Link x = Link.

	private HtmlCalculator calculator = new HtmlCalculator();

	public void testToHtml() {
		Game game = new Game("Sbillidong",
				new Categoria(CASUAL.cathegoryType()), "uscito",
				new Controller(C1N.controllerType()), new Supporto(SC
						.supportoType()), Link.Metacritic("ciro"), Link
						.Trailer("ciro"), null, "ciro");
		String result = game.toHtml();
		System.out.println(result);
		assertNotNull(result);
		assertTrue(calculator.balanced("tr", result));
		assertEquals(1, calculator.numberOf("tr", result));
		assertTrue(calculator.balanced("td", result));
		assertEquals(9, calculator.numberOf("td", result));
		assertEquals("Sbillidong", calculator.valueOf("td", 1, result));
		assertEquals("ciro", calculator.valueOf("td", 9, result));
		assertEquals("Sbillidong", game.title());
	}

	public void testToHtml2() {
		// XXX : attenzione, la seguente riga da errore di compilazione in
		// eclipse con la seguente versione dell'editor, MA NON E' UN ERRORE!
		// Scala IDE for Eclipse 1.0.0.201009112352
		// org.scala-ide.sdt.feature.feature.group
		Game game = new Game(
				"Eyepet",
				new Categoria(CASUAL.cathegoryType()),
				"uscito",
				new Controller(C1N.controllerType()),
				new Supporto(SC.supportoType()),
				Link
						.Metacritic("http://www.metacritic.com/game/playstation-3/eyepet"),
				Link.Trailer("http://www.youtube.com/watch?v=TztCY2iz450"),
				new Link[] { new Link("Ign",
						"http://www.youtube.com/watch?v=oZ_06GUcRx0") }, null);
		String result = game.toHtml();
		System.out.println(result);
		assertNotNull(result);
		assertTrue(calculator.balanced("tr", result));
		assertEquals(1, calculator.numberOf("tr", result));
		assertTrue(calculator.balanced("td", result));
		assertEquals(9, calculator.numberOf("td", result));
		assertEquals("", calculator.valueOf("td", 9, result));
	}

	public static class HtmlCalculator {

		private String open(String tag) {
			return "<" + tag + ">";
		}

		private String closed(String tag) {
			return "</" + tag + ">";
		}

		public int numberOf(String tagName, String html) {
			return reallyPrivateNumberOf(open(tagName), html);
		}

		private int reallyPrivateNumberOf(String tag, String html) {
			int ping = 0;
			int indexOf = html.indexOf(tag);
			while (indexOf > -1) {
				ping++;
				indexOf = html.indexOf(tag, indexOf + 1);
			}
			return ping;
		}

		public boolean balanced(String tag, String html) {
			return numberOf(open(tag), html) == numberOf(closed(tag), html);
		}

		public String valueOf(String tag, int content, String html) {
			int open = 1;
			int indexOf = html.indexOf(open(tag));
			while (open < content) {
				open++;
				indexOf = html.indexOf(open(tag), indexOf + 1);
			}
			int indexOfE = html.indexOf(closed(tag), indexOf + 1);
			return html.substring(indexOf + open(tag).length(), indexOfE);
		}

	}

}
