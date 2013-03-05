package ee.pri.rl.blog.web;

import ee.pri.rl.blog.service.BlogService;

/**
 * Class for accessing the blog service from web components.
 * 
 * @author Raivo Laanemets
 */
public class BlogServiceHolder {
	private static BlogService blogService;
	
	public static BlogService get() {
		if (blogService == null) {
			throw new IllegalStateException("BlogService instance is null");
		} else {
			return blogService;
		}
	}
	
	public static void set(BlogService blogService) {
		BlogServiceHolder.blogService = blogService;
	}
}
