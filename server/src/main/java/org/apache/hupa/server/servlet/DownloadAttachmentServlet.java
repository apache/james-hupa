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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.shared.SConsts;
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

        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            throw new ServletException("Invalid session");

        String message_uuid = request.getParameter(SConsts.PARAM_UID);
        String attachmentName = request.getParameter(SConsts.PARAM_NAME);
        String folderName = request.getParameter(SConsts.PARAM_FOLDER);
        response.setHeader("Content-disposition", "attachment; filename="
                + attachmentName + "");
        InputStream in = null;
        OutputStream out = response.getOutputStream();

        IMAPFolder folder = null;
        try {
            Store store = cache.get(user);
            folder = (IMAPFolder) store.getFolder(folderName);
            if (folder.isOpen() == false) {
                folder.open(Folder.READ_ONLY);
            }
            Message m = folder.getMessageByUID(Long.parseLong(message_uuid));

            Object content = m.getContent();
            Part part  = handleMultiPart(logger, content, attachmentName);
            if (part.getContentType()!=null)
                response.setContentType(part.getContentType());
            else
                response.setContentType("application/download");

            in = part.getInputStream();
            if (in != null) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                int bytesComplete = 0;
                while ((bytesRead = in.read(buffer)) != -1) {
                    bytesComplete = bytesComplete + bytesRead;
                    out.write(buffer, 0, bytesRead); // write
                }
                response.setContentLength(bytesComplete);
            } else {
                response.setContentLength(0);
            }

            out.flush();

        } catch (Exception e) {
            logger.error("Error while downloading attachment "
                    + attachmentName + " of message " + message_uuid
                    + " for user " + user.getName(), e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            if (folder != null) {
                try {
                    folder.close(false);
                } catch (MessagingException e) {
                    // ignore on close
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
     *            The attachmentname or the unique id for the searched attachment
     * @throws MessagingException
     * @throws IOException
     */
    static protected Part handleMultiPart(Log logger, Object content, String attachmentName)
            throws MessagingException, IOException {
        if (content instanceof Multipart) {
            Multipart part = (Multipart) content;
            for (int i = 0; i < part.getCount(); i++) {
                Part bodyPart = part.getBodyPart(i);
                String fileName = bodyPart.getFileName();
                String[] contentId = bodyPart.getHeader("Content-ID");
                if (bodyPart.isMimeType("multipart/*")) {
                    Part p = handleMultiPart(logger, bodyPart.getContent(), attachmentName);
                    if (p != null)
                        return p;
                } else {
                    if (contentId != null) {
                        for (String id: contentId) {
                            id = id.replaceAll("^.*<(.+)>.*$", "$1");
                            if (attachmentName.equals(id))
                                return bodyPart;
                        }
                    }
                    if (fileName != null){
                        if (attachmentName.equalsIgnoreCase(MimeUtility.decodeText(fileName)))
                            return bodyPart;
                    }
                }
            }
        } else {
            logger.error("Unknown content: " + content.getClass().getName());
        }
        return null;
    }

}
