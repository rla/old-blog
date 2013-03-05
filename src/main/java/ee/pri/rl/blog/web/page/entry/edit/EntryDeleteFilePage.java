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

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;

public class EntryDeleteFilePage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(EntryDeleteFilePage.class);
	
	public EntryDeleteFilePage(PageParameters parameters) {
		String entryName = parameters.getString("e");
		String fileName = parameters.getString("f");
		
		add(new Label("title", "Delete file " + fileName + "?"));
		add(new ConfirmDeleteLink("confirmLink", entryName, fileName));
		add(new BookmarkablePageLink<Void>("cancelLink", EntryFilesPage.class, new PageParameters("e=" + entryName)));
	}
	
	private static class ConfirmDeleteLink extends Link<String> {
		private static final long serialVersionUID = 1L;
		
		private String entryName;
		private String fileName;

		public ConfirmDeleteLink(String id, String entryName, String fileName) {
			super(id);
			this.entryName = entryName;
			this.fileName = fileName;
		}

		@Override
		public void onClick() {
			try {
				BlogServiceHolder.get().deleteFile(entryName, fileName);
				setRedirect(true);
				setResponsePage(EntryFilesPage.class, new PageParameters("e=" + entryName));
			} catch (IOException e) {
				log.warn("File "+ fileName + " cannot be deleted from " + entryName + ": " + e.getMessage());
				getSession().error("File "+ fileName + " cannot be deleted from " + entryName + ": " + e.getMessage());
				setRedirect(true);
				setResponsePage(EntryFilesPage.class, new PageParameters("e=" + entryName));
			}
		}
		
	}
}
