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


/**
 * Enum for blog setting.
 * 
 * @see Setting
 * 
 * @author Raivo Laanemets
 */
public enum SettingName {
	/**
	 * Blog owner password.
	 */
	OWNER_PASSWORD,
	
	/**
	 * Max number of latest entries displayed on index page.
	 */
	NUM_MAIN_ENTRIES,
	
	/**
	 * Number of lines in the blog entry editor.
	 */
	EDITOR_ROWS,
	
	/**
	 * Number of columns in the blog entry editor.
	 */
	EDITOR_COLS,
	
	/**
	 * The filesystem path where the uploaded files are saved.
	 */
	UPLOAD_PATH,
	
	/**
	 * Prefix part of internal links.
	 */
	LINK_BASE_PATH,
	
	/**
	 * Prefix part for entry files.
	 */
	FILE_BASE_PATH,
	
	/**
	 * Name of the entry that is displayed on the index page.
	 */
	MAIN_ENTRY,
	
	/**
	 * Boolean flag whether to display the list of entries in the index page.
	 */
	MAIN_DISPLAY_ENTRIES,
	
	/**
	 * Boolean flag whether to display the list of categories in the index page.
	 */
	MAIN_DISPLAY_CATEGORIES,
	
	/**
	 * Boolean flag whether to display link "All Entries" in the index page.
	 */
	MAIN_DISPLAY_ENTRIES_LINK,
	
	/**
	 * Boolean flag whether to cache blog pages.
	 */
	PAGE_CACHE_ENABLED
}