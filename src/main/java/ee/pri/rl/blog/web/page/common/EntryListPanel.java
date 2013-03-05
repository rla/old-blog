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
package ee.pri.rl.blog.web.page.common;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.web.model.EntityModel;
import ee.pri.rl.blog.web.page.category.CategoryPage;
import ee.pri.rl.blog.web.page.entry.EntryViewPage;

/**
 * General panel for displaying a list of entries.
 * 
 * @author Raivo Laanemets
 */
public class EntryListPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public EntryListPanel(String id, String title, IModel<List<Entry>> model) {
		super(id);
		add(new Label("title", StringUtils.defaultIfEmpty(title, "")).setVisible(StringUtils.isNotEmpty(title)));
		add(new EntryListView("entries", model));
	}
	
	private static class EntryListView extends ListView<Entry> {
		private static final long serialVersionUID = 1L;

		public EntryListView(String id, IModel<List<Entry>> model) {
			super(id, model);
		}
		
		@Override
		protected void populateItem(ListItem<Entry> item) {
			Entry entry = item.getModelObject();
			item.add(new Label("date", DateFormatUtils.format(entry.getLastModificationDate(), "dd/MM/yy")));
			item.add(new BookmarkablePageLink<Void>("link", EntryViewPage.class, new PageParameters("e=" + entry.getEntryName())).add(new Label("title", entry.getTitle())));
			item.add(new WebMarkupContainer("categoriesLabel").setVisible(!entry.getCategories().isEmpty()));
			item.add(new EntryCategoryListView(entry));
		}
		
		private class EntryCategoryListView extends ListView<Category> {
			private static final long serialVersionUID = 1L;

			public EntryCategoryListView(Entry entry) {
				super("categories", new EntryCategoriesModel(new EntityModel<Entry>(entry)));
			}
			
			@Override
			protected void populateItem(ListItem<Category> item) {
				Category category = item.getModelObject();
				item.add(new BookmarkablePageLink<Void>("link", CategoryPage.class, new PageParameters("c=" + category.getName())).add(new Label("name", category.getName())));
				item.add(new Label("separator", "|").setVisibilityAllowed(item.getIndex() > 0));
			}
			
		}
		
	}
	
	private static class EntryCategoriesModel extends LoadableDetachableModel<List<Category>> {
		private static final long serialVersionUID = 1L;
		
		private IModel<Entry> entry;

		public EntryCategoriesModel(IModel<Entry> entry) {
			this.entry = entry;
		}

		@Override
		protected List<Category> load() {
			return entry.getObject().getCategories();
		}
		
	}
}
