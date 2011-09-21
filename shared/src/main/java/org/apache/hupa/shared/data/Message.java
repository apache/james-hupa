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

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 *
 */
public class Message extends AbstractMessage {

    /**
     * 
     */
    private static final long serialVersionUID = -101492974974136423L;
    private long uid;
    private ArrayList<IMAPFlag> flags;
    private ArrayList<Tag> tags;
    private Date rDate;
    
    public enum IMAPFlag {
        SEEN, DELETED, RECENT, ANSWERED, JUNK, DRAFT, FLAGGED, USER
    }

    

    public void setFlags(ArrayList<IMAPFlag> flags) {
        this.flags = flags;
    }

    public ArrayList<IMAPFlag> getFlags() {
        return flags;
    }
    
    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
    
    public ArrayList<Tag> getTags() {
        return tags;
    }
    
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
    
    
    public void setReceivedDate(Date rDate) {
        this.rDate = rDate;
    }

    public Date getReceivedDate() {
        return rDate == null ? new Date(): rDate;
    }
    

    public String toString() {
        return String.valueOf(getUid());
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            if (((Message)obj).getUid() == getUid()) {
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        return Long.valueOf(getUid()).hashCode();
    }
}
