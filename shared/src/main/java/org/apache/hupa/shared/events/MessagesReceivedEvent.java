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

import java.util.List;

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;

import com.google.gwt.event.shared.GwtEvent;

public class MessagesReceivedEvent extends GwtEvent<MessagesReceivedEventHandler>{
    public final static Type<MessagesReceivedEventHandler> TYPE = new Type<MessagesReceivedEventHandler>();
    private List<Message> messages;
    private ImapFolder folder;

    public MessagesReceivedEvent(ImapFolder folder, List<Message> messages) {
        this.messages = messages;
        this.folder = folder;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ImapFolder getFolder() {
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
