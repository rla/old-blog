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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.pri.rl.blog.dao.BlogDao;
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
import ee.pri.rl.blog.model.Setting;
import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.util.FileUtil;
import ee.pri.rl.blog.web.textile.BlogTextileToHtml;

/**
 * Implementation of blog service. The service is exposed as
 * a Spring bean "blogService".
 * 
 * @see BlogService
 * 
 * @author Raivo Laanemets
 */
@Service(value = "blogService")
public class BlogServiceImpl implements BlogService {
	private static final Logger log = Logger.getLogger(BlogServiceImpl.class);
	
	private Map<String, String> fileChecksums;
	
	@Autowired
	private BlogDao blogDao;
	
	public BlogServiceImpl() {
		fileChecksums = new HashMap<String, String>();
	}
	
	/**
	 * @see BlogService#deleteCategory(Long)
	 */
	@Override
	@Transactional
	public void deleteCategory(Long categoryId) throws NoSuchCategoryException {
		log.info("Deleting category " + categoryId);
		
		blogDao.deleteCategory(categoryId);
	}
	
	/**
	 * @see BlogService#deleteComment(Long)
	 */
	@Override
	@Transactional
	public void deleteComment(Long commentId) throws NoSuchCommentException {
		log.info("Deleting comment " + commentId);
		
		blogDao.deleteComment(commentId);
	}

