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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.GetMessageDetails;
import org.apache.hupa.shared.rpc.GetMessageDetailsResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

public class GetMessageDetailsHandler extends
		AbstractSessionHandler<GetMessageDetails, GetMessageDetailsResult> {

	@Inject
	public GetMessageDetailsHandler(IMAPStoreCache cache, Log logger,
			Provider<HttpSession> sProvider) {
		super(cache, logger, sProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal
	 * (org.apache.hupa.shared.rpc.Session,
	 * net.customware.gwt.dispatch.server.ExecutionContext)
	 */
	public GetMessageDetailsResult executeInternal(GetMessageDetails action,
			ExecutionContext arg1) throws ActionException {
		return new GetMessageDetailsResult(exposeMessage(getUser(action
				.getSessionId()), action.getFolder(), action.getUid()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
	 */
	public Class<GetMessageDetails> getActionType() {
		return GetMessageDetails.class;
	}

	protected MessageDetails exposeMessage(User user, IMAPFolder folder,
			long uid) throws ActionException {
		IMAPStore store = null;
		try {
			store = cache.get(user);

			com.sun.mail.imap.IMAPFolder f = (com.sun.mail.imap.IMAPFolder) store
					.getFolder(folder.getFullName());

			if (f.isOpen() == false) {
				f.open(com.sun.mail.imap.IMAPFolder.READ_WRITE);
			}
			MimeMessage message = (MimeMessage) f.getMessageByUID(uid);
			MessageDetails mDetails = new MessageDetails();
			mDetails.setUid(uid);

			boolean isHTML = false;
			Object con = message.getContent();

			StringBuffer sbPlain = new StringBuffer();
			ArrayList<MessageAttachment> attachmentList = new ArrayList<MessageAttachment>();

			handleParts(message, con, sbPlain, isHTML, attachmentList);

			mDetails.setText(sbPlain.toString());

			mDetails.setIsHTML(isHTML);
			mDetails.setMessageAttachments(attachmentList);

			mDetails.setRawHeader(message.getAllHeaders().toString());

			f.setFlags(new Message[] { message }, new Flags(Flag.SEEN), true);
			f.close(false);

			return mDetails;
		} catch (Exception e) {
			logger.error("Unable to expose msg for user " + user
					+ " in folder " + folder + " with uid " + uid, e);
			throw new ActionException("Unable to expose msg for user " + user
					+ " in folder " + folder + " with uid " + uid);

		}
	}

	/**
	 * Handle the parts of the given message. The method will call itself recursively to handle all nested parts
	 * @param message the MimeMessage 
	 * @param con the current processing Content
	 * @param sbPlain the StringBuffer to fill with text
	 * @param isHTML identicate if the message is HTML
	 * @param attachmentList ArrayList with attachments
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void handleParts(MimeMessage message, Object con,
			StringBuffer sbPlain, boolean isHTML,
			ArrayList<MessageAttachment> attachmentList)
			throws UnsupportedEncodingException, MessagingException,
			IOException {
		if (con instanceof String) {
			if (message.getContentType().startsWith("text/html")) {
				isHTML = true;
			} else {
				isHTML = false;
			}
			sbPlain.append((String) con);

		} else if (con instanceof Multipart) {
			Multipart mp = (Multipart) con;

			for (int i = 0; i < mp.getCount(); i++) {
				Part part = mp.getBodyPart(i);

				String contentType = part.getContentType().toLowerCase();
				System.out.println("c: " + contentType);

				if (contentType.startsWith("text/plain")) {
					isHTML = false;
					sbPlain.append((String) part.getContent());
				} else if (contentType.startsWith("text/html")) {
					isHTML = true;
					sbPlain.append((String) part.getContent());

				} else if (contentType.startsWith("message/rfc822")) {
					// Extract the message and pass it
					MimeMessage msg = (MimeMessage) part.getDataHandler()
							.getContent();
					handleParts(msg, msg.getContent(), sbPlain, isHTML,
							attachmentList);

				} else {

					if (part.getFileName() != null) {
						MessageAttachment attachment = new MessageAttachment();
						attachment.setName(MimeUtility.decodeText(part
								.getFileName()));
						attachment.setContentType(part.getContentType());
						attachment.setSize(part.getSize());

						attachmentList.add(attachment);
					} else {
						handleParts(message, part, sbPlain, isHTML,
								attachmentList);
					}
				}

			}

		}
	}

}
