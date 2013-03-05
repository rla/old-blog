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
package ee.pri.rl.blog.web;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.settings.IExceptionSettings;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ee.pri.rl.blog.service.BlogService;
import ee.pri.rl.blog.web.page.auth.BlogAuthorizationStrategy;
import ee.pri.rl.blog.web.page.auth.LoginPage;
import ee.pri.rl.blog.web.page.category.CategoryAddPage;
import ee.pri.rl.blog.web.page.category.CategoryDeletePage;
import ee.pri.rl.blog.web.page.category.CategoryListPage;
import ee.pri.rl.blog.web.page.category.CategoryPage;
import ee.pri.rl.blog.web.page.entry.EntryAddCommentPage;
import ee.pri.rl.blog.web.page.entry.EntryAddPage;
import ee.pri.rl.blog.web.page.entry.EntryDeleteCommentPage;
import ee.pri.rl.blog.web.page.entry.EntryDeletePage;
import ee.pri.rl.blog.web.page.entry.EntryListPage;
import ee.pri.rl.blog.web.page.entry.EntryViewPage;
import ee.pri.rl.blog.web.page.entry.edit.EntryDeleteFilePage;
import ee.pri.rl.blog.web.page.entry.edit.EntryEditPage;
import ee.pri.rl.blog.web.page.entry.edit.EntryFilesPage;
import ee.pri.rl.blog.web.page.error.BlogErrorPage;
import ee.pri.rl.blog.web.page.index.IndexPage;
import ee.pri.rl.blog.web.page.setting.SettingsPage;

/**
 * Web application class for the blog application. Here are configured how the
 * Wicket part of the application works.
 * 
 * @author Raivo Laanemets
 */
public class BlogApplication extends WebApplication {
	private static final Logger log = Logger.getLogger(BlogApplication.class);

	/**
	 * Returns the default page of the blog application - index page.
	 * 
	 * @see IndexPage
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return IndexPage.class;
	}

	@Override
	protected void init() {		
		// Set the blog service for the service holder.
		BlogServiceHolder.set((BlogService) WebApplicationContextUtils.getWebApplicationContext(getServletContext())
				.getBean("blogService"));

		// See method comments.
		mountBookmarkablePages();
		configureSecurity();
		setupErrorHandling();
		setupDatabaseWhenNecessary();

		// Strip wicket:* tags from the HTML output.
		getMarkupSettings().setStripWicketTags(true);

		// Strip comments from HTML output.
		getMarkupSettings().setStripComments(true);
	}

	/**
	 * Sets up the error handling.
	 */
	private void setupErrorHandling() {
		// Page expired - redirect to the index page.
		getApplicationSettings().setPageExpiredErrorPage(IndexPage.class);
		// Internal error - show error page.
		getApplicationSettings().setInternalErrorPage(BlogErrorPage.class);
		// Unexpected exception - show error page.
		getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
	}

	/**
	 * Mounts bookmarkable pages.
	 */
	private void mountBookmarkablePages() {
		mountBookmarkablePage("index", IndexPage.class);
		mountBookmarkablePage("entry", EntryViewPage.class);
		mountBookmarkablePage("edit", EntryEditPage.class);
		mountBookmarkablePage("delete", EntryDeletePage.class);
		mountBookmarkablePage("comment", EntryAddCommentPage.class);
		mountBookmarkablePage("category", CategoryPage.class);
		mountBookmarkablePage("settings", SettingsPage.class);
		mountBookmarkablePage("addEntry", EntryAddPage.class);
		mountBookmarkablePage("addCategory", CategoryAddPage.class);
		mountBookmarkablePage("login", LoginPage.class);
		mountBookmarkablePage("entries", EntryListPage.class);
		mountBookmarkablePage("categories", CategoryListPage.class);
		mountBookmarkablePage("deleteCategory", CategoryDeletePage.class);
		mountBookmarkablePage("deleteComment", EntryDeleteCommentPage.class);
		mountBookmarkablePage("entryFiles", EntryFilesPage.class);
		mountBookmarkablePage("deleteFile", EntryDeleteFilePage.class);
		mountBookmarkablePage("error", BlogErrorPage.class);
	}

	/**
	 * Sets up the blog database. Calls {@link BlogService#setUp()}.
	 */
	private void setupDatabaseWhenNecessary() {
		try {
			BlogServiceHolder.get().setUp();
		} catch (Exception e) {
			log.error("Blog setup failed", e);
		}
	}

	/**
	 * Configures blog application security.
	 */
	private void configureSecurity() {
		// Sets the authorization strategy.
		getSecuritySettings().setAuthorizationStrategy(new BlogAuthorizationStrategy());

		// Page that is redirected to when non-authorized user
		// tries to access secured page.
		getApplicationSettings().setAccessDeniedPage(LoginPage.class);
	}

	/**
	 * Method for creating a new custom blog session. Overrides
	 * {@link WebApplication#newSession()}.
	 */
	@Override
	public Session newSession(Request request, Response response) {
		return new BlogSession(request);
	}

	@Override
	protected ISessionStore newSessionStore() {
		return new HttpSessionStore(this);
	}

}
