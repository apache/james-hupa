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

package org.apache.hupa.server.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.ForwardMessage;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which handles the forwarding of a message
 * 
 */
public class ForwardMessageHandler extends AbstractSendMessageHandler<ForwardMessage> {

    @Inject
    public ForwardMessageHandler(Log logger, IMAPStoreCache store, Provider<HttpSession> provider, @Named("SMTPServerAddress") String address, @Named("SMTPServerPort") int port,
            @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL) {
        super(logger, store, provider, address, port, auth, useSSL);
    }

    @Override
    protected Message createMessage(Session session, ForwardMessage action) throws AddressException, MessagingException, ActionException {
        MimeMessage message = new MimeMessage(session);
        SMTPMessage m = action.getMessage();
        message.setFrom(new InternetAddress(m.getFrom()));
        List<String> to = m.getTo();
        for (int i = 0; i < to.size(); i++) {
            message.addRecipient(RecipientType.TO, new InternetAddress(to.get(i)));
        }

        List<String> cc = m.getCc();
        for (int i = 0; cc != null && i < cc.size(); i++) {
            message.addRecipient(RecipientType.CC, new InternetAddress(cc.get(i)));
        }
        message.setSubject(m.getSubject());
        message.saveChanges();
        return message;
    }


    @Override
    @SuppressWarnings("unchecked")
    protected List getAttachments(ForwardMessage action) throws MessagingException, ActionException {
        List<BodyPart> items = new ArrayList<BodyPart>();
        IMAPStore store = cache.get(getUser());

        IMAPFolder folder = (IMAPFolder) store.getFolder(action.getFolder().getFullName());
        if (folder.isOpen() == false) {
            folder.open(Folder.READ_ONLY);
        }
        // Put the original attachments in the list 
        Message msg = folder.getMessageByUID(action.getReplyMessageUid());
        try {
            items = MessageUtils.extractMessageAttachments(logger, msg.getContent());
            logger.debug("Forwarding a message, extracted: " + items.size() + " from original.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Put in the list the attachments uploaded by the user
        items.addAll(super.getAttachments(action));
        return items;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<ForwardMessage> getActionType() {
        return ForwardMessage.class;
    }

    /**
     * DataStore which wrap a FileItem
     * 
     */
    protected static class PartDataStore implements DataSource {

        private FileItem item;

        public PartDataStore(FileItem item) {
            this.item = item;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.activation.DataSource#getContentType()
         */
        public String getContentType() {
            return item.getContentType();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.activation.DataSource#getInputStream()
         */
        public InputStream getInputStream() throws IOException {
            return item.getInputStream();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.activation.DataSource#getName()
         */
        public String getName() {
            String fullName = item.getName();

            // Strip path from file
            int index = fullName.lastIndexOf(File.separator);
            if (index == -1) {
                return fullName;
            } else {
                return fullName.substring(index + 1, fullName.length());
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.activation.DataSource#getOutputStream()
         */
        public OutputStream getOutputStream() throws IOException {
            return null;
        }

    }

}
