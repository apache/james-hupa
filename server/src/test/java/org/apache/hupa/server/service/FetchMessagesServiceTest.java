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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.shared.data.FetchMessagesActionImpl;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.exception.HupaException;
import org.junit.Test;

public class FetchMessagesServiceTest extends HupaGuiceTestCase {
    @Test public void convert() throws MessagingException{
        MockIMAPFolder f = (MockIMAPFolder)store.getFolder("WHATEVER");
        f.create(Folder.HOLDS_MESSAGES);

        ByteArrayInputStream is = new ByteArrayInputStream("From: \"a b c\"<aa@foo.com>\nTo: b@foo.com\nSubject: something\n\ndata".getBytes());
        MimeMessage m1 = new MimeMessage(session, is);
        is = new ByteArrayInputStream("From: \"=?ISO-8859-1?Q?Manolo_Pe=F1a?=\" <penya@foo.com>\nTo: b@foo.com\nSubject: something\n\ndata".getBytes());
        MimeMessage m2 = new MimeMessage(session, is);
        is = new ByteArrayInputStream("From: a@foo.com\nTo: \"<b@foo.com>\" <b@foo.com>\nSubject: =?ISO-8859-1?Q?Monta=F1a?=\n\ndata".getBytes());
        MimeMessage m3 = new MimeMessage(session, is);

        List<org.apache.hupa.shared.domain.Message> msgs = fetchMessagesService.convert(2, f, new Message[]{m1, m2, m3});
        assertEquals(2, msgs.size());

        msgs = fetchMessagesService.convert(10, f, new Message[]{m1, m2, m3});
        assertEquals(3, msgs.size());

        msgs = fetchMessagesService.convert(10, f, new Message[]{m1});
        assertEquals("a b c <aa@foo.com>",  msgs.get(0).getFrom());

        msgs = fetchMessagesService.convert(10, f, new Message[]{m2});
        assertEquals("Manolo Pe\u00F1a <penya@foo.com>",  msgs.get(0).getFrom());

        msgs = fetchMessagesService.convert(10, f, new Message[]{m3});
        assertEquals("Monta\u00F1a",  msgs.get(0).getSubject());
        assertEquals("b@foo.com <b@foo.com>",  msgs.get(0).getTo().get(0));

    }


    @Test public void fetchMessages() throws HupaException, MessagingException {

        MockIMAPFolder serverfolder = (MockIMAPFolder)store.getFolder("WHATEVER");
        serverfolder.create(Folder.HOLDS_MESSAGES);

        ImapFolder clientfolder = new ImapFolderImpl("WHATEVER");
        FetchMessagesResult result = fetchMessagesService.fetch(new FetchMessagesActionImpl(clientfolder, 0, 10, "*"));
        assertEquals(0, result.getRealCount());

        ByteArrayInputStream is = new ByteArrayInputStream("From: a@foo.com\nTo: b@foo.com\nSubject: something\n\ndata 1".getBytes());
        MimeMessage msg = new MimeMessage(session, is);
        serverfolder.addMessages(new Message[]{msg});
        result = fetchMessagesService.fetch(new FetchMessagesActionImpl(clientfolder, 0, 10, "something"));
        assertEquals(1, result.getRealCount());
        assertEquals(1, result.getMessages().size());

        result = fetchMessagesService.fetch(new FetchMessagesActionImpl(clientfolder, 0, 10, null));
        assertEquals(1, result.getRealCount());
        assertEquals(1, result.getMessages().size());

        is = new ByteArrayInputStream("From: a@foo.com\nTo: b@foo.com\nSubject: something\n\ndata 2".getBytes());
        msg = new MimeMessage(session, is);
        serverfolder.appendMessages(new Message[]{msg});

        result = fetchMessagesService.fetch(new FetchMessagesActionImpl(clientfolder, 0, 10, "data"));
        assertEquals(2, result.getRealCount());
        assertEquals(2, result.getMessages().size());

        result = fetchMessagesService.fetch(new FetchMessagesActionImpl(clientfolder, 0, 10, "data 2"));
        assertEquals(1, result.getRealCount());

        result = fetchMessagesService.fetch(new FetchMessagesActionImpl(clientfolder, 0, 10, null));
        assertEquals(2, serverfolder.getMessageCount());
        assertEquals(2, serverfolder.getUnreadMessageCount());
        assertEquals(2, result.getRealCount());
        assertEquals(2, result.getMessages().size());
        assertEquals(2, result.getMessages().size());

        msg.setFlags(new Flags(Flag.SEEN), true);
        assertEquals(1, serverfolder.getUnreadMessageCount());

        serverfolder.appendMessages(new Message[]{msg});
        result = fetchMessagesService.fetch(new FetchMessagesActionImpl(clientfolder, 0, 10, "data"));
        assertEquals(3, result.getRealCount());
        assertEquals(1, result.getRealUnreadCount());
    }
}
