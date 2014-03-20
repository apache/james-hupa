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

package org.apache.hupa.server.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.server.utils.RegexPatterns;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.exception.HupaException;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class SendMessageBaseServiceImpl extends AbstractService implements SendMessageService {

    UserPreferencesStorage userPreferences;

    @Inject
    public SendMessageBaseServiceImpl(UserPreferencesStorage preferences, IMAPStoreCache cache) {
        this.cache = cache;
        this.userPreferences = preferences;
    }

    public GenericResult send(SendMessageAction action)
            throws Exception {
        GenericResult result = new GenericResultImpl();
        try {
            User user = getUser();
            Message message = createMessage(cache.getMailSession(user), action);
            message = fillBody(message,action);
            sendMessage(getUser(), message);
            if (!user.getSettings().getSmtpServer().contains("gmail.com")) {
                saveSentMessage(getUser(), message);
            }
            resetAttachments(action);

            // TODO: notify the user more accurately where the error is
            // if the message was sent and the storage in the sent folder failed, etc.
        } catch (AddressException e) {
            result.setError("Error while parsing recipient: " + e.getMessage());
            logger.error("Error while parsing recipient", e);
        } catch (AuthenticationFailedException e) {
            result.setError("Error while sending message: SMTP Authentication error.");
            logger.error("SMTP Authentication error", e);
        } catch (MessagingException e) {
            result.setError("Error while sending message: " + e.getMessage());
            logger.error("Error while sending message", e);
        } catch (Exception e) {
            result.setError("Unexpected exception while sendig message: " + e.getMessage());
            logger.error("Unexpected exception while sendig message: ", e);
        }
        return result;
    }

    /**
     * Create basic Message which contains all headers. No body is filled
     *
     * @param session the Session
     * @param action the action
     * @return message
     * @throws AddressException
     * @throws MessagingException
     */
    public Message createMessage(Session session, SendMessageAction action) throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        SmtpMessage m = action.getMessage();
        message.setFrom(new InternetAddress(m.getFrom()));

        userPreferences.addContact(m.getTo());
        userPreferences.addContact(m.getCc());
        userPreferences.addContact(m.getBcc());

        message.setRecipients(RecipientType.TO, MessageUtils.getRecipients(m.getTo()));
        message.setRecipients(RecipientType.CC, MessageUtils.getRecipients(m.getCc()));
        message.setRecipients(RecipientType.BCC, MessageUtils.getRecipients(m.getBcc()));
        message.setSentDate(new Date());
        message.addHeader("User-Agent:", "HUPA, The Apache JAMES webmail client.");
        message.addHeader("X-Originating-IP", getClientIpAddr());
        message.setSubject(m.getSubject(), "utf-8");
        updateHeaders(message, action);
        message.saveChanges();
        return message;
    }

    public static String getClientIpAddr() {
        HttpServletRequest request = RequestFactoryServlet.getThreadLocalRequest();
        String ip = "unknown";
        if (request != null) {
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    protected void updateHeaders(MimeMessage message, SendMessageAction action) {
        if (action.getInReplyTo() != null) {
            try {
                message.addHeader(SConsts.HEADER_IN_REPLY_TO, action.getInReplyTo());
            } catch (MessagingException e) {
                logger.error("Error while setting header:" + e.getMessage(), e);
            }
        }
        if (action.getReferences() != null) {
            try {
                message.addHeader(SConsts.HEADER_REFERENCES, action.getReferences());
            } catch (MessagingException e) {
                logger.error("Error while setting header:" + e.getMessage(), e);
            }
        }
    }

    /**
     * Fill the body of the given message with data which the given action contain
     *
     * @param message the message
     * @param action the action
     * @return filledMessage
     * @throws MessagingException
     * @throws IOException
     * @throws HupaException
     */
    public Message fillBody(Message message, SendMessageAction action) throws MessagingException, IOException, HupaException {

        String html = restoreInlineLinks(action.getMessage().getText());

        // TODO: client sends the message as a html document right now,
        // the idea is that it should be sent in both formats because
        // it is easier to handle html in the browser.
        String text = htmlToText(html);

        @SuppressWarnings("rawtypes")
        List items = getAttachments(action);

        return composeMessage(message, text, html, items);
    }

    protected String restoreInlineLinks(String s) {
        return RegexPatterns.replaceAll(s, RegexPatterns.regex_revertInlineImg, RegexPatterns.repl_revertInlineImg);
    }

    // TODO: just temporary stuff because it has to be done in the client side
    protected String htmlToText(String s){
        s=s.replaceAll("\n", " ");
        s=s.replaceAll("(?si)<br\\s*?/?>", "\n");
        s=s.replaceAll("(?si)</div\\s*?>", "\n");
        s=s.replaceAll("(\\w)<.*?>(\\w)", "$1 $2");
        s=s.replaceAll("<.*?>", "");
        s=s.replaceAll("[ \t]+", " ");
        return s;
    }

    /**
     * Get the attachments stored in the registry.
     *
     * @param action
     * @return A list of stored attachments
     * @throws HupaException
     */
    @SuppressWarnings("rawtypes")
    protected List getAttachments(SendMessageAction action) throws MessagingException, HupaException {
        FileItemRegistry registry = SessionUtils.getSessionRegistry(logger, httpSessionProvider.get());
        List<MessageAttachment> attachments = action.getMessage().getMessageAttachments();

        ArrayList<FileItem> items = new ArrayList<FileItem>();
        if (attachments != null && attachments.size() > 0) {
            for (MessageAttachment attachment: attachments) {
                FileItem fItem = registry.get(attachment.getName());
                if (fItem != null)
                    items.add(fItem);
            }
            logger.debug("Found " + items.size() + " attachmets in the registry.");
        }
        return items;
    }

    /**
     * Remove attachments from the registry
     *
     * @param action
     * @throws MessagingException
     * @throws ActionException
     */
    protected void resetAttachments(SendMessageAction action) throws MessagingException {
        SmtpMessage msg = action.getMessage();
        List<MessageAttachment> attachments = msg.getMessageAttachments();
        if (attachments != null && ! attachments.isEmpty()) {
            for(MessageAttachment attach : attachments)
                SessionUtils.getSessionRegistry(logger, httpSessionProvider.get()).remove(attach.getName());
        }
    }

    /**
     * Send the message using SMTP, if the configuration uses authenticated SMTP, it uses
     * the user stored in session to get the given login and password.
     *
     * @param user
     * @param session
     * @param message
     * @throws MessagingException
     */
    protected void sendMessage(User user, Message message) throws MessagingException {
        cache.sendMessage(message);
        logger.info("Send message from " + message.getFrom()[0].toString());
    }

    /**
     * Save the message in the sent folder
     *
     * @param user
     * @param message
     * @throws MessagingException
     * @throws IOException
     */
    protected void saveSentMessage(User user, Message message) throws MessagingException, IOException {
        IMAPStore iStore = cache.get(user);
        IMAPFolder folder = (IMAPFolder) iStore.getFolder(user.getSettings().getSentFolderName());

        if (folder.exists() || folder.create(IMAPFolder.READ_WRITE)) {
            if (folder.isOpen() == false) {
                folder.open(Folder.READ_WRITE);
            }

            // It is necessary to copy the message, before putting it
            // in the sent folder. If not, it is not guaranteed that it is
            // stored in ascii and is not possible to get the attachments
            // size. message.saveChanges() doesn't fix the problem.
            // There are tests which demonstrate this.
            message = new MimeMessage((MimeMessage)message);

            message.setFlag(Flag.SEEN, true);
            folder.appendMessages(new Message[] {message});

            try {
                folder.close(false);
            } catch (MessagingException e) {
                // we don't care on close
            }
        }
    }

    /**
     * Fill the body of a message already created.
     * The result message depends on the information given.
     *
     * @param message
     * @param text
     * @param html
     * @param parts
     * @return The composed message
     * @throws MessagingException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static Message composeMessage (Message message, String text, String html, List parts) throws MessagingException, IOException {

        MimeBodyPart txtPart = null;
        MimeBodyPart htmlPart = null;
        MimeMultipart mimeMultipart = null;

        if (text == null && html == null) {
           text = "";
        }
        if (text != null) {
            txtPart = new MimeBodyPart();
            txtPart.setContent(text, "text/plain; charset=UTF-8");
        }
        if (html != null) {
            htmlPart = new MimeBodyPart();
            htmlPart.setContent(html, "text/html; charset=UTF-8");
        }
        if (html != null && text != null) {
            mimeMultipart = new MimeMultipart();
            mimeMultipart.setSubType("alternative");
            mimeMultipart.addBodyPart(txtPart);
            mimeMultipart.addBodyPart(htmlPart);
        }

        if (parts == null || parts.isEmpty()) {
            if (mimeMultipart != null) {
                message.setContent(mimeMultipart);
            } else if (html != null) {
                message.setText(html);
                message.setHeader("Content-type", "text/html");
            } else if (text != null) {
                message.setText(text);
            }
        } else {
            MimeBodyPart bodyPart = new MimeBodyPart();
            if (mimeMultipart != null) {
                bodyPart.setContent(mimeMultipart);
            } else if (html != null) {
                bodyPart.setText(html);
                bodyPart.setHeader("Content-type", "text/html");
            } else if (text != null) {
                bodyPart.setText(text);
            }
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            for (Object attachment: parts) {
                if (attachment instanceof FileItem) {
                    multipart.addBodyPart(MessageUtils.fileitemToBodypart((FileItem)attachment));
                } else {
                    multipart.addBodyPart((BodyPart)attachment);
                }
            }
            message.setContent(multipart);
        }

        message.saveChanges();
        return message;

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


}
