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
package ee.pri.rl.blog.web.page.entry.edit;

import java.io.File;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class FileListView extends ListView<File> {
	private static final long serialVersionUID = 1L;
	
	private String entryName;
	private boolean showDelete;

	public FileListView(String id, String entryName, boolean showDelete) {
		super(id, new FileListModel(entryName));
		this.entryName = entryName;
		this.showDelete = showDelete;
	}

	@Override
	protected void populateItem(ListItem<File> item) {
		File file = item.getModelObject();
		final String name = file.getName();
		item.add(new Label("name", name));
		item.add(new Label("size", (file.length() / 1024) + "kB"));
		if (showDelete) {
			item.add(new BookmarkablePageLink<Void>("deleteLink", EntryDeleteFilePage.class, new PageParameters("e=" + entryName + ",f=" + name)));
		}
	}

}