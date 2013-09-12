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

package org.apache.hupa.server.preferences;

import java.util.ArrayList;
import java.util.Arrays;
<<<<<<< HEAD
<<<<<<< HEAD

import javax.mail.Flags;
import javax.mail.Folder;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.guice.GuiceServerTestModule;
import org.apache.hupa.server.guice.providers.LogProvider;
=======
import java.util.Properties;
=======
>>>>>>> constantly changed by manolo

import javax.mail.Flags;
import javax.mail.Folder;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.guice.GuiceServerTestModule;
<<<<<<< HEAD
import org.apache.hupa.server.guice.demo.DemoGuiceServerModule.DemoIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.handler.AbstractSendMessageHandler;
import org.apache.hupa.server.handler.ContactsHandler;
import org.apache.hupa.server.handler.CreateFolderHandler;
import org.apache.hupa.server.handler.DeleteFolderHandler;
import org.apache.hupa.server.handler.DeleteMessageByUidHandler;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.handler.ForwardMessageHandler;
import org.apache.hupa.server.handler.GetMessageDetailsHandler;
import org.apache.hupa.server.handler.IdleHandler;
import org.apache.hupa.server.handler.LoginUserHandler;
import org.apache.hupa.server.handler.LogoutUserHandler;
import org.apache.hupa.server.handler.ReplyMessageHandler;
import org.apache.hupa.server.handler.SendMessageHandler;
import org.apache.hupa.server.mock.MockConstants;
import org.apache.hupa.server.mock.MockHttpSessionProvider;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockLogProvider;
import org.apache.hupa.server.utils.ConfigurationProperties;
>>>>>>> first commit
=======
import org.apache.hupa.server.guice.providers.LogProvider;
>>>>>>> constantly changed by manolo
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.ImapFolderImpl;
<<<<<<< HEAD
import org.apache.hupa.shared.data.SMTPMessage;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.Contacts;
>>>>>>> first commit
=======
>>>>>>> constantly changed by manolo
=======
import org.apache.hupa.shared.domain.SmtpMessage;
>>>>>>> forward and reply message to use RF
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;
import org.apache.hupa.shared.rpc.SendMessage;

import com.google.inject.Module;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.google.inject.Singleton;
import com.google.inject.name.Names;
>>>>>>> first commit
=======
>>>>>>> constantly changed by manolo
import com.sun.mail.imap.IMAPStore;

public class InImapUserPreferencesStorageTest extends HupaGuiceTestCase {

