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
