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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import ee.pri.rl.blog.model.Setting;
import ee.pri.rl.blog.web.BlogServiceHolder;
import ee.pri.rl.blog.web.page.BasePage;
import ee.pri.rl.blog.web.page.auth.AuthPageMarker;
import ee.pri.rl.blog.web.page.common.form.CancelButton;
import ee.pri.rl.blog.web.page.common.message.LocalFeedbackPanel;

public class SettingEditPage extends BasePage implements AuthPageMarker {
	
	public SettingEditPage(IModel<Setting> setting) {
		Setting settingObject = setting.getObject();
		add(new Label("title", "Edit setting " + settingObject.getName()));
		add(new EditForm("editForm", setting));
	}
	
	private static class EditForm extends Form<Setting> {
		private static final long serialVersionUID = 1L;
		
		private TextField<String> value;
		
		public EditForm(String id, final IModel<Setting> setting) {
			super(id, new CompoundPropertyModel<Setting>(setting));
			add(new LocalFeedbackPanel("feedback", this));
			value = new RequiredTextField<String>("value");
			value.add(new SettingValidator(setting.getObject().getName()));
			add(value);
			add(new Button("save") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit() {
					if (!value.hasErrorMessage()) {
						BlogServiceHolder.get().updateSetting(EditForm.this.getModelObject());
						getSession().info("Setting saved");
						setRedirect(true);
						setResponsePage(SettingsPage.class);
					}
				}
			});
			add(new CancelButton("cancel", SettingsPage.class));
		}
		
	}

}
