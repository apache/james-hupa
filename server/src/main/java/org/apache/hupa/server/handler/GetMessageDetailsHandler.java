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
=======
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import static org.apache.hupa.server.utils.RegexPatterns.regex_badAttrs;
import static org.apache.hupa.server.utils.RegexPatterns.regex_badTags;
import static org.apache.hupa.server.utils.RegexPatterns.regex_email;
import static org.apache.hupa.server.utils.RegexPatterns.regex_existingEmailLinks;
import static org.apache.hupa.server.utils.RegexPatterns.regex_existingHttpLinks;
import static org.apache.hupa.server.utils.RegexPatterns.regex_gt;
import static org.apache.hupa.server.utils.RegexPatterns.regex_htmllink;
import static org.apache.hupa.server.utils.RegexPatterns.regex_inlineImg;
import static org.apache.hupa.server.utils.RegexPatterns.regex_lt;
import static org.apache.hupa.server.utils.RegexPatterns.regex_nl;
import static org.apache.hupa.server.utils.RegexPatterns.regex_unneededTags;
import static org.apache.hupa.server.utils.RegexPatterns.repl_badAttrs;
import static org.apache.hupa.server.utils.RegexPatterns.repl_badTags;
import static org.apache.hupa.server.utils.RegexPatterns.repl_email;
import static org.apache.hupa.server.utils.RegexPatterns.repl_existingHttpLinks;
import static org.apache.hupa.server.utils.RegexPatterns.repl_existngEmailLinks;
import static org.apache.hupa.server.utils.RegexPatterns.repl_gt;
import static org.apache.hupa.server.utils.RegexPatterns.repl_htmllink;
import static org.apache.hupa.server.utils.RegexPatterns.repl_inlineImg;
import static org.apache.hupa.server.utils.RegexPatterns.repl_lt;
import static org.apache.hupa.server.utils.RegexPatterns.repl_nl;
import static org.apache.hupa.server.utils.RegexPatterns.repl_unneededTags;
import static org.apache.hupa.server.utils.RegexPatterns.replaceAll;
import static org.apache.hupa.server.utils.RegexPatterns.replaceAllRecursive;

