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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.hupa.shared.data.MessageAttachmentImpl;
import org.apache.hupa.shared.domain.MessageAttachment;



/**
 * Utility methods in server side
 */
public class MessageUtils {

    /**
     * Get a Address array for a set of address passed as arguments
     *
     * @param addresses
     * @return Address array
     * @throws AddressException
     */
    public static Address[] getRecipients(String... addresses) throws AddressException {
        return getRecipients(Arrays.asList(addresses));
    }

    /**
     * Get a Address array for the given ArrayList
     *
     * @param recipients
     * @return addressArray
     * @throws AddressException
     */
    public static Address[] getRecipients(List<String> recipients) throws AddressException {
        if (recipients == null) {
            return new InternetAddress[]{};
        }
        Address[] array = new Address[recipients.size()];
        for (int i = 0; i < recipients.size(); i++) {
            array[i] = new InternetAddress(encodeEmail(recipients.get(i)));
        }
        return array;
    }

    /**
     * Extract the attachments present in a mime message
     *
     * @param logger
     * @param content
     * @return A list of body parts of the attachments
     * @throws MessagingException
     * @throws IOException
     */
    static public List<BodyPart> extractMessageAttachments(Log logger, Object content) throws MessagingException, IOException {
        ArrayList<BodyPart> ret = new ArrayList<BodyPart>();
        if (content instanceof Multipart) {
            Multipart part = (Multipart) content;
            for (int i = 0; i < part.getCount(); i++) {
                BodyPart bodyPart = part.getBodyPart(i);
                String fileName = bodyPart.getFileName();
                String[] contentId = bodyPart.getHeader("Content-ID");
                if (bodyPart.isMimeType("multipart/*")) {
                    ret.addAll(extractMessageAttachments(logger, bodyPart.getContent()));
                } else {
                    if (contentId != null || fileName != null) {
                        ret.add(bodyPart);
                    }
                }
            }
        } else {
            logger.error("Unknown content: " + content.getClass().getName());
        }
        return ret;
    }

    static public List<BodyPart> extractInlineImages(Log logger, Object content) throws MessagingException, IOException {
        ArrayList<BodyPart> ret = new ArrayList<BodyPart>();
        for (BodyPart attach : extractMessageAttachments(logger, content)) {
            if (attach.getHeader("Content-ID") != null && attach.getContentType().startsWith("image/"))
                ret.add(attach);
        }
        return ret;
    }

    /**
     * Handle the parts of the given message. The method will call itself
     * recursively to handle all nested parts
     *
     * @param message the MimeMessage
     * @param content the current processing Content
     * @param sbPlain the StringBuffer to fill with text
     * @param attachmentList ArrayList with attachments
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws IOException
     */
    public static boolean handleParts(Message message, Object content, StringBuffer sbPlain,
            ArrayList<MessageAttachment> attachmentList) throws UnsupportedEncodingException, MessagingException,
            IOException {
        boolean isHTML = false;
        if (content instanceof String) {
            if (message.getContentType().toLowerCase().startsWith("text/html")) {
                isHTML = true;
            } else {
                isHTML = false;
            }
            sbPlain.append((String) content);

        } else if (content instanceof Multipart) {

            Multipart mp = (Multipart) content;
            String multipartContentType = mp.getContentType().toLowerCase();

            String text = null;

            if (multipartContentType.startsWith("multipart/alternative")) {
                isHTML = handleMultiPartAlternative(mp, sbPlain);
            } else {
                for (int i = 0; i < mp.getCount(); i++) {
                    Part part = mp.getBodyPart(i);

                    String contentType = part.getContentType().toLowerCase();

                    Boolean bodyRead = sbPlain.length() > 0;

                    if (!bodyRead && contentType.startsWith("text/plain")) {
                        isHTML = false;
                        text = (String) part.getContent();
                    } else if (!bodyRead && contentType.startsWith("text/html")) {
                        isHTML = true;
                        text = (String) part.getContent();
                    } else if (!bodyRead && contentType.startsWith("message/rfc822")) {
                        // Extract the message and pass it
                        MimeMessage msg = (MimeMessage) part.getDataHandler().getContent();
                        isHTML = handleParts(msg, msg.getContent(), sbPlain, attachmentList);
                    } else {
                        if (part.getFileName() != null) {
                            // Inline images are not added to the attachment
                            // list
                            // TODO: improve the in-line images detection
                            if (part.getHeader("Content-ID") == null) {
                                MessageAttachment attachment = new MessageAttachmentImpl();
                                attachment.setName(MimeUtility.decodeText(part.getFileName()));
                                attachment.setContentType(part.getContentType());
                                attachment.setSize(part.getSize());
                                attachmentList.add(attachment);
                            }
                        } else {
                            isHTML = handleParts(message, part.getContent(), sbPlain, attachmentList);
                        }
                    }

                }
                if (text != null)
                    sbPlain.append(text);
            }

        }
        return isHTML;
    }

