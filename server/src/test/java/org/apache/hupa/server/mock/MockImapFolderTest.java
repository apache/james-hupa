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
package org.apache.hupa.server.mock;

import java.net.URL;

import javax.mail.Folder;
import javax.mail.Message;

import org.apache.hupa.server.HupaGuiceTestCase;

public class MockImapFolderTest extends HupaGuiceTestCase {

    public void testReadMessageFile() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(MockIMAPFolder.MOCK_MESSAGES_LOCATION + "0.msg");
        assertNotNull("There aren't message files for demo mode, check that the files mime/\\d.msg are in your classpath", url);
    }
    
    public void testLoadMessageFiles() throws Exception {
        MockIMAPStore store = new MockIMAPStore(session, null);
        MockIMAPFolder folder = new MockIMAPFolder("WHATEVER", store);
        folder.create(Folder.HOLDS_MESSAGES);
        folder.loadDemoMessages(session);
        assertTrue(folder.getMessages().length > 0);
        for (Message m: folder.getMessages()) {
            assertEquals(m, folder.getMessageByUID(folder.getUID(m)));
        }
    }

}
