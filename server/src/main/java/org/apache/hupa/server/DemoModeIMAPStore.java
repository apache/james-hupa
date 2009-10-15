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

package org.apache.hupa.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;


// TODO: implement full set of methods
public class DemoModeIMAPStore extends IMAPStore {
	
	public static final String DEMO_MODE_DEFAULT_FOLDER = "/";
	public static final String DEMO_MODE_INBOX_FOLDER = "Demo-Inbox";
	public static final String DEMO_MODE_SENT_FOLDER = "Demo-Sent";
	public static final String DEMO_MODE_TRASH_FOLDER = "Demo-Trash";
	
	
	static final URLName demoUrl = new URLName(null, InMemoryIMAPStoreCache.DEMO_MODE, 143, null, null, null);
	static final HashMap<String, Folder> folders = new HashMap<String, Folder>();

	public DemoModeIMAPStore(Session session) {
		super(session, demoUrl);
	}

	@Override
	public boolean isConnected() {
		return true;
	}
	
	@Override
	public void idle() throws MessagingException {
	}

	@Override
	public Folder getFolder(String name) throws MessagingException {
		Folder ret = folders.get(name) != null ? folders.get(name) : new DemoModeIMAPFolder(name, this) {};
		folders.put(name, ret);
		return ret;
	}

	@Override
	public Folder getDefaultFolder() throws MessagingException {
		return getFolder(DEMO_MODE_DEFAULT_FOLDER);
	}

	private class DemoModeIMAPFolder extends IMAPFolder {
		String fullName = null;
		IMAPStore store = null;
		Vector<Message> messages = new Vector<Message>();

		DemoModeIMAPFolder(String fullName, IMAPStore store) {
			super(fullName, '/', store);
			this.fullName = fullName;
			this.store = store;
		}

		@Override
		public void open(int mode) throws MessagingException {
		}

		@Override
		public int getMessageCount() throws MessagingException {
			return messages.size();
		}

		@Override
		public int getUnreadMessageCount() throws MessagingException {
			return 0;
		}

		@Override
		public boolean delete(boolean recurse) throws MessagingException {
			return true;
		}

		@Override
		public Folder[] list(String pattern) throws MessagingException {
			if (DEMO_MODE_DEFAULT_FOLDER.equals(fullName)) { 
				return new Folder[] { 
					store.getFolder(DEMO_MODE_INBOX_FOLDER), 
					store.getFolder(DEMO_MODE_SENT_FOLDER), 
					store.getFolder(DEMO_MODE_TRASH_FOLDER) 
					};
			}
			return new Folder[0];
		}
		
		@Override
		public boolean create(int type) throws MessagingException {
			return true;
		}
		
		@Override
	    public boolean exists() throws MessagingException {
	    	return true;
	    }
		
		@Override
		public void appendMessages(Message[] msgs) {
			messages.addAll(Arrays.asList(msgs));
		}
		
		@Override
		public void close(boolean expunge) throws MessagingException {
		}
		
	}

}
