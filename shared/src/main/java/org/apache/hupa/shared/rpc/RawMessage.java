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

import net.customware.gwt.dispatch.shared.Action;

import org.apache.hupa.shared.data.ImapFolderImpl;

public class RawMessage implements Action<RawMessageResult>, Serializable {

    private static final long serialVersionUID = 5826298202494313834L;
    private ImapFolderImpl folder;
    private long uid;

    public RawMessage(ImapFolderImpl folder, long uid) {
        this.folder = folder;
        this.uid = uid;
    }

    @SuppressWarnings("unused")
    private RawMessage() {
    }
    
    public ImapFolderImpl getFolder() {
        return folder;
    }
    
    public long getUid() {
        return uid;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof RawMessage) {
            RawMessage action = (RawMessage) obj;
            if (action.getFolder().equals(getFolder()) && action.getUid() == getUid()) {
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        return (int) (getFolder().hashCode() * getUid());
    }
    
}
