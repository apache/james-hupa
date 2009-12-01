package org.apache.hupa.server.utils;

import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.guice.GuiceTestModule;

import com.google.inject.Injector;

import junit.framework.TestCase;

public class MessageUtilsTest extends TestCase {

    GuiceTestModule module = new GuiceTestModule();
    Injector injector = module.getInjector();
    
    public void testExtractMessageAttachments() throws Exception {
        Session session = injector.getInstance(Session.class);
        Log logger = injector.getInstance(Log.class);
        Message message = TestUtils.createMockMimeMessage(session, 2);
        List<BodyPart> parts = MessageUtils.extractMessageAttachments(logger, message.getContent());
        assertEquals(2, parts.size());
    }
}