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

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.MoveMessageAction;

public class MoveMessageActionImpl implements MoveMessageAction {

    private ImapFolder oldFolder;
    private ImapFolder newFolder;
    private long messageUid;

    public MoveMessageActionImpl(ImapFolder oldFolder, ImapFolder newFolder, long messageUid) {
        this.oldFolder = oldFolder;
        this.newFolder = newFolder;
        this.messageUid = messageUid;
    }

    protected MoveMessageActionImpl() {
    }

    public long getMessageUid() {
        return messageUid;
    }

    public ImapFolder getOldFolder() {
        return oldFolder;
    }

    public ImapFolder getNewFolder() {
        return newFolder;
    }

    public void setMessageUid(long messageUid){
        this.messageUid = messageUid;
    }

    public void setOldFolder(ImapFolder oldFolder) {
        this.oldFolder = oldFolder;
    }

    public void setNewFolder(ImapFolder newFolder) {
        this.newFolder = newFolder;
    }
}
