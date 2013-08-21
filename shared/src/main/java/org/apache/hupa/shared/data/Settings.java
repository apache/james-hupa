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

import java.io.Serializable;

public class Settings implements Serializable{

    public static final String DEFAULT_INBOX = "INBOX"; 
    public static final String DEFAULT_TRASH = "Trash"; 
    public static final String DEFAULT_SENT = "Sent"; 
    public static final String DEFAULT_DRAFT = "Draft"; 
    
    /**
     * 
     */
    private static final long serialVersionUID = -8051377307421345664L;
    private String trashFolderName = DEFAULT_TRASH;
    private String sentFolderName = DEFAULT_SENT;
    private String inboxFolderName = DEFAULT_INBOX;
    private String draftsFolderName = DEFAULT_DRAFT;
    
    private int prefetchCount = 20;
    
    public String getInboxFolderName() {
        return inboxFolderName;
    }
    
    public void setInboxFolderName(String inboxFolderName) {
        this.inboxFolderName = inboxFolderName;
    }
    
    public String getTrashFolderName() {
        return trashFolderName;
    }
    
    public void setTrashFolderName(String trashFolderName) {
        this.trashFolderName = trashFolderName;
    }
    
    public String getSentFolderName() {
        return sentFolderName;
    }
    
    public void setSentFolderName(String sentFolderName) {
        this.sentFolderName = sentFolderName;
    }
    
    public int getPostFetchMessageCount() {
        return prefetchCount;
    }
    
    public void setPostFetchMessageCount(int prefetchCount) {
        this.prefetchCount  = prefetchCount;
    }
    
    public String getDraftsFolderName() {
        return draftsFolderName;
    }

    public void setDraftsFolderName(String draftFolderName) {
        this.draftsFolderName = draftFolderName;
    }

    
}
