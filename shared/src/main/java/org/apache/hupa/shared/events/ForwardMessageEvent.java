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

public class ForwardMessageEvent extends GwtEvent<ForwardMessageEventHandler> {

    public final static Type<ForwardMessageEventHandler> TYPE = new Type<ForwardMessageEventHandler>();
    private User user;
    private ImapFolder folder;
    private Message message;
    private MessageDetails details;

    public ForwardMessageEvent(User user, ImapFolder folder, Message message, MessageDetails details) {
        this.user = user;
        this.folder = folder;
        this.message = message;
        this.details = details;
    }

    public User getUser() {
        return user;
    }

    public ImapFolder getFolder() {
        return folder;
    }

    public Message getMessage() {
        return message;
    }


    public MessageDetails getMessageDetails() {
        return details;
    }

    @Override
    protected void dispatch(ForwardMessageEventHandler handler) {
        handler.onForwardMessageEvent(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ForwardMessageEventHandler> getAssociatedType() {
        return TYPE;
    }
}
