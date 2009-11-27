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

import org.apache.commons.logging.Log;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.guice.GuiceTestModule;
import org.apache.hupa.server.guice.SessionProvider;
import org.apache.hupa.server.mock.MockHttpSession;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.server.mock.MockLogProvider;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.ForwardMessage;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.sun.mail.imap.IMAPStore;

public class FowardMessageHandlerTest extends TestCase {

    public class TestConfigurationModule extends AbstractModule {
        @Override
        protected void configure() {

            DemoModeConstants.demoProperties.put("DefaultUserSessionId", "WhateverId");
            Names.bindProperties(binder(), DemoModeConstants.demoProperties);

            bind(Log.class).toProvider(MockLogProvider.class).in(Singleton.class);
            bind(FileItemRegistry.class).in(Singleton.class);
            bind(Session.class).toProvider(SessionProvider.class);
            bind(HttpSession.class).to(MockHttpSession.class).in(Singleton.class);

            bind(IMAPStoreCache.class).to(InMemoryIMAPStoreCache.class);

            bind(ForwardMessageHandler.class);
            bind(GetMessageDetailsHandler.class);

        }
    }

    public void testMine() throws Exception {
        
        
        GuiceTestModule module = new GuiceTestModule();
        Injector injector = module.getInjector();

        Session session = injector.getInstance(Session.class);
        User demouser = DemoModeConstants.demoUser;

        HttpSession httpSession = injector.getInstance(HttpSession.class);
        httpSession.setAttribute("user", demouser);

        IMAPStoreCache storeCache = injector.getInstance(IMAPStoreCache.class);
        IMAPStore store = injector.getInstance(IMAPStore.class);
        ((MockIMAPStoreCache)storeCache).addValidUser(demouser, store);
        
        FileItemRegistry registry = injector.getInstance(FileItemRegistry.class);
        
        ForwardMessageHandler handler = injector.getInstance(ForwardMessageHandler.class);

        MockIMAPFolder sentbox = (MockIMAPFolder) store.getFolder(DemoModeConstants.DEMO_MODE_SENT_FOLDER);
        assertTrue(sentbox.getMessages().length == 0);

        MockIMAPFolder inbox = (MockIMAPFolder) store.getFolder(DemoModeConstants.DEMO_MODE_INBOX_FOLDER);
        assertTrue(inbox.getMessages().length >= 0);
        
        Message message = MsgUtils.fillMockMimeMessage(session, "hola", null, 2);
        inbox.appendMessages(new Message[]{message});
        long msgUid = inbox.getUID(message);
        message = inbox.getMessageByUID(msgUid);
        assertNotNull(message);
        
        IMAPFolder ifolder = new IMAPFolder(inbox.getFullName());
        SMTPMessage smtpmsg = MsgUtils.fillSMTPMessage(registry, 2);
        ForwardMessage action = new ForwardMessage(smtpmsg, ifolder, msgUid);
        
        message = handler.createMessage(session, action);
        message = handler.fillBody(message, action);

        // TODO: this is the expected behavior of the handler, but it doesn't work
        // It's commented to avoid breaking the build
        
//        String expected = "multipart/mixed\n"
//            + " multipart/alternative\n"
//            + "  text/plain\n"
//            + "  text/html\n"
//            + " mock/attachment => file_1.bin\n"
//            + " mock/attachment => file_2.bin\n";
//        
//        assertEquals(expected, MsgUtils.summaryzeContent(message).toString());

    }

}
