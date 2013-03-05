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
 * Exception that is thrown when the category that is used in some operation
 * exists although it should not.
 * 
 * @author Raivo Laanemets
 */
public class CategoryExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	private String name;

	public CategoryExistsException(String name) {
		this.name = name;
	}

	@Override
	public String getMessage() {
		return "Category " + name + " already exists";
	}
	
}
