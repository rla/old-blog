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
package ee.pri.rl.blog.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springmodules.cache.annotations.CacheFlush;

import ee.pri.rl.blog.exception.CategoryExistsException;
import ee.pri.rl.blog.exception.CommentingNowAllowedException;
import ee.pri.rl.blog.exception.EntryExistsException;
import ee.pri.rl.blog.exception.NoSuchCategoryException;
import ee.pri.rl.blog.exception.NoSuchCommentException;
import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.exception.NoSuchFileException;
import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.model.Comment;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.model.Identifiable;
import ee.pri.rl.blog.model.Setting;
import ee.pri.rl.blog.model.SettingName;

/**
 * Service interface of the blog application.
 * 
 * @author Raivo Laanemets
 */
public interface BlogService {
	
	/**
	 * Deletes the category with the given id.
	 * 
	 * @param categoryId Id of the category.
	 * @throws NoSuchCategoryException When the category does not exist.
	 */
	@CacheFlush(modelId = "pageFlushModel")
	void deleteCategory(Long categoryId) throws NoSuchCategoryException;
	
	/**
	 * Deletes the comment with the given id.
	 * 
	 * @param commentId Id of the comment.
	 * @throws NoSuchCommentException When the comment does not exist.
	 */
	@CacheFlush(modelId = "pageFlushModel")
	void deleteComment(Long commentId) throws NoSuchCommentException;
	
	/**
	 * Deletes the entry with the given name.
	 * 
	 * @param entryName Name of the entry.
	 * @throws NoSuchEntryException When the entry does not exist.
	 */
	@CacheFlush(modelId = "pageFlushModel")
	void deleteEntry(String entryName) throws NoSuchEntryException;
	
	/**
	 * Deletes the given file from the given entry.
	 * @throws IOException 
	 */
	void deleteFile(String entryName, String fileName) throws IOException;
	
	/**
	 * Returns the list of all categories.
	 */
	List<Category> getCategories();
	
	/**
	 * Returns the category with the given name.
	 * 
	 * @param name Name of the category.
	 * @throws NoSuchCategoryException When the category does not exist.
	 */
	Category getCategory(String name) throws NoSuchCategoryException;

	/**
	 * Returns the entries of category (by name).
	 * 
	 * @param name Name of the category.
	 * @param authenticated Whether the user is authenticated or not (show private entries?).
	 */
	List<Entry> getCategoryEntries(String name, boolean authenticated);

	/**
	 * Returns the comment by id.
	 * 
	 * @param commentId Id of the comment.
	 */
	Comment getComment(Long commentId);
	
	/**
	 * Returns the list of all entries.
	 * 
	 * @param authenticated Indicates whether to include private entries.
	 */
	List<Entry> getEntries(boolean authenticated);

	/**
	 * Return specific entry by entry name.
	 * 
	 * @param entryName Name of the entry.
	 * @throws NoSuchEntryException When the entry with the specified name does not exist.
	 */
	Entry getEntry(String entryName) throws NoSuchEntryException;

	/**
	 * Returns the entry title by the entry name.
	 * 
	 * @param entryName Name of the entry.
	 * @throws NoSuchEntryException When the entry with the specified name does not exist.
	 */
	String getEntryTitle(String entryName) throws NoSuchEntryException;

	/**
	 * Returns the list of files that belong to the given entry.
	 * 
	 * @param entryName Name of the entry.
	 */
	List<File> getFiles(String entryName);
	
	/**
	 * Returns the list of latest entries.
	 * 
	 * @param authenticated Whether to retrieve entries that are private.
	 */
	List<Entry> getLatestEntries(boolean authenticated);
	
	/**
	 * Returns the list of categories that contain at least one entry.
	 * 
	 * @param authenticated Whether to show categories with at least one private entry.
	 */
	List<Category> getNonEmptyCategories(boolean authenticated);

	/**
	 * Returns the setting with the given value.
	 * 
	 * @param name Name of the setting.
	 */
	Setting getSetting(SettingName name);
	
	/**
	 * Returns the  list of all settings.
	 */
	List<Setting> getSettings();
	
	/**
	 * Calculates the tag (checksum) for the uploaded file.
	 * 
	 * @param path Path to the file. (<code>Entry_Name/Filename.Extension</code>)
	 * @throws NoSuchFileException When the file is not found.
	 */
	String getUploadedFileTag(String path) throws NoSuchFileException;
	
	/**
	 * Returns true when the given password matches the saved value of
	 * the owner password.
	 */
	boolean isOwnerPasswordMatch(String password);
	
	/**
	 * Returns whether the given entry is private entry.
	 * If the entry does not exist then it returns <code>false</code>.
	 */
	boolean isPrivateEntry(String entryName);
	
	/**
	 * Part of Wicket smart entity model.
	 * 
	 * @param <T> Class of entity.
	 * @param clazz Class of entity.
	 * @param id Primary key.
	 * 
	 * @see Identifiable
	 */
	<T> T loadIdentifiable(Class<T> clazz, Serializable id);
	
	/**
	 * Creates a new category with the given name.
	 * 
	 * @param name Name of the new category.
	 * @throws CategoryExistsException When the category already exists.
	 */
	void newCategory(String name) throws CategoryExistsException;
	
	/**
	 * Creates a new comment.
	 * 
	 * @param entryName Name of the entry that will have the comment.
	 * @param contents Contents of the comment.
	 * @param author Author of the comment.
	 * @param email Email of the comment author.
	 * @param webpage Webpage of the comment author.
	 * @param ip IP address of the author.
	 * 
	 * @throws NoSuchEntryException When the entry does not exist.
	 * @throws CommentingNowAllowedException When commenting is not allowed for the entry.
	 */
	@CacheFlush(modelId = "pageFlushModel")
	void newComment(String entryName, String contents, String author, String email, String webpage, String ip) throws NoSuchEntryException, CommentingNowAllowedException;
	
	/**
	 * Creates a new entry.
	 * 
	 * @param title Title of the entry.
	 * @throws EntryExistsException When the entry already exists.
	 */
	String newEntry(String title) throws EntryExistsException;
	
	/**
	 * Stores a new uploaded file.
	 * 
	 * @param entryName Name of the entry that uses the file.
	 * @param name Name of the file.
	 * @param contents Contents of the file.
	 * @throws IOException When storing fails.
	 */
	void newUploadFile(String entryName, String name, byte[] contents) throws IOException;
	
	/**
	 * Render the given entry into HTML.
	 * 
	 * @param entryName Name of the entry.
	 * @throws NoSuchEntryException When the entry does not exist.
	 */
	String renderEntry(String entryName) throws NoSuchEntryException;

	/**
	 * Inserts initial data into blog database. The initial data contains
	 * blog settings with default values and sample entries.
	 */
	void setUp() throws NoSuchEntryException, EntryExistsException, CategoryExistsException, CommentingNowAllowedException;

	/**
	 * Updates the entry.
	 */
	@CacheFlush(modelId = "pageFlushModel")
	void updateEntry(String entryName, String contents, boolean privateEntry, boolean commentingAllowed, List<Long> categoryIds) throws NoSuchEntryException;

	/**
	 * Updates the setting.
	 */
	void updateSetting(Setting setting);

}