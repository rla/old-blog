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

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.index.IndexPage;

public class EntryDeletePage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(EntryDeletePage.class);
	
	public EntryDeletePage(PageParameters parameters) {
		String entryName = parameters.getString("e");
		try {
			String title = BlogServiceHolder.get().getEntryTitle(entryName);
			add(new Label("title", "Delete entry " + title + "?"));
			add(new ConfirmDeleteLink("confirmLink", entryName, title));
			add(new BookmarkablePageLink<Void>("cancelLink", EntryViewPage.class, new PageParameters("e=" + entryName)));
		} catch (NoSuchEntryException e) {
			log.warn("Entry with name " + entryName + " does not exist", e);
			
			setRedirect(true);
			setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
		}
	}

	private static class ConfirmDeleteLink extends Link<String> {
		private static final long serialVersionUID = 1L;
		
		private String entryName;
		private String title;

		public ConfirmDeleteLink(String id, String entryName, String title) {
			super(id);
			this.entryName = entryName;
			this.title = title;
		}

		@Override
		public void onClick() {
			try {
				BlogServiceHolder.get().deleteEntry(entryName);
				getSession().info("Entry " + title + " deleted");
				setRedirect(true);
				setResponsePage(IndexPage.class);
			} catch (NoSuchEntryException e) {
				log.warn("Entry " + entryName + " was aready deleted", e);
				setRedirect(true);
				setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
			}
		}
		
	}
}
