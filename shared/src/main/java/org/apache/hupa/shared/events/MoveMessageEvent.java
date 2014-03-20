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
import org.apache.hupa.shared.domain.User;

import com.google.gwt.event.shared.GwtEvent;

public class MoveMessageEvent extends GwtEvent<MoveMessageEventHandler> {

    public final static Type<MoveMessageEventHandler> TYPE = new Type<MoveMessageEventHandler>();
    private User user;
    private ImapFolder oldFolder;
    private ImapFolder newFolder;
    private Message message;

    public MoveMessageEvent(User user, ImapFolder oldFolder,
            ImapFolder newFolder, Message message) {
        this.user = user;
        this.oldFolder = oldFolder;
        this.newFolder = newFolder;
        this.message = message;
    }

    public MoveMessageEvent(ImapFolder newFolder) {
        this.newFolder = newFolder;
    }

    public User getUser() {
        return user;
    }

    public ImapFolder getOldFolder() {
        return oldFolder;
    }

    public ImapFolder getNewFolder() {
        return newFolder;
    }

    public Message getMessage() {
        return message;
    }

    protected void dispatch(MoveMessageEventHandler handler) {
        handler.onMoveMessageHandler(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<MoveMessageEventHandler> getAssociatedType() {
        return TYPE;
    }

}
