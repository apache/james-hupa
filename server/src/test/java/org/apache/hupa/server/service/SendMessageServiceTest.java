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

import java.util.Arrays;

import javax.mail.Folder;
import javax.mail.Message;

import junit.framework.Assert;

import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.guice.AbstractGuiceTestModule;
import org.apache.hupa.server.guice.GuiceServerTestModule;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.data.SendMessageActionImpl;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.junit.Test;

import com.google.inject.Module;
import com.sun.mail.imap.IMAPFolder;

public class SendMessageServiceTest extends HupaGuiceTestCase {

    static final String imapUser = "mcdodot@gmail.com";
    static final String imapPass = "5234manolito";
    final private boolean useMock = true;

    @Override
    protected Module[] getModules() {
        if (useMock ) {
            return super.getModules();
        } else {
            return new Module[]{new GuiceServerTestModule(){{
                properties = AbstractGuiceTestModule.gmailProperties;
                properties.setProperty("Username", imapUser);
                properties.setProperty("Password", imapPass);
            }}};
        }
    }

    @Test
    public void testSendMessage() throws Exception {
        IMAPFolder folder = (IMAPFolder) store.getFolder(testUser.getSettings().getSentFolderName());
        folder.open(Folder.READ_ONLY);
        long count = folder.getMessageCount();

        FileItemRegistry registry = SessionUtils.getSessionRegistry(logger, httpSession);

        // Create a reply user action with an uploaded message
        SmtpMessage smtpmsg = TestUtils.createMockSMTPMessage(registry, 1);
        SendMessageActionImpl action = new SendMessageActionImpl(smtpmsg);
        String subject = "Test Message: " + System.currentTimeMillis();
        smtpmsg.setTo(Arrays.asList("manolo@alcala.org"));
        smtpmsg.setCc(Arrays.<String>asList());
        smtpmsg.setBcc(Arrays.<String>asList());
        smtpmsg.setFrom(imapUser);
        smtpmsg.setSubject(subject);

        Message message = sendMessageService.createMessage(session, action);
        message = sendMessageService.fillBody(message, action);

        sendMessageService.send(action);

        Assert.assertTrue(count < folder.getMessageCount());
    }


}