	/**
	 * @see BlogService#deleteEntry(String)
	 */
	@Override
	@Transactional
	public void deleteEntry(String entryName) throws NoSuchEntryException {
		log.info("Deleting entry " + entryName);
		
		Entry entry = blogDao.getEntry(entryName);
		entry.getCategories().clear();
		blogDao.deleteEntry(entry);
		
		File uploadPath = new File(getSetting(SettingName.UPLOAD_PATH).getValue());
		File directory = new File(uploadPath, entryName);
		
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException e) {
				log.warn("Cannot delete entry " + entryName + " files directory");
			}
		}
	}
	
	/**
	 * @see BlogService#deleteFile(String, String)
	 */
	@Override
	public void deleteFile(String entryName, String fileName) throws IOException {
		log.info("Deleting file " + fileName + " from " + entryName);
		
		File uploadPath = new File(getSetting(SettingName.UPLOAD_PATH).getValue());
		File directory = new File(uploadPath, entryName);
		File file = new File(directory, fileName);
		
		if (file.exists() && file.canWrite()) {
			if (file.delete()) {
				log.info("File deleted");
			} else {
				throw new IOException("Could not delete file");
			}
		} else {
			throw new IOException("File does not exist or cannot be deleted");
		}
	}
	
	/**
	 * @see BlogService#getCategories()
	 */
	public List<Category> getCategories() {
		return blogDao.getCategories();
	}
	
	/**
	 * @see BlogService#getCategory(String)
	 */
	@Override
	public Category getCategory(String name) throws NoSuchCategoryException {
		return blogDao.getCategory(name);
	}
	
	/**
	 * @see BlogService#getCategoryEntries(String, boolean)
	 */
	@Override
	public List<Entry> getCategoryEntries(String name, boolean authenticated) {
		return blogDao.getCategoryEntries(name, authenticated);
	}

	/**
	 * @see BlogService#getComment(Long)
	 */
	@Override
	public Comment getComment(Long commentId) {
		return blogDao.getComment(commentId);
	}
	
	/**
	 * @see BlogService#getEntries(boolean)
	 */
	@Override
	public List<Entry> getEntries(boolean authenticated) {
		return blogDao.getEntries(authenticated);
	}

	/**
	 * @see BlogService#getEntry(String)
	 */
	@Override
	public Entry getEntry(String entryName) throws NoSuchEntryException {
		return blogDao.getEntry(entryName);
	}

	/**
	 * @see BlogService#getEntryTitle(String)
	 */
	@Override
	public String getEntryTitle(String entryName) throws NoSuchEntryException {
		return blogDao.getEntryTitle(entryName);
	}

	/**
	 * @see BlogService#getFiles(String)
	 */
	@Override
	public List<File> getFiles(String entryName) {
		File directory = new File(new File(getSetting(SettingName.UPLOAD_PATH).getValue()), entryName);
		if (directory.exists()) {
			return Arrays.asList(directory.listFiles());
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * @see BlogService#getLatestEntries(boolean)
	 */
	@Override
	public List<Entry> getLatestEntries(boolean authenticated) {
		int maxEntries = getSetting(SettingName.NUM_MAIN_ENTRIES).getIntValue();
		
		return blogDao.getLatestEntries(authenticated, maxEntries);
	}

	/**
	 * @see BlogService#getNonEmptyCategories(boolean)
	 */
	@Override
	public List<Category> getNonEmptyCategories(boolean authenticated) {
		return blogDao.getNonEmptyCategories(authenticated);
	}

	/**
	 * @see BlogService#getSetting(SettingName)
	 */
	@Override
	public Setting getSetting(SettingName name) {
		return blogDao.getSetting(name);
	}

	/**
	 * @see BlogService#getSettings()
	 */
	@Override
	public List<Setting> getSettings() {
		return blogDao.getSettings();
	}

	/**
	 * @see BlogService#getUploadedFileTag(String)
	 */
	@Override
	public String getUploadedFileTag(String path) throws NoSuchFileException {
		synchronized (fileChecksums) {
			if (!fileChecksums.containsKey(path)) {
				File directory = new File(getSetting(SettingName.UPLOAD_PATH).getValue());
				File file = new File(directory, path);
				try {
					log.info("Calculating checksum for " + path);
					fileChecksums.put(path, DigestUtils.md5Hex(FileUtils.readFileToByteArray(file)));
				} catch (IOException e) {
					log.warn("Cannot find checksum for " + path + ": " + e.getMessage());
					throw new NoSuchFileException(path, e);
				}
			}
			
			return fileChecksums.get(path);
		}
	}

	/**
	 * @see BlogService#isOwnerPasswordMatch(String)
	 */
	@Override
	public boolean isOwnerPasswordMatch(String password) {
		return getSetting(SettingName.OWNER_PASSWORD).getValue().equals(password);
	}

	/**
	 * @see BlogService#isPrivateEntry(String)
	 */
	@Override
	public boolean isPrivateEntry(String entryName) {
		return blogDao.isPrivateEntry(entryName);
	}

	/**
	 * @see BlogService#loadIdentifiable(Class, Serializable)
	 */
	@Override
	public <T> T loadIdentifiable(Class<T> clazz, Serializable id) {
		return blogDao.loadIdentifiable(clazz, id);
	}

	/**
	 * @see BlogService#newCategory(String)
	 */
	@Override
	@Transactional
	public void newCategory(String name) throws CategoryExistsException {
		try {
			blogDao.getCategory(name);
			log.info("Category " + name + " exists already");
			throw new CategoryExistsException(name);
		} catch (NoSuchCategoryException e) {
			log.warn("New category: " + name);
			Category category = new Category();
			category.setName(name);
			blogDao.newCategory(category);
		}
	}

	/**
	 * @see BlogService#newComment(String, String, String, String, String, String)
	 */
	@Override
	@Transactional
	public void newComment(String entryName, String contents, String author, String email, String webpage, String ip) throws NoSuchEntryException, CommentingNowAllowedException {
		log.info("Adding new comment to " + entryName + " by " + author + " from " + ip);
		
		Entry entry = blogDao.getEntry(entryName);
		
		if (!entry.isCommentingAllowed()) {
			throw new CommentingNowAllowedException(entry.getTitle());
		}
		
		Comment comment = new Comment();
		comment.setAuthor(author);
		comment.setContents(contents);
		comment.setEmail(email);
		comment.setEntry(entry);
		comment.setIp(ip);
		comment.setPostDate(new Date());
		comment.setWebpage(webpage);
		
		blogDao.newComment(comment);
	}

	/**
	 * @see BlogService#newEntry(String)
	 */
	@Transactional
	public String newEntry(String title) throws EntryExistsException {
		log.info("Creating new entry with title " + title);
		
		String entryName = title.replace(' ', '_');
		
		if (blogDao.hasEntry(entryName)) {
			log.warn("Entry " + title + " already exists");
			throw new EntryExistsException(title);
		}
		
		Entry entry = new Entry();
		entry.setTitle(title);
		entry.setContents("h1. " + title);
		entry.setCreationDate(new Date());
		entry.setLastModificationDate(new Date());
		entry.setPrivateEntry(true);
		entry.setEntryName(entryName);
		entry.setCommentingAllowed(false);
		
		blogDao.newEntry(entry);
		
		return entry.getEntryName();
	}

	/**
	 * @see BlogService#newUploadFile(String, String, byte[])
	 */
	@Override
	@Transactional
	public void newUploadFile(String entryName, String name, byte[] contents) throws IOException {
		synchronized (fileChecksums) {
			name = FilenameUtils.getName(name);
			
			log.info("Saving uploaded file " + name + " to entry " + entryName);
			
			File uploadPath = new File(getSetting(SettingName.UPLOAD_PATH).getValue());
			File directory = new File(uploadPath, entryName);
			if (!directory.exists()) {
				directory.mkdir();
			}
			
			File newFile = new File(directory, name);
			if (!FileUtil.insideDirectory(newFile, directory)) {
				log.warn("Uploaded file " + name + " is not in the upload directory");
				return;
			}
			FileUtils.writeByteArrayToFile(newFile, contents);
			
			log.info("Uploaded file " + newFile);
			
			fileChecksums.remove(name);
		}
	}

	/**
	 * @see BlogService#renderEntry(String)
	 */
	@Override
	public String renderEntry(String fileName) throws NoSuchEntryException {
		log.info("Rendering entry " + fileName);
		
		Entry entry = blogDao.getEntry(fileName);

		return renderWithTextile(entry);
	}

	/**
	 * Renders the entry with textile.
	 */
	private String renderWithTextile(Entry entry) {
		return new BlogTextileToHtml(
			entry,
			getSetting(SettingName.FILE_BASE_PATH).getValue(),
			getSetting(SettingName.LINK_BASE_PATH).getValue()
		).getHtml();
	}

	/**
	 * @see BlogService#setUp()
	 */
	@Override
	@Transactional
	public void setUp() throws NoSuchEntryException, EntryExistsException, CategoryExistsException, CommentingNowAllowedException {
		if (blogDao.getSettings().isEmpty()) {
			log.info("No settings found from database, setting up blog");
			
			blogDao.newSetting(new Setting(SettingName.NUM_MAIN_ENTRIES, "10"));
			blogDao.newSetting(new Setting(SettingName.OWNER_PASSWORD, "admin"));
			blogDao.newSetting(new Setting(SettingName.EDITOR_ROWS, "15"));
			blogDao.newSetting(new Setting(SettingName.EDITOR_COLS, "60"));
			blogDao.newSetting(new Setting(SettingName.UPLOAD_PATH, "files"));
			blogDao.newSetting(new Setting(SettingName.LINK_BASE_PATH, "/blog/app/entry/e"));
			blogDao.newSetting(new Setting(SettingName.FILE_BASE_PATH, "/blog/files"));
			blogDao.newSetting(new Setting(SettingName.MAIN_ENTRY, "Main"));
			blogDao.newSetting(new Setting(SettingName.MAIN_DISPLAY_ENTRIES, "true"));
			blogDao.newSetting(new Setting(SettingName.MAIN_DISPLAY_CATEGORIES, "true"));
			blogDao.newSetting(new Setting(SettingName.MAIN_DISPLAY_ENTRIES_LINK, "true"));
			blogDao.newSetting(new Setting(SettingName.PAGE_CACHE_ENABLED, "true"));

			newCategory("Development");
			newCategory("Sport");
			newCategory("Biking");

			newEntry("Main");
			String entryName = newEntry("First Entry");

			// FIXME add categories

			newComment(entryName, "First comment", "Raivo", "rl@starline.ee", "http://www.rl.pri.ee", "192.168.0.1");
			newComment(entryName, "Second comment", "Raivo", "rl@starline.ee", "http://www.rl.pri.ee", "192.168.0.1");
			newComment(entryName, "Third comment", "Raivo", "rl@starline.ee", "http://www.rl.pri.ee", "192.168.0.1");
		}
	}

	/**
	 * @see BlogService#updateEntry(String, String, boolean, boolean, List)
	 */
	@Override
	@Transactional
	public void updateEntry(String fileName, String contents, boolean privateEntry, boolean commentingAllowed, List<Long> categoryIds) throws NoSuchEntryException {
		Entry entry = blogDao.getEntry(fileName);
		
		log.info("Updating entry contents with title " + entry.getTitle());
		
		entry.setContents(contents);
		entry.setPrivateEntry(privateEntry);
		entry.setCommentingAllowed(commentingAllowed);
		entry.setLastModificationDate(new Date());
		entry.getCategories().clear();
		
		for (Category category : getCategories()) {
			if (categoryIds.contains(category.getId())) {
				entry.getCategories().add(category);
			}
		}
		
		blogDao.updateEntry(entry);
	}

	/**
	 * @see BlogService#updateSetting(Setting)
	 */
	@Override
	@Transactional
	public void updateSetting(Setting setting) {
		log.info("Updating setting " + setting.getName());
		
		blogDao.updateSetting(setting);
	}

}
