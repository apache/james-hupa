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

package org.apache.hupa.shared.data;

import java.util.List;

import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.FetchMessagesResult;

public class FetchMessagesResultImpl implements FetchMessagesResult {

    public FetchMessagesResultImpl() {
    }

    public FetchMessagesResultImpl(List<Message> messages, int start, int offset, int realCount, int realUnreadCount) {
        this.messages = messages;
        this.start = start;
        this.offset = offset;
        this.realCount = realCount;
        this.realUnreadCount = realUnreadCount;
    }

    private List<Message> messages;
    private int start;
    private int offset;
    private int realCount;
    private int realUnreadCount;

    public int getOffset() {
        return offset;
    }

    public int getStart() {
        return start;
    }

    public List<Message> getMessages() {
        return messages;
    }
    public int getRealCount() {
        return realCount;
    }

    public int getRealUnreadCount() {
        return realUnreadCount;
    }
}
