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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.model.EntityModel;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.entry.EntryViewPage;
import ee.pri.rl.blog.web.page.index.IndexPage;

public class EntryEditPage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(EntryEditPage.class);

	public EntryEditPage(PageParameters parameters) {
		String entryName = parameters.getString("e");
		try {
			Entry entry = BlogServiceHolder.get().getEntry(entryName);
			EntityModel<Entry> entryModel = new EntityModel<Entry>(entry);

			add(new Label("title", entry.getTitle()));
			add(new EntryEditForm("editForm", entryModel));
			add(new FileListView("files", entry.getEntryName(), false));
			add(new BookmarkablePageLink<Void>("filesLink", EntryFilesPage.class, new PageParameters("e=" + entryName)));
		} catch (NoSuchEntryException e) {
			log.warn("Entry " + entryName + " not found");
			getSession().error("Entry " + entryName + " not found");
			setRedirect(true);
			setResponsePage(IndexPage.class);
		}
	}

	private static class EntryEditForm extends Form<EditFormModel> {
		private static final long serialVersionUID = 1L;

		public EntryEditForm(String id, EntityModel<Entry> entry) {
			super(id, new CompoundPropertyModel<EditFormModel>(new EditFormModel(entry.getObject())));

			add(new TextArea<String>("contents") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					tag.put("rows", BlogServiceHolder.get().getSetting(SettingName.EDITOR_ROWS).getValue());
					tag.put("cols", BlogServiceHolder.get().getSetting(SettingName.EDITOR_COLS).getValue());
				}
			});
			add(new CheckBox("privateEntry"));
			add(new CheckBox("commentingAllowed"));
			
			CheckGroup<String> checks = new CheckGroup<String>("categoryIds");
			add(checks);

			ListView<Category> checksList = new ListView<Category>("categoryIds", new CategoryListModel()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Category> item) {
					item.add(new Check<Long>("id", new Model<Long>(item.getModelObject().getId())));
					item.add(new Label("category", item.getModelObject().getName()));
				};
				
			}.setReuseItems(true);
			checks.add(checksList);

			add(new Button("save") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					try {
						EditFormModel entry = EntryEditForm.this.getModelObject();
						BlogServiceHolder.get().updateEntry(
							entry.getEntryName(),
							entry.getContents(),
							entry.isPrivateEntry(),
							entry.isCommentingAllowed(),
							entry.getCategoryIds()
						);
					} catch (NoSuchEntryException e) {
						log.warn("Entry " + EntryEditForm.this.getModelObject().getEntryName() + " not found");
					}

					String entryName = EntryEditForm.this.getModelObject().getEntryName();
					if (BlogServiceHolder.get().getSetting(SettingName.MAIN_ENTRY).getValue().equals(entryName)) {
						setRedirect(true);
						setResponsePage(IndexPage.class);
					} else {
						setRedirect(true);
						setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
					}
				}
			});

			add(new Button("cancel") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					String entryName = EntryEditForm.this.getModelObject().getEntryName();
					if (BlogServiceHolder.get().getSetting(SettingName.MAIN_ENTRY).getValue().equals(entryName)) {
						setRedirect(true);
						setResponsePage(IndexPage.class);
					} else {
						setRedirect(true);
						setResponsePage(EntryViewPage.class, new PageParameters("e=" + entryName));
					}
				}
			});
		}

	}
	
	private static class CategoryListModel extends LoadableDetachableModel<List<Category>> {
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Category> load() {
			return BlogServiceHolder.get().getCategories();
		}
		
	}

	private static class EditFormModel implements Serializable {
		private static final long serialVersionUID = 1L;

		private String entryName;
		private String contents;
		private boolean privateEntry;
		private boolean commentingAllowed;
		private List<Long> categoryIds;

		public EditFormModel(Entry entry) {
			entryName = entry.getEntryName();
			contents = entry.getContents();
			privateEntry = entry.isPrivateEntry();
			commentingAllowed = entry.isCommentingAllowed();
			categoryIds = new ArrayList<Long>();
			for (Category category : entry.getCategories()) {
				categoryIds.add(category.getId());
			}
		}

		public String getContents() {
			return contents;
		}

		public void setContents(String contents) {
			this.contents = contents;
		}

		public boolean isPrivateEntry() {
			return privateEntry;
		}

		public void setPrivateEntry(boolean privateEntry) {
			this.privateEntry = privateEntry;
		}

		public boolean isCommentingAllowed() {
			return commentingAllowed;
		}

		public void setCommentingAllowed(boolean commentingAllowed) {
			this.commentingAllowed = commentingAllowed;
		}

		public List<Long> getCategoryIds() {
			return categoryIds;
		}

		public void setCategoryIds(List<Long> categoryIds) {
			this.categoryIds = categoryIds;
		}

		public String getEntryName() {
			return entryName;
		}

	}

}
