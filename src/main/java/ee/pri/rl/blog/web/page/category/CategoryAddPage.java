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

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import ee.pri.rl.blog.exception.CategoryExistsException;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.common.form.CancelButton;
import ee.pri.rl.blog.web.page.common.message.LocalFeedbackPanel;
import ee.pri.rl.blog.web.page.index.IndexPage;

/**
 * Page for adding a new category.
 * 
 * @author Raivo Laanemets
 */
public class CategoryAddPage extends BasePage implements AuthPageMarker {
	private static final Logger log = Logger.getLogger(CategoryAddPage.class);
	
	public CategoryAddPage() {
		add(new AddCategoryForm("addForm"));
	}
	
	private static class AddCategoryForm extends Form<CategoryFormModel> {
		private static final long serialVersionUID = 1L;
		
		public AddCategoryForm(String id) {
			super(id, new CompoundPropertyModel<CategoryFormModel>(new CategoryFormModel()));
			
			add(new LocalFeedbackPanel("feedback", this));
			add(new TextField<Void>("name"));
			
			add(new Button("save") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					CategoryFormModel category = AddCategoryForm.this.getModelObject();
					if (StringUtils.isEmpty(category.getName())) {
						error("Category name must not be empty");
					} else {
						try {
							BlogServiceHolder.get().newCategory(category.getName());
							getSession().info("Category added");
							setRedirect(true);
							setResponsePage(IndexPage.class);
						} catch (CategoryExistsException e) {
							log.warn("Category " + category.getName() + " exists already", e);
							error("Category already exists");
						}
					}
				}
			});
			add(new CancelButton("cancel", IndexPage.class));
		}
	}
	
	private static class CategoryFormModel implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
