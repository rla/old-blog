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
package ee.pri.rl.blog.web.servlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ee.pri.rl.blog.exception.NoSuchFileException;
import ee.pri.rl.blog.model.SettingName;
import ee.pri.rl.blog.service.BlogService;
import ee.pri.rl.blog.util.FileUtil;
import ee.pri.rl.blog.web.BlogSession;

public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(FileDownloadServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        BlogService blogService = (BlogService) context.getBean("blogService");
        
		String path = req.getRequestURI().substring(req.getContextPath().length());
		log.debug("Requested file " + path);
		
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.startsWith("files/")) {
			path = path.substring("files/".length());
		}
		
		int slashIndex = path.indexOf('/');
		if (slashIndex > 0) {
			String entryName = path.substring(0, slashIndex);
			log.debug("Entry name: " + entryName);
			boolean authenticated = Session.exists() && ((BlogSession) Session.get()).isAuthenticated();
			if (blogService.isPrivateEntry(entryName) && !authenticated) {
				log.warn("Tried to access private files");
				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		File directory = new File(blogService.getSetting(SettingName.UPLOAD_PATH).getValue());
		File file = new File(directory, path);
		
		if (!file.exists()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			log.warn("File " + file + " does not exist");
			return;
		}
		
		// Check if the requested file is still inside the upload dir.
		if (!FileUtil.insideDirectory(file, directory)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			log.warn("File " + file + " is not inside upload dir");
			return;
		}

		try {
			String calculatedTag = blogService.getUploadedFileTag(path);
			String tag = req.getHeader("If-None-Match");

			if (tag == null) {
				log.debug("Tag not found, sending file");
				sendFile(path, calculatedTag, directory, resp);
			} else if (tag.equals(calculatedTag)) {
				log.debug("Tag matches, sending 304");
				sendNotModified(calculatedTag, resp);
			} else {
				log.debug("Tag does not match, sending file");
				sendFile(path, calculatedTag, directory, resp);
			}
		} catch (NoSuchFileException e) {
			log.warn("File " + file + " does not exist");
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
	}

	private void sendFile(String path, String calculatedTag, File directory, HttpServletResponse resp) {
		File file = new File(directory, path);

		String mimeType = getServletContext().getMimeType(path);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		resp.setHeader("ETag", calculatedTag);
		resp.setDateHeader("Date", System.currentTimeMillis());
		resp.setContentType(mimeType);
		resp.setContentLength((int) file.length());
		long liveTime = 3600 * 24 * 30;
		resp.setDateHeader("Expires", System.currentTimeMillis() + liveTime * 1000);
		resp.setHeader("Cache-Control", "public, max-age=" + liveTime);

		try {
			DataInputStream input = new DataInputStream(new FileInputStream(file));
			try {
				IOUtils.copy(input, resp.getOutputStream());
				resp.getOutputStream().flush();
				resp.getOutputStream().close();
			} catch (IOException e) {
				log.warn("Sending " + file + " failed", e);
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			if (input != null) {
				input.close();
			}
		} catch (IOException e) {
			log.warn("Sending " + file + " failed", e);
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				log.warn("Sending response for " + file + " failed");
			}
		}
	}

	private void sendNotModified(String calculatedTag, HttpServletResponse resp) {
		resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		resp.setHeader("ETag", calculatedTag);
	}

}
