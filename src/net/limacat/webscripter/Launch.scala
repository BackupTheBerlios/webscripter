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

object Launch {
  def main(args: Array[String]) {
	  new Script().execute()
  }
}