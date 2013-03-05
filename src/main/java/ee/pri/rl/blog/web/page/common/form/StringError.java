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
package ee.pri.rl.blog.web.page.common.form;

import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidationError;

/**
 * Validation error that uses specified message instead of message key as the
 * validation error message.
 * 
 * @author Raivo Laanemets
 */
public class StringError implements IValidationError {
	private String errorMessage;

	public StringError(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getErrorMessage(IErrorMessageSource errorMessageSource) {
		return errorMessage;
	}

}
