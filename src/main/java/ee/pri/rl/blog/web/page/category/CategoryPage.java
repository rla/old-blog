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
package ee.pri.rl.blog.web.page.category;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.common.EntryListPanel;
import ee.pri.rl.blog.web.page.index.IndexPage;

/**
 * Page for displaying a list of entries of the given category.
 * 
 * @author Raivo Laanemets
 */
public class CategoryPage extends BasePage {
	public CategoryPage(PageParameters parameters) {
		String name = parameters.getString("c");
		
		add(new Label("title", "Category: " + name));
		add(new EntryListPanel("entryListPanel", null, new EntryListModel(name, getBlogSession().isAuthenticated())));
		add(new BookmarkablePageLink<Void>("backLink", IndexPage.class));
	}
	
	private class EntryListModel extends LoadableDetachableModel<List<Entry>> {
		private static final long serialVersionUID = 1L;
		
		private String categoryName;
		private boolean authenticated;	

		public EntryListModel(String categoryName, boolean authenticated) {
			this.categoryName = categoryName;
			this.authenticated = authenticated;
		}

		@Override
		protected List<Entry> load() {
			return BlogServiceHolder.get().getCategoryEntries(categoryName, authenticated);
		}
		
	}
}
