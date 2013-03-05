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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.util.lang.Bytes;

import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.model.EntityModel;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.entry.EntryViewPage;

public class EntryFilesPage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(EntryFilesPage.class);
	
	public EntryFilesPage(PageParameters parameters) {
		String entryName = parameters.getString("e");
		
		try {
			Entry entry = BlogServiceHolder.get().getEntry(entryName);
			
			add(new Label("title", "Entry " + entry.getTitle() + " files"));
			add(new FileListView("files", entry.getEntryName(), true));
			add(new FileUploadForm(new EntityModel<Entry>(entry)));
			add(new BookmarkablePageLink<Void>("backLink", EntryEditPage.class, new PageParameters("e=" + entryName)));
		} catch (NoSuchEntryException e) {
			log.warn("Entry " + entryName + " not found");
			setRedirect(true);
			setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
		}
	}
	
	private static class FileUploadForm extends Form<Void> {
		private static final long serialVersionUID = 1L;

		private EntityModel<Entry> entry;
		private FileUploadField fileUploadField;

		public FileUploadForm(EntityModel<Entry> entry) {
			super("uploadForm");

			this.entry = entry;

			setMultiPart(true);
			add(fileUploadField = new FileUploadField("fileInput"));
			setMaxSize(Bytes.megabytes(50));
		}

		@Override
		protected void onSubmit() {
			FileUpload fileUpload = fileUploadField.getFileUpload();

			try {
				BlogServiceHolder.get().newUploadFile(entry.getObject().getEntryName(), fileUpload.getClientFileName(), fileUpload.getBytes());
				getSession().info("File uploaded");
			} catch (IOException e) {
				log.error("File upload failed", e);
				getSession().error("File upload failed: " + e.getMessage());
			}
		}
	}

}
