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

<<<<<<< HEAD
<<<<<<< HEAD
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;
=======
import com.google.inject.Provider;

import com.sun.mail.imap.IMAPStore;
>>>>>>> first commit
=======
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;
>>>>>>> constantly changed by manolo

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.data.Tag;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.ImapFolder;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;
=======
=======
import org.apache.hupa.server.utils.MessageUtils;
>>>>>>> constantly changed by manolo
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.data.Tag;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

<<<<<<< HEAD
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;
>>>>>>> first commit
=======
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;
>>>>>>> constantly changed by manolo

public abstract class AbstractFetchMessagesHandler <A extends FetchMessages> extends AbstractSessionHandler<A, FetchMessagesResult>{

    UserPreferencesStorage userPreferences;
    
    public AbstractFetchMessagesHandler(IMAPStoreCache cache, Log logger, Provider<HttpSession> sessionProvider, UserPreferencesStorage preferences) {
        super(cache, logger, sessionProvider);
        this.userPreferences = preferences;
    }

    @Override
    protected FetchMessagesResult executeInternal(A action,
            ExecutionContext context) throws ActionException {
        User user = getUser();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        IMAPFolderProxy folder = action.getFolder();
        if (folder == null) {
            folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());
=======
        IMAPFolder folder = action.getFolder();
        if (folder == null) {
            folder = new IMAPFolder(user.getSettings().getInboxFolderName());
>>>>>>> first commit
=======
        IMAPFolderProxy folder = action.getFolder();
        if (folder == null) {
            folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
        ImapFolder folder = action.getFolder();
        if (folder == null) {
            folder = (ImapFolder)new ImapFolderImpl(user.getSettings().getInboxFolderName());
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        }
        com.sun.mail.imap.IMAPFolder f = null;
        int start = action.getStart();
        int offset = action.getOffset();
        try {
            IMAPStore store = cache.get(user);
            
            f =  (com.sun.mail.imap.IMAPFolder)store.getFolder(folder.getFullName());

             // check if the folder is open, if not open it read only
            if (f.isOpen() == false) {
                f.open(com.sun.mail.imap.IMAPFolder.READ_ONLY);
            }

            // if the folder is empty we have no need to process 
            int exists = f.getMessageCount();
            if (exists == 0) {
                 return new FetchMessagesResult(new ArrayList<org.apache.hupa.shared.data.Message>(), start, offset, 0, 0);
            }        
            
            MessageConvertArray convArray = getMessagesToConvert(f,action);
            return new FetchMessagesResult(convert(offset, f, convArray.getMesssages()),start, offset,convArray.getRealCount(),f.getUnreadMessageCount());
        } catch (MessagingException e) {
            logger.info("Error fetching messages in folder: " + folder.getFullName() + " " + e.getMessage());
            // Folder can not contain messages
            return new FetchMessagesResult(new ArrayList<org.apache.hupa.shared.data.Message>(), start, offset, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while fetching headers for user " + user.getName() + " in folder " + folder,e);
            throw new ActionException(
                    "Error while fetching headers for user " + user.getName() + " in folder " + folder);
        
        } finally {
            if (f != null && f.isOpen()) {
                try {
                    f.close(false);
                } catch (MessagingException e) {
                    // we don't care to much about an exception on close here...
                }
            }
        }
    }
    
    protected abstract MessageConvertArray getMessagesToConvert(com.sun.mail.imap.IMAPFolder f, A action) throws MessagingException, ActionException;
    
    protected ArrayList<org.apache.hupa.shared.data.Message> convert(int offset, com.sun.mail.imap.IMAPFolder folder, Message[] messages) throws MessagingException {
        ArrayList<org.apache.hupa.shared.data.Message> mList = new ArrayList<org.apache.hupa.shared.data.Message>();
        // Setup fetchprofile to limit the stuff which is fetched 
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(FetchProfile.Item.FLAGS);
        fp.add(FetchProfile.Item.CONTENT_INFO);
<<<<<<< HEAD
<<<<<<< HEAD
        fp.add(UIDFolder.FetchProfileItem.UID);
=======
>>>>>>> first commit
=======
        fp.add(UIDFolder.FetchProfileItem.UID);
>>>>>>> constantly changed by manolo
        folder.fetch(messages, fp);
        
        // loop over the fetched messages
        for (int i = 0; i < messages.length && i < offset; i++) {
            org.apache.hupa.shared.data.Message msg = new org.apache.hupa.shared.data.Message();
            Message m = messages[i];                
            String from = null;
            if (m.getFrom() != null && m.getFrom().length >0 ) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                from = MessageUtils.decodeText(m.getFrom()[0].toString());
=======
                from = m.getFrom()[0].toString().trim();
                try {
                    from = MimeUtility.decodeText(from);
                    userPreferences.addContact(from);
                } catch (UnsupportedEncodingException e) {
                    logger.debug("Unable to decode from " + from + " " + e.getMessage());
                }
>>>>>>> first commit
=======
                from = decodeText(m.getFrom()[0].toString());
>>>>>>> constant changed by manolo
=======
                from = MessageUtils.decodeText(m.getFrom()[0].toString());
>>>>>>> constantly changed by manolo
            }
            msg.setFrom(from);

            String replyto = null;
            if (m.getReplyTo() != null && m.getReplyTo().length >0 ) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                replyto = MessageUtils.decodeText(m.getReplyTo()[0].toString());
=======
                replyto = m.getReplyTo()[0].toString().trim();
                try {
                    replyto = MimeUtility.decodeText(replyto);
                    userPreferences.addContact(replyto);
                } catch (UnsupportedEncodingException e) {
                    logger.debug("Unable to decode replyto " + replyto + " " + e.getMessage());
                }
>>>>>>> first commit
=======
                replyto = decodeText(m.getReplyTo()[0].toString());
>>>>>>> constant changed by manolo
=======
                replyto = MessageUtils.decodeText(m.getReplyTo()[0].toString());
>>>>>>> constantly changed by manolo
            }
            msg.setReplyto(replyto);
            
            ArrayList<String> to = new ArrayList<String>();
            // Add to addresses
            Address[] toArray = m.getRecipients(RecipientType.TO);
            if (toArray != null) {
                for (Address addr : toArray) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                    String mailTo = MessageUtils.decodeText(addr.toString());
                    to.add(mailTo);
=======
                    String mailTo = null;
                    try {
                        mailTo = MimeUtility.decodeText(addr.toString());
                        userPreferences.addContact(mailTo);
                    } catch (UnsupportedEncodingException e) {
                        logger.debug("Unable to decode mailTo " + mailTo + " " + e.getMessage());
                    }
                    if (mailTo != null)
                        to.add(mailTo);
>>>>>>> first commit
=======
                    String mailTo = decodeText(addr.toString());
=======
                    String mailTo = MessageUtils.decodeText(addr.toString());
>>>>>>> constantly changed by manolo
                    to.add(mailTo);
>>>>>>> constant changed by manolo
                }
            }
            msg.setTo(to);
            
            // Check if a subject exist and if so decode it
            String subject = m.getSubject();
            if (subject != null) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                subject = MessageUtils.decodeText(subject);
=======
                try {
                    subject = MimeUtility.decodeText(subject);
                } catch (UnsupportedEncodingException e) {
                    logger.debug("Unable to decode subject " + subject + " " + e.getMessage());
                }
>>>>>>> first commit
=======
                subject = decodeText(subject);
>>>>>>> constant changed by manolo
=======
                subject = MessageUtils.decodeText(subject);
>>>>>>> constantly changed by manolo
            }
            msg.setSubject(subject);
            
            // Add cc addresses
            Address[] ccArray = m.getRecipients(RecipientType.CC);
            ArrayList<String> cc = new ArrayList<String>();
            if (ccArray != null) {
                for (Address addr : ccArray) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                    String mailCc = MessageUtils.decodeText(addr.toString());
                    cc.add(mailCc);
=======
                    String mailCc = null;
                    try {
                    	mailCc = MimeUtility.decodeText(addr.toString());
                        userPreferences.addContact(mailCc);
                    } catch (UnsupportedEncodingException e) {
                        logger.debug("Unable to decode mailTo " + mailCc + " " + e.getMessage());
                    }
                    if (mailCc != null)
                        cc.add(mailCc);
>>>>>>> first commit
=======
                    String mailCc = decodeText(addr.toString());
=======
                    String mailCc = MessageUtils.decodeText(addr.toString());
>>>>>>> constantly changed by manolo
                    cc.add(mailCc);
>>>>>>> constant changed by manolo
                }            	
            }
            msg.setCc(cc);

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> constant changed by manolo
            userPreferences.addContact(from);
            userPreferences.addContact(to);
            userPreferences.addContact(replyto);
            userPreferences.addContact(cc);

<<<<<<< HEAD
=======
>>>>>>> first commit
=======
>>>>>>> constant changed by manolo
            // Using sentDate since received date is not useful in the view when using fetchmail
            msg.setReceivedDate(m.getSentDate());

