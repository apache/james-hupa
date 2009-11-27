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
import javax.mail.Session;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.guice.GuiceTestModule;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.SendMessage;

import com.google.inject.Injector;
import com.sun.mail.imap.IMAPStore;

public class AbtractSendMessageHandlerTest extends TestCase {
    
    GuiceTestModule module = new GuiceTestModule();
    Injector injector = module.getInjector();
    
    public void testFillMockMessages () throws Exception{
        Session session = injector.getInstance(Session.class);
        
        Message msg = MsgUtils.fillMockMimeMessage(session, "Hola", null, 0);
        String exp = "text/plain\n";
        assertEquals(exp, MsgUtils.summaryzeContent(msg).toString());

        msg = MsgUtils.fillMockMimeMessage(session, null, "<div>hola</div>", 0);
        exp = "text/html\n";
        assertEquals(exp, MsgUtils.summaryzeContent(msg).toString());

        msg = MsgUtils.fillMockMimeMessage(session, "hola", "<div>hola</div>", 0);
        exp = "multipart/alternative\n" +
              " text/plain\n" +
              " text/html\n";
        assertEquals(exp, MsgUtils.summaryzeContent(msg).toString());
        
        msg = MsgUtils.fillMockMimeMessage(session, "hola", "<div>hola</div>", 1);
        exp = "multipart/mixed\n" +
              " multipart/alternative\n" +
              "  text/plain\n" +
              "  text/html\n" +
              " mock/attachment => file_1.bin\n";
        assertEquals(exp, MsgUtils.summaryzeContent(msg).toString());
        
        msg = MsgUtils.fillMockMimeMessage(session, "hola", "<div>hola</div>", 3);
        exp = "multipart/mixed\n" +
              " multipart/alternative\n" +
              "  text/plain\n" +
              "  text/html\n" +
              " mock/attachment => file_1.bin\n" +
              " mock/attachment => file_2.bin\n" +
              " mock/attachment => file_3.bin\n";
        assertEquals(exp, MsgUtils.summaryzeContent(msg).toString());
    }
    
    public void testHtmlmessageToText() {
        String txt, res;
        txt = "<div>Hola:</div>Como \n estas<br/>Adios\n\n";
        
        @SuppressWarnings("unchecked")
        AbstractSendMessageHandler<SendMessage> handler = injector.getInstance(AbstractSendMessageHandler.class);

        res = handler.htmlToText(txt);
        assertEquals("Hola:\nComo estas\nAdios ", res);
    }
    
    public void testSendEmailWithAttachments() throws Exception {
        
        Session session = injector.getInstance(Session.class);
        User demouser = DemoModeConstants.demoUser;

        HttpSession httpSession = injector.getInstance(HttpSession.class);
        httpSession.setAttribute("user", demouser);

        IMAPStoreCache storeCache = injector.getInstance(IMAPStoreCache.class);
        IMAPStore store = injector.getInstance(IMAPStore.class);
        ((MockIMAPStoreCache)storeCache).addValidUser(demouser, store);
        
        FileItemRegistry registry = injector.getInstance(FileItemRegistry.class);
        
        @SuppressWarnings("unchecked")
        AbstractSendMessageHandler<SendMessage> handler = injector.getInstance(AbstractSendMessageHandler.class);

        MockIMAPFolder sentbox = (MockIMAPFolder) store.getFolder(DemoModeConstants.DEMO_MODE_SENT_FOLDER);
        assertTrue(sentbox.getMessages().length == 0);
        
        SMTPMessage smtpmsg = MsgUtils.fillSMTPMessage(registry, 2);
        SendMessage action = new SendMessage(smtpmsg);
        
        Message message = handler.createMessage(session, action);
        message =  handler.fillBody(message, action);
        
        String expected = "multipart/mixed\n"
                        + " multipart/alternative\n"
                        + "  text/plain\n"
                        + "  text/html\n"
                        + " mock/attachment => file_1.bin\n"
                        + " mock/attachment => file_2.bin\n";
        
        assertEquals(expected, MsgUtils.summaryzeContent(message).toString());
        
        handler.sendMessage(demouser, session, message);
        handler.saveSentMessage(demouser, message);
        assertTrue(sentbox.getMessages().length == 1);
        
    }

}
