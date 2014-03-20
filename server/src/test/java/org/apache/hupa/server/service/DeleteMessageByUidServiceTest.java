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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.data.DeleteMessageByUidActionImpl;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.exception.HupaException;
import org.junit.Test;

public class DeleteMessageByUidServiceTest extends HupaGuiceTestCase {

    @Test public void deleteFolderNotExists() throws MessagingException {
        ImapFolder folder = new ImapFolderImpl();
        folder.setFullName("NOT_EXISTS");
        DeleteMessageByUidAction action = new DeleteMessageByUidActionImpl(folder,new ArrayList<Long>());
        try {
            deleteMessageByUidService.delete(action);
            fail("Folder should not exists!");
        } catch (HupaException e) {
        }
    }

    @Test public void deleteFolderExistsAndNotTrash() throws MessagingException {
        ImapFolder folder = new ImapFolderImpl();
        folder.setFullName("EXISTS");
        MockIMAPStore store = (MockIMAPStore) storeCache.get(testUser);
        store.clear();

        MockIMAPFolder f = (MockIMAPFolder)store.getFolder(folder.getFullName());
        f.create(Folder.HOLDS_FOLDERS);
        f.addMessages(new Message[] { new MimeMessage(session), new MimeMessage(session), new MimeMessage(session)});
        ArrayList<Long> uids = new ArrayList<Long>();
        uids.add(0l);
        uids.add(2l);
        DeleteMessageByUidAction action = new DeleteMessageByUidActionImpl(folder, uids);

        MockIMAPFolder f3 = (MockIMAPFolder) store.getFolder(testUser.getSettings().getTrashFolderName());
        assertFalse("Trash folder already exists", f3.exists());
        try {
            deleteMessageByUidService.delete(action);
            assertEquals("Only 1 message left", 1, f.getMessageCount());

            MockIMAPFolder f2 = (MockIMAPFolder) store.getFolder(testUser.getSettings().getTrashFolderName());
            assertTrue("Trash folder created",f2.exists());
            assertEquals("2 messages moved", 2, f2.getMessageCount());
        } catch (HupaException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test public void deleteFolderExistsAndIsTrash() throws MessagingException {
        ImapFolder folder = new ImapFolderImpl();
        folder.setFullName(testUser.getSettings().getTrashFolderName());
        MockIMAPStore store = (MockIMAPStore) storeCache.get(testUser);

        MockIMAPFolder f = (MockIMAPFolder)store.getFolder(folder.getFullName());
        f.addMessages(new Message[] { new MimeMessage(session), new MimeMessage(session), new MimeMessage(session)});
        ArrayList<Long> uids = new ArrayList<Long>();
        uids.add(0l);
        uids.add(2l);
        DeleteMessageByUidAction action = new DeleteMessageByUidActionImpl(folder, uids);
        try {
            deleteMessageByUidService.delete(action);
            assertEquals("Only 1 message left", 1, f.getMessageCount());
        } catch (HupaException e) {
            e.printStackTrace();
            fail();
        }
    }
}
