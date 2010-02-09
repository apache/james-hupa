package org.apache.hupa.server.preferences;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;
import org.apache.hupa.shared.rpc.SendMessage;

public class InSessionUserPreferencesStorageTest extends HupaGuiceTestCase {

    public void setUp() throws Exception {
        httpSession.removeAttribute(InImapUserPreferencesStorage.CONTACTS_ATTR);
    }
    
    public void testFetchMessagesFillsContactsList() throws Exception {
        assertEquals(0, userPreferences.getContacts().length);
    
        FetchFoldersHandler fetchFoldersHandler = injector.getInstance(FetchFoldersHandler.class); 
        fetchFoldersHandler.execute(new FetchFolders(), null);
        
        IMAPFolder folder = new IMAPFolder(testUser.getSettings().getInboxFolderName());
        FetchMessagesHandler fetchMessagesHandler = injector.getInstance(FetchMessagesHandler.class); 
        FetchMessagesResult result = fetchMessagesHandler.execute(new FetchMessages(folder, 0, 10, null), null);
        
        assertTrue(result.getRealCount()>1);
    }
    
    public void testSendMessagesAddContactsToList() throws Exception {
        assertEquals(0, userPreferences.getContacts().length);
        
        SMTPMessage smtpmsg = TestUtils.createMockSMTPMessage(SessionUtils.getSessionRegistry(logger, httpSession), 2);
        SendMessage action = new SendMessage(smtpmsg);
        abstSendMsgHndl.execute(action, null);
        
        assertEquals(3, userPreferences.getContacts().length);
    }    

}
