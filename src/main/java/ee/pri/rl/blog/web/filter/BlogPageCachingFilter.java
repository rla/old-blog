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
package ee.pri.rl.blog.web.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.web.filter.CacheFilter;
import com.opensymphony.oscache.web.filter.CacheHttpServletResponseWrapper;
import com.opensymphony.oscache.web.filter.ResponseContent;

import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.service.BlogService;
import ee.pri.rl.blog.web.BlogSession;

/**
 * Page cache for blog. This is oscache-specific solution. It does not take into
 * account query (?var=value) neither #fragment parts of page URI. It is meant
 * for caching rendered blog pages. The cache is bypassed for certain operations
 * like adding a comment. The cache is completely bypassed for the authorized
 * user.
 * 
 * @author Raivo Laanemets
 */
public class BlogPageCachingFilter implements Filter {
	private static final Logger log = Logger.getLogger(BlogPageCachingFilter.class);

	private GeneralCacheAdministrator cacheAdministrator;
	private BlogService blogService;

	@Override
	public void destroy() {
		// Nothing to destroy.
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		boolean cacheEnabled = blogService.getSetting(SettingName.PAGE_CACHE_ENABLED).getBoolValue();
		boolean cacheable = cacheEnabled && !(Session.exists() && ((BlogSession) Session.get()).isAuthenticated());
		if (cacheable && request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;

			try {
				String key = "#PAGE" + new URI(httpServletRequest.getRequestURI()).getPath();
				Cache cache = cacheAdministrator.getCache();

				ResponseContent respContent = null;
				try {
					respContent = (ResponseContent) cache.getFromCache(key);
					log.debug("Sending cached page " + key);
					respContent.writeTo(response, false, false);
				} catch (NeedsRefreshException e) {
					log.debug("Page needs refresh");
					CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper(
							(HttpServletResponse) response, false, Long.MAX_VALUE, CacheFilter.LAST_MODIFIED_OFF,
							CacheFilter.EXPIRES_OFF, 0);

					chain.doFilter(request, cacheResponse);
					cacheResponse.flushBuffer();

					ResponseContent content = cacheResponse.getContent();
					if (cacheResponse.getStatus() == HttpServletResponse.SC_OK) {
						// Store only pages with status SC_OK
						cache.putInCache(key, content, new String[] { "pageCache" }, null, null);
						log.debug("Page " + key + " stored in cache");
					} else {
						// Cancel update
						cache.cancelUpdate(key);
					}
				}
			} catch (URISyntaxException e) {
				log.warn("Invalid URI: " + httpServletRequest.getRequestURI());
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		synchronized (this.getClass()) {
			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
			// Use the cache administrator created by Spring context.
			cacheAdministrator = (GeneralCacheAdministrator) context.getBean("cacheManager");
			blogService = (BlogService) context.getBean("blogService");
		}
	}

}
