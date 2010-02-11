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
package org.apache.hupa.server.utils;

import org.apache.hupa.server.HupaGuiceTestCase;

import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Message;

public class MessageUtilsTest extends HupaGuiceTestCase {
    
    public void testExtractMessageAttachments() throws Exception {
        Message message = TestUtils.createMockMimeMessage(session, 2);
        List<BodyPart> parts = MessageUtils.extractMessageAttachments(logger, message.getContent());
        assertEquals(2, parts.size());
    }

    public void testExtractInlineAttachments() throws Exception {
        Message message = TestUtils.createMockMimeMessage(session, 1);
        
        List<BodyPart> attachments = MessageUtils.extractMessageAttachments(logger, message.getContent());
        List<BodyPart> inlineImgs = MessageUtils.extractInlineImages(logger, message.getContent());
        assertEquals(1, attachments.size());
        assertEquals(0, inlineImgs.size());
        
        TestUtils.addMockAttachment(message, "mfile.bin", false);
        
        attachments = MessageUtils.extractMessageAttachments(logger, message.getContent());
        inlineImgs = MessageUtils.extractInlineImages(logger, message.getContent());
        assertEquals(2, attachments.size());
        assertEquals(0, inlineImgs.size());

        TestUtils.addMockAttachment(message, "mfile.jpg", true);
        
        attachments = MessageUtils.extractMessageAttachments(logger, message.getContent());
        inlineImgs = MessageUtils.extractInlineImages(logger, message.getContent());
        assertEquals(3, attachments.size());
        assertEquals(1, inlineImgs.size());
    }
}