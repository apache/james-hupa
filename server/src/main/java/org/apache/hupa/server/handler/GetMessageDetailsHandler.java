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
        return new GetMessageDetailsResult(exposeMessage(getUser(), action.getFolder(), action.getUid()));
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
        com.sun.mail.imap.IMAPFolder f = null;
        try {
            store = cache.get(user);

            f = (com.sun.mail.imap.IMAPFolder) store
                    .getFolder(folder.getFullName());

            if (f.isOpen() == false) {
                f.open(com.sun.mail.imap.IMAPFolder.READ_WRITE);
            }
            
            MimeMessage message = (MimeMessage) f.getMessageByUID(uid);

            MessageDetails mDetails = mimeToDetails(message);

            mDetails.setUid(uid);
            if (mDetails.isHTML()) {
            	mDetails.setText(changeHtmlLinks(mDetails.getText(), f.getFullName(), uid));
            }
            
            f.setFlags(new Message[] { message }, new Flags(Flag.SEEN), true);

            return mDetails;
        } catch (Exception e) {
            logger.error("Unable to expose msg for user " + user
                    + " in folder " + folder + " with uid " + uid, e);
            throw new ActionException("Unable to expose msg for user " + user
                    + " in folder " + folder + " with uid " + uid);

        } finally {
            if (f != null && f.isOpen()) {
                try {
                    f.close(false);
                } catch (MessagingException e) {
                    // ignore on close
                }
            }
        }
    }

	protected MessageDetails mimeToDetails(MimeMessage message)
            throws IOException, MessagingException,
            UnsupportedEncodingException {
	    MessageDetails mDetails = new MessageDetails();

	    
	    Object con = message.getContent();

	    StringBuffer sbPlain = new StringBuffer();
	    ArrayList<MessageAttachment> attachmentList = new ArrayList<MessageAttachment>();

	    boolean isHTML = handleParts(message, con, sbPlain, attachmentList);

	    mDetails.setText(sbPlain.toString());

	    mDetails.setIsHTML(isHTML);
	    mDetails.setMessageAttachments(attachmentList);

	    mDetails.setRawHeader(message.getAllHeaders().toString());
	    return mDetails;
    }
	
	// TODO: use constants for servlet paths to match DispatchServlet configuration (also in client side)
	protected String changeHtmlLinks(String html, String folderName, long uuid) {
		html = html.replaceAll("(img\\s+src=\")cid:([^\"]+\")", 
				"$1/hupa/downloadAttachmentServlet?folder_name=" 
				+ folderName + "&message_uuid=" + uuid + "&attachment_name=$2");
		return html;
	}

    /**
     * Handle the parts of the given message. The method will call itself recursively to handle all nested parts
     * @param message the MimeMessage 
     * @param con the current processing Content
     * @param sbPlain the StringBuffer to fill with text
     * @param attachmentList ArrayList with attachments
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws IOException
     */
    private boolean handleParts(MimeMessage message, Object con,
            StringBuffer sbPlain,
            ArrayList<MessageAttachment> attachmentList)
            throws UnsupportedEncodingException, MessagingException,
            IOException {
    	boolean isHTML = false;
        if (con instanceof String) {
            System.out.println("sc: " + message.getContentType());
            if (message.getContentType().startsWith("text/html")) {
                isHTML = true;
            } else {
                isHTML = false;
            }
            sbPlain.append((String) con);

        } else if (con instanceof Multipart) {

            Multipart mp = (Multipart) con;
            String multipartContentType = mp.getContentType().toLowerCase();
            System.out.println("mc: " + multipartContentType);
            
            String text = null;

            if (multipartContentType.startsWith("multipart/alternative")) {
                isHTML = handleMultiPartAlternative(mp, sbPlain);
            } else {
                for (int i = 0; i < mp.getCount(); i++) {
                    Part part = mp.getBodyPart(i);

                    String contentType = part.getContentType().toLowerCase();
                    System.out.println("c: " + contentType);

                    if (text == null && contentType.startsWith("text/plain") ) {
                        isHTML = false;
                        text = (String)part.getContent();
                    } else if (contentType.startsWith("text/html")) {
                        isHTML = true;
                        text = (String)part.getContent();
                    } else if (contentType.startsWith("message/rfc822")) {
                        // Extract the message and pass it
                        MimeMessage msg = (MimeMessage) part.getDataHandler().getContent();
                        isHTML =  handleParts(msg, msg.getContent(), sbPlain, attachmentList);
                    } else {
                        if (part.getFileName() != null) {
                            MessageAttachment attachment = new MessageAttachment();
                            attachment.setName(MimeUtility.decodeText(part
                                    .getFileName()));
                            attachment.setContentType(part.getContentType());
                            attachment.setSize(part.getSize());

                            attachmentList.add(attachment);
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
    
    private boolean handleMultiPartAlternative(Multipart mp, StringBuffer sbPlain) throws MessagingException, IOException {
        String text = null;
        boolean isHTML = false;
        for (int i = 0; i < mp.getCount(); i++) {
            Part part = mp.getBodyPart(i);
            
            String contentType = part.getContentType().toLowerCase();
            System.out.println("m: " + contentType);

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
    
}
