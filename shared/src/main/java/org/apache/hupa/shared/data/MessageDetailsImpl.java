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
import java.util.List;

import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.MailHeader;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;

public class MessageDetailsImpl implements MessageDetails{

    private String text;
    private List<MessageAttachment> messageAttachments;
    private long uid;
    private String raw;
    private List<MailHeader> mailHeaders = new ArrayList<MailHeader>();


    public List<MailHeader> getMailHeaders() {
        return mailHeaders;
    }


    public void setMailHeaders(List<MailHeader> mailHeaders) {
        this.mailHeaders = mailHeaders;
    }


    public String toString() {
        return "uid=" + String.valueOf(getUid()) +
        " text.length=" + (text != null ? text.length() : 0) +
        " raw.length=" + (raw != null ? raw.length() : 0) +
        " attachments=" + (messageAttachments != null ? messageAttachments.size() : 0) +
        " headers=" + mailHeaders.size();
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * Set a raw String representation of the header
     *
     * @param raw
     */
    public void setRawHeader(String raw) {
        this.raw = raw;
    }

    /**
     * Return a raw String representation of the header
     *
     * @return raw
     */
    public String getRawHeader() {
        return raw;
    }


    /**
     * Set the body text of the content
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Return the body text of the content
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * Set the attachments
     *
     * @param messageAttachments
     */
    public void setMessageAttachments(List<MessageAttachment> messageAttachments) {
        this.messageAttachments = messageAttachments;
    }

    /**
     *
     * @return the In-Reply-To header field.
     */
    public String getInReplyTo() {
        return getHeaderValue(SConsts.HEADER_IN_REPLY_TO);
    }

    public String getHeaderValue(String key) {
        for (MailHeader h : getMailHeaders()) {
           if (h.getName().equals(key)) {
               return h.getValue();
           }
        }
        return null;
    }

    /**
     *
     * @return the References header field.
     */
    public String getReferences() {
        return getHeaderValue(SConsts.HEADER_REFERENCES);
    }

    public String getMessageId() {
        return getHeaderValue(SConsts.HEADER_MESSAGE_ID);
    }

    /**
     * Return the attachments
     *
     * @return aList
     */
    public List<MessageAttachment> getMessageAttachments() {
        return messageAttachments;
    }


    public boolean equals(Object obj) {
        if (obj instanceof MessageDetailsImpl) {
            if (((MessageDetailsImpl)obj).getUid() == getUid()) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        Long l = Long.valueOf(getUid());
        return l.intValue() * 16;
    }
}
