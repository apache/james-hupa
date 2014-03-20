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

import org.apache.hupa.shared.domain.Message;

import com.google.gwt.event.shared.GwtEvent;

public class MessageListRangeChangedEvent extends GwtEvent<MessageListRangeChangedEventHandler>{
    public final static Type<MessageListRangeChangedEventHandler> TYPE = new Type<MessageListRangeChangedEventHandler>();

    public Message message;


    public int start = 0, size = 0;
    public String search;

    public MessageListRangeChangedEvent(int start, int size, String search) {
        this.start = start;
        this.size = size;
        this.search = search;
    }

    @Override
    protected void dispatch(MessageListRangeChangedEventHandler handler) {
        handler.onRangeChangedEvent(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<MessageListRangeChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

}