    /*
       These tests should work with Courier, Gmail and any other real IMAP implementation.
       So, if you wanted to run these tests against your IMAP server do this:
         - Set the correct properties.
         - Be sure the user and password are set correctly
         - Comment the delay
     */
<<<<<<< HEAD
<<<<<<< HEAD
    static class InImapUserPreferencesGuiceModule extends GuiceServerTestModule {
    	public InImapUserPreferencesGuiceModule() {
    		
=======
    static class MyModule extends GuiceServerTestModule {
        public MyModule() {
>>>>>>> first commit
=======
    static class InImapUserPreferencesGuiceModule extends GuiceServerTestModule {
    	public InImapUserPreferencesGuiceModule() {
    		
>>>>>>> constantly changed by manolo
            // Select a valid imap provider, comment all to use Mock
            // properties = courierProperties;
            // properties = gmailProperties;
            // properties = jamesProperties;
<<<<<<< HEAD
<<<<<<< HEAD

    	    // Uncomment to use production logger
            logProviderClass = LogProvider.class;
    		
            userPreferencesStorageClass = InImapUserPreferencesStorage.class;
            
            // to run test faster
            InImapUserPreferencesStorage.IMAP_SAVE_DELAY = 400;
        }
        
=======
            
            // Uncomment to use production logger
            // logClass = LogProvider.class;

            // Change the default delay to run test faster
            InImapUserPreferencesStorage.IMAP_SAVE_DELAY = 400;
        }
=======
>>>>>>> constantly changed by manolo

    	    // Uncomment to use production logger
            logProviderClass = LogProvider.class;
    		
            userPreferencesStorageClass = InImapUserPreferencesStorage.class;
            
            // to run test faster
            InImapUserPreferencesStorage.IMAP_SAVE_DELAY = 400;
        }
<<<<<<< HEAD
>>>>>>> first commit
=======
        
>>>>>>> constantly changed by manolo
    }
    
    @Override
    protected Module[] getModules() {
<<<<<<< HEAD
<<<<<<< HEAD
        return new Module[]{new InImapUserPreferencesGuiceModule()};
=======
        return new Module[]{new MyModule()};
>>>>>>> first commit
=======
        return new Module[]{new InImapUserPreferencesGuiceModule()};
>>>>>>> constantly changed by manolo
    }
    
    /**
     * Delete all messages in user's dratfs folder
     */
    public void setUp() throws Exception {
        super.setUp();
        Folder f = storeCache.get(testUser).getFolder(testUser.getSettings().getDraftsFolderName());
        if (f.exists() && f.getMessageCount() > 0) {
            f.open(Folder.READ_WRITE);
            f.setFlags(f.getMessages(), new Flags(Flags.Flag.DELETED), true);
            f.close(true);
        }
    }
    
    public void testAnySerializableObjectCanBeSavedInIMAP() throws Exception {
        IMAPStore store = storeCache.get(testUser);
        String folderName = testUser.getSettings().getInboxFolderName() + store.getDefaultFolder().getSeparator() + "aFolder";
        String magicSubject = "magicSubject";
        Object objectOne = new String("a serializable object");
        Object objectTwo = new String("another serializable object");

        // Remove the folder if exist
        Folder f = store.getFolder(folderName);
        if (f.exists()) {
            f.delete(true);
        }

        // Check saving an object when the folder doesnt exist
        Object o = InImapUserPreferencesStorage.readUserPreferencesFromIMAP(logger, testUser, store, folderName, magicSubject);
        assertNull(o);

        InImapUserPreferencesStorage.saveUserPreferencesInIMAP(logger, testUser, session, store, folderName, magicSubject, objectOne);

        Folder folder = store.getFolder(folderName);
        assertNotNull(folder);
        assertEquals(1, folder.getMessageCount());

        o = InImapUserPreferencesStorage.readUserPreferencesFromIMAP(logger, testUser, store, folderName, magicSubject);
        assertNotNull(o);
        assertEquals(objectOne, o);

        // Check saving an object when the folder already exist
        InImapUserPreferencesStorage.saveUserPreferencesInIMAP(logger, testUser, session, store, folderName, magicSubject, objectTwo);
        folder = store.getFolder(folderName);
        assertEquals(1, folder.getMessageCount());
        o = InImapUserPreferencesStorage.readUserPreferencesFromIMAP(logger, testUser, store, folderName, magicSubject);
        assertEquals(objectTwo, o);
        
        // Remove the folder
        store.getFolder(folderName).delete(true);
    }
    
    public void testFetchMessagesFillsTheContactsListAndItIsSavedAsynchronously() throws Exception {
        IMAPStore store = storeCache.get(testUser);
        String folderName = testUser.getSettings().getDraftsFolderName();
        
        // Setup deletes all Drafts messages and contacts in session
        assertEquals(0, userPreferences.getContacts().length);
        Folder folder = store.getFolder(folderName);
        if (folder.exists())
            assertTrue(folder.getMessageCount() == 0);
        
        // Fetch inbox messages
        ImapFolderImpl cFolder = new ImapFolderImpl(testUser.getSettings().getInboxFolderName());
        FetchMessagesResult result = fetchMessagesHandler.execute(new FetchMessages(cFolder, 0, 10, null), null);
        
        // Could be possible that there insn't any message in inbox
        if (result.getRealCount() > 0) {
            int contactsCount = userPreferences.getContacts().length;
            assertTrue(contactsCount > 0);

            // The imap is saved asynchronously after a delay, so the folder exists after a while
            folder = store.getFolder(folderName);
            if (folder.exists())
                assertTrue(folder.getMessageCount() == 0);
            
            Thread.sleep(InImapUserPreferencesStorage.IMAP_SAVE_DELAY + 500);
            folder = store.getFolder(folderName);
            assertNotNull(folder);
            assertTrue(folder.exists());
            assertEquals(1, folder.getMessageCount());
            
            // When data is deleted from session, contacts came from imap
            httpSession.removeAttribute(InImapUserPreferencesStorage.USER_ATTR);
            assertEquals(contactsCount, userPreferences.getContacts().length );
        }
    }
    
    public void testSendMessagesAddContactsToList() throws Exception {
        // Setup deletes all Drafts messages and contacts in session
        assertEquals(0, userPreferences.getContacts().length);

        // Send an email to only one email
        SmtpMessage smtpmsg = TestUtils.createMockSMTPMessage(SessionUtils.getSessionRegistry(logger, httpSession), 2);
        smtpmsg.setFrom(testUser.getName());
        smtpmsg.setTo(new ArrayList<String>(Arrays.asList(testUser.getName())));
        smtpmsg.setCc(new ArrayList<String>());
        smtpmsg.setBcc(new ArrayList<String>());
        SendMessage action = new SendMessage(smtpmsg);
<<<<<<< HEAD
<<<<<<< HEAD

        String folderName = testUser.getSettings().getDraftsFolderName();
        
        Folder folder = store.getFolder(folderName);
        folder = store.getFolder(folderName);
        if (folder.exists())
            assertEquals("Folder " + folderName + " has messages" , 0, folder.getMessageCount());
        
        // When sending a new email new addresses should be added to the contact list 
        sendMessageHandler.execute(action, null);
        assertEquals(1, userPreferences.getContacts().length);

        // The imap is saved asynchronously after a delay, so the folder exists after a while
=======
        sendMessageHandler.execute(action, null);

        // The email has to be added to the contact list 
        assertEquals(1, userPreferences.getContacts().length);
=======
>>>>>>> constantly changed by manolo

        String folderName = testUser.getSettings().getDraftsFolderName();
        
        Folder folder = store.getFolder(folderName);
        folder = store.getFolder(folderName);
        if (folder.exists())
            assertEquals("Folder " + folderName + " has messages" , 0, folder.getMessageCount());
        
<<<<<<< HEAD
>>>>>>> first commit
=======
        // When sending a new email new addresses should be added to the contact list 
        sendMessageHandler.execute(action, null);
        assertEquals(1, userPreferences.getContacts().length);

        // The imap is saved asynchronously after a delay, so the folder exists after a while
>>>>>>> constantly changed by manolo
        Thread.sleep(InImapUserPreferencesStorage.IMAP_SAVE_DELAY + 500);
        folder = store.getFolder(folderName);
        assertNotNull(folder);
        assertTrue(folder.exists());
        assertEquals(1, folder.getMessageCount());
        
        // When data is deleted from session, contacts came from imap
        httpSession.removeAttribute(InImapUserPreferencesStorage.USER_ATTR);
        assertEquals(1, userPreferences.getContacts().length);
    }

}
