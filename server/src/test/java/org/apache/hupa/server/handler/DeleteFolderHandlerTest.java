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
import org.apache.hupa.shared.rpc.DeleteFolder;

import javax.mail.Folder;
import javax.mail.MessagingException;

public class DeleteFolderHandlerTest extends HupaGuiceTestCase {

    public void testDelete() throws MessagingException {
        IMAPFolder folder = createFolder();
        MockIMAPStore store = (MockIMAPStore) storeCache.get(testUser);
        Folder f1 = store.getFolder(folder.getFullName());
        f1.create(Folder.HOLDS_FOLDERS);
        try {
            deleteFolderHandler.execute(new DeleteFolder(folder), null);
            Folder f = store.getFolder(folder.getFullName());
            assertFalse("not exists",f.exists());
        } catch (ActionException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testDeleteNonExistFolder() throws MessagingException {
        IMAPFolder folder = createFolder();
        try {
            deleteFolderHandler.execute(new DeleteFolder(folder), null);
            fail("Folder should not exist");
        } catch (ActionException e) {
        }    
    }
    
    public void testInvalidSessionId() {
        httpSession.removeAttribute(SConsts.USER_SESS_ATTR);
        IMAPFolder folder = createFolder();
        try {
            deleteFolderHandler.execute(new DeleteFolder(folder), null);
            fail("Invalid session");
        } catch (InvalidSessionException e) {
        } catch (ActionException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    private IMAPFolder createFolder() {
        IMAPFolder folder = new IMAPFolder();
        folder.setFullName("NewFolder");
        folder.setDelimiter(String.valueOf(MockIMAPFolder.SEPARATOR));
        return folder;
    }
}
