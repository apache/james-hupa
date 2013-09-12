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

package org.apache.hupa.shared.events;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;
=======
=======
>>>>>>> first commit
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.Message;
=======
>>>>>>> try to change fetch messages to use RF
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
<<<<<<< HEAD
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
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
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

import com.google.gwt.event.shared.GwtEvent;

public class ReplyMessageEvent extends GwtEvent<ReplyMessageEventHandler>{

    public final static Type<ReplyMessageEventHandler> TYPE = new Type<ReplyMessageEventHandler>();
    private User user;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private IMAPFolderProxy folder;
=======
    private IMAPFolder folder;
>>>>>>> first commit
=======
    private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private ImapFolder folder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
    private Message message;
    private MessageDetails details;
    private boolean replyAll;

    @Override
    protected void dispatch(ReplyMessageEventHandler handler) {
        handler.onReplyMessageEvent(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ReplyMessageEventHandler> getAssociatedType() {
        return TYPE;
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public ReplyMessageEvent(User user, IMAPFolderProxy folder, Message message, MessageDetails details, boolean replyAll) {
=======
    public ReplyMessageEvent(User user, IMAPFolder folder, Message message, MessageDetails details, boolean replyAll) {
>>>>>>> first commit
=======
    public ReplyMessageEvent(User user, IMAPFolderProxy folder, Message message, MessageDetails details, boolean replyAll) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public ReplyMessageEvent(User user, ImapFolder folder, Message message, MessageDetails details, boolean replyAll) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        this.user = user;
        this.folder = folder;
        this.message = message;
        this.replyAll = replyAll;
        this.details = details;
    }
    
    public User getUser() {
        return user;
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public IMAPFolderProxy getFolder() {
=======
    public IMAPFolder getFolder() {
>>>>>>> first commit
=======
    public IMAPFolderProxy getFolder() {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public ImapFolder getFolder() {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        return folder;
    }
    
    public Message getMessage() {
        return message;
    }
    

    public MessageDetails getMessageDetails() {
        return details;
    }

    public boolean getReplyAll() {
        return replyAll;
    }

}
