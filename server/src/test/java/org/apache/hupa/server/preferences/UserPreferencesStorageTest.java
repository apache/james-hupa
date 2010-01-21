package org.apache.hupa.server.preferences;

import com.sun.mail.imap.IMAPStore;

import org.apache.hupa.server.HupaTestCase;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import javax.servlet.http.HttpSession;


public class UserPreferencesStorageTest extends HupaTestCase {

    public void testPutContactsInSession() throws Exception {
        UserPreferencesStorage userPreferences = injector.getInstance(UserPreferencesStorage.class);
        
        userPreferences.addContact(" ' \"Somebody\" <me@domain.com>");
        assertEquals("Somebody", userPreferences.getContacts()[0].realname);
        assertEquals("me@domain.com", userPreferences.getContacts()[0].mail);
        
    }

    public void testFetchMessagesFillsContactsList() throws Exception {

        assertEquals(0, userPreferences.getContacts().length);
        
        User demouser = DemoModeConstants.demoUser;

        HttpSession httpSession = injector.getInstance(HttpSession.class);
        httpSession.setAttribute("user", demouser);

        IMAPStoreCache storeCache = injector.getInstance(IMAPStoreCache.class);
        IMAPStore store = injector.getInstance(IMAPStore.class);
        ((MockIMAPStoreCache)storeCache).addValidUser(demouser, store);

        FetchFoldersHandler fetchFoldersHandler = injector.getInstance(FetchFoldersHandler.class); 
        fetchFoldersHandler.execute(new FetchFolders(), null);
        
        IMAPFolder folder = new IMAPFolder(demouser.getSettings().getInboxFolderName());
        FetchMessagesHandler fetchMessagesHandler = injector.getInstance(FetchMessagesHandler.class); 
        FetchMessagesResult result = fetchMessagesHandler.execute(new FetchMessages(folder, 0, 10, null), null);
        
        assertTrue(result.getRealCount()>1);
        
        assertTrue(userPreferences.getContacts().length>1);
        
    }
    
}
