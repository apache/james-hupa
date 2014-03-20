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
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.SendForwardMessageAction;

public class SendForwardMessageActionImpl extends SendMessageActionImpl implements SendForwardMessageAction {

    private long uid;
    private ImapFolder folder;
    private String inReplyTo;
    private String references;

    public SendForwardMessageActionImpl() {
    }

    public SendForwardMessageActionImpl(SmtpMessage msg, ImapFolder folder, long uid) {
        super(msg);
        this.uid = uid;
        this.folder = folder;
    }

    @Override
    public void setFolder(ImapFolder folder) {
        this.folder = folder;
    }

    @Override
    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public long getUid() {
        return uid;
    }

    @Override
    public ImapFolder getFolder() {
        return folder;
    }

    @Override
    public String getInReplyTo() {
        return inReplyTo;
    }

    @Override
    public String getReferences() {
        return references;
    }

    @Override
    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    @Override
    public void setReferences(String references) {
        this.references = references;
    }
}
