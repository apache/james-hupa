package org.apache.hupa.server.handler;

import com.google.inject.Module;

import com.sun.mail.imap.IMAPStore;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.guice.GuiceTestModule;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.DeleteFolder;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public class HandlersTest extends HupaGuiceTestCase {
    
    @Override
    protected Module getModule() {
        // These tests should work with Courier, Gmail and any other real IMAP implementations
        // If you want to run these tests against your IMAP server do this:
        //    1.- Change properties and classes to do integration tests and
        //    2.- Be sure the user and password are set correctly
        class MyModule extends GuiceTestModule {
            public MyModule() {
                // properties = courierProperties;
                // properties = gmailProperties;
                // logClass = LogProvider.class;
            }
        }
        return new MyModule();
    }

    public void testLoginAndFetchFolders() {
        try {
            org.apache.hupa.shared.rpc.LoginUser l = new org.apache.hupa.shared.rpc.LoginUser(testUser.getName(),testUser.getPassword());
            loginUser.execute(l, null);
            FetchFoldersResult result = fetchFHandler.execute(new FetchFolders(), null);
            assertNotNull(result);
        } catch (ActionException e) {
            e.printStackTrace();
            fail("Shouldn't throw an exception");
        }
    }
    
    public void testCreateAndDeleteFolder() throws MessagingException {
        IMAPStore store = storeCache.get(testUser);
        
        String folderName = testUser.getSettings().getInboxFolderName() + store.getDefaultFolder().getSeparator() + "newFolder";
        IMAPFolder sFolder = new IMAPFolder();
        sFolder.setFullName(folderName);
        
        Folder f1 = store.getFolder(sFolder.getFullName());
        assertFalse("not exists", f1.exists());
        
        try {
            createFHandler.execute(new CreateFolder(sFolder), null);
            Folder f = store.getFolder(sFolder.getFullName());
            assertTrue("exists", f.exists());
            assertFalse("Not opened", f.isOpen());
            f.open(Folder.READ_WRITE);
            assertTrue("opened", f.isOpen());
            
            Message[] msgs = f.getMessages();
            assertEquals(0, msgs.length);
            
            deleteFHandler.execute(new DeleteFolder(sFolder), null);
            f = store.getFolder(sFolder.getFullName());
            assertFalse("not exists",f.exists());

        } catch (ActionException e) {
            e.printStackTrace();
            fail("Shouldn't throw an exception ");
        }
    }
}
