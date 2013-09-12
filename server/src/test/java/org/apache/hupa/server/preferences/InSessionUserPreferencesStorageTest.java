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

package org.apache.hupa.server.preferences;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.IMAPFolderImpl;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;
import org.apache.hupa.shared.rpc.SendMessage;

public class InSessionUserPreferencesStorageTest extends HupaGuiceTestCase {

    public void setUp() throws Exception {
        super.setUp();
        httpSession.removeAttribute(InImapUserPreferencesStorage.CONTACTS_ATTR);
    }
    
    public void testFetchMessagesFillsContactsList() throws Exception {
        assertEquals(0, userPreferences.getContacts().length);
    
        FetchFoldersHandler fetchFoldersHandler = injector.getInstance(FetchFoldersHandler.class); 
        fetchFoldersHandler.execute(new FetchFolders(), null);
        
        IMAPFolder folder = new IMAPFolderImpl(testUser.getSettings().getInboxFolderName());
        FetchMessagesHandler fetchMessagesHandler = injector.getInstance(FetchMessagesHandler.class); 
        FetchMessagesResult result = fetchMessagesHandler.execute(new FetchMessages(folder, 0, 10, null), null);
        
        assertTrue(result.getRealCount()>1);
    }
    
    public void testSendMessagesAddContactsToList() throws Exception {
        assertEquals(0, userPreferences.getContacts().length);
        
        SMTPMessage smtpmsg = TestUtils.createMockSMTPMessage(SessionUtils.getSessionRegistry(logger, httpSession), 2);
        SendMessage action = new SendMessage(smtpmsg);
        sendMessageHandler.execute(action, null);
        
        assertEquals(3, userPreferences.getContacts().length);
    }    

}
