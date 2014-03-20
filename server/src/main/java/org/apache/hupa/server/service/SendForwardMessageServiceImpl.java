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

package org.apache.hupa.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.domain.SendForwardMessageAction;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.exception.HupaException;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class SendForwardMessageServiceImpl extends SendMessageBaseServiceImpl implements SendForwardMessageService {

    @Inject
    public SendForwardMessageServiceImpl(UserPreferencesStorage preferences, IMAPStoreCache cache) {
        super(preferences, cache);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List getAttachments(SendMessageAction action) throws MessagingException, HupaException {
        SendForwardMessageAction forwardAction = (SendForwardMessageAction)action;
        List<?> items = new ArrayList();
        IMAPStore store = cache.get(getUser());

        IMAPFolder folder = (IMAPFolder) store.getFolder(forwardAction.getFolder().getFullName());
        if (folder.isOpen() == false) {
            folder.open(Folder.READ_ONLY);
        }
        // Put the original attachments in the list
        Message msg = folder.getMessageByUID(forwardAction.getUid());
        try {
            items = MessageUtils.extractMessageAttachments(logger, msg.getContent());
            logger.debug("Forwarding a message, extracted: " + items.size() + " from original.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Put in the list the attachments uploaded by the user
        items.addAll(super.getAttachments(forwardAction));
        return items;
    }
}
