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

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.shared.domain.DeleteMessageAction;
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.exception.HupaException;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class DeleteMessageAllServiceImpl extends DeleteMessageBaseServiceImpl implements DeleteMessageAllService {

    @Override
    protected Message[] getMessagesToDelete(DeleteMessageAction actionBase) throws HupaException {
        DeleteMessageAllAction action = (DeleteMessageAllAction) actionBase;
        User user = getUser();
        try {
            logger.info("Delete all messages in folder " + action.getFolder() + " for user " + user);
            IMAPStore store = cache.get(user);
            IMAPFolder folder = (IMAPFolder) store.getFolder(action.getFolder().getFullName());
            if (folder.isOpen() == false) {
                folder.open(Folder.READ_WRITE);
            }
            return folder.getMessages();
        } catch (MessagingException e) {
            String errorMsg = "Error while deleting all messages in folder " + action.getFolder() + " for user " + user;
            logger.error(errorMsg, e);
            throw new HupaException(errorMsg);

        }

    }

}
