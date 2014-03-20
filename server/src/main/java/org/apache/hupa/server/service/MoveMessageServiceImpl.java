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

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.MoveMessageAction;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class MoveMessageServiceImpl extends AbstractService implements MoveMessageService {
    public GenericResult move(MoveMessageAction action) throws Exception {
        User user = getUser();
        try {
            IMAPStore store = cache.get(user);
            IMAPFolder folder = (IMAPFolder) store.getFolder(action.getOldFolder().getFullName());
            if (folder.isOpen() == false) {
                folder.open(Folder.READ_WRITE);
            }
            Message m = folder.getMessageByUID(action.getMessageUid());
            Message[] mArray = new Message[] { m };
            folder.copyMessages(mArray, store.getFolder(action.getNewFolder().getFullName()));
            folder.setFlags(mArray, new Flags(Flags.Flag.DELETED), true);
            try {
                folder.expunge(mArray);
                folder.close(false);
            } catch (MessagingException e) {
                // prolly UID expunge is not supported
                folder.close(true);
            }
            return new GenericResultImpl();
        } catch (MessagingException e) {
            logger.error(
                    "Error while moving message " + action.getMessageUid() + " from folder " + action.getOldFolder()
                            + " to " + action.getNewFolder(), e);
            throw new Exception(e);
        }
    }
}