<<<<<<< HEAD
=======
>>>>>>> first commit
=======
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.mail.Flags;
<<<<<<< HEAD
<<<<<<< HEAD
import javax.mail.Flags.Flag;
<<<<<<< HEAD
=======
>>>>>>> first commit
=======
import javax.mail.Flags.Flag;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import javax.mail.Header;
>>>>>>> Fixes HUPA-96 : pass reference ids when replying. Patch by Zsombor Gegesy
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import javax.mail.Flags.Flag;
>>>>>>> first commit
=======
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
import static org.apache.hupa.server.utils.RegexPatterns.*;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
<<<<<<< HEAD
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.
=======
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    protected MessageDetails exposeMessage(User user, IMAPFolderProxy folder,
=======
    protected MessageDetails exposeMessage(User user, IMAPFolder folder,
>>>>>>> first commit
=======
    protected MessageDetails exposeMessage(User user, IMAPFolderProxy folder,
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    protected MessageDetails exposeMessage(User user, ImapFolder folder,
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
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

            MessageDetails mDetails = mimeToDetails(message, f.getFullName(), uid);

            mDetails.setUid(uid);
            
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

    protected MessageDetails mimeToDetails(MimeMessage message, String folderName, long uid)
            throws IOException, MessagingException,
            UnsupportedEncodingException {
        MessageDetails mDetails = new MessageDetails();

        
        Object con = message.getContent();

        StringBuffer sbPlain = new StringBuffer();
        ArrayList<MessageAttachment> attachmentList = new ArrayList<MessageAttachment>();
        
        boolean isHTML = handleParts(message, con, sbPlain, attachmentList);
        
        System.out.println(isHTML);
        
        if (isHTML) {
            mDetails.setText(filterHtmlDocument(sbPlain.toString(), folderName, uid));
        } else {
            mDetails.setText(txtDocumentToHtml(sbPlain.toString(), folderName, uid));
        }

        mDetails.setMessageAttachments(attachmentList);

        for (@SuppressWarnings("unchecked")
        Enumeration<Header> en = message.getAllHeaders(); en.hasMoreElements();) {
            Header header = en.nextElement();
            mDetails.addHeader(header.getName(), header.getValue());
        }
        
        return mDetails;
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
    protected boolean handleParts(MimeMessage message, Object con,
            StringBuffer sbPlain,
            ArrayList<MessageAttachment> attachmentList)
            throws UnsupportedEncodingException, MessagingException,
            IOException {
        boolean isHTML = false;
        if (con instanceof String) {
<<<<<<< HEAD
<<<<<<< HEAD
            if (message.getContentType().toLowerCase().startsWith("text/html")) {
=======
            if (message.getContentType().startsWith("text/html")) {
>>>>>>> first commit
=======
            if (message.getContentType().toLowerCase().startsWith("text/html")) {
>>>>>>> constantly changed by manolo
                isHTML = true;
            } else {
                isHTML = false;
            }
            sbPlain.append((String) con);

        } else if (con instanceof Multipart) {

            Multipart mp = (Multipart) con;
            String multipartContentType = mp.getContentType().toLowerCase();
            
            String text = null;

            if (multipartContentType.startsWith("multipart/alternative")) {
                isHTML = handleMultiPartAlternative(mp, sbPlain);
            } else {
                for (int i = 0; i < mp.getCount(); i++) {
                    Part part = mp.getBodyPart(i);

                    String contentType = part.getContentType().toLowerCase();
                    
                    Boolean bodyRead = sbPlain.length() > 0;

                    if (!bodyRead && contentType.startsWith("text/plain") ) {
                        isHTML = false;
                        text = (String)part.getContent();
                    } else if (!bodyRead && contentType.startsWith("text/html")) {
                        isHTML = true;
                        text = (String)part.getContent();
                    } else if (!bodyRead && contentType.startsWith("message/rfc822")) {
                        // Extract the message and pass it
                        MimeMessage msg = (MimeMessage) part.getDataHandler().getContent();
                        isHTML =  handleParts(msg, msg.getContent(), sbPlain, attachmentList);
                    } else {
                        if (part.getFileName() != null) {
                            // Inline images are not added to the attachment list
                            // TODO: improve the in-line images detection 
                            if (part.getHeader("Content-ID") == null) {
                                MessageAttachment attachment = new MessageAttachment();
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
    
    private boolean handleMultiPartAlternative(Multipart mp, StringBuffer sbPlain) throws MessagingException, IOException {
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
    
    protected String txtDocumentToHtml(String txt, String folderName, long uuid) {
        
        if (txt == null || txt.length()==0)
            return txt;

        // escape html tags symbols 
        txt = replaceAll(txt, regex_lt, repl_lt);
        txt = replaceAll(txt, regex_gt, repl_gt);
        
        // enclose between <a> http links and emails
        txt = replaceAll(txt, regex_htmllink, repl_htmllink);
        txt = replaceAll(txt, regex_email, repl_email);
        
        // put break lines
        txt = replaceAll(txt, regex_nl, repl_nl);
        
        txt = filterHtmlDocument(txt, folderName, uuid);

        return txt;
    }
    
    protected String filterHtmlDocument(String html, String folderName, long uuid) {
        
        if (html == null || html.length()==0)
            return html;

        // Replace in-line images links to use Hupa's download servlet
        html = replaceAll(html, regex_inlineImg, repl_inlineImg).replaceAll("%%FOLDER%%",folderName).replaceAll("%%UID%%", String.valueOf(uuid));
        // Remove head, script and style tags to avoid interferences with Hupa
        html = replaceAll(html, regex_badTags, repl_badTags);
        // Remove body and html tags
        html = replaceAll(html, regex_unneededTags, repl_unneededTags);
        // Remove all onClick attributes 
        html = replaceAllRecursive(html, regex_badAttrs, repl_badAttrs);
        html = replaceAll(html, regex_existingHttpLinks, repl_existingHttpLinks);

        //FIXME: These have serious performance problems (see testMessageDetails_Base64Image_Performance)
        // Add <a> tags to links which are not already into <a>
        // html = replaceAll(html, regex_orphandHttpLinks, repl_orphandHttpLinks);
        // Add javascript method to <a> in order to open links in a different window
        // Add <a> tags to emails which are not already into <a>
        // html = replaceAll(html, regex_orphandEmailLinks, repl_orphandEmailLinks);
        // Add a js method to mailto links in order to compose new mails inside hupa
        html = replaceAll(html, regex_existingEmailLinks, repl_existngEmailLinks);
        
        return html;
    }
    
}
