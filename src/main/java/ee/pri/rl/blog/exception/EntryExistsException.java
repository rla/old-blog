/**
 * Copyright (C) 2009 Raivo Laanemets <rl@starline.ee>
 *
 * This file is part of rl-blog.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rl-blog.  If not, see <http://www.gnu.org/licenses/>.
 */
package ee.pri.rl.blog.exception;

/**
 * Exception that is thrown in creation of new entries when the entry already
 * exists.
 * 
 * @author Raivo Laanemets
 */
public class EntryExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	private String entryTitle;

	public EntryExistsException(String entryTitle) {
		this.entryTitle = entryTitle;
	}

	@Override
	public String getMessage() {
		return "Entry " + entryTitle + " already exists";
	}
	
}
