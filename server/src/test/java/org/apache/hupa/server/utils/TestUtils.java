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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.service.SendMessageBaseServiceImpl;
import org.apache.hupa.shared.data.MessageAttachmentImpl;
import org.apache.hupa.shared.data.SmtpMessageImpl;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.SmtpMessage;

import com.sun.mail.imap.IMAPStore;

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
            char[] padding = new char[spaces];
            Arrays.fill(padding, ' ');
            return new String(padding) + text;
        }

        public String toString() {
            StringBuilder ret = new StringBuilder();
            for (String s : this)
                ret.append(s).append("\n");
            return ret.toString();
        }

    }

    /**
     * Creates a FileItem which stores data in memory
     *
     * @param filename
     * @return a new item
     * @throws IOException
     */
    public static FileItem createMockFileItem(String filename, String contentType) throws IOException {
        FileItemFactory f = new DiskFileItemFactory();
        FileItem item = f.createItem("fieldname_" + filename, contentType, false, filename);
        OutputStream os = item.getOutputStream();
        os.write("ABCDEFGHIJK\n".getBytes());
        os.close();
        return item;
    }

    public static FileItem createMockFileItem(String filename) throws IOException {
        return createMockFileItem(filename, "mock/attachment");
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
        msgFile = MockIMAPFolder.MOCK_MESSAGES_LOCATION + msgFile;

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(msgFile);
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
        return SendMessageBaseServiceImpl.composeMessage(message, text, html, items);
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
    public static SmtpMessage createMockSMTPMessage(FileItemRegistry registry, int nfiles) throws AddressException, MessagingException, IOException {
        ArrayList<MessageAttachment> attachments = new ArrayList<MessageAttachment>();

        for (int i = 1; i <= nfiles; i++) {
            FileItem fileItem;
            fileItem = TestUtils.createMockFileItem("uploadedFile_" + i + ".bin");
            registry.add(fileItem);

            MessageAttachment msgAttach = new MessageAttachmentImpl();
            msgAttach.setName(fileItem.getFieldName());
            msgAttach.setContentType(fileItem.getContentType());
            msgAttach.setSize((int) fileItem.getSize());

            attachments.add(msgAttach);
        }

        SmtpMessage smtpMessage = new SmtpMessageImpl();
        smtpMessage.setFrom("Test user <from@dom.com>");
        smtpMessage.setTo(Arrays.asList("to@dom.com"));
        smtpMessage.setCc(Arrays.asList("cc@dom.com"));
        smtpMessage.setBcc(Arrays.asList("bcc@dom.com"));
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

    /**
     * Add a mock attachment to a mime message, you can specify whether the attachment
     * is an in-line image, and the file name
     *
     * @param message
     * @param fileName
     * @param isInline
     * @throws IOException
     * @throws MessagingException
     */
    public static void addMockAttachment(Message message, String fileName, boolean isInline) throws IOException, MessagingException {
        FileItem item = createMockFileItem(fileName, isInline ? "image/mock" : "mock/attachment");

        BodyPart part = MessageUtils.fileitemToBodypart(item);
        if (isInline)
            part.addHeader("Content-ID", "any-id");

        Multipart mpart = (Multipart) message.getContent();
        mpart.addBodyPart(part);
        message.saveChanges();
    }


    public static String dumpStore(IMAPStore store) throws MessagingException {
        String ret = "";
        for (Folder f : store.getDefaultFolder().list()) {
            ret += f.getFullName() + " " + f.getMessageCount() + "\n";
        }
        return ret;
    }

}