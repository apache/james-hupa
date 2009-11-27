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

import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.ForwardMessage;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which handles the forwarding of a message
 * @author norman
 *
 */
public class ForwardMessageHandler extends AbstractSendMessageHandler<ForwardMessage>{

    @Inject
    public ForwardMessageHandler(Log logger, FileItemRegistry registry,
            IMAPStoreCache store, Provider<HttpSession> provider,
            @Named("SMTPServerAddress") String address, @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL) {
        super(logger, registry, store, provider, address, port, auth, useSSL);
    }

    @Override
    protected Message createMessage(Session session, ForwardMessage action)
            throws AddressException, MessagingException, ActionException {
            MimeMessage message = new MimeMessage(session);
            SMTPMessage m = action.getMessage();
            message.setFrom(new InternetAddress(m.getFrom()));
            List<String> to = m.getTo();
            for (int i = 0; i < to.size(); i++) {
                message.addRecipient(RecipientType.TO, new InternetAddress(to
                        .get(i)));
            }

            List<String> cc = m.getCc();
            for (int i = 0; cc != null && i < cc.size(); i++) {
                message.addRecipient(RecipientType.CC, new InternetAddress(cc
                        .get(i)));
            }
            message.setSubject(m.getSubject());
            message.saveChanges();
            return message;
    }

    @Override
    protected Message fillBody(Message message,
            ForwardMessage action) throws MessagingException, ActionException {
        SMTPMessage m = action.getMessage();

        // create the message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        // fill message
        messageBodyPart.setText(m.getText());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        
        IMAPStore store = cache.get(getUser());
        
        IMAPFolder folder = (IMAPFolder) store.getFolder(action.getFolder().getFullName());
        if (folder.isOpen() == false) {
            folder.open(Folder.READ_ONLY);
        }
        Message fMessage = folder.getMessageByUID(action.getReplyMessageUid());
        
        // Create and fill part for the forwarded content
        messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(fMessage.getDataHandler());
        multipart.addBodyPart(messageBodyPart);

        multipart = handleAttachments(multipart, m.getMessageAttachments());
        
        
        // Put parts in message
        message.setContent(multipart);
        message.saveChanges();
        return message;
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<ForwardMessage> getActionType() {
        return ForwardMessage.class;
    }

}
