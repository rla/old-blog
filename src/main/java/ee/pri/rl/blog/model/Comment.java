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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Represents one blog comment. A comment belongs to single entry. One entry can
 * contain any number of comments.
 * 
 * @author Raivo Laanemets
 */
@Entity
public class Comment {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, length = 4096)
	private String contents;

	@Column(nullable = false, length = 30)
	private String author;

	@Column(length = 100, updatable = false)
	private String email;

	@Column(length = 250, updatable = false)
	private String webpage;

	@ManyToOne(fetch = FetchType.LAZY)
	private Entry entry;

	@Column(nullable = false, length = 100, updatable = false)
	private String ip;

	@Column(nullable = false, updatable = false)
	private Date postDate;

	/**
	 * Returns the author of the comment.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Returns the contents of the comment.
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * Returns the email address of the comment author.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the entry for which the comment was made.
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * Returns the identificator of this comment.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the IP address from which the comment was made.
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Returns the date when the comment was posted.
	 */
	public Date getPostDate() {
		return postDate;
	}

	/**
	 * Returns the web page address of the comment author. Might be <code>null</code>.
	 */
	public String getWebpage() {
		return webpage;
	}

	/**
	 * Sets the author of the comment.
	 */
	public void setAuthor(String author) {
		if (author == null) {
			throw new IllegalArgumentException("Author cannot be null");
		}
		this.author = author;
	}

	/**
	 * Sets the contents of the comment.
	 */
	public void setContents(String contents) {
		if (contents == null) {
			throw new IllegalArgumentException("Contents cannot be null");
		}
		this.contents = contents;
	}

	/**
	 * Sets the email address of the comment author.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the entry for which the comment was made.
	 */
	public void setEntry(Entry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Entry cannot be null");
		}
		this.entry = entry;
	}

	/**
	 * Sets the IP address from which the comment was made.
	 * 
	 * @param ip IP address
	 */
	public void setIp(String ip) {
		if (ip == null) {
			throw new IllegalArgumentException("IP address cannot be null");
		}
		this.ip = ip;
	}

	/**
	 * Sets the post date of the comment.
	 */
	public void setPostDate(Date postDate) {
		if (postDate == null) {
			throw new IllegalArgumentException("Post date cannot be null");
		}
		this.postDate = postDate;
	}

	/**
	 * Sets the web page address of the comment author.
	 */
	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

}