            // Add flags
            ArrayList<IMAPFlag> iFlags = JavamailUtil.convert(m.getFlags());
          
            ArrayList<Tag> tags = new ArrayList<Tag>();
            for (String flag : m.getFlags().getUserFlags()) {
                if (flag.startsWith(Tag.PREFIX)) {
                    tags.add(new Tag(flag.substring(Tag.PREFIX.length())));
                }
            }
            
            msg.setUid(folder.getUID(m));
            msg.setFlags(iFlags);
            msg.setTags(tags);
            try {
                msg.setHasAttachments(hasAttachment(m));
            } catch (MessagingException e) {
                logger.debug("Unable to identify attachments in message UID:" + msg.getUid() + " subject:" + msg.getSubject() + " cause:" + e.getMessage());
                logger.info("");
            }
            mList.add(0, msg);
            
        }
        return mList;
    }

    private boolean hasAttachment(Message message) throws MessagingException {
        if (message.getContentType().startsWith("multipart/")) {
            try {
                Object content;

                content = message.getContent();

                if (content instanceof Multipart) {
                    Multipart mp = (Multipart) content;
                    if (mp.getCount() > 1) {
                        for (int i = 0; i < mp.getCount(); i++) {
                            String disp = mp.getBodyPart(i).getDisposition();
                            if (disp != null
                                    && disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                                return true;
                            }
                        }
                    }

                }
            } catch (IOException e) {
                logger.error("Error while get content of message " + message.getMessageNumber());
            }
            
        }
        return false;
    }
    
    
    protected final class MessageConvertArray {
        private Message[] messages;
        private int realCount;

        public MessageConvertArray(int realCount, Message[] messages) {
            this.messages = messages;
            this.realCount = realCount;
        }
        
        public int getRealCount() {
            return realCount;
        }
        
        public Message[] getMesssages() {
            return messages;
        }
    }
<<<<<<< HEAD
<<<<<<< HEAD


=======
>>>>>>> first commit
=======

<<<<<<< HEAD
    /**
     * Decode iso-xxxx strings present in subjects and emails like:
     * 
     * =?ISO-8859-1?Q?No=20hay=20ma=F1ana?= <hello@hupa.org> 
     */
    private String decodeText(String s) {
    	String ret = s;
    	try {
    		ret = MimeUtility.decodeText(s);
        } catch (UnsupportedEncodingException e) {
            logger.debug("Unable to decode text " + s + " " + e.getMessage());
        }
        // Remove quotes around names in email addresses
        ret =  ret.replaceFirst("^[\"' ]+([^\"]*)[\"' ]+<", "$1 <");
        return ret;
    }
>>>>>>> constant changed by manolo
=======

>>>>>>> constantly changed by manolo
}
