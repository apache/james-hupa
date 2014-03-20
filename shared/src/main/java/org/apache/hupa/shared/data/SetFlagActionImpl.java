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

import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.SetFlagAction;

public class SetFlagActionImpl implements SetFlagAction {

    private IMAPFlag flag;
    private List<Long> uids;
    private ImapFolder folder;
    private boolean value;

    public SetFlagActionImpl(ImapFolder folder, IMAPFlag flag, boolean value, List<Long> uids) {
        this.flag = flag;
        this.value = value;
        this.uids = uids;
        this.folder = folder;
    }

    protected SetFlagActionImpl() {
    }

    public ImapFolder getFolder() {
        return folder;
    }

    public boolean getValue() {
        return value;
    }
    public IMAPFlag getFlag() {
        return flag;
    }

    public List<Long> getUids() {
        return uids;
    }

    public void setFlag(IMAPFlag flag) {
        this.flag = flag;
    }

    public void setUids(List<Long> uids) {
        this.uids = uids;
    }

    public void setFolder(ImapFolder folder) {
        this.folder = folder;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
