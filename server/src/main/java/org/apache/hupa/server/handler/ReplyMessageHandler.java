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

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.ReplyMessage;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which handle replies to a message
 * 
 *
 */
public class ReplyMessageHandler extends AbstractSendMessageHandler<ReplyMessage>{

    @Inject
    public ReplyMessageHandler(Log logger, IMAPStoreCache store, Provider<HttpSession> provider,
            @Named("SMTPServerAddress") String address, @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL) {
        super(logger, store, provider, address, port, auth, useSSL);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSendMessageHandler#createMessage(javax.mail.Session, org.apache.hupa.shared.rpc.SendMessage)
     */
    protected Message createMessage(Session session, ReplyMessage action)
            throws AddressException, MessagingException, ActionException {
        IMAPStore store = cache.get(getUser());
        IMAPFolder folder = (IMAPFolder) store.getFolder(action.getFolder().getFullName());
        if (folder.isOpen() == false) {
            folder.open(Folder.READ_ONLY);
        }
        Message rMessage =  folder.getMessageByUID(action.getReplyMessageUid()).reply(action.getReplyAll());
        SMTPMessage m = action.getMessage();
        // Use the new recipient list, maybe it has changed
        rMessage.setRecipients(RecipientType.TO, MessageUtils.getRecipients(m.getTo()));
        rMessage.setRecipients(RecipientType.CC, MessageUtils.getRecipients(m.getCc()));
        rMessage.setRecipients(RecipientType.BCC, MessageUtils.getRecipients(m.getBcc()));
        rMessage.setFrom(new InternetAddress(m.getFrom()));
        // replace subject
        rMessage.setSubject(m.getSubject());
        rMessage.saveChanges();
        return rMessage;
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<ReplyMessage> getActionType() {
        return ReplyMessage.class;
    }

}
