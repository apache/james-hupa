package org.apache.hupa.server.handler;

import com.sun.mail.imap.IMAPStore;

import org.apache.hupa.server.HupaTestCase;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import javax.servlet.http.HttpSession;


public class ContactsHandlerTest extends HupaTestCase {

    public void testPutContactsInSession() throws Exception {
        
        HttpSession httpSession = injector.getInstance(HttpSession.class);
        
        SessionUtils.addContact(httpSession, " ' \"Somebody\" <me@domain.com>");
        assertEquals("Somebody",SessionUtils.getContacts(httpSession)[0].realname);
        assertEquals("me@domain.com",SessionUtils.getContacts(httpSession)[0].mail);
        
    }

    public void testFetchMessagesFillsContactsList() throws Exception {
        
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
        assertTrue(SessionUtils.getContacts(httpSession).length>1);
        
    }
    
}
