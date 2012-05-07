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

import javax.mail.Flags;
import javax.mail.Folder;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.guice.GuiceServerTestModule;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;
import org.apache.hupa.shared.rpc.SendMessage;

import com.google.inject.Module;
import com.sun.mail.imap.IMAPStore;

public class InImapUserPreferencesStorageTest extends HupaGuiceTestCase {

    /*
       These tests should work with Courier, Gmail and any other real IMAP implementation.
       So, if you wanted to run these tests against your IMAP server do this:
         - Set the correct properties.
         - Be sure the user and password are set correctly
         - Comment the delay
     */
    static class InImapUserPreferencesGuiceModule extends GuiceServerTestModule {
    	public InImapUserPreferencesGuiceModule() {
    		
            // Select a valid imap provider, comment all to use Mock
            // properties = courierProperties;
            // properties = gmailProperties;
            // properties = jamesProperties;

    	    // Uncomment to use production logger
            logProviderClass = LogProvider.class;
    		
            userPreferencesStorageClass = InImapUserPreferencesStorage.class;
            
            // to run test faster
            InImapUserPreferencesStorage.IMAP_SAVE_DELAY = 400;
        }
        
    }
    
    @Override
    protected Module[] getModules() {
        return new Module[]{new InImapUserPreferencesGuiceModule()};
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
        IMAPFolder cFolder = new IMAPFolder(testUser.getSettings().getInboxFolderName());
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
        SMTPMessage smtpmsg = TestUtils.createMockSMTPMessage(SessionUtils.getSessionRegistry(logger, httpSession), 2);
        smtpmsg.setFrom(testUser.getName());
        smtpmsg.setTo(new ArrayList<String>(Arrays.asList(testUser.getName())));
        smtpmsg.setCc(new ArrayList<String>());
        smtpmsg.setBcc(new ArrayList<String>());
        SendMessage action = new SendMessage(smtpmsg);

        String folderName = testUser.getSettings().getDraftsFolderName();
        
        Folder folder = store.getFolder(folderName);
        folder = store.getFolder(folderName);
        if (folder.exists())
            assertEquals("Folder " + folderName + " has messages" , 0, folder.getMessageCount());
        
        // When sending a new email new addresses should be added to the contact list 
        sendMessageHandler.execute(action, null);
        assertEquals(1, userPreferences.getContacts().length);

        // The imap is saved asynchronously after a delay, so the folder exists after a while
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
