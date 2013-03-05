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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import ee.pri.rl.blog.exception.NoSuchCategoryException;
import ee.pri.rl.blog.exception.NoSuchCommentException;
import ee.pri.rl.blog.exception.NoSuchEntryException;
import ee.pri.rl.blog.model.Category;
import ee.pri.rl.blog.model.Comment;
import ee.pri.rl.blog.model.Entry;
import ee.pri.rl.blog.model.Setting;
import ee.pri.rl.blog.model.SettingName;

/**
 * {@link BlogDao} implementation with JPA.
 * 
 * @author Raivo Laanemets
 */
@Repository
public class BlogDaoImpl implements BlogDao {
	private static final Logger log = Logger.getLogger(BlogDaoImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @see BlogDao#deleteCategory(Long)
	 */
	@Override
	public void deleteCategory(Long categoryId) throws NoSuchCategoryException {
		try {
			entityManager.remove(entityManager.find(Category.class, categoryId));
		} catch (NoResultException e) {
			throw new NoSuchCategoryException(categoryId);
		}
	}

	/**
	 * @see BlogDao#deleteComment(Long)
	 */
	@Override
	public void deleteComment(Long commentId) throws NoSuchCommentException {
		try {
			entityManager.remove(entityManager.find(Comment.class, commentId));
		} catch (NoResultException e) {
			throw new NoSuchCommentException(commentId);
		}
	}

	/**
	 * @see BlogDao#deleteEntry(Entry)
	 */
	@Override
	public void deleteEntry(Entry entry) throws NoSuchEntryException {
		try {
			entityManager.remove(entry);
		} catch (NoResultException e) {
			throw new NoSuchEntryException(entry.getEntryName());
		}
		
	}

	/**
	 * @see BlogDao#getCategories()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getCategories() {
		return (List<Category>) entityManager
			.createQuery("SELECT c FROM Category AS c ORDER BY c.name ASC")
			.getResultList();
	}

	/**
	 * @see BlogDao#getCategory(Long)
	 */
	@Override
	public Category getCategory(Long id) {
		return entityManager.find(Category.class, id);
	}

	/**
	 * @see BlogDao#getCategory(String)
	 */
	@Override
	public Category getCategory(String name) throws NoSuchCategoryException {
		try {
			return (Category) entityManager
				.createQuery("SELECT c FROM Category AS c WHERE c.name = :name")
				.setParameter("name", name)
				.getSingleResult();
		} catch (NoResultException e) {
			throw new NoSuchCategoryException(name);
		}
	}

	/**
	 * @see BlogDao#getCategoryEntries(String, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Entry> getCategoryEntries(String name, boolean authenticated) {
		if (authenticated) {
			return entityManager
				.createQuery("SELECT DISTINCT OBJECT(e) FROM Entry e, IN(e.categories) c WHERE c.name = :name ORDER BY e.title")
				.setParameter("name", name)
				.getResultList();
		} else {
			return entityManager
				.createQuery("SELECT DISTINCT OBJECT(e) FROM Entry e, IN(e.categories) c WHERE c.name = :name AND e.privateEntry = false ORDER BY e.title")
				.setParameter("name", name)
				.getResultList();
		}
	}

	/**
	 * @see BlogDao#getComment(Long)
	 */
	@Override
	public Comment getComment(Long commentId) {
		return entityManager.find(Comment.class, commentId);
	}

	/**
	 * @see BlogDao#getEntries(boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Entry> getEntries(boolean authenticated) {
		String mainEntry = getSetting(SettingName.MAIN_ENTRY).getValue();
		if (authenticated) {
			return entityManager
				.createQuery("SELECT e FROM Entry AS e WHERE e.entryName <> :mainEntry ORDER BY e.title")
				.setParameter("mainEntry", mainEntry)
				.getResultList();
		} else {
			return entityManager
				.createQuery("SELECT e FROM Entry AS e WHERE e.privateEntry = false AND e.entryName <> :mainEntry ORDER BY e.title")
				.setParameter("mainEntry", mainEntry)
				.getResultList();
		}
	}

	/**
	 * @see BlogDao#getEntry(String)
	 */
	@Override
	public Entry getEntry(String entryName) throws NoSuchEntryException {
		try {
			return (Entry) entityManager
				.createQuery("SELECT e FROM Entry AS e WHERE e.entryName = :entryName")
				.setParameter("entryName", entryName)
				.getSingleResult();
		} catch (NoResultException e) {
			throw new NoSuchEntryException(entryName);
		}
	}

	/**
	 * @see BlogDao#getEntryTitle(String)
	 */
	@Override
	public String getEntryTitle(String entryName) throws NoSuchEntryException {
		try {
			return (String) entityManager
				.createQuery("SELECT e.title FROM Entry e WHERE e.entryName = :entryName")
				.setParameter("entryName", entryName)
				.getSingleResult();
		} catch (NoResultException e) {
			throw new NoSuchEntryException(entryName);
		}
	}

	/**
	 * @see BlogDao#getLatestEntries(boolean, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Entry> getLatestEntries(boolean privateAllowed, int maxEntries) {
		String mainEntry = getSetting(SettingName.MAIN_ENTRY).getValue();
		if (privateAllowed) {
			return entityManager
				.createQuery("SELECT e FROM Entry AS e WHERE e.entryName <> :mainEntry ORDER BY e.lastModificationDate DESC")
				.setMaxResults(maxEntries)
				.setParameter("mainEntry", mainEntry)
				.getResultList();
		} else {
			return entityManager
				.createQuery("SELECT e FROM Entry AS e WHERE e.privateEntry = false AND e.entryName <> :mainEntry ORDER BY e.lastModificationDate DESC")
				.setMaxResults(maxEntries)
				.setParameter("mainEntry", mainEntry)
				.getResultList();
		}
	}

	/**
	 * @see BlogDao#getNonEmptyCategories(boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getNonEmptyCategories(boolean privateAllowed) {
		if (privateAllowed) {
			return entityManager
				.createQuery("SELECT DISTINCT OBJECT(c) FROM Category c, IN(c.entries) e ORDER BY c.name")
				.getResultList();
		} else {
			return entityManager
				.createQuery("SELECT DISTINCT OBJECT(c) FROM Category c, IN(c.entries) e WHERE e.privateEntry = false ORDER BY c.name")
				.getResultList();
		}
	}

	/**
	 * @see BlogDao#getSetting(SettingName)
	 */
	@Override
	public Setting getSetting(SettingName name) {
		return entityManager.find(Setting.class, name);
	}

	/**
	 * @see BlogDao#getSettings()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Setting> getSettings() {
		return entityManager
			.createQuery("SELECT s FROM Setting AS s ORDER BY s.name")
			.getResultList();
	}

	/**
	 * @see BlogDao#hasEntry(String)
	 */
	@Override
	public boolean hasEntry(String entryName) {
		return ((Long) entityManager
			.createQuery("SELECT COUNT(e) FROM Entry e WHERE e.entryName = :entryName")
			.setParameter("entryName", entryName)
			.getSingleResult()) > 0;
	}

	/**
	 * @see BlogDao#isPrivateEntry(String)
	 */
	@Override
	public boolean isPrivateEntry(String entryName) {
		try {
			return (Boolean) entityManager
				.createQuery("SELECT e.privateEntry FROM Entry e WHERE e.entryName = :entryName")
				.setParameter("entryName", entryName)
				.getSingleResult();
		} catch (NoResultException e) {
			log.warn("Entry " + entryName + " not found");
			return false;
		}
	}

	/**
	 * @see BlogDao#loadIdentifiable(Class, Serializable)
	 */
	@Override
	public <T> T loadIdentifiable(Class<T> clazz, Serializable id) {
		return entityManager.find(clazz, id);
	}

	/**
	 * @see BlogDao#newCategory(Category)
	 */
	@Override
	public void newCategory(Category category) {
		entityManager.persist(category);
	}

	/**
	 * @see BlogDao#newComment(Comment)
	 */
	@Override
	public void newComment(Comment comment) {
		entityManager.persist(comment);
	}

	/**
	 * @see BlogDao#newEntry(Entry)
	 */
	@Override
	public void newEntry(Entry entry) {
		entityManager.persist(entry);
	}

	/**
	 * @see BlogDao#newSetting(Setting)
	 */
	@Override
	public void newSetting(Setting setting) {
		entityManager.persist(setting);
	}

	/**
	 * @see BlogDao#updateCategory(Category)
	 */
	@Override
	public void updateCategory(Category category) {
		entityManager.merge(category);
	}

	/**
	 * @see BlogDao#updateEntry(Entry)
	 */
	@Override
	public void updateEntry(Entry entry) {
		entityManager.merge(entry);
	}

	/**
	 * @see BlogDao#updateSetting(Setting)
	 */
	@Override
	public void updateSetting(Setting setting) {
		entityManager.merge(setting);
	}
		
}
