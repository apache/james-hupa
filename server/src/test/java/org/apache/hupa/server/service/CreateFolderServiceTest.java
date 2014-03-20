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

package org.apache.hupa.server.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.mail.Folder;
import javax.mail.MessagingException;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.CreateFolderActionImpl;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.exception.InvalidSessionException;
import org.junit.Test;

public class CreateFolderServiceTest extends HupaGuiceTestCase {

    @Test public void create() throws MessagingException {
        ImapFolder folder = createFolder();
        MockIMAPStore store = (MockIMAPStore) storeCache.get(testUser);
        Folder f1 = store.getFolder(folder.getFullName());
        assertFalse("not exists", f1.exists());
        try {
            createFolderService.create(new CreateFolderActionImpl(folder));
            Folder f = store.getFolder(folder.getFullName());
            assertTrue("exists", f.exists());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test public void duplicateFolder() throws MessagingException {
        ImapFolder folder = createFolder();
        MockIMAPStore store = (MockIMAPStore) storeCache.get(testUser);
        Folder f1 = store.getFolder(folder.getFullName());
        f1.create(Folder.HOLDS_FOLDERS);
        try {
            createFolderService.create(new CreateFolderActionImpl(folder));
            fail("Folder already exists");
        } catch (Exception e) {
        }
    }
    @Test public void invalidSessionId(){
        httpSession.removeAttribute(SConsts.USER_SESS_ATTR);
        ImapFolder folder = createFolder();
        try {
            createFolderService.create(new CreateFolderActionImpl(folder));
            fail("Invalid session");
        } catch (InvalidSessionException e) {
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    private ImapFolder createFolder() {
        ImapFolder folder = new ImapFolderImpl();
        folder.setFullName("NewFolder");
        folder.setDelimiter(String.valueOf(MockIMAPFolder.SEPARATOR));
        return folder;
    }

}
