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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.SendMessage;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * Handler which handle sending of new messages
 * 
 *
 */
public class SendMessageHandler extends AbstractSendMessageHandler<SendMessage> {

    @Inject
    public SendMessageHandler(Log logger, IMAPStoreCache store, Provider<HttpSession> provider,
            @Named("SMTPServerAddress") String address, @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL) {
        super(logger, store, provider, address, port, auth,useSSL);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSendMessageHandler#createMessage(javax.mail.Session, org.apache.hupa.shared.rpc.SendMessage)
     */
    protected Message createMessage(Session session, SendMessage action)
            throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        SMTPMessage m = action.getMessage();
        message.setFrom(new InternetAddress(m.getFrom()));
        message.setRecipients(RecipientType.TO, MessageUtils.getRecipients(m.getTo()));
        message.setRecipients(RecipientType.CC, MessageUtils.getRecipients(m.getCc()));
        message.setRecipients(RecipientType.BCC, MessageUtils.getRecipients(m.getBcc()));
        message.setSubject(m.getSubject());
        return message;
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<SendMessage> getActionType() {
        return SendMessage.class;
    }

}
