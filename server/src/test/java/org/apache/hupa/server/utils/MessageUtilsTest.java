package org.apache.hupa.server.utils;

import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.HupaTestCase;

public class MessageUtilsTest extends HupaTestCase {
    
    public void testExtractMessageAttachments() throws Exception {
        Session session = injector.getInstance(Session.class);
        Log logger = injector.getInstance(Log.class);
        Message message = TestUtils.createMockMimeMessage(session, 2);
        List<BodyPart> parts = MessageUtils.extractMessageAttachments(logger, message.getContent());
        assertEquals(2, parts.size());
    }

    public void testExtractInlineAttachments() throws Exception {
        Session session = injector.getInstance(Session.class);
        Log logger = injector.getInstance(Log.class);

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