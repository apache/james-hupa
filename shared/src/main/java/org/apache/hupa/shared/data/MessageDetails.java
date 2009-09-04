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
import java.util.ArrayList;

public class MessageDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7611536915564919521L;
	private boolean isHTML;
	private String text;
	private ArrayList<MessageAttachment> aList;
	private long uid;
	private String raw;

	
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
	 * Set if the count is plain/html
	 * 
	 * @param isHTML
	 */
	public void setIsHTML(boolean isHTML) {
		this.isHTML = isHTML;
	}

	/**
	 * Return if the content is plain/html or plain/text
	 * 
	 * @return isHTML
	 */
	public boolean isHTML() {
		return isHTML;
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
	 * @return
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


	public String toString() {
		return String.valueOf(getUid());
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof MessageDetails) {
			if (((MessageDetails)obj).getUid() == getUid()) {
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		Long l = new Long(getUid());
		return l.intValue() * 16;
	}
}
