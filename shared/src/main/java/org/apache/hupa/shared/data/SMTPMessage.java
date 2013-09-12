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

import org.apache.hupa.shared.domain.MessageAttachment;

public class SMTPMessage extends AbstractMessage{
    private static final long serialVersionUID = 7331361994526216161L;
    private ArrayList<String> bcc;
    private String text;
    private ArrayList<MessageAttachment> aList;
    
    public String toString() {
        StringBuffer bccList = new StringBuffer("");
        if (bcc !=null) 
            for (String s: bcc)
                bccList.append(s).append(" ");
        
        StringBuffer attachNames = new StringBuffer("");
        for (MessageAttachment m: aList) 
            attachNames.append(m.getName()).append(" ");
        
        return super.toString()
             + " Bcc='" + bccList.toString()
             + "'\nAttachments=" + attachNames.toString()
             + "'\nMessage:\n" + text;
    }
    
    public ArrayList<String> getBcc() {
        return bcc;
    }
    public void setBcc(ArrayList<String> bcc) {
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
    public void setMessageAttachments(ArrayList<MessageAttachment> aList) {
        this.aList = aList;
    }

    /**
     * Return the attachments 
     * 
     * @return aList
     */
    public ArrayList<MessageAttachment> getMessageAttachments() {
        return aList;
    }

}
