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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.handler.AbstractSendMessageHandler;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.SMTPMessage;

/**
 * A bunch of useful methods used for testing
 */
public class TestUtils extends TestCase {

    /**
     * A class which represents the tree of content-types present in a mime
     * message. It is thought to be used only for testing assertions or
     * debugging purposes
     */
    static class ContenTypeArrayList extends ArrayList<String> {

        private static final long serialVersionUID = 1L;

        public boolean add(String type, int spaces) {
            return super.add(ident(spaces, cleanCType(type)));
        }

        public boolean add(String type, String name, int spaces) {
            return super.add(ident(spaces, cleanCType(type) + " => " + name));
        }

        private String cleanCType(String contenType) {
            return contenType.toLowerCase().replaceAll("(?s)[\"\\s]*(.+);.*$", "$1");
        }

        private String ident(int spaces, String text) {
            String ret = "";
            for (int i = 0; i < spaces; i++)
                ret += " ";
            return ret + text;
        }

        public String toString() {
            String ret = "";
            for (String s : this) {
                ret += s + "\n";
            }
            return ret;
        }
    }

    /**
     * Creates a FileItem which stores data in memory
     * 
     * @param filename
     * @return a new item
     * @throws IOException
     */
    public static FileItem createMockFileItem(String filename) throws IOException {
        FileItemFactory f = new DiskFileItemFactory();
        FileItem item = f.createItem("fieldname_" + filename, "mock/attachment", false, filename);
        OutputStream os = item.getOutputStream();
        os.write("ABCDEFGHIJK\n".getBytes());
        os.close();
        return item;
    }
    
    public void testCreateMockFileItem() throws Exception {
        FileItem item = createMockFileItem("filename.jpg");
        assertEquals("ABCDEFGHIJK\n", item.getString());
    }

    /**
     * Create a new mime-message from a file stored in the fixtures folder
     * 
     * @param session
     * @param msgFile
     * @return
     * @throws Exception
     */
    public static MimeMessage loadMessageFromFile(Session session, String msgFile) throws Exception {
        msgFile = DemoModeConstants.DEMO_MODE_MESSAGES_LOCATION + msgFile;
        URL url = Thread.currentThread().getContextClassLoader().getResource(msgFile);
        assertNotNull("Check that the file " + msgFile + " is in the classpath", url);
    
        FileInputStream is = new FileInputStream(url.getFile());
        return new MimeMessage(session, is);
    }

    /**
     * Creates a new mime message.
     * It is possible to specify which parts to create (text, html or both) and
     * the number of attachments 
     *  
     * @param session
     * @param text
     * @param html
     * @param nfiles
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public static Message createMockMimeMessage(Session session, String text, String html, int nfiles) throws MessagingException, IOException {
        ArrayList<FileItem> items = new ArrayList<FileItem>();
        for (int i = 1; i <= nfiles; i++) {
            FileItem fileItem;
            fileItem = TestUtils.createMockFileItem("file_" + i + ".bin");
            items.add(fileItem);
        }
        MimeMessage message = new MimeMessage(session);
        message.setFrom(MessageUtils.getRecipients("from@dom.com")[0]);
        message.setRecipients(RecipientType.TO, MessageUtils.getRecipients("to@dom.com"));
        message.setRecipients(RecipientType.CC, MessageUtils.getRecipients("cc@dom.com"));
        message.setRecipients(RecipientType.BCC, MessageUtils.getRecipients("bcc@dom.com"));
        message.setSubject("Subject");
        return AbstractSendMessageHandler.composeMessage(message, text, html, items);
    }
    
    public static Message createMockMimeMessage(Session session, int nfiles) throws MessagingException, IOException {
        return createMockMimeMessage(session, "Body", "<div>Body</div>", nfiles);
    }

    /**
     * Creates a client side mock smtp message.
     * It is possible to say the number of attachments we want.
     *  
     * @param registry
     * @param nfiles
     * @return
     * @throws AddressException
     * @throws MessagingException
     * @throws IOException
     */
    public static SMTPMessage createMockSMTPMessage(FileItemRegistry registry, int nfiles) throws AddressException, MessagingException, IOException {
        ArrayList<MessageAttachment> attachments = new ArrayList<MessageAttachment>();

        for (int i = 1; i <= nfiles; i++) {
            FileItem fileItem;
            fileItem = TestUtils.createMockFileItem("file_" + i + ".bin");
            registry.add(fileItem);

            MessageAttachment msgAttach = new MessageAttachment();
            msgAttach.setName(fileItem.getFieldName());
            msgAttach.setContentType(fileItem.getContentType());
            msgAttach.setSize((int) fileItem.getSize());

            attachments.add(msgAttach);
        }

        SMTPMessage smtpMessage = new SMTPMessage();
        smtpMessage.setFrom("from@dom.com");
        smtpMessage.setTo(new ArrayList<String>(Arrays.asList("to@dom.com")));
        smtpMessage.setCc(new ArrayList<String>(Arrays.asList("cc@dom.com")));
        smtpMessage.setBcc(new ArrayList<String>(Arrays.asList("bcc@dom.com")));
        smtpMessage.setSubject("Subject");
        smtpMessage.setText("<div>Body</div>");
        smtpMessage.setMessageAttachments(attachments);

        return smtpMessage;

    }

    /**
     * Parses a mime message and returns an array of content-types.
     * Each line in the array represents a mime part and is indented 
     * with spaces.
     *  
     * It's used in testing or debugging.
     * 
     * @param msg
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public static ArrayList<String> summaryzeContent(Message msg) throws IOException, MessagingException {
        return summaryzeContent(msg.getContent(), msg.getContentType(), 0);
    }

    public static ArrayList<String> summaryzeContent(Object content, String contentType, final int spaces) throws IOException, MessagingException {

        ContenTypeArrayList ret = new ContenTypeArrayList();

        ret.add(contentType, spaces);
        if (content instanceof Multipart) {
            Multipart mp = (Multipart) content;
            contentType = mp.getContentType();
            for (int i = 0; i < mp.getCount(); i++) {
                Part part = mp.getBodyPart(i);
                contentType = part.getContentType();
                if (contentType.startsWith("text")) {
                    ret.add(contentType, spaces + 1);
                } else if (contentType.startsWith("message/rfc822")) {
                    MimeMessage msg = (MimeMessage) part.getDataHandler().getContent();
                    ret.addAll(summaryzeContent(msg.getContent(), msg.getContentType(), spaces + 1));
                } else {
                    if (part.getFileName() != null) {
                        ret.add(part.getContentType(), part.getFileName(), spaces + 1);
                    } else {
                        ret.addAll(summaryzeContent(part.getContent(), contentType, spaces + 1));
                    }
                }
            }
        }
        return ret;
    }

}