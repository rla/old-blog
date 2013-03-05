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

import java.util.List;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.common.EntryListPanel;
import ee.pri.rl.blog.web.page.index.IndexPage;

public class EntryListPage extends BasePage {
	public EntryListPage() {
		add(new EntryListPanel("entryListPanel", null, new EntryListModel(getBlogSession().isAuthenticated())));
		add(new BookmarkablePageLink<Void>("backLink", IndexPage.class));
	}
	
	private static class EntryListModel extends LoadableDetachableModel<List<Entry>> {
		private static final long serialVersionUID = 1L;
		
		private boolean authenticated;

		public EntryListModel(boolean authenticated) {
			this.authenticated = authenticated;
		}

		@Override
		protected List<Entry> load() {
			return BlogServiceHolder.get().getEntries(authenticated);
		}
		
	}
}
