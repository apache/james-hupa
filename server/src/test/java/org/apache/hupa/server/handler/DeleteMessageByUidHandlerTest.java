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

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;

public class DeleteMessageByUidHandlerTest extends AbstractHandlerTest{
    
    public void testDeleteFolderNotExists() throws MessagingException {
        DeleteMessageByUidHandler handler = new DeleteMessageByUidHandler(storeCache, logger, httpSessionProvider);
    
        User user = createUser();
        storeCache.addValidUser(user.getName(), user.getPassword());
        httpSession.setAttribute("user", user);
        IMAPFolder folder = new IMAPFolder();
        folder.setFullName("NOT_EXISTS");
        DeleteMessageByUid action = new DeleteMessageByUid(folder,new ArrayList<Long>());

        try {
            handler.execute(action, null);
            fail("Folder should not exists!");
        } catch (ActionException e) {
            //e.printStackTrace();
        }
    }
    
    public void testDeleteFolderExistsAndNotTrash() throws MessagingException {
        Session s = Session.getInstance(new Properties());
        DeleteMessageByUidHandler handler = new DeleteMessageByUidHandler(storeCache, logger, httpSessionProvider);
    
        User user = createUser();
        storeCache.addValidUser(user.getName(), user.getPassword());
        httpSession.setAttribute("user", user);
        IMAPFolder folder = new IMAPFolder();
        folder.setFullName("EXISTS");
        MockIMAPStore store = (MockIMAPStore) storeCache.get(user);
        store.clear();

        MockIMAPFolder f = (MockIMAPFolder)store.getFolder(folder.getFullName());
        f.create(Folder.HOLDS_FOLDERS);
        f.addMessages(new Message[] { new MimeMessage(s), new MimeMessage(s), new MimeMessage(s)});
        ArrayList<Long> uids = new ArrayList<Long>();
        uids.add(0l);
        uids.add(2l);
        DeleteMessageByUid action = new DeleteMessageByUid(folder, uids);

        MockIMAPFolder f3 = (MockIMAPFolder) store.getFolder(user.getSettings().getTrashFolderName());
        assertFalse("Trash folder not exists yet",f3.exists());
        
        try {
            handler.execute(action, null);
            assertEquals("Only 1 message left", 1, f.getMessageCount());
            
            MockIMAPFolder f2 = (MockIMAPFolder) store.getFolder(user.getSettings().getTrashFolderName());
            assertTrue("Trash folder created",f2.exists());
            assertEquals("2 messages moved", 2, f2.getMessageCount());
        } catch (ActionException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testDeleteFolderExistsAndIsTrash() throws MessagingException {
        Session s = Session.getInstance(new Properties());
        DeleteMessageByUidHandler handler = new DeleteMessageByUidHandler(storeCache, logger, httpSessionProvider);
    
        User user = createUser();
        storeCache.addValidUser(user.getName(), user.getPassword());
        httpSession.setAttribute("user", user);
        IMAPFolder folder = new IMAPFolder();
        folder.setFullName(user.getSettings().getTrashFolderName());
        MockIMAPStore store = (MockIMAPStore) storeCache.get(user);
        store.clear();
        
        MockIMAPFolder f = (MockIMAPFolder)store.getFolder(folder.getFullName());
        f.create(Folder.HOLDS_FOLDERS);
        f.addMessages(new Message[] { new MimeMessage(s), new MimeMessage(s), new MimeMessage(s)});
        ArrayList<Long> uids = new ArrayList<Long>();
        uids.add(0l);
        uids.add(2l);
        DeleteMessageByUid action = new DeleteMessageByUid(folder, uids);

        try {
            handler.execute(action, null);

            assertEquals("Only 1 message left", 1, f.getMessageCount());
        } catch (ActionException e) {
            e.printStackTrace();
            fail();
        }
    }
}
