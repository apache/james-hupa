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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.mail.MessagingException;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.exception.InvalidSessionException;
import org.junit.Before;
import org.junit.Test;

public class FetchFolderServiceTest extends HupaGuiceTestCase {

    @Test public void invalidSessionId() {
        httpSession.removeAttribute(SConsts.USER_SESS_ATTR);
        try {
            fetchFoldersService.fetch(new ImapFolderImpl(), false);
            fail("Invalid session");
        } catch (InvalidSessionException e) {
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test public void noFolders() {
        httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
        try {
            List<ImapFolder> folders = fetchFoldersService.fetch(null, false);
            assertTrue(folders.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Before public void setup() throws MessagingException {
        MockIMAPStore store = (MockIMAPStore) storeCache.get(testUser);
        store.clear();
    }

}
