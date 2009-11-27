package org.apache.hupa.server.servlet;

import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.apache.hupa.server.handler.AbstractHandlerTest;

public class DownloadAttachmentServletTest extends AbstractHandlerTest {

    public void testDownloadAttachmentByName() throws Exception {
        MimeMessage message = loadMessage("7.msg");
        Part part = DownloadAttachmentServlet.handleMultiPart(logger, message
                .getContent(), "Image.4FB480B138F7456382ABBD1EE7B0748A");
        assertNotNull(part);
    }
    
    public void testDownloadAttachmentByContentId() throws Exception {
        MimeMessage message = loadMessage("7.msg");
        Part part = DownloadAttachmentServlet.handleMultiPart(logger, message
                .getContent(), "1.1934304663@web28309.mail.ukl.yahoo.com");
        assertNotNull(part);
    }

}
