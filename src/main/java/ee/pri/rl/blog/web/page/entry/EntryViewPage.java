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

import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.model.EntityModel;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.entry.edit.EntryEditPage;
import ee.pri.rl.blog.web.page.index.IndexPage;

public class EntryViewPage extends BasePage {
	private static final Logger log = Logger.getLogger(EntryViewPage.class);
	
	public EntryViewPage(PageParameters parameters) {
		String entryName = parameters.getString("e");
		
		boolean authenticated = getBlogSession().isAuthenticated();
		
		Entry entry = null;
		try {
			entry = BlogServiceHolder.get().getEntry(entryName);
			if (entry.isPrivateEntry() && !authenticated) {
				log.warn("Entry " + entryName + " is private but user is not authenticated");
				setRedirect(true);
				setResponsePage(IndexPage.class);
			}
		} catch (NoSuchEntryException e) {
			log.warn("Entry " + entryName + " does not exist", e);
			setRedirect(true);
			setResponsePage(IndexPage.class);
		}
		
		if (entry != null) {
			setPageTitle(entry.getTitle());
			try {
				add(new Label("entry", BlogServiceHolder.get().renderEntry(entryName)).setEscapeModelStrings(false));
			} catch (NoSuchEntryException e) {
				log.warn("Cannot render entry " + entryName + " because it does not exist", e);
			}
			add(new EntryCommentListPanel("commentListPanel", new EntityModel<Entry>(entry)));
			add(new BookmarkablePageLink<Void>("backLink", IndexPage.class));
			add(new BookmarkablePageLink<Void>("commentLink", EntryAddCommentPage.class, new PageParameters("e=" + entryName)).setVisible(entry.isCommentingAllowed()));
			add(new BookmarkablePageLink<Void>("editLink", EntryEditPage.class, new PageParameters("e=" + entryName)).setVisible(authenticated));
			add(new BookmarkablePageLink<Void>("deleteLink", EntryDeletePage.class, new PageParameters("e=" + entryName)).setVisible(authenticated));
		}
	}
}
