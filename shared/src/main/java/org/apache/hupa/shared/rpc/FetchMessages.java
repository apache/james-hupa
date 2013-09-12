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

import org.apache.hupa.shared.proxy.IMAPFolderProxy;

public class FetchMessages implements Action<FetchMessagesResult>, Serializable {
    
    private static final long serialVersionUID = -3181183289937321202L;
    private IMAPFolderProxy folder;
    private int start;
    private int offset;
    private String searchString;

    protected FetchMessages() {
    }
    
    public FetchMessages(IMAPFolderProxy folder,int start, int offset,String searchString) {
        this.folder = folder;
        this.start = start;
        this.offset = offset;
        this.searchString = searchString;
    }
    
    public IMAPFolderProxy getFolder() {
        return folder;
    }
    
    public int getStart() {
        return start;
    }
    
    public int getOffset() {
        return offset;
    }

    public String getSearchString() {
        return searchString;
    }

}
