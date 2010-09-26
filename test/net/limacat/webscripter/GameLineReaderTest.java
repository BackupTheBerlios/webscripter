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
package net.limacat.webscripter;

import junit.framework.TestCase;

public class GameLineReaderTest extends TestCase {

	GameLineReader reader = new GameLineReader();

	// Formato accettabile:
	// titolo (o); categoria (o); uscita (o); controller (o); supporto (o);
	// metacritic (fa); trailer (fa); video1, videoN (fa); note(fa)\n
	// stringa; C|H|P|U|Z; stringa; 1|2|N|U; stringa; stringa; stringa, stringa,
	// stringa; stringa

	public void testInvalidReading() {
		try {
			reader.readLine("");
			fail("expected a format exception!");
		} catch (FormatException exc) {
		}
	}

	public void testInvalidReadingName() {
		try {
			reader.readLine(";C;uscito;1;C;x;xx;;ciao;");
			fail("expected a format exception!");
		} catch (FormatException exc) {
		}
	}

	public void testOk() {
		Game aGame = reader.readLine("eyepeto;C;uscito;1;C;x;xx;;ciao;");
		System.out.println(aGame.toHtml());
		assertEquals("eyepeto", aGame.title());
		assertEquals("uscito", aGame.uscita());
		assertEquals(0, aGame.links().length);
		
	}
	
	public void testOk2() {
		Game aGame = reader.readLine("eyepeto;C;uscito;1;C;x;xx;x1, x2, x3;ciao;");
		assertEquals("eyepeto", aGame.title());
		assertEquals("uscito", aGame.uscita());
		assertEquals(3, aGame.links().length);
	}
	
	public void testNoMetacritic() {
		Game aGame = reader.readLine("Ape Escape Fury! Fury!;C;inverno 2010;1;C;;http://www.youtube.com/watch?v=8TYwxKA9JxU;;;");
		assertEquals("", aGame.metacritic().toHtml());
	}

	public void testSupporto() {
		Game aGame = reader.readLine("Ape Escape Fury! Fury!;C;inverno 2010;1;U;;http://www.youtube.com/watch?v=8TYwxKA9JxU;;;");
		assertEquals("sconosciuto", aGame.supporto().supportoType());
	}
	
}
