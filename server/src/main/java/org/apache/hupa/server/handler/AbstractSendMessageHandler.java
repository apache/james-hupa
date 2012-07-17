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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.server.utils.RegexPatterns;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.SendMessage;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handle sending of email messages
 * 
 */
public abstract class AbstractSendMessageHandler<A extends SendMessage> extends AbstractSessionHandler<A,GenericResult> {

    private final boolean auth;
    private final String address;
    private final int port;
    private boolean useSSL = false;
    UserPreferencesStorage userPreferences;
    Session session;

    @Inject
    public AbstractSendMessageHandler(Log logger, IMAPStoreCache store, Provider<HttpSession> provider, UserPreferencesStorage preferences, @Named("SMTPServerAddress") String address, @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL) {
        super(store,logger,provider);
        this.auth = auth;
        this.address = address;
        this.port = port;
        this.useSSL  = useSSL;
        this.userPreferences = preferences;
        this.session = store.getMailSession();
        session.getProperties().put("mail.smtp.auth", auth);
    }

    @Override
    protected GenericResult executeInternal(A action, ExecutionContext context)
            throws ActionException {
        GenericResult result = new GenericResult();
        try {

            Message message = createMessage(session, action);
            message = fillBody(message,action);

            sendMessage(getUser(), message);
            saveSentMessage(getUser(), message);
        
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
     * @throws ActionException
     */
    protected Message createMessage(Session session, A action) throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        SMTPMessage m = action.getMessage();
        message.setFrom(new InternetAddress(m.getFrom()));

        userPreferences.addContact(m.getTo());
        userPreferences.addContact(m.getCc());
        userPreferences.addContact(m.getBcc());

        message.setRecipients(RecipientType.TO, MessageUtils.getRecipients(m.getTo()));
        message.setRecipients(RecipientType.CC, MessageUtils.getRecipients(m.getCc()));
        message.setRecipients(RecipientType.BCC, MessageUtils.getRecipients(m.getBcc()));
        message.setSubject(MessageUtils.encodeTexts(m.getSubject()));
        message.saveChanges();
        return message;
    }
    /**
     * Fill the body of the given message with data which the given action contain
     * 
     * @param message the message
     * @param action the action
     * @return filledMessage
     * @throws MessagingException
     * @throws ActionException
     * @throws IOException 
     */
    protected Message fillBody(Message message, A action) throws MessagingException, ActionException, IOException {

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
     */
    @SuppressWarnings("rawtypes")
    protected List getAttachments(A action) throws MessagingException, ActionException {
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
    protected void resetAttachments(A action) throws MessagingException, ActionException {
        SMTPMessage msg = action.getMessage();
        ArrayList<MessageAttachment> attachments = msg.getMessageAttachments();
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
        
        Transport transport = cache.getMailTransport(useSSL);
    
        if (auth) {
            logger.debug("Use auth for smtp connection");
            transport.connect(address,port,user.getName(), user.getPassword());
        } else {
            transport.connect(address, port, null,null);
        }
        
        Address[] recips = message.getAllRecipients();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < recips.length; i++) {
            sb.append(recips[i]);
            if (i != recips.length -1) {
                sb.append(", ");
            }
        }
        logger.info("Send message from " + message.getFrom()[0].toString()+ " to " + sb.toString());
        transport.sendMessage(message, recips);
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
