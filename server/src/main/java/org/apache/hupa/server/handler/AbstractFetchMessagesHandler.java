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

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.data.Tag;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

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
        IMAPFolder folder = action.getFolder();
        if (folder == null) {
            folder = new IMAPFolder(user.getSettings().getInboxFolderName());
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
        fp.add(UIDFolder.FetchProfileItem.UID);
        folder.fetch(messages, fp);
        
        // loop over the fetched messages
        for (int i = 0; i < messages.length && i < offset; i++) {
            org.apache.hupa.shared.data.Message msg = new org.apache.hupa.shared.data.Message();
            Message m = messages[i];                
            String from = null;
            if (m.getFrom() != null && m.getFrom().length >0 ) {
                from = MessageUtils.decodeText(m.getFrom()[0].toString());
            }
            msg.setFrom(from);

            String replyto = null;
            if (m.getReplyTo() != null && m.getReplyTo().length >0 ) {
                replyto = MessageUtils.decodeText(m.getReplyTo()[0].toString());
            }
            msg.setReplyto(replyto);
            
            ArrayList<String> to = new ArrayList<String>();
            // Add to addresses
            Address[] toArray = m.getRecipients(RecipientType.TO);
            if (toArray != null) {
                for (Address addr : toArray) {
                    String mailTo = MessageUtils.decodeText(addr.toString());
                    to.add(mailTo);
                }
            }
            msg.setTo(to);
            
            // Check if a subject exist and if so decode it
            String subject = m.getSubject();
            if (subject != null) {
                subject = MessageUtils.decodeText(subject);
            }
            msg.setSubject(subject);
            
            // Add cc addresses
            Address[] ccArray = m.getRecipients(RecipientType.CC);
            ArrayList<String> cc = new ArrayList<String>();
            if (ccArray != null) {
                for (Address addr : ccArray) {
                    String mailCc = MessageUtils.decodeText(addr.toString());
                    cc.add(mailCc);
                }            	
            }
            msg.setCc(cc);

            userPreferences.addContact(from);
            userPreferences.addContact(to);
            userPreferences.addContact(replyto);
            userPreferences.addContact(cc);

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


}
