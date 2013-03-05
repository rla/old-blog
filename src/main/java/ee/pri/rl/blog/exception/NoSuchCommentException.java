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
 * Exception that is thrown in operations where the existence of some comment is
 * required.
 * 
 * @author Raivo Laanemets
 */
public class NoSuchCommentException extends Exception {
	private static final long serialVersionUID = 1L;

	private Long commentId;

	public NoSuchCommentException(Long commentId) {
		this.commentId = commentId;
	}

	@Override
	public String getMessage() {
		return "Comment with id " + commentId + " does not exist";
	}

}
