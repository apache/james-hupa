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

import net.customware.gwt.dispatch.shared.Action;

import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Tag;

public class TagMessage implements Action<GenericResult>, Serializable {

    private static final long serialVersionUID = 4323236257115412763L;
    private IMAPFolder folder;
    private ArrayList<Long> messageUids;
    private Tag tag;

    protected TagMessage() {
    }
    
    public TagMessage(Tag tag, IMAPFolder folder, ArrayList<Long> messageUids) {
        this.tag = tag;
        this.folder = folder;
        this.messageUids = messageUids;
    }
    
    public Tag getTag() {
        return tag;
    }
    
    public IMAPFolder getFolder() {
        return folder;
    }
    
    public ArrayList<Long> getMessageUids() {
        return messageUids;
    }

}
