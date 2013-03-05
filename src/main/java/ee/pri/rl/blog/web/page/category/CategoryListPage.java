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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.index.IndexPage;

/**
 * Page for listing all categories. This is part of admin
 * interface and allows to delete categories.
 * 
 * @author Raivo Laanemets
 */
public class CategoryListPage extends BasePage implements AuthPageMarker {
	
	public CategoryListPage() {
		add(new CategoryListView("categoryListView"));
		add(new BookmarkablePageLink<Void>("addCategoryLink", CategoryAddPage.class));
		add(new BookmarkablePageLink<Void>("backLink", IndexPage.class));
	}
	
	private static class CategoryListView extends ListView<Category> {
		private static final long serialVersionUID = 1L;

		public CategoryListView(String id) {
			super(id, new CategoryListModel());
		}

		@Override
		protected void populateItem(ListItem<Category> item) {
			Category category = item.getModelObject();
			item.add(new Label("name", category.getName()));
			item.add(new BookmarkablePageLink<Void>("deleteLink", CategoryDeletePage.class, new PageParameters("c=" + category.getName())));
		}
		
	}
	
	private static class CategoryListModel extends LoadableDetachableModel<List<Category>> {
		private static final long serialVersionUID = 1L;
		
		@Override
		protected List<Category> load() {
			return BlogServiceHolder.get().getCategories();
		}
		
	}
}
