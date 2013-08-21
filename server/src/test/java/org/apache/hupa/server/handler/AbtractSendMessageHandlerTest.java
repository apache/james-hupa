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
import javax.mail.Part;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.rpc.SendMessage;

import com.sun.mail.imap.IMAPStore;

public class AbtractSendMessageHandlerTest extends HupaGuiceTestCase {

    String contentTwoAttach = "multipart/mixed\n"
                            + " multipart/alternative\n"
                            + "  text/plain\n"
                            + "  text/html\n"
                            + " mock/attachment => uploadedFile_1.bin\n"
                            + " mock/attachment => uploadedFile_2.bin\n";

    public void testComposeMessage() throws Exception{
        Message msg = TestUtils.createMockMimeMessage(session, "body", null, -1);
        String exp = "text/plain\n";
        assertEquals(exp, TestUtils.summaryzeContent(msg).toString());
        
        msg = TestUtils.createMockMimeMessage(session, "body", null, 0);
        exp = "text/plain\n";
        assertEquals(exp, TestUtils.summaryzeContent(msg).toString());

        msg = TestUtils.createMockMimeMessage(session, null, "html", 0);
        exp = "text/html\n";
        assertEquals(exp, TestUtils.summaryzeContent(msg).toString());

        msg = TestUtils.createMockMimeMessage(session, 0);
        exp = "multipart/alternative\n" +
              " text/plain\n" +
              " text/html\n";
        assertEquals(exp, TestUtils.summaryzeContent(msg).toString());
        
        msg = TestUtils.createMockMimeMessage(session, 1);
        exp = "multipart/mixed\n" +
              " multipart/alternative\n" +
              "  text/plain\n" +
              "  text/html\n" +
              " mock/attachment => file_1.bin\n";
        assertEquals(exp, TestUtils.summaryzeContent(msg).toString());
        
        msg = TestUtils.createMockMimeMessage(session, 3);
        exp = "multipart/mixed\n" +
              " multipart/alternative\n" +
              "  text/plain\n" +
              "  text/html\n" +
              " mock/attachment => file_1.bin\n" +
              " mock/attachment => file_2.bin\n" +
              " mock/attachment => file_3.bin\n";
        assertEquals(exp, TestUtils.summaryzeContent(msg).toString());
    }
    
    public void testRestoreInlineLinks() {
        String txt, res;
        txt = ".. <img\nsrc='...&name=abcd' name='cid:abcd'\nwhatever=/> ..";
        res = sendMessageHandler.restoreInlineLinks(txt);
        assertEquals(".. <img\nsrc='cid:abcd'\nwhatever=/> ..", res);
    }

    public void testHtmlmessageToText() {
        String txt, res;
        txt = "<div>Hola:</div>Como \n estas<br/>Adios\n\n";
        res = sendMessageHandler.htmlToText(txt);
        assertEquals("Hola:\nComo estas\nAdios ", res);
    }
    
    public void testSendEmailWithAttachments() throws Exception {
        IMAPStore store = storeCache.get(testUser);
        MockIMAPFolder sentbox = (MockIMAPFolder) store.getFolder(MockIMAPStore.MOCK_SENT_FOLDER);
        
        SMTPMessage smtpmsg = TestUtils.createMockSMTPMessage(SessionUtils.getSessionRegistry(logger, httpSession), 2);
        SendMessage action = new SendMessage(smtpmsg);
        
        Message message = sendMessageHandler.createMessage(session, action);
        message =  sendMessageHandler.fillBody(message, action);
        
        assertEquals(contentTwoAttach, TestUtils.summaryzeContent(message).toString());

        sendMessageHandler.sendMessage(testUser, message);
        
        // The reported size is wrong before the message has been saved
        Part part = MessageUtils.handleMultiPart(logger, message.getContent(), "uploadedFile_1.bin");
        assertTrue(part.getSize() < 0);

        assertTrue(sentbox.getMessages().length == 0);
        sendMessageHandler.saveSentMessage(testUser, message);
        assertTrue(sentbox.getMessages().length == 1);
        
        message = sentbox.getMessage(0);
        assertNotNull(message);
        assertEquals(contentTwoAttach, TestUtils.summaryzeContent(message).toString());

        // After saving the message, the reported size has to be OK
        part = MessageUtils.handleMultiPart(logger, message.getContent(), "uploadedFile_1.bin");
        assertTrue(part.getSize() > 0);
    }

    public void testExecute() throws Exception {
        IMAPStore store = storeCache.get(testUser);
        MockIMAPFolder sentbox = (MockIMAPFolder) store.getFolder(MockIMAPStore.MOCK_SENT_FOLDER);
        
        SMTPMessage smtpmsg = TestUtils.createMockSMTPMessage(SessionUtils.getSessionRegistry(logger, httpSession), 2);
        SendMessage action = new SendMessage(smtpmsg);
        
        assertTrue(sentbox.getMessages().length == 0);
        sendMessageHandler.execute(action, null);
        Message message = sentbox.getMessage(0);
        assertNotNull(message);
        assertEquals(contentTwoAttach, TestUtils.summaryzeContent(message).toString());
        
        Part part = MessageUtils.handleMultiPart(logger, message.getContent(), "uploadedFile_1.bin");
        assertTrue(part.getSize() > 0);
    }
}
