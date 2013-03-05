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
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * Represents one blog entry. Entry can be in any number of categories and can
 * have any number of comments.
 * 
 * @author Raivo Laanemets
 */
@Entity
public class Entry implements Identifiable<String> {
	@Column(nullable = false, length = 200000)
	@Basic(fetch = FetchType.LAZY)
	private String contents;

	@Column(nullable = false, length = 150, updatable = false)
	private String title;

	@Id
	@Column(length = 150, updatable = false)
	private String entryName;

	@OneToMany(targetEntity = Comment.class, mappedBy = "entry", cascade = CascadeType.REMOVE)
	@OrderBy("postDate ASC")
	private List<Comment> comments = new ArrayList<Comment>();

	@ManyToMany
	@JoinTable(
		name = "Entry_Category",
		joinColumns = @JoinColumn(name = "entry_name", referencedColumnName = "entryName"),
		inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
	)
	@OrderBy("name ASC")
	private List<Category> categories = new ArrayList<Category>();

	@Column(nullable = false, updatable = false)
	private Date creationDate;

	@Column(nullable = false)
	private Date lastModificationDate;

	private boolean privateEntry = true;

	private boolean commentingAllowed = true;

	/**
	 * Returns the categories in which this entry is.
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * Returns the comments of this entry.
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * Returns the contents of the entry.
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * Returns the date when this entry was created.
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Returns the name of this entry. The name serves as an identificator for entries.
	 */
	public String getEntryName() {
		return entryName;
	}

	/**
	 * Returns the identification of the entry.
	 * 
	 * @see Identifiable#getId()
	 */
	@Override
	public String getId() {
		return entryName;
	}

	/**
	 * Returns the date when entry was last modified.
	 */
	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	/**
	 * Returns the title of the entry.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns whether commenting is allowed for this entry.
	 */
	public boolean isCommentingAllowed() {
		return commentingAllowed;
	}

	/**
	 * Returns whether the entry is private (e.g can only be seen by
	 * authorized user).
	 */
	public boolean isPrivateEntry() {
		return privateEntry;
	}

	/**
	 * Sets whether the commenting is allowed for this entry.
	 */
	public void setCommentingAllowed(boolean commentingAllowed) {
		this.commentingAllowed = commentingAllowed;
	}

	/**
	 * Sets the contents of the entry.
	 */
	public void setContents(String contents) {
		if (contents == null) {
			throw new IllegalArgumentException("Contents cannot be null");
		}
		this.contents = contents;
	}

	/**
	 * Sets the creation date of this entry.
	 */
	public void setCreationDate(Date creationDate) {
		if (creationDate == null) {
			throw new IllegalArgumentException("Creation date cannot be null");
		}
		this.creationDate = creationDate;
	}

	/**
	 * Sets the name of this entry.
	 */
	public void setEntryName(String entryName) {
		if (entryName == null) {
			throw new IllegalArgumentException("Entry name cannot be null");
		}
		this.entryName = entryName;
	}

	/**
	 * Sets the last modification date of this entry.
	 */
	public void setLastModificationDate(Date lastModificationDate) {
		if (lastModificationDate == null) {
			throw new IllegalArgumentException("Last modification date cannot be null");
		}
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * Sets whether this entry is private entry or not. Private
	 * entry can only be read by the authorized blog owner.
	 */
	public void setPrivateEntry(boolean privateEntry) {
		this.privateEntry = privateEntry;
	}

	/**
	 * Sets the title of this entry.
	 * 
	 * @param title The new title.
	 */
	public void setTitle(String title) {
		if (title == null) {
			throw new IllegalArgumentException("Title cannot be null");
		}
		this.title = title;
	}

}
