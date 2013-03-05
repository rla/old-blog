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
package ee.pri.rl.blog.dao;

import java.io.Serializable;
import java.util.List;

import org.springmodules.cache.annotations.CacheFlush;
import org.springmodules.cache.annotations.Cacheable;

import ee.pri.rl.blog.exception.NoSuchCategoryException;
import ee.pri.rl.blog.exception.NoSuchCommentException;
import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.model.Comment;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.model.Setting;
import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.web.model.EntityModel;

/**
 * Database interface of the blog application.
 */
public interface BlogDao {
	
	/**
	 * Deletes the category with the given id.
	 * 
	 * @throws NoSuchCategoryException When the category does not exist.
	 */
	void deleteCategory(Long categoryId) throws NoSuchCategoryException;

	/**
	 * Deletes the comment by its id.
	 * 
	 * @throws NoSuchCommentException When the comment does not exist.
	 */
	void deleteComment(Long commentId) throws NoSuchCommentException;
	
	/**
	 * Deletes the given entry.
	 * 
	 * @throws NoSuchEntryException When the entry does not exist in database.
	 */
	void deleteEntry(Entry entry) throws NoSuchEntryException;
	
	/**
	 * Retrieves the list of all categories.
	 */
	List<Category> getCategories();

	/**
	 * Retrieves the category based on its id.
	 */
	Category getCategory(Long id);
	
	/**
	 * Retrieves the category by its name.
	 * 
	 * @param name Name of the category.
	 * @throws NoSuchCategoryException When category is not found.
	 */
	Category getCategory(String name) throws NoSuchCategoryException;
	
	/**
	 * Returns entries of the given category.
	 * 
	 * @param authenticated When true, then private entries will be returned too.
	 */
	List<Entry> getCategoryEntries(String name, boolean authenticated);

	/**
	 * Returns the specified comment.
	 */
	Comment getComment(Long commentId);
	
	/**
	 * Retrieves the list of all entries.
	 */
	List<Entry> getEntries(boolean authenticated);
	
	/**
	 * Retrieves the entry based on entry name. If not found then
	 * throws {@link NoSuchEntryException};
	 */
	Entry getEntry(String entryName) throws NoSuchEntryException;
	
	/**
	 * Returns the title of given entry.
	 */
	String getEntryTitle(String entryName) throws NoSuchEntryException;
	
	/**
	 * Retrieves last modified entries.
	 * 
	 * @param privateAllowed When true, then private entries will be retrieved too.
	 * @param maxEntries Maximum number of entries to retrieve.
	 */
	List<Entry> getLatestEntries(boolean privateAllowed, int maxEntries);
	
	/**
	 * Retrieves the list of categories that contain at least one entry.
	 * 
	 * @param privateAllowed When true then categories with at least on private entry will be returned too.
	 */
	List<Category> getNonEmptyCategories(boolean privateAllowed);

	/**
	 * Retrieves the setting based on setting name.
	 */
	@Cacheable(modelId = "settingCacheModel")
	Setting getSetting(SettingName name);

	/**
	 * Retrieves the list of all settings.
	 */
	List<Setting> getSettings();
	
	/**
	 * Checks whether the entry exists.
	 */
	boolean hasEntry(String entryName);

	/**
	 * Returns whether the given entry is private entry.
	 * If the entry does not exist then it returns <code>false</code>.
	 */
	boolean isPrivateEntry(String entryName);

	/**
	 * Utility method used by smart entity model. Loads entity by its
	 * primary key.
	 * 
	 * @see EntityModel
	 */
	<T> T loadIdentifiable(Class<T> clazz, Serializable id);

	/**
	 * Stores new category in the database.
	 */
	void newCategory(Category category);
	
	/**
	 * Stores new comment in the database.
	 */
	void newComment(Comment comment);

	/**
	 * Stores new entry in the database.
	 */
	void newEntry(Entry entry);

	/**
	 * Stores new setting in the database.
	 */
	void newSetting(Setting setting);

	/**
	 * Updates the given category.
	 */
	void updateCategory(Category category);

	/**
	 * Updates the given entry.
	 */
	void updateEntry(Entry entry);

	/**
	 * Updates the given setting.
	 */
	@CacheFlush(modelId = "settingFlushModel")
	void updateSetting(Setting setting);
}
