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

import java.io.ByteArrayInputStream;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.guice.GuiceServerTestModule;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.DeleteFolder;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import com.google.inject.Module;
import com.sun.mail.imap.IMAPStore;

public class HandlersTest extends HupaGuiceTestCase {

    /*
     These tests should work with Courier, Gmail and any other real IMAP implementations
     If you want to run these tests against your IMAP server do this:
        1.- Change properties and classes to do integration tests and
        2.- Be sure the user and password are set correctly
    */
    class MyModule extends GuiceServerTestModule {
        public MyModule() {
            // properties = courierProperties;
            // properties = gmailProperties;
            // logProviderClass = LogProvider.class;
        }
    }
    
    @Override
    protected Module[] getModules() {
        return new Module[]{new MyModule()};
    }

    public void testLoginAndFetchFolders() throws Exception {
        org.apache.hupa.shared.rpc.LoginUser l = new org.apache.hupa.shared.rpc.LoginUser(testUser.getName(),testUser.getPassword());
        loginUser.execute(l, null);
        FetchFoldersResult result = fetchFoldersHandler.execute(new FetchFolders(), null);
        assertNotNull(result);
    }
    
    
    public void testFetchMessages() throws Exception {
        IMAPStore store = storeCache.get(testUser);
        
        String folderName = testUser.getSettings().getInboxFolderName();
        IMAPFolder sFolder = new IMAPFolder();
        sFolder.setFullName(folderName);
        
        com.sun.mail.imap.IMAPFolder f1 = (com.sun.mail.imap.IMAPFolder)store.getFolder(sFolder.getFullName());
        assertTrue(f1.exists());
        
        FetchMessagesResult result = fetchMessagesHandler.execute(new FetchMessages(sFolder, 0, 100, ""), null);
        int nmsgs = result.getMessages().size();

        ByteArrayInputStream is = new ByteArrayInputStream("From: a@foo.com\nTo: b@foo.com\nSubject: something\n\ndata".getBytes());
        MimeMessage msg = new MimeMessage(session, is);
        if (!f1.isOpen()) {
            f1.open(Folder.READ_WRITE);
        }
        f1.addMessages(new Message[]{msg});
        
        result = fetchMessagesHandler.execute(new FetchMessages(sFolder, 0, 100, ""), null);
        assertEquals(1, result.getMessages().size() - nmsgs);
    }
    
    public void testCreateAndDeleteFolder() throws MessagingException {
        IMAPStore store = storeCache.get(testUser);
        
        String folderName = testUser.getSettings().getInboxFolderName() + store.getDefaultFolder().getSeparator() + "newFolder";
        IMAPFolder sFolder = new IMAPFolder();
        sFolder.setFullName(folderName);
        
        Folder f1 = store.getFolder(sFolder.getFullName());
        assertFalse("not exists", f1.exists());
        
        try {
            createFolderHandler.execute(new CreateFolder(sFolder), null);
            Folder f = store.getFolder(sFolder.getFullName());
            assertTrue("exists", f.exists());
            assertFalse("Not opened", f.isOpen());
            f.open(Folder.READ_WRITE);
            assertTrue("opened", f.isOpen());
            
            Message[] msgs = f.getMessages();
            assertEquals(0, msgs.length);
            
            deleteFolderHandler.execute(new DeleteFolder(sFolder), null);
            f = store.getFolder(sFolder.getFullName());
            assertFalse("not exists",f.exists());

        } catch (ActionException e) {
            e.printStackTrace();
            fail("Shouldn't throw an exception ");
        }
    }
}