    private static boolean handleMultiPartAlternative(Multipart mp, StringBuffer sbPlain) throws MessagingException, IOException {
        String text = null;
        boolean isHTML = false;
        for (int i = 0; i < mp.getCount(); i++) {
            Part part = mp.getBodyPart(i);

            String contentType = part.getContentType().toLowerCase();

            // we prefer html
            if (text == null && contentType.startsWith("text/plain")) {
                isHTML = false;
                text = (String) part.getContent();
            } else if (contentType.startsWith("text/html")) {
                isHTML = true;
                text = (String) part.getContent();
            }
        }
        sbPlain.append(text);
        return isHTML;
    }

    /**
     * Loop over MuliPart and write the content to the Outputstream if a
     * attachment with the given name was found.
     *
     * @param logger
     *            The logger to use
     * @param content
     *            Content which should checked for attachments
     * @param attachmentName
     *            The attachmentname or the unique id for the searched attachment
     * @throws MessagingException
     * @throws IOException
     */
    public static Part handleMultiPart(Log logger, Object content, String attachmentName)
            throws MessagingException, IOException {
        if (content instanceof Multipart) {
            Multipart part = (Multipart) content;
            for (int i = 0; i < part.getCount(); i++) {
                Part bodyPart = part.getBodyPart(i);
                String fileName = bodyPart.getFileName();
                String[] contentId = bodyPart.getHeader("Content-ID");
                if (bodyPart.isMimeType("multipart/*")) {
                    Part p = handleMultiPart(logger, bodyPart.getContent(), attachmentName);
                    if (p != null)
                        return p;
                } else {
                    if (contentId != null) {
                        for (String id: contentId) {
                            id = id.replaceAll("^.*<(.+)>.*$", "$1");
                            System.out.println(attachmentName + " " + id);
                            if (attachmentName.equals(id))
                                return bodyPart;
                        }
                    }
                    if (fileName != null) {
                        if (cleanName(attachmentName).equalsIgnoreCase(cleanName(MimeUtility.decodeText(fileName))))
                            return bodyPart;
                    }
                }
            }
        } else {
            logger.error("Unknown content: " + content.getClass().getName());
        }
        return null;
    }

    private static String cleanName(String s) {
        return s.replaceAll("[^\\w .+-]", "");
    }

    /**
     * Convert a FileItem to a BodyPart
     *
     * @param item
     * @return message body part
     * @throws MessagingException
     */
    public static BodyPart fileitemToBodypart(FileItem item) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileItemDataStore(item);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(source.getName());
        return messageBodyPart;
    }

    /**
     * DataStore which wrap a FileItem
     *
     */
    public static class FileItemDataStore implements DataSource {

        private FileItem item;

        public FileItemDataStore(FileItem item) {
            this.item = item;
        }

        /*
         * (non-Javadoc)
         * @see javax.activation.DataSource#getContentType()
         */
        public String getContentType() {
            return item.getContentType();
        }

        /*
         * (non-Javadoc)
         * @see javax.activation.DataSource#getInputStream()
         */
        public InputStream getInputStream() throws IOException {
            return item.getInputStream();
        }

        /*
         * (non-Javadoc)
         * @see javax.activation.DataSource#getName()
         */
        public String getName() {
            String fullName = item.getName();

            // Strip path from file
            int index = fullName.lastIndexOf(File.separator);
            if (index == -1) {
                return fullName;
            } else {
                return fullName.substring(index +1 ,fullName.length());
            }
        }

        /*
         * (non-Javadoc)
         * @see javax.activation.DataSource#getOutputStream()
         */
        public OutputStream getOutputStream() throws IOException {
            return null;
        }

    }
    /**
     * Decode iso-xxxx strings present in subjects and emails like:
     *
     * =?ISO-8859-1?Q?No=20hay=20ma=F1ana?= <hello@hupa.org>
     */
    public static String decodeText(String s) {
        String ret = s;
        try {
            ret = MimeUtility.decodeText(s);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        ret =  ret
          // Remove quotes around names in email addresses
          .replaceFirst("^[<\"' ]+([^\"<>]*)[>\"' ]+<", "$1 <");
        return ret;
    }

    /**
     * Encode non ascii characters present in emails like:
     *
     * =?ISO-8859-1?Q?No=20hay=20ma=F1ana?= <hello@hupa.org>
     */
    public static String encodeEmail(String s) {
        if (s == null) {
            return s;
        }
        Pattern p = Pattern.compile("^\\s*(.*?)\\s*(<[^>]+>)\\s*");
        Matcher m = p.matcher(s);
        return m.matches() ? encodeTexts(m.group(1)) + " " + m.group(2) : s;
    }

    /**
     * Encode non ascii characters present in email headers
     */
    public static String encodeTexts(String s) {
        String ret = s;
        if (s != null) {
            try {
                ret = MimeUtility.encodeText(s, "ISO-8859-1", null);
            } catch (UnsupportedEncodingException e) {
            }
        }
        return ret;
    }
}