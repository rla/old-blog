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
package ee.pri.rl.blog.web.page.auth;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.CompoundPropertyModel;

import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.BlogSession;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.common.message.LocalFeedbackPanel;
import ee.pri.rl.blog.web.page.index.IndexPage;

/**
 * Log-in page. Used by the blog owner. Accessable by everyone.
 * 
 * @author Raivo Laanemets
 */
public class LoginPage extends BasePage {

	public LoginPage() {		
		add(new LoginForm("loginForm"));
	}
	
	private static class LoginForm extends Form<LoginFormModel> {
		private static final long serialVersionUID = 1L;

		public LoginForm(String id) {
			super(id, new CompoundPropertyModel<LoginFormModel>(new LoginFormModel()));
			
			add(new LocalFeedbackPanel("feedback", this));
			add(new PasswordTextField("password"));
			add(new Button("submit") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					if (BlogServiceHolder.get().isOwnerPasswordMatch(LoginForm.this.getModelObject().getPassword())) {
						((BlogSession) getSession()).setAuthenticated(true);
						setResponsePage(IndexPage.class);
					} else {
						error("Wrong password");
					}
				}
			});
		}
		
	}

	private static class LoginFormModel implements Serializable {
		private static final long serialVersionUID = 1L;

		private String password;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}
}
