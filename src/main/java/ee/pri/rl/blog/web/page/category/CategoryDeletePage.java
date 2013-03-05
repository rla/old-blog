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

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import ee.pri.rl.blog.exception.NoSuchCategoryException;
import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;

/**
 * Page for deleting a category. Asks confirmation.
 * 
 * @author Raivo Laanemets
 */
public class CategoryDeletePage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(CategoryDeletePage.class);

	public CategoryDeletePage(PageParameters parameters) {
		String categoryName = parameters.getString("c");
		
		try {
			Category category = BlogServiceHolder.get().getCategory(categoryName);
			add(new Label("title", "Delete category " + categoryName + "?"));
			add(new ConfirmDeleteLink("confirmLink", categoryName, category.getId()));
			add(new BookmarkablePageLink<Void>("cancelLink", CategoryListPage.class));
		} catch (NoSuchCategoryException e) {
			log.warn("Category " + categoryName + " does not exist", e);
			getSession().error("Category " + categoryName + " does not exist");
			setRedirect(true);
			setResponsePage(CategoryListPage.class);
		}
	}
	
	private static class ConfirmDeleteLink extends Link<String> {
		private static final long serialVersionUID = 1L;
		
		private String categoryName;
		private Long categoryId;

		public ConfirmDeleteLink(String id, String categoryName, Long categoryId) {
			super(id);
			this.categoryName = categoryName;
			this.categoryId = categoryId;
		}

		@Override
		public void onClick() {
			try {
				BlogServiceHolder.get().deleteCategory(categoryId);
				getSession().info("Category " + categoryName + " deleted");
				setRedirect(true);
				setResponsePage(CategoryListPage.class);
			} catch (NoSuchCategoryException e) {
				log.warn("Category " + categoryName + " was aready deleted", e);
				getSession().error("Category " + categoryName + " does not exist");
				setRedirect(true);
				setResponsePage(CategoryListPage.class);
			}
		}
		
	}
}
