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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handle viewing of message source
 */
public class MessageSourceServlet extends HttpServlet {

    private static final long serialVersionUID = 1245563204035792963L;
    private IMAPStoreCache cache;
    private Log logger;

    @Inject
    public MessageSourceServlet(IMAPStoreCache cache, Log logger) {
        this.cache = cache;
        this.logger = logger;
    }

    /**
     * Handle to write back the requested attachment
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute(SConsts.USER_SESS_ATTR);

        String message_uuid = request.getParameter(SConsts.PARAM_UID);
        String folderName = request.getParameter(SConsts.PARAM_FOLDER);
        OutputStream outs = null;
        try {
            long uid = Long.parseLong(message_uuid);

            IMAPStore store = cache.get(user);
            IMAPFolder f = (IMAPFolder) store.getFolder(folderName);
            if (f.isOpen() == false) {
                f.open(Folder.READ_ONLY);
            }

            Message m = f.getMessageByUID(uid);

            response.setContentType("text/plain");
            outs = response.getOutputStream();
            m.writeTo(outs);
            outs.flush();

            if (f.isOpen()) {
                f.close(false);
            }
        } catch (Exception e) {
            String msg = "Unable to get raw content of msg for user " + user + " in folder " + folderName + " with uid " + message_uuid;
            logger.error(msg, e);
            throw new ServletException(msg, e);
        } finally {
            IOUtils.closeQuietly(outs);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
