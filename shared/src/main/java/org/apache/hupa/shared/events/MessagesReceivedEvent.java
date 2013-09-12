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
import java.util.List;

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
=======
import java.util.ArrayList;

<<<<<<< HEAD
import org.apache.hupa.shared.data.Message;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> first commit
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import java.util.ArrayList;

import org.apache.hupa.shared.data.Message;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> first commit
=======
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
>>>>>>> try to change fetch messages to use RF

import com.google.gwt.event.shared.GwtEvent;

public class MessagesReceivedEvent extends GwtEvent<MessagesReceivedEventHandler>{
    public final static Type<MessagesReceivedEventHandler> TYPE = new Type<MessagesReceivedEventHandler>();
<<<<<<< HEAD
<<<<<<< HEAD
    private List<Message> messages;
    private ImapFolder folder;
    
    public MessagesReceivedEvent(ImapFolder folder, List<Message> messages) {
=======
    private ArrayList<Message> messages;
    private IMAPFolderProxy folder;
    
<<<<<<< HEAD
    public MessagesReceivedEvent(IMAPFolder folder, ArrayList<Message> messages) {
>>>>>>> first commit
=======
    public MessagesReceivedEvent(IMAPFolderProxy folder, ArrayList<Message> messages) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private ArrayList<Message> messages;
    private ImapFolder folder;
    
<<<<<<< HEAD
<<<<<<< HEAD
    public MessagesReceivedEvent(IMAPFolder folder, ArrayList<Message> messages) {
>>>>>>> first commit
=======
    public MessagesReceivedEvent(IMAPFolderProxy folder, ArrayList<Message> messages) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public MessagesReceivedEvent(ImapFolder folder, ArrayList<Message> messages) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        this.messages = messages;
        this.folder = folder;
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
    public List<Message> getMessages() {
        return messages;
    }
    
    public ImapFolder getFolder() {
=======
=======
>>>>>>> first commit
    public ArrayList<Message> getMessages() {
        return messages;
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public IMAPFolder getFolder() {
>>>>>>> first commit
=======
    public IMAPFolderProxy getFolder() {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
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
    
    
    @Override
    protected void dispatch(MessagesReceivedEventHandler handler) {
        handler.onMessagesReceived(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<MessagesReceivedEventHandler> getAssociatedType() {
        return TYPE;
    }

}
