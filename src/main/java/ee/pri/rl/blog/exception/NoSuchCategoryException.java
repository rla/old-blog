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
 * Exception that is thrown in cases where some required category does not
 * exist.
 * 
 * @author Raivo Laanemets
 */
public class NoSuchCategoryException extends Exception {
	private static final long serialVersionUID = 1L;

	private Long categoryId;
	private String categoryName;
	
	public NoSuchCategoryException(Long categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	
	public NoSuchCategoryException(Long categoryId) {
		this(categoryId, null);
	}
	
	public NoSuchCategoryException(String categoryName) {
		this(null, categoryName);
	}

	@Override
	public String getMessage() {
		if (categoryName == null) {
			return "Category with id " + categoryId + " does not exist";
		} else {
			return "Category " + categoryName + " does not exist";
		}
	}
	
}
