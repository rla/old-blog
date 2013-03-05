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
package ee.pri.rl.blog.web.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;

import ee.pri.rl.blog.web.BlogSession;
import ee.pri.rl.blog.web.page.common.message.GlobalFeedbackPanel;

public class BasePage extends WebPage {
	private String pageTitle = "Blog";

	public BasePage() {
		super();
		add(new Label("pageTitle", new PropertyModel<Void>(this, "pageTitle")));

		WebComponent styleLink = new WebComponent("styleLink");
		styleLink.add(new AttributeModifier("href", new Model<String>(getStyleAndJSPath() + "/style.css")));
		add(styleLink);

		WebComponent scriptLocation = new WebComponent("scriptLocation");
		scriptLocation.add(new AttributeModifier("src", new Model<String>(getStyleAndJSPath() + "/script.js")));
		add(scriptLocation);
		
		add(new GlobalFeedbackPanel("pageFeedback") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return !getCurrentMessages().isEmpty();
			}
		});
	}

	public BasePage(PageParameters parameters) {
		super(parameters);
	}

	protected BlogSession getBlogSession() {
		return (BlogSession) getSession();
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	private String getStyleAndJSPath() {
		return ((WebRequest) getWebRequestCycle().getRequest()).getHttpServletRequest().getContextPath() + "/files";
	}
}
