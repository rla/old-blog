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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Filter that removes jsession ID from URLs. It required here for caching to
 * work efficiently. Read more from
 * http://randomcoder.com/articles/jsessionid-considered-harmful
 * 
 * @author Raivo Laanemets
 */
public class JSessionIdRemoverFilter implements Filter {

	@Override
	public void destroy() {
		// Nothing to destroy.
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		if (request instanceof HttpServletRequest) {
			// Wrapper to make jsessionid dispappear (do not use standard url handling mechanism).
			HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper((HttpServletResponse) response) {
				@Override
				public String encodeRedirectUrl(String url) {
					return url;
				}

				@Override
				public String encodeRedirectURL(String url) {
					return url;
				}

				@Override
				public String encodeUrl(String url) {
					return url;
				}

				@Override
				public String encodeURL(String url) {
					return url;
				}
			};
			chain.doFilter(request, wrappedResponse);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// Nothing to set up.
	}

}
