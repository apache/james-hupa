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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.MessageUtils;
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
public class ReplyMessageHandler extends AbstractSendMessageHandler<ReplyMessage> {

    @Inject
    public ReplyMessageHandler(Log logger, IMAPStoreCache store, Provider<HttpSession> provider, UserPreferencesStorage preferences,
            @Named("SMTPServerAddress") String address, @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL) {
        super(logger, store, provider, preferences, address, port, auth, useSSL);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List getAttachments(ReplyMessage action) throws MessagingException, ActionException {
        List<?> items = new ArrayList();
        IMAPStore store = cache.get(getUser());

        IMAPFolder folder = (IMAPFolder) store.getFolder(action.getFolder().getFullName());
        if (folder.isOpen() == false) {
            folder.open(Folder.READ_ONLY);
        }

        // Only original inline images have to be added to the list 
        Message msg = folder.getMessageByUID(action.getReplyMessageUid());
        try {
            items = MessageUtils.extractInlineImages(logger, msg.getContent());
            if (items.size() > 0)
                logger.debug("Replying a message, extracted: " + items.size() + " inline image from");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Put into the list the attachments uploaded by the user
        items.addAll(super.getAttachments(action));
        
        return items;
    }


    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<ReplyMessage> getActionType() {
        return ReplyMessage.class;
    }

}
