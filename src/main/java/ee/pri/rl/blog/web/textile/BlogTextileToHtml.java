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
package ee.pri.rl.blog.web.textile;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import ee.pri.rl.blog.model.Entry;

public class BlogTextileToHtml {
	private Entry entry;
	private String fileBasePath;
	private String linkBasePath;
	
	public BlogTextileToHtml(Entry entry, String fileBasePath, String linkBasePath) {
		this.entry = entry;
		this.fileBasePath = fileBasePath;
		this.linkBasePath = linkBasePath;
	}
	
	public String getHtml() {
		StringWriter writer = new StringWriter();

		BlogHtmlBuilder builder = new BlogHtmlBuilder(writer, entry, fileBasePath);

		MarkupParser parser = new MarkupParser(new TextileLanguage());
		parser.setBuilder(builder);
		String contents = entry.getContents();
		
		Pattern pattern = Pattern.compile("\\[\\[([^\\]]*?)\\]\\]");
		Matcher matcher = pattern.matcher(contents);
		
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String title = matcher.group(1);
			matcher.appendReplacement(sb, "<a href=\"" + linkBasePath + "/" + title.replace(' ', '_') + "\">" + title + "</a>");
		}
		matcher.appendTail(sb);
		
		parser.parse(sb.toString());

		return writer.toString();
	}
}
