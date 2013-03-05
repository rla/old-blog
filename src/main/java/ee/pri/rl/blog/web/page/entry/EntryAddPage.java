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
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import ee.pri.rl.blog.exception.EntryExistsException;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.common.form.CancelButton;
import ee.pri.rl.blog.web.page.index.IndexPage;

/**
 * Form for creating new entries.
 */
public class EntryAddPage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(EntryAddPage.class);
	
	public EntryAddPage() {
		add(new FeedbackPanel("feedback"));
		add(new AddEntryForm("addForm"));
	}
	
	private static class AddEntryForm extends Form<EntryFormModel> {
		private static final long serialVersionUID = 1L;
		
		public AddEntryForm(String id) {
			super(id, new CompoundPropertyModel<EntryFormModel>(new EntryFormModel()));
			add(new TextField<Void>("title"));
			add(new Button("save") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					EntryFormModel entry = AddEntryForm.this.getModelObject();
					String title = entry.getTitle();
					if (StringUtils.isEmpty(title)) {
						error("Title must be not empty");
					} else if (!StringUtils.isAlphanumericSpace(title)) {
						error("Title must contain only letters, numbers and spaces");
					} else {
						try {
							String entryName = BlogServiceHolder.get().newEntry(title);
							getSession().info("Created new entry");
							setRedirect(true);
							setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
						} catch (EntryExistsException e) {
							log.warn("Entry " + title + " already exists", e);
							error("Entry already exists");
						}
					}
				}
			});
			add(new CancelButton("cancel", IndexPage.class));
		}
	}
	
	private static class EntryFormModel implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}
}
