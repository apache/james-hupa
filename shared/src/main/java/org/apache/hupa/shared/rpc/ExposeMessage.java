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

import org.apache.hupa.shared.data.IMAPFolder;

public class ExposeMessage extends Session<ExposeMessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5826298202494313834L;
	private IMAPFolder folder;
	private long uid;

	public ExposeMessage(String sessionId, IMAPFolder folder, long uid) {
		super(sessionId);
		this.folder = folder;
		this.uid = uid;
	}

	@SuppressWarnings("unused")
	private ExposeMessage() {
	}
	
	public IMAPFolder getFolder() {
		return folder;
	}
	
	public long getUid() {
		return uid;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof ExposeMessage) {
			ExposeMessage action = (ExposeMessage) obj;
			if (action.getSessionId().equals(getSessionId()) && action.getFolder().equals(getFolder()) && action.getUid() == getUid()) {
				return true;
			}
		}
		return false;
		
	}
	
	public int hashCode() {
		return (int) (getSessionId().hashCode() * getFolder().hashCode() * getUid());
	}
}
