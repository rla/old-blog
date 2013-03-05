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
package ee.pri.rl.blog.web.page.index;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.category.CategoryAddPage;
import ee.pri.rl.blog.web.page.category.CategoryListPage;
import ee.pri.rl.blog.web.page.common.EntryListPanel;
import ee.pri.rl.blog.web.page.entry.EntryAddPage;
import ee.pri.rl.blog.web.page.entry.EntryListPage;
import ee.pri.rl.blog.web.page.entry.edit.EntryEditPage;
import ee.pri.rl.blog.web.page.setting.SettingsPage;

/**
 * Main page of the blog. Can display latest entries and categories.
 * Also displays some management links when the blog owner is
 * authnticated.
 */
public class IndexPage extends BasePage {
	private static final Logger log = Logger.getLogger(IndexPage.class);
	
	public IndexPage() {
		String mainEntryName = BlogServiceHolder.get().getSetting(SettingName.MAIN_ENTRY).getValue();
		try {
			add(new Label("mainEntry", BlogServiceHolder.get().renderEntry(mainEntryName)).setEscapeModelStrings(false));
		} catch (NoSuchEntryException e) {
			log.error("Main entry not found");
			add(new Label("mainEntry"));
		}
		
		boolean authenticated = getBlogSession().isAuthenticated();
		
		add(new EntryListPanel("entryListPanel", "Latest entries", new EntryListModel(authenticated)).setVisible(BlogServiceHolder.get().getSetting(SettingName.MAIN_DISPLAY_ENTRIES).getBoolValue()));
		add(new IndexCategoryListPanel("categoryListPanel"));
		
		add(new BookmarkablePageLink<Void>("entriesLink", EntryListPage.class).setVisible(authenticated || BlogServiceHolder.get().getSetting(SettingName.MAIN_DISPLAY_ENTRIES_LINK).getBoolValue()));
		
		add(new BookmarkablePageLink<Void>("editLink", EntryEditPage.class, new PageParameters("e=" + mainEntryName)).setVisible(authenticated));
		add(new BookmarkablePageLink<Void>("settingsLink", SettingsPage.class).setVisible(authenticated));
		add(new BookmarkablePageLink<Void>("addEntryLink", EntryAddPage.class).setVisible(authenticated));
		add(new BookmarkablePageLink<Void>("addCategoryLink", CategoryAddPage.class).setVisible(authenticated));
		add(new BookmarkablePageLink<Void>("categoriesLink", CategoryListPage.class).setVisible(authenticated));
		add(new LogoutLink("logoutLink").setVisible(authenticated));
	}
	
	private static class LogoutLink extends Link<Void> {
		private static final long serialVersionUID = 1L;

		public LogoutLink(String id) {
			super(id);
		}

		@Override
		public void onClick() {
			getSession().invalidate();
			setRedirect(true);
			setResponsePage(IndexPage.class);
		}
	}
	
	private class EntryListModel extends LoadableDetachableModel<List<Entry>> {
		private static final long serialVersionUID = 1L;
		
		private boolean authenticated;
		
		private EntryListModel(boolean authenticated) {
			this.authenticated = authenticated;
		}

		@Override
		protected List<Entry> load() {
			return BlogServiceHolder.get().getLatestEntries(authenticated);
		}
		
	}
}
