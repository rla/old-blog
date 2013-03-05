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
package ee.pri.rl.blog.web.page.common.form;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Button;

/**
 * General form "cancel" button that redirects to the given page.
 * 
 * @author Raivo Laanemets
 */
public class CancelButton extends Button {
	private static final long serialVersionUID = 1L;
	
	private Class<? extends Page> responsePage;
	private PageParameters pageParameters;
	
	/**
	 * Constructs a new CancelButton.
	 * 
	 * @param id Wicket id of the component.
	 * @param responsePage Page where to redirect.
	 * @param parameters Parameters for the page.
	 */
	public CancelButton(String id, Class<? extends Page> responsePage, PageParameters parameters) {
		super(id);
		this.responsePage = responsePage;
		this.pageParameters = parameters;
		setDefaultFormProcessing(false);
	}

	/**
	 * Constructs a new CancelButton.
	 * 
	 * @param id Wicket id of the component.
	 * @param responsePage Page where to redirect.
	 */
	public CancelButton(String id, Class<? extends Page> responsePage) {
		this(id, responsePage, null);
	}

	@Override
	public void onSubmit() {
		setRedirect(true);
		if (pageParameters == null) {
			setResponsePage(responsePage);
		} else {
			setResponsePage(responsePage, pageParameters);
		}
	}

}
