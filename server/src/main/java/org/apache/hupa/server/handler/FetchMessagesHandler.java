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
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.BodyTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;

import org.apache.hupa.shared.data.Tag;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Fetch Messages for a user. The Messages don't contain any body, just some fields of the headers are fetched for perfomance reasons
 * 
 */
public class FetchMessagesHandler extends AbstractSessionHandler<FetchMessages, FetchMessagesResult>{

	@Inject
	public FetchMessagesHandler(IMAPStoreCache cache, Log logger,Provider<HttpSession> provider) {
		super(cache,logger,provider);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
	 */
	public FetchMessagesResult executeInternal(FetchMessages action, ExecutionContext arg1)
			throws ActionException {
		User user = getUser(action.getSessionId());
		IMAPFolder folder = action.getFolder();
		String searchString = action.getSearchString();
		ArrayList<org.apache.hupa.shared.data.Message> mList = new ArrayList<org.apache.hupa.shared.data.Message>();
		int start = action.getStart();
		int offset = action.getOffset();
		int end = start + offset;
		try {
			IMAPStore store = cache.get(user);
			com.sun.mail.imap.IMAPFolder f =  (com.sun.mail.imap.IMAPFolder)store.getFolder(folder.getFullName());

			// check if the folder is open, if not open it read only
			 if (f.isOpen() == false) {
                 f.open(com.sun.mail.imap.IMAPFolder.READ_ONLY);
             }

			int exists = f.getMessageCount();

			// if the folder is empty we have no need to process 
			if (exists == 0) {
				return new FetchMessagesResult(mList,start,offset,exists);
			}

			if (end > exists) {
				end =  exists;
			}
			
			int firstIndex = exists - end;
			if (firstIndex < 1) {
				firstIndex = 1;
			}
			int lastIndex = exists - start;
			Message[] messages;
			
			// check if a searchString was given, and if so use it
			if (searchString == null) {
				messages = f.getMessages(firstIndex,lastIndex);
			} else {
				SearchTerm subjectTerm = new SubjectTerm(searchString);
				SearchTerm fromTerm = new FromStringTerm(searchString);
				SearchTerm bodyTerm = new BodyTerm(searchString);
				SearchTerm orTerm = new OrTerm(new SearchTerm[]{subjectTerm,fromTerm,bodyTerm});
				messages = f.search(orTerm);
				if (end > messages.length) {
					end =  messages.length;
				}
				exists = messages.length;
			}
			
			// Setup fetchprofile to limit the stuff which is fetched 
  		    FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.FLAGS);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            f.fetch(messages, fp);

            // loop over the fetched messages
			for (int i = 0; i < messages.length; i++) {
				org.apache.hupa.shared.data.Message msg = new org.apache.hupa.shared.data.Message();
				Message m = messages[i];				
				String from = null;
				if (m.getFrom() != null && m.getFrom().length >0 ) {
					from = MimeUtility.decodeText(m.getFrom()[0].toString().trim());
				}
				msg.setFrom(from);
				
				ArrayList<String> to = new ArrayList<String>();
				// Add to addresses
				Address[] toArray = m.getRecipients(RecipientType.TO);
				if (toArray != null) {
					for (int b =0; b < toArray.length;b++) {
						to.add(toArray[b].toString());
					}
				}
				msg.setTo(to);
				
				// Check if a subject exist and if so decode it
				String subject = m.getSubject();
				if (subject != null) {
					subject = MimeUtility.decodeText(subject);
				}
				msg.setSubject(subject);
				
				// Add cc addresses
				Address[] ccArray = m.getRecipients(RecipientType.CC);
				ArrayList<String> cc = new ArrayList<String>();

				if (ccArray != null) {
					for (int b =0; b < ccArray.length;b++) {
						cc.add(ccArray[b].toString());
					}
				}
				msg.setCc(cc);
	
				msg.setReceivedDate(m.getReceivedDate());

				// Add flags
				ArrayList<IMAPFlag> iFlags = new ArrayList<IMAPFlag>();
				Flags flags = m.getFlags();
				Flag[] systemFlags = flags.getSystemFlags();
				for (int a = 0; a < systemFlags.length;a++) {
					Flag flag = systemFlags[a];
					if (flag == Flag.DELETED) {
						iFlags.add(IMAPFlag.DELETED);
					}
					if (flag == Flag.SEEN) {
						iFlags.add(IMAPFlag.SEEN);
					}
					if (flag == Flag.RECENT) {
						iFlags.add(IMAPFlag.RECENT);

					}
				}
			  
				ArrayList<Tag> tags = new ArrayList<Tag>();
				String[] userFlags = flags.getUserFlags();
				for (int a = 0; a < userFlags.length;a++) {
					String flag = userFlags[a];
					if (flag.startsWith(Tag.PREFIX)) {
						tags.add(new Tag(flag.substring(Tag.PREFIX.length())));
					}
				}
				
				msg.setUid(f.getUID(m));
				msg.setFlags(iFlags);
				msg.setTags(tags);
				msg.setHasAttachments(hasAttachment(m));
				
				mList.add(0, msg);
				if (i > action.getOffset()) {
					break;
				}
			}
			f.close(false);

			// return result
			return new FetchMessagesResult(mList,start,offset,exists);
		} catch (Exception e) {
			logger.error("Error while fetching headers for user " + user.getName() + " in folder " + folder,e);
			throw new ActionException(
					"Error while fetching headers for user " + user.getName() + " in folder " + folder);
		}
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

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
	 */
	public Class<FetchMessages> getActionType() {
		return FetchMessages.class;
	}
}
