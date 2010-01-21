package org.apache.hupa.server;

import java.net.URL;

import javax.mail.Folder;
import javax.mail.Message;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.handler.AbstractHandlerTest;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.handler.LoginUserHandler;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;
import org.apache.hupa.shared.rpc.LoginUser;


public class DemoModeTest extends AbstractHandlerTest {

    private LoginUser demoUser = new LoginUser("demo", "demo");
    
    private InMemoryIMAPStoreCache memoryStore = new InMemoryIMAPStoreCache(logger, DemoModeConstants.DEMO_MODE, 143, false, 1, 300000, provider);
    
    private LoginUserHandler loginUserHandler = new LoginUserHandler(memoryStore, logger, httpSessionProvider, settingsProvider);
    
    private FetchFoldersHandler fetchFoldersHandler = new FetchFoldersHandler(memoryStore, logger, httpSessionProvider);
    
    private FetchMessagesHandler fetchMessagesHandler = new FetchMessagesHandler(memoryStore, logger, httpSessionProvider, preferences);

    public void testDemoLoginUser() {
        LoginUser badUser = new LoginUser("baduser", "whatever");
        try {
            loginUserHandler.execute(demoUser, null);
        } catch (ActionException e) {
            fail("Shouldn't throw an exception");
            e.printStackTrace();
        }
        try {
            loginUserHandler.execute(badUser, null);
            fail("Should throw an exception");
        } catch (ActionException e) {
        }
    }

    public void testDemoFetchFolders() {
        try {
            loginUserHandler.execute(demoUser, null);
            FetchFoldersResult result = fetchFoldersHandler.execute(new FetchFolders(), null);
            assertEquals("In demo mode should be 3 folders predefined", 3, result.getFolders().size());
        } catch (ActionException e) {
            e.printStackTrace();
            fail("Shouldn't throw an exception");
        }
    }

    public void testReadMessageFile() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(DemoModeConstants.DEMO_MODE_MESSAGES_LOCATION + "0.msg");
        assertNotNull("There aren't message files for demo mode, check that the files mime/\\d.msg are in your classpath", url);
    }
    
    public void testLoadMessageFiles() throws Exception {
        MockIMAPStore store = new MockIMAPStore(session);
        MockIMAPFolder folder = new MockIMAPFolder("WHATEVER", store);
        folder.create(Folder.HOLDS_MESSAGES);
        folder.loadDemoMessages(session);
        assertTrue(folder.getMessages().length > 0);
        
        for (Message m: folder.getMessages()) {
            assertEquals(m, folder.getMessageByUID(folder.getUID(m)));
        }
        
    }

    public void testDemoFetchMessages() throws Exception {
        try {
            User user = loginUserHandler.execute(demoUser, null).getUser();
            fetchFoldersHandler.execute(new FetchFolders(), null);
            IMAPFolder folder = new IMAPFolder(user.getSettings().getInboxFolderName());
            FetchMessagesResult result = fetchMessagesHandler.execute(new FetchMessages(folder, 0, 10, null), null);
            assertEquals(8, result.getRealCount());
            assertEquals(8, result.getMessages().size());
            assertEquals(8, result.getRealUnreadCount());
        } catch (ActionException e) {
            e.printStackTrace();
            fail("Shouldn't throw an exception");
        }
    }
    
}
