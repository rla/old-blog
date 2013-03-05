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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Comment;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.web.BlogSession;

/**
 * Displays the list of comments of the given entry.
 */
public class EntryCommentListPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public EntryCommentListPanel(String id, IModel<Entry> entry) {
		super(id);
		add(new CommentListView("comments", entry));
		setVisible(!entry.getObject().getComments().isEmpty());
	}

	private static class CommentListView extends ListView<Comment> {
		private static final long serialVersionUID = 1L;
		
		private String entryName;

		public CommentListView(String id, IModel<Entry> entry) {
			super(id, new CommentListModel(entry));
			entryName = entry.getObject().getEntryName();
		}

		@Override
		protected void populateItem(ListItem<Comment> item) {
			Comment comment = item.getModelObject();
			item.add(new Label("author", "By " + comment.getAuthor()));
			item.add(new Label("date", DateFormatUtils.format(comment.getPostDate(), "dd/MM/yy")));
			
			ExternalLink webpageLink = new ExternalLink("webpage", comment.getWebpage());
			webpageLink.add(new Label("webpageLabel", comment.getWebpage()));
			webpageLink.setVisibilityAllowed(!StringUtils.isEmpty(comment.getWebpage()));
			item.add(webpageLink);
			
			item.add(new BookmarkablePageLink<Void>("deleteLink", EntryDeleteCommentPage.class, new PageParameters("e=" + entryName + ",c=" + comment.getId())).setVisibilityAllowed(((BlogSession) getSession()).isAuthenticated()));
			
			item.add(new Label("contents", comment.getContents()));
		}
	}
	
	private static class CommentListModel extends LoadableDetachableModel<List<Comment>> {
		private static final long serialVersionUID = 1L;
		
		private IModel<Entry> entry;

		private CommentListModel(IModel<Entry> entry) {
			this.entry = entry;
		}

		@Override
		protected List<Comment> load() {
			return entry.getObject().getComments();
		}
		
	}
}