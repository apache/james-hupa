package org.apache.hupa.client.storage;

import java.util.List;

import junit.framework.Assert;

import org.apache.hupa.client.HupaClientTestBase;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.storage.AppCache;
import org.junit.Test;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.query.client.Function;
import com.google.inject.Inject;

public class HupaStorageTest extends HupaClientTestBase {

    @Inject HupaStorage storage;
    @Inject AppCache cache;
    @Inject EventBus eventBus;

    @Test
    public void testUserPrefix() {
        String uk1 = cache.enCrypt("manuel.carrasco.m@gmail.com");
        System.out.println(uk1);
        String uk2 = cache.enCrypt("manuel.carrasco.m@outlook.com");
        System.out.println(uk2);
//        cache.setPrefix(uk.substring(0, 6) + "_");
    }

    @Test
    public void testFolderList() {
        storage.gettingFolders().done(new Function() {
            public void f() {
                List<ImapFolder> folderNodes = arguments(0);
                Assert.assertEquals(4, folderNodes.size());
            }
        });
        Assert.assertEquals(5,cache.getLength());

        cache.removeItem("Mock-Sent");
        storage.gettingFolders().done(new Function() {
            public void f() {
                List<ImapFolder> folderNodes = arguments(0);
                Assert.assertEquals(3, folderNodes.size());
            }
        });
        cache.setExpires("fld", 0);
        Assert.assertEquals(0,cache.getLength());

        storage.gettingFolders().done(new Function() {
            public void f() {
                List<ImapFolder> folderNodes = arguments(0);
                Assert.assertEquals(4, folderNodes.size());
            }
        });

        Assert.assertEquals(5,cache.getLength());

        eventBus.fireEvent(new LoginEvent(testUser));
        storage.gettingFolders().done(new Function() {
            public void f() {
                List<ImapFolder> folderNodes = arguments(0);
                Assert.assertEquals(4, folderNodes.size());
            }
        });

        Assert.assertEquals(10,cache.getLength());
    }

    @Test
    public void testEncrypt() {
        storage.gettingFolders().done(new Function() {
            public void f() {
                List<ImapFolder> folderNodes = arguments(0);
                Assert.assertEquals(4, folderNodes.size());
                Assert.assertEquals(5,cache.getLength());
                cache.storeProxiesCrypt("fld", folderNodes);
                Assert.assertEquals(5,cache.getLength());
                folderNodes = cache.restoreProxiesCrypt(ImapFolder.class, "fld");
                Assert.assertEquals(4, folderNodes.size());
                folderNodes = cache.restoreProxies(ImapFolder.class, "fld");
                Assert.assertEquals(0, folderNodes.size());
            }
        });
    }
}
