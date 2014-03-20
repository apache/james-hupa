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

import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.ImapFolder;

public class DeleteMessageByUidActionImpl extends DeleteMessageActionImpl implements DeleteMessageByUidAction {

    public DeleteMessageByUidActionImpl(){}
    public DeleteMessageByUidActionImpl(ImapFolder folder, List<Long> messageUids){
        super(folder);
        this.messageUids = messageUids;
    }

    private List<Long> messageUids;

    @Override
    public List<Long> getMessageUids() {
        return messageUids;
    }

    @Override
    public void setMessageUids(List<Long> messageUids) {
        this.messageUids = messageUids;
    }

}
