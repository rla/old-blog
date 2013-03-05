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
import org.apache.wicket.markup.html.link.Link;

import ee.pri.rl.blog.exception.NoSuchCommentException;
import ee.pri.rl.blog.model.Comment;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;

public class EntryDeleteCommentPage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(EntryDeleteCommentPage.class);

	public EntryDeleteCommentPage(PageParameters parameters) {
		String entryName = parameters.getString("e");
		Long commentId = parameters.getAsLong("c");

		Comment comment = BlogServiceHolder.get().getComment(commentId);
		
		add(new Label("title", "Delete comment by " + comment.getAuthor() + "?"));
		add(new ConfirmDeleteLink("confirmLink", commentId, entryName));
		add(new BookmarkablePageLink<Void>("cancelLink", EntryViewPage.class, new PageParameters("e=" + entryName)));
	}
	
	private static class ConfirmDeleteLink extends Link<Void> {
		private static final long serialVersionUID = 1L;
		
		private Long commentId;
		private String entryName;

		public ConfirmDeleteLink(String id, Long commentId, String entryName) {
			super(id);
			this.commentId = commentId;
			this.entryName = entryName;
		}

		@Override
		public void onClick() {
			try {
				BlogServiceHolder.get().deleteComment(commentId);
			} catch (NoSuchCommentException e) {
				log.warn("Comment was already deleted", e);
				getSession().info("Comment was already deleted");
				setRedirect(true);
				setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
			}
			getSession().info("Comment deleted");
			setRedirect(true);
			setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
		}
		
	}
}
