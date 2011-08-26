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

package org.apache.hupa.shared.rpc;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.hupa.shared.data.Message;
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

import net.customware.gwt.dispatch.shared.Result;

public class FetchMessagesResult implements Result, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8692400285949934424L;
    private ArrayList<Message> messages;
    private int start;
    private int offset;
    private int realCount;
    private int realUnreadCount;

    @SuppressWarnings("unused")
    private FetchMessagesResult() {
    }
    
    public FetchMessagesResult(ArrayList<Message> messages,int start,int offset,int realCount, int realUnreadCount) {
        this.messages = messages;
        this.start = start;
        this.offset = offset;
        this.realCount = realCount;
        this.realUnreadCount = realUnreadCount;
    }
    
    public ArrayList<Message> getMessages() {
        return messages;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public int getStart() {
        return start;
    }
    
    public int getRealCount() {
        return realCount;
    }
    
    public int getRealUnreadCount() {
        return realUnreadCount;
    }
}
