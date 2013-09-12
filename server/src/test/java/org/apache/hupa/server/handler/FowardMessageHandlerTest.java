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

import javax.mail.Message;

import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.rpc.ForwardMessage;

import com.sun.mail.imap.IMAPStore;

public class FowardMessageHandlerTest extends HupaGuiceTestCase {

    public void testForwardMessage() throws Exception {
        IMAPStore store = storeCache.get(testUser);
        
        FileItemRegistry registry = injector.getInstance(FileItemRegistry.class);
        
        MockIMAPFolder sentbox = (MockIMAPFolder) store.getFolder(MockIMAPStore.MOCK_SENT_FOLDER);
        assertTrue(sentbox.getMessages().length == 0);

        MockIMAPFolder inbox = (MockIMAPFolder) store.getFolder(MockIMAPStore.MOCK_INBOX_FOLDER);
        assertTrue(inbox.getMessages().length >= 0);
        
        Message message = TestUtils.createMockMimeMessage(session, 2);
        inbox.appendMessages(new Message[]{message});
        long msgUid = inbox.getUID(message);
        message = inbox.getMessageByUID(msgUid);
        assertNotNull(message);
        
        ImapFolderImpl ifolder = new ImapFolderImpl(inbox.getFullName());
        SmtpMessage smtpmsg = TestUtils.createMockSMTPMessage(registry, 2);
        ForwardMessage action = new ForwardMessage(smtpmsg, ifolder, msgUid);
        
        message = forwardMessageHandler.createMessage(session, action);
        message = forwardMessageHandler.fillBody(message, action);
        
        String expected = "multipart/mixed\n"
            + " multipart/alternative\n"
            + "  text/plain\n"
            + "  text/html\n"
            + " mock/attachment => file_1.bin\n"
            + " mock/attachment => file_2.bin\n";
        
        assertEquals(expected, TestUtils.summaryzeContent(message).toString());
    }

}
