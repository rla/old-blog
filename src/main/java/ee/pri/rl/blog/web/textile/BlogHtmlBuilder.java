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

import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.ImageAttributes;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;

import ee.pri.rl.blog.model.Entry;

/**
 * Html builder for Blog markup.
 */
public class BlogHtmlBuilder extends HtmlDocumentBuilder {
	private Entry entry;
	private String fileBasePath;
	
	public BlogHtmlBuilder(Writer out, Entry entry, String fileBasePath) {
		super(out);
		
		this.entry = entry;
		this.fileBasePath = fileBasePath;
		
		setEmitAsDocument(false);
		setPrependImagePrefix(fileBasePath + "/" + entry.getId() + "/");
		setSuppressBuiltInStyles(true);
		setXhtmlStrict(true);
		setUseInlineStyles(false);
	}

	@Override
	public void link(Attributes attributes, String hrefOrHashName, String text) {
		try {
			if (new URI(hrefOrHashName).isAbsolute()) {
				super.link(attributes, hrefOrHashName, text);
			} else {
				super.link(attributes, fileBasePath + "/" + entry.getId() + "/" + hrefOrHashName, text);
			}
		} catch (URISyntaxException e) {
			super.link(attributes, hrefOrHashName, text);
		}
	}

	@Override
	public void image(Attributes attributes, String url) {
		if (attributes instanceof ImageAttributes) {
			final ImageAttributes imageAttributes = (ImageAttributes) attributes;
			
			super.image(new ImageAttributes() {

				@Override
				public String getCssStyle() {
					if (imageAttributes.getAlign() != null) {
						return "text-align: " + imageAttributes.getAlign().toString().toLowerCase();
					} else {
						return null;
					}
				}

				@Override
				public String getAlt() {
					return imageAttributes.getAlt();
				}
				
			}, url);
		} else {
			super.image(attributes, url);
		}
	}

}
