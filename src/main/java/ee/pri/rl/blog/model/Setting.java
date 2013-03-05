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
package ee.pri.rl.blog.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * Represents a blog setting. This entity class is serializable to allow easy
 * caching.
 * 
 * @author Raivo Laanemets
 */
@Entity
public class Setting implements Identifiable<SettingName>, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Enumerated
	private SettingName name;

	@Column(nullable = false, length = 150)
	private String value;

	public Setting(SettingName name, String value) {
		this.name = name;
		this.value = value;
	}

	public Setting() {
	}

	/**
	 * Returns the name of the setting.
	 */
	public SettingName getName() {
		return name;
	}

	/**
	 * Sets the name of the setting.
	 */
	public void setName(SettingName name) {
		if (name == null) {
			throw new IllegalArgumentException("Setting name cannot be null");
		}
		this.name = name;
	}

	/**
	 * Returns the value of the setting.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the setting.
	 */
	public void setValue(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
		this.value = value;
	}

	/**
	 * Returns the integer value of the setting.
	 * 
	 * @throws NumberFormatException When the value is not an integer.
	 */
	public int getIntValue() {
		return Integer.valueOf(value);
	}

	/**
	 * Returns the boolean value of the setting.
	 */
	public boolean getBoolValue() {
		return Boolean.valueOf(value);
	}

	/**
	 * Returns the identifier of the setting. Returns same value
	 * as <code>getName()</code>.
	 */
	@Override
	public SettingName getId() {
		return name;
	}

}
