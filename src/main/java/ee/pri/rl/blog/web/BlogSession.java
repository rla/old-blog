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
package ee.pri.rl.blog.web;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

import ee.pri.rl.blog.web.page.auth.BlogAuthorizationStrategy;

/**
 * Custom session class for the blog application.
 * 
 * @see BlogAuthorizationStrategy
 * @see BlogApplication
 * 
 * @author Raivo Laanemets
 */
public class BlogSession extends WebSession {
	private static final long serialVersionUID = 1L;

	private boolean authenticated = false;

	public BlogSession(Request request) {
		super(request);
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	/**
	 * Returns session from context.
	 * 
	 * @see Session#get()
	 */
	public static BlogSession get() {
		return (BlogSession) Session.get();
	}

}
