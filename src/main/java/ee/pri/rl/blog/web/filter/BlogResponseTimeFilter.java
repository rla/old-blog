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

import org.apache.log4j.Logger;

/**
 * Filter to show response time. The response time is logged using the standard
 * logging mechanism.
 * 
 * @author Raivo Laanemets
 */
public class BlogResponseTimeFilter implements Filter {
	private static final Logger log = Logger.getLogger(BlogResponseTimeFilter.class);

	@Override
	public void destroy() {
		// Nothing to destroy.
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			long start = System.currentTimeMillis();
			chain.doFilter(request, response);
			log.info(httpServletRequest.getRequestURI() + " took " + (System.currentTimeMillis() - start) + "ms from " + httpServletRequest.getRemoteAddr());
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Nothing to set up.
	}

}
