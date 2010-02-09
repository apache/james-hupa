package org.apache.hupa.server.preferences;

import com.google.inject.Module;

import com.sun.mail.imap.IMAPStore;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.guice.GuiceTestModule;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;
import org.apache.hupa.shared.rpc.SendMessage;

import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.Flags;
import javax.mail.Folder;

public class InImapUserPreferencesStorageTest extends HupaGuiceTestCase {

    /**
     * These tests should work with Courier, Gmail and any other real IMAP implementation.
     * So, if you wanted to run these tests against your IMAP server do this:
     *   - Set the correct properties.
     *   - Be sure the user and password are set correctly
     *   - Comment the delay
     */
    static class MyModule extends GuiceTestModule {
        public MyModule() {
            // properties = courierProperties;
            // properties = gmailProperties;
            // logClass = LogProvider.class;
            userPreferencesClass = InImapUserPreferencesStorage.class;
            // Change the default delay to run test faster
            InImapUserPreferencesStorage.IMAP_SAVE_DELAY = 400;
        }
    }

    @Override
    protected Module getModule() {
        return new MyModule();
    }
    
    /**
     * Delete contacts from session and all messages in user's dratfs folder
     */
    public void setUp() throws Exception {
        httpSession.removeAttribute(InImapUserPreferencesStorage.CONTACTS_ATTR);
        Folder f = storeCache.get(testUser).getFolder(testUser.getSettings().getDraftsFolderName());
        if (f.exists() && f.getMessageCount() > 0) {
            f.open(Folder.READ_WRITE);
            f.setFlags(f.getMessages(), new Flags(Flags.Flag.DELETED), true);
            f.close(true);
        }
    }
    
    public void atestAnySerializableObjectCanBeSavedInIMAP() throws Exception {
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
        abstSendMsgHndl.execute(action, null);

        // The email has to be added to the contact list 
        assertEquals(1, userPreferences.getContacts().length);

        // The imap is saved asynchronously after a delay, so the folder exists after a while
        String folderName = testUser.getSettings().getDraftsFolderName();
        Folder folder = store.getFolder(folderName);
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
        assertEquals(1, userPreferences.getContacts().length);
    }

}
