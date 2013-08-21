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

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.exception.InvalidSessionException;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;

import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;

public class FetchFoldersHandlerTest extends HupaGuiceTestCase {
    

    public void setUp() throws Exception {
        super.setUp();
        MockIMAPStore store = (MockIMAPStore) storeCache.get(testUser);
        store.clear();
    }
    
    public void testInvalidSessionId() {
        httpSession.removeAttribute(SConsts.USER_SESS_ATTR);
        try {
            fetchFoldersHandler.execute(new FetchFolders(), null);
            fail("Invalid session");
        } catch (InvalidSessionException e) {
        } catch (ActionException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testNoFolders() {
        httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
        try {
            FetchFoldersResult result = fetchFoldersHandler.execute(new FetchFolders(), null);
            assertTrue(result.getFolders().isEmpty());
        } catch (ActionException e) {
            e.printStackTrace();
            fail();
        }
    }


// Skip test because
// MockIMAPFolder somehow doesn't creates subfolders, but put all created folders
// as main folder using as name the lates after the separator:
// 'WHATEVER.XXX.AAA' will result in a fullname 'AAA', what is wrong.
//
//    public void testFoundFolders() throws MessagingException {
//        httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
//    
//        store.getFolder("WHATEVER").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.XXX").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.XXX.AAA").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.XXX.AAA.111").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.XXX.AAA.222").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.XXX.BBB").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.YYY").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.YYY.AAA").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER.YYY.BBB").create(Folder.HOLDS_FOLDERS);
//        store.getFolder("WHATEVER1").create(Folder.HOLDS_FOLDERS);
//        try {
//            FetchFoldersResult result = fetchFoldersHandler.execute(new FetchFolders(), null);
//            
//            System.out.println(result.toString());
//            
//            List<IMAPFolder> folders = result.getFolders();
//            assertFalse(folders.isEmpty());
//            
//            // 2 different main folders, everything else are subfolders
//            assertEquals(2, folders.size());
//
//            // WHATEVER
//            assertEquals("WHATEVER",folders.get(0).getFullName());
//
//            // WHATEVER1
//            assertEquals("WHATEVER1",folders.get(1).getFullName());
//
//            // WHATEVER.XXX
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "XXX",
//                    folders.get(0).getChildIMAPFolders().get(0).getFullName());
//
//            // WHATEVER.XXX.AAA
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "XXX" + MockIMAPFolder.SEPARATOR + "AAA",
//                    folders.get(0).getChildIMAPFolders().get(0).getChildIMAPFolders().get(0).getFullName());
//
//            // WHATEVER.XXX.AAA.111
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "XXX" + MockIMAPFolder.SEPARATOR + "AAA" + MockIMAPFolder.SEPARATOR + "111",
//                    folders.get(0).getChildIMAPFolders().get(0).getChildIMAPFolders().get(0).getChildIMAPFolders().get(0).getFullName());
//
//            // WHATEVER.XXX.AAA.222
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "XXX" + MockIMAPFolder.SEPARATOR + "AAA" + MockIMAPFolder.SEPARATOR + "222",
//                    folders.get(0).getChildIMAPFolders().get(0).getChildIMAPFolders().get(0).getChildIMAPFolders().get(1).getFullName());
//
//            // WHATEVER.XXX.BBB
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "XXX" + MockIMAPFolder.SEPARATOR + "BBB",
//                    folders.get(0).getChildIMAPFolders().get(0).getChildIMAPFolders().get(1).getFullName());
//
//            // WHATEVER.YYY
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "YYY",
//                    folders.get(0).getChildIMAPFolders().get(1).getFullName());
//
//            // WHATEVER.YYY.AAA
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "YYY" + MockIMAPFolder.SEPARATOR + "AAA",
//                    folders.get(0).getChildIMAPFolders().get(1).getChildIMAPFolders().get(0).getFullName());
//
//            // WHATEVER.YYY.BBB
//            assertEquals("WHATEVER" + MockIMAPFolder.SEPARATOR + "YYY" + MockIMAPFolder.SEPARATOR + "BBB",
//                    folders.get(0).getChildIMAPFolders().get(1).getChildIMAPFolders().get(1).getFullName());
//        } catch (ActionException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
}
