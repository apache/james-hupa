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
import javax.mail.Part;
import javax.mail.Store;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPFolder;

/**
 * Handle to download attachments in messages
 */
public class DownloadAttachmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1245563204035792963L;

    private IMAPStoreCache cache;
    private Log logger;

    @Inject
    public DownloadAttachmentServlet(IMAPStoreCache cache, Log logger) {
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

        User user = (User) request.getSession().getAttribute(SConsts.USER_SESS_ATTR);
        if (user == null)
            throw new ServletException("Invalid session");

        String message_uuid = request.getParameter(SConsts.PARAM_UID);
        String attachmentName = request.getParameter(SConsts.PARAM_NAME);
        String folderName = request.getParameter(SConsts.PARAM_FOLDER);
        String mode = request.getParameter(SConsts.PARAM_MODE);
        boolean inline = "inline".equals(mode);
        if (!inline) {
        response.setHeader("Content-disposition", "attachment; filename="
            + attachmentName + "");
        }
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
            Part part  = MessageUtils.handleMultiPart(logger, content, attachmentName);
            if (part.getContentType()!=null) {
                response.setContentType(part.getContentType());
            } else {
                response.setContentType("application/download");
            }

            handleAttachmentData(request, m, attachmentName, part.getInputStream(), out);
            return;
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
                }
            }
        }
    }

    /**
     * Override this to create customized servlets
     */
    protected void handleAttachmentData(HttpServletRequest req, Message message,
            String attachmentName, InputStream is, OutputStream os) throws Exception {
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(os);
    }
}
