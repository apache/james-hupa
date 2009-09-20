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

import java.util.ArrayList;

import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message.IMAPFlag;

public class SetFlag extends Session<EmptyResult>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 662741801793895357L;
	private ArrayList<IMAPFlag> flags;
	private ArrayList<Long> uids;
	private IMAPFolder folder;

	public SetFlag(String session, IMAPFolder folder, ArrayList<IMAPFlag> flags, ArrayList<Long> uids) {
		super(session);
		this.flags = flags;
		this.uids = uids;
		this.folder = folder;
	}
	
	protected SetFlag() {
		
	}
	
	public IMAPFolder getFolder() {
		return folder;
	}
	
	public ArrayList<IMAPFlag> getFlags() {
		return flags;
	}
	
	public ArrayList<Long> getUids() {
		return uids;
	}
}
