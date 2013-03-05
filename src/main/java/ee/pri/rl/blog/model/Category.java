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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

/**
 * Represents blog category. The category can contain any number of entries and
 * also one entry can be in any number of categories.
 * 
 * @author Raivo Laanemets
 */
@Entity
public class Category implements Identifiable<Long> {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true, length = 150)
	private String name;

	@ManyToMany(mappedBy = "categories")
	@OrderBy("lastModificationDate DESC")
	private List<Entry> entries = new ArrayList<Entry>();

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Category && ((Category) obj).getName().equals(name);
	}

	/**
	 * Returns the entries from this category.
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * Returns the identificator of this category.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the name of this category.
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Sets the name of this category.
	 * 
	 * @param name New name of the category.
	 */
	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		this.name = name;
	}

}
