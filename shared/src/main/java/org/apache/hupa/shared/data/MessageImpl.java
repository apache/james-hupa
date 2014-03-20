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

import java.util.Date;
import java.util.List;

import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.Tag;

/**
 *
 *
 */
public class MessageImpl extends AbstractMessageImpl implements Message{

    private List<String> extra;
    private long uid;
    private List<IMAPFlag> flags;
    private List<Tag> tags;
    private Date rDate;

    public enum IMAPFlag {
        SEEN, DELETED, RECENT, ANSWERED, JUNK, DRAFT, FLAGGED, USER
    }

    public void setFlags(List<IMAPFlag> flags) {
        this.flags = flags;
    }

    public List<IMAPFlag> getFlags() {
        return flags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Tag> getTags() {
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
        if (obj instanceof MessageImpl) {
            if (((MessageImpl)obj).getUid() == getUid()) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Long.valueOf(getUid()).hashCode();
    }

    @Override
    public void setExtra(List<String> extra) {
        this.extra = extra;

    }

    @Override
    public List<String> getExtra() {
        return extra;
    }
}
