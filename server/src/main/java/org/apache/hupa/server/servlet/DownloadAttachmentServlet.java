/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.hupa.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.shared.data.User;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPFolder;

/**
 * Handle download of attachments
 * 
 *
 */
public class DownloadAttachmentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1245563204035792963L;
	private InMemoryIMAPStoreCache cache;
	private Log logger;

	@Inject
	public DownloadAttachmentServlet(InMemoryIMAPStoreCache cache, Log logger) {
		this.cache = cache;
		this.logger = logger;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Handle to write back the requested attachment
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String message_uuid = request.getParameter("message_uuid");
		String attachmentName = request.getParameter("attachment_name");
		String sessionId = request.getParameter("sessionId");
		String folderName = request.getParameter("folder_name");
		HttpSession session = request.getSession();
		if (session.getId().equals(sessionId)) {
			response.setContentType("application/download");
			response.setHeader("Content-disposition", "attachment; filename="
					+ attachmentName + "");
			InputStream in = null;
			OutputStream out = response.getOutputStream();
			

			IMAPFolder folder = null;
			try {
				Store store = cache.get((User) request.getSession().getAttribute(
				"user"));
				folder = (IMAPFolder) store.getFolder(folderName);
				if (folder.isOpen() == false) {
					folder.open(Folder.READ_ONLY);
				}
				Message m = folder.getMessageByUID(Long.parseLong(message_uuid));
				Object content = m.getContent();
				Part part  = handleMultiPart(content, attachmentName);
				in = part.getInputStream();
				if (in != null) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					int bytesComplete = 0;
					while ((bytesRead = in.read(buffer)) != -1) {
						bytesComplete = bytesComplete + bytesRead;
						out.write(buffer, 0, bytesRead); // write
					}
					out.flush();
					response.setContentLength(bytesComplete);
				} else {
					response.setContentLength(0);
				}
			} catch (Exception e) {
				logger.error("Error while downloading attachment "
						+ attachmentName + " of message " + message_uuid
						+ " for sessionId " + sessionId, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// ignore on close
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// ignore on close
					}
				}
				if (folder != null) {
					try {
						folder.close(false);
					} catch (MessagingException e) {
						// ignore on close
					}
				}

			}
		}

	}
	
	/**
	 * Loop over MuliPart and write the content to the Outputstream if a
	 * attachment with the given name was found.
	 * 
	 * @param out
	 *            Outputstream to write the content to
	 * @param content
	 *            Content which should checked for attachments
	 * @param attachmentName
	 *            The attachmentname for the searched attachment
	 * @throws MessagingException
	 * @throws IOException
	 */
	private Part handleMultiPart(Object content, String attachmentName)
			throws MessagingException, IOException {
		if (content instanceof Multipart) {
			Multipart part = (Multipart) content;
			for (int i = 0; i < part.getCount(); i++) {
				Part p = part.getBodyPart(i);
				if (isAttachment(p)) {
					if (MimeUtility.decodeText(p.getFileName()).equals(
							attachmentName)) {
						return p;
					}

				} else if (p.isMimeType("multipart/*")) {
					return handleMultiPart(p.getContent(), attachmentName);
				}
			}
		}
		return null;
	}

	/**
	 * Check if the given Part is an attachment
	 * 
	 * @param part
	 * @return isAttachment
	 * @throws MessagingException
	 */
	private boolean isAttachment(Part part) throws MessagingException {
		String disposition = part.getDisposition();
		if (part.getContentType().toLowerCase().startsWith("application/")
				|| (disposition != null && (disposition.equals(Part.ATTACHMENT) || disposition
						.equals(Part.INLINE)))) {
			return true;
		}
		return false;
	}

}
