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
package ee.pri.rl.blog.web.page.setting;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Setting;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.model.EntityModel;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.index.IndexPage;

public class SettingsPage extends BasePage implements AuthPageMarker {

	public SettingsPage() {
		add(new SettingsListView());
		add(new BookmarkablePageLink<Void>("backLink", IndexPage.class));
	}
	
	private static class SettingsListView extends ListView<Setting> {
		private static final long serialVersionUID = 1L;

		public SettingsListView() {
			super("settings", new SettingsModel());
		}

		@Override
		protected void populateItem(ListItem<Setting> item) {
			Setting setting = item.getModelObject();
			item.add(new Label("name", setting.getName().toString()));
			item.add(new Label("value", setting.getValue()));
			item.add(new Link<Setting>("editLink", new EntityModel<Setting>(setting)) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new SettingEditPage(getModel()));
				}
			});
		}
		
	}
	
	private static class SettingsModel extends LoadableDetachableModel<List<Setting>> {
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Setting> load() {
			return BlogServiceHolder.get().getSettings();
		}
		
	}
}
