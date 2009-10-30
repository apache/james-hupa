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

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.MessageDetails;

public class GetMessageDetailsHandlerTest extends AbstractHandlerTest {
	
	GetMessageDetailsHandler handler = new GetMessageDetailsHandler(storeCache, logger, httpSessionProvider);

	private MessageDetails loadMessageDetails(String msgFile) throws Exception {
        return handler.mimeToDetails(loadMessage(msgFile));
    }
	
    public void testMessageDetails_textPlain() throws Exception {
    	MessageDetails details = loadMessageDetails("0.msg");
    	assertFalse(details.isHTML());
    	assertTrue(details.getText().length() > 0);
    }

    public void testMessageDetails_multiparMixed() throws Exception {
    	MessageDetails details = loadMessageDetails("1.msg");
    	assertFalse(details.isHTML());
    	assertEquals(1, details.getMessageAttachments().size());
    	assertTrue(details.getText().length() > 0);
    }

    public void testMessageDetails_multiparAlternative() throws Exception {
    	MessageDetails details = loadMessageDetails("2.msg");
    	assertTrue(details.isHTML());
    	assertEquals(0, details.getMessageAttachments().size());
    	assertTrue(details.getText().length() > 0);
    }

    public void testMessageDetails_charsetIso() throws Exception {
    	MimeMessage message = loadMessage("3.msg");
    	String from = message.getFrom()[0].toString();
    	assertTrue(from.contains("\u00AE"));
    	
    	MessageDetails details = loadMessageDetails("3.msg");
    	assertTrue(details.isHTML());
    	assertEquals(0, details.getMessageAttachments().size());
    	assertTrue(details.getText().length() > 0);
    }

    public void testMessageDetails_textHtml() throws Exception {
    	MessageDetails details = loadMessageDetails("4.msg");
    	assertTrue(details.isHTML());
    	assertTrue(details.getText().length() > 0);
    }
    
    public void testMessageDetails_multiparMixed_multipartAlternative() throws Exception {
    	MessageDetails details = loadMessageDetails("6.msg");
    	assertTrue(details.isHTML());
    	assertEquals(1, details.getMessageAttachments().size());
    	assertTrue(details.getText().length() > 0);
    }

    public void testMessageDetails_html_with_inline_images() throws Exception {
    	
    	MockIMAPStore store = (MockIMAPStore) storeCache.get(user);
        MockIMAPFolder serverfolder = (MockIMAPFolder)store.getFolder("WHATEVER"); 
        serverfolder.create(Folder.HOLDS_MESSAGES);
        
    	MimeMessage msg = loadMessage("7.msg");
    	serverfolder.addMessages(new Message[]{msg});

        
        IMAPFolder clientfolder = new IMAPFolder("WHATEVER");
        MessageDetails details = handler.exposeMessage(user, clientfolder, 0);
    	assertTrue(details.isHTML());
        assertTrue(details.getText().contains("img src=\"/hupa/downloadAttachmentServlet?folder_name=WHATEVER&message_uuid=0&attachment_name=1.1934304663@web28309.mail.ukl.yahoo.com\""));
    	
    }
    
}
