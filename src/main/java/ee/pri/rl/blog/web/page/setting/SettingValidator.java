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

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.web.page.common.form.StringError;

/**
 * Separate validator for validating various blog settings.
 */
public class SettingValidator implements IValidator<String> {
	private static final long serialVersionUID = 1L;
	
	private SettingName name;

	public SettingValidator(SettingName name) {
		this.name = name;
	}

	@Override
	public void validate(IValidatable<String> validatable) {
		String value = validatable.getValue();
		if (StringUtils.isEmpty(value)) {
			error(validatable, "Value cannot be empty");
		}
		switch (name) {
		case EDITOR_COLS:
		case EDITOR_ROWS:
			validateEditorSettings(validatable);
			break;
		case FILE_BASE_PATH:
		case MAIN_ENTRY:
		case LINK_BASE_PATH:
			validatePathSettings(validatable);
			break;
		case UPLOAD_PATH:
			validateUploadPath(validatable);
			break;
		case NUM_MAIN_ENTRIES:
			validateNumMainEntries(validatable);
			break;
		case OWNER_PASSWORD:
			validatePassword(validatable);
			break;
		case MAIN_DISPLAY_CATEGORIES:
		case MAIN_DISPLAY_ENTRIES:
		case PAGE_CACHE_ENABLED:
		case MAIN_DISPLAY_ENTRIES_LINK:
			validateBoolean(validatable);
			break;
		default:
			break;
		}
	}
	
	private void validateBoolean(IValidatable<String> validatable) {
		String value = validatable.getValue();
		if (!"true".equals(value) && !"false".equals(value)) {
			error(validatable, "Value can be either true or false");
		}
	}

	private void validatePassword(IValidatable<String> validatable) {
		String value = validatable.getValue();
		if (value.length() < 6) {
			error(validatable, "Password must be at least 6 characters");
		}
	}

	private void validateNumMainEntries(IValidatable<String> validatable) {
		String value = validatable.getValue();
		if (!StringUtils.isNumeric(value)) {
			error(validatable, "Value must be number");
		} else {
			int intValue = Integer.valueOf(value);
			if (intValue < 0) {
				error(validatable, "Value must be greater than or equal to 0");
			}
		}
	}

	private void validateUploadPath(IValidatable<String> validatable) {
		String value = validatable.getValue();
		File directory = new File(value);
		if (!directory.exists()) {
			error(validatable, "Directory must exist");
		} else if (!directory.canWrite()) {
			error(validatable, "Directory must be writable");
		}
	}

	private void validatePathSettings(IValidatable<String> validatable) {}

	private void validateEditorSettings(IValidatable<String> validatable) {
		String value = validatable.getValue();
		if (!StringUtils.isNumeric(value)) {
			error(validatable, "Value must be number");
		} else {
			int intValue = Integer.valueOf(value);
			if (intValue < 10 || intValue > 200) {
				error(validatable, "Value must be between 10 and 200");
			}
		}
	}

	public static void error(IValidatable<String> validatable, String message) {
		validatable.error(new StringError(message));
	}

}
