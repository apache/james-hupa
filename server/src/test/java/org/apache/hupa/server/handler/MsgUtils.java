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

import gwtupload.server.MemoryFileItemFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.fileupload.FileItem;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.SMTPMessage;

public class MsgUtils {

    /**
     * Creates a FileItem which is stored in memory
     * 
     * @param filename
     * @return a new item
     * @throws IOException
     */
    public static FileItem createMockFileItem(String filename) throws IOException {
        MemoryFileItemFactory f = new MemoryFileItemFactory();
        FileItem item = f.createItem("fieldname_" + filename, "mock/attachment", false, filename);
        item.getOutputStream().write("file content".getBytes());
        item.getOutputStream().close();
        return item;
    }

    /**
     * Parses a mime message and returns an array of content-types which is a
     * simple representation of its structure. Each line of the array represents
     * a part and it is indented with spaces. It's used in testing or debugging.
     * 
     * @param msg
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    protected static ArrayList<String> summaryzeContent(Message msg) throws IOException, MessagingException {
        return summaryzeContent(msg.getContent(), msg.getContentType(), 0);
    }

    static ArrayList<String> summaryzeContent(Object content, String contentType, final int spaces) throws IOException, MessagingException {

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

        public String toString() {
            String ret = "";
            for (String s : this) {
                ret += s + "\n";
            }
            return ret;
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
    }

    public static SMTPMessage fillSMTPMessage(FileItemRegistry registry, int nfiles) throws AddressException, MessagingException, IOException {
        ArrayList<MessageAttachment> attachments = new ArrayList<MessageAttachment>();

        for (int i = 1; i <= nfiles; i++) {
            FileItem fileItem;
            fileItem = MsgUtils.createMockFileItem("file_" + i + ".bin");
            registry.add(fileItem);

            MessageAttachment msgAttach = new MessageAttachment();
            msgAttach.setName(fileItem.getFieldName());
            msgAttach.setContentType(fileItem.getContentType());
            msgAttach.setSize((int) fileItem.getSize());

            attachments.add(msgAttach);
        }

        SMTPMessage smtpMessage = new SMTPMessage();
        smtpMessage.setFrom("from@dom.com");
        smtpMessage.setTo(Arrays.asList("to@dom.com"));
        smtpMessage.setCc(Arrays.asList("cc@dom.com"));
        smtpMessage.setBcc(Arrays.asList("bcc@dom.com"));
        smtpMessage.setSubject("Subject");
        smtpMessage.setText("<div>Body</div>");
        smtpMessage.setMessageAttachments(attachments);

        return smtpMessage;

    }

    public static Message fillMockMimeMessage(Session session, String text, String html, int nfiles) throws MessagingException, IOException {
        ArrayList<FileItem> items = new ArrayList<FileItem>();
        for (int i = 1; i <= nfiles; i++) {
            FileItem fileItem;
            fileItem = MsgUtils.createMockFileItem("file_" + i + ".bin");
            items.add(fileItem);
        }
        return AbstractSendMessageHandler.composeMessage(session, text, html, items);
    }

}