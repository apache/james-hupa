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

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;

import com.google.gwt.event.shared.GwtEvent;

public class ExpandMessageEvent extends GwtEvent<ExpandMessageEventHandler>{

    public final static Type<ExpandMessageEventHandler> TYPE = new Type<ExpandMessageEventHandler>();
    private Message message;
    private User user;
    private ImapFolder folder;
    private MessageDetails messageDetails;

    public ExpandMessageEvent(User user, ImapFolder folder, Message message) {
        this.message = message;
        this.folder = folder;
        this.user = user;
    }

    public ExpandMessageEvent(User user, ImapFolder folder, Message message, MessageDetails messageDetails) {
        this.message = message;
        this.folder = folder;
        this.user = user;
        this.messageDetails = messageDetails;
    }

    public Message getMessage() {
        return message;
    }

    public User getUser () {
        return user;
    }

    public ImapFolder getFolder () {
        return folder;
    }

    public MessageDetails getMessageDetails(){
        return messageDetails;
    }


    @Override
    protected void dispatch(ExpandMessageEventHandler handler) {
        handler.onExpandMessage(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ExpandMessageEventHandler> getAssociatedType() {
        return TYPE;
    }



}
