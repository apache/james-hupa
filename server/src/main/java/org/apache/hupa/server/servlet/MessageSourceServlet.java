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
import java.io.OutputStream;

import javax.mail.Folder;
import javax.mail.Message;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.shared.data.User;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handle download of attachments
 */
public class MessageSourceServlet extends HttpServlet {

    private static final long serialVersionUID = 1245563204035792963L;
    private InMemoryIMAPStoreCache cache;
    private Log logger;

    @Inject
    public MessageSourceServlet(InMemoryIMAPStoreCache cache, Log logger) {
        this.cache = cache;
        this.logger = logger;
    }

    /**
     * Handle to write back the requested attachment
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        String message_uid = (String) request.getParameter("uid");
        String folder = (String) request.getParameter("folder");
        
        try {
            long uid = Long.parseLong(message_uid);

            IMAPStore store = cache.get(user);
            IMAPFolder f = (IMAPFolder) store.getFolder(folder);
            if (f.isOpen() == false) {
                f.open(Folder.READ_ONLY);
            }

            Message m = f.getMessageByUID(uid);

            response.setContentType("text/plain");
            OutputStream outs = response.getOutputStream();
            m.writeTo(outs);
            outs.flush();
            outs.close();

            if (f.isOpen()) {
                f.close(false);
            }
        } catch (Exception e) {
            logger.error("Unable to get raw content of msg for user " + user + " in folder " + folder + " with uid " + message_uid, e);
            throw new ServletException("Unable to get raw content of msg for user " + user + " in folder " + folder + " with uid " + message_uid);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
