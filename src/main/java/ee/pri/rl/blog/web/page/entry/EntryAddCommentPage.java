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
package ee.pri.rl.blog.web.page.entry;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.protocol.http.WebRequest;

import ee.pri.rl.blog.exception.CommentingNowAllowedException;
import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.common.form.CancelButton;
import ee.pri.rl.blog.web.page.common.message.LocalFeedbackPanel;

public class EntryAddCommentPage extends BasePage {
	private static final Logger log = Logger.getLogger(EntryAddCommentPage.class);
	private static final String CAPTCHA_ANSWER = "30";
	
	public EntryAddCommentPage(PageParameters parameters) {
		String entryName = parameters.getString("e");
		
		try {
			String title = BlogServiceHolder.get().getEntryTitle(entryName);
			
			add(new Label("title", "Add comment to: " + title));
			add(new AddCommentForm("addForm", entryName));
			
		} catch (NoSuchEntryException e) {
			log.warn("Entry with name " + entryName + " does not exist", e);
			
			// We let EntryPage handle missing entry.
			setRedirect(true);
			setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
		}
	}
	
	private static class AddCommentForm extends Form<CommentFormModel> {
		private static final long serialVersionUID = 1L;

		public AddCommentForm(String id, final String entryName) {
			super(id, new CompoundPropertyModel<CommentFormModel>(new CommentFormModel()));
			getModelObject().setWebpage("http://");
			
			WebRequest request = (WebRequest) getRequest();
			int timeout = request.getHttpServletRequest().getSession().getMaxInactiveInterval() / 60;
			add(new Label("timeoutScript", "startSessionTimeoutDisplay(" + timeout + ");").setEscapeModelStrings(false));			
			add(new LocalFeedbackPanel("feedback", this));
			add(new RequiredTextField<Void>("author"));
			add(new TextField<Void>("email"));
			add(new TextField<Void>("webpage"));
			add(new TextArea<Void>("contents"));
			add(new RequiredTextField<Void>("captcha"));
			add(new Button("save") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					CommentFormModel comment = AddCommentForm.this.getModelObject();
					if (StringUtils.isEmpty(comment.getContents())) {
						error("Comment is empty");
					} else if (StringUtils.isEmpty(comment.getCaptcha())) {
						error("Captcha is not answered");
					} else if (!CAPTCHA_ANSWER.equals(comment.getCaptcha())) {
						error("Captcha answer is wrong");
					} else {
						if ("http://".equals(comment.getWebpage())) {
							comment.setWebpage(null);
						}
						try {
							try {
								BlogServiceHolder.get().newComment(
									entryName,
									comment.getContents(),
									comment.getAuthor(),
									comment.getEmail(),
									comment.getWebpage(),
									((WebRequest) getRequest()).getHttpServletRequest().getRemoteAddr()
								);
							} catch (CommentingNowAllowedException e) {
								log.warn("Commenting not allowed for " + entryName, e);
								error("Commenting is not allowed for this entry");
							}
							setRedirect(true);
							setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
						} catch (NoSuchEntryException e) {
							setRedirect(true);
							setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
						}
					}
				}
			});
			add(new CancelButton("cancel", EntryViewPage.class, new PageParameters("e=" + entryName)));
		}
	}

	private static class CommentFormModel implements Serializable {
		private static final long serialVersionUID = 1L;

		private String author;
		private String email;
		private String webpage;
		private String contents;
		private String captcha;

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getWebpage() {
			return webpage;
		}

		public void setWebpage(String webpage) {
			this.webpage = webpage;
		}

		public String getContents() {
			return contents;
		}

		public void setContents(String contents) {
			this.contents = contents;
		}

		public String getCaptcha() {
			return captcha;
		}

		public void setCaptcha(String captcha) {
			this.captcha = captcha;
		}

	}
}
