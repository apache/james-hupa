package org.apache.hupa.shared.rpc;

import java.util.ArrayList;

import org.apache.hupa.shared.data.IMAPFolder;

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

public class DeleteMessage extends Session<DeleteMessageResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 801294103124082592L;
	private IMAPFolder folder;
	private ArrayList<Long> messageUids;

	public DeleteMessage(String sessionId,IMAPFolder folder, ArrayList<Long> messageUids) {
		super(sessionId);
		this.folder = folder;
		this.messageUids = messageUids;
	}
	
	@SuppressWarnings("unused")
	private DeleteMessage() {
		
	}
	
	public IMAPFolder getFolder() {
		return folder;
	}
	
	public ArrayList<Long> getMessageUids() {
		return messageUids;
	}

}
