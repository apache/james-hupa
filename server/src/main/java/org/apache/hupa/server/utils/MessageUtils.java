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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.FileItemRegistry;



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
            array[i] = new InternetAddress(recipients.get(i));
        }
        return array;
    }

    /**
     * Extract the attachments present in a mime message
     * 
     * @param logger
     * @param content
     * @return
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

    public static FileItemRegistry getSessionRegistry(HttpSession session, Log logger) {
        FileItemRegistry registry = (FileItemRegistry)session.getAttribute("registry");
        if (registry == null) {
            registry = new FileItemRegistry(logger);
            session.setAttribute("registry", registry);
        }
        return registry;
    }

    /**
     * Loop over MuliPart and write the content to the Outputstream if a
     * attachment with the given name was found.
     *
     * @param out
     *            Outputstream to write the content to
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
                            if (attachmentName.equals(id))
                                return bodyPart;
                        }
                    }
                    if (fileName != null){
                        if (attachmentName.equalsIgnoreCase(MimeUtility.decodeText(fileName)))
                            return bodyPart;
                    }
                }
            }
        } else {
            logger.error("Unknown content: " + content.getClass().getName());
        }
        return null;
    }
    
}