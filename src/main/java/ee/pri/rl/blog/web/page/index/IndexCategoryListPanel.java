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

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.BlogSession;
import ee.pri.rl.blog.web.page.category.CategoryPage;

/**
 * Displays non-empty categories on the index page.
 * 
 * @see IndexPage
 */
public class IndexCategoryListPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public IndexCategoryListPanel(String id) {
		super(id);
		add(new CategoryListView("categories", ((BlogSession) getSession()).isAuthenticated()));
	}
	
	@Override
	public boolean isVisible() {
		return BlogServiceHolder.get().getSetting(SettingName.MAIN_DISPLAY_CATEGORIES).getBoolValue();
	}

	private class CategoryListView extends ListView<Category> {
		private static final long serialVersionUID = 1L;

		public CategoryListView(String id, boolean authenticated) {
			super(id, new NonEmptyCategoriesModel(authenticated));
		}
		
		@Override
		protected void populateItem(ListItem<Category> item) {
			Category category = item.getModelObject();
			item.add(new BookmarkablePageLink<Void>("link", CategoryPage.class, new PageParameters("c=" + category.getName())).add(new Label("name", category.getName())));
		}
		
	}
	
	private static class NonEmptyCategoriesModel extends LoadableDetachableModel<List<Category>> {
		private static final long serialVersionUID = 1L;
		
		private boolean authenticated;

		public NonEmptyCategoriesModel(boolean authenticated) {
			this.authenticated = authenticated;
		}

		@Override
		protected List<Category> load() {
			return BlogServiceHolder.get().getNonEmptyCategories(authenticated);
		}
		
	}

}
