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

import org.apache.hupa.shared.domain.MailHeader;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.SmtpMessage;

public class SmtpMessageImpl extends AbstractMessageImpl implements SmtpMessage {
    private List<String> bcc;
    private String text;
    private List<MessageAttachment> messageAttachments;
    private List<MailHeader> mailHeaders = new ArrayList<MailHeader>();

    public String toString() {
        StringBuffer bccList = new StringBuffer("");
        if (bcc != null)
            for (String s : bcc)
                bccList.append(s).append(" ");

        StringBuffer attachNames = new StringBuffer("");
        for (MessageAttachment m : messageAttachments)
            attachNames.append(m.getName()).append(" ");

        return super.toString() + " Bcc='" + bccList.toString() + "'\nAttachments=" + attachNames.toString()
                + "'\nMessage:\n" + text;
    }

    public List<String> getBcc() {
        return bcc;
    }
    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
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
     *
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * Set the attachments
     *
     * @param aList
     */
    public void setMessageAttachments(List<MessageAttachment> aList) {
        this.messageAttachments = aList;
    }

    /**
     * Return the attachments
     *
     * @return aList
     */
    public List<MessageAttachment> getMessageAttachments() {
        return messageAttachments;
    }

    @Override
    public List<MailHeader> getMailHeaders() {
        return mailHeaders;
    }

    @Override
    public void setMailHeaders(List<MailHeader> mailHeaders) {
        this.mailHeaders = mailHeaders == null ? new ArrayList<MailHeader>(): mailHeaders;
    }

}
