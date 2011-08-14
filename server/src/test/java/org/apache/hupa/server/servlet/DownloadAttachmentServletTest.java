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

package org.apache.hupa.server.servlet;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.server.utils.TestUtils;

import javax.mail.Part;
import javax.mail.internet.MimeMessage;


public class DownloadAttachmentServletTest extends HupaGuiceTestCase {

    public void testDownloadAttachmentByName() throws Exception {
        MimeMessage message = TestUtils.loadMessageFromFile(session, "7.msg");
        Part part = MessageUtils.handleMultiPart(logger, message
                .getContent(), "Image.4FB480B138F7456382ABBD1EE7B0748A");
        assertNotNull(part);
    }
    
    public void testDownloadAttachmentByContentId() throws Exception {
        MimeMessage message = TestUtils.loadMessageFromFile(session, "7.msg");
        Part part = MessageUtils.handleMultiPart(logger, message
                .getContent(), "1.1934304663@web28309.mail.ukl.yahoo.com");
        assertNotNull(part);
    }

}
