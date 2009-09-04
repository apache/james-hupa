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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.apache.commons.logging.Log;
import com.google.inject.name.Named;
import org.apache.hupa.shared.data.User;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sun.mail.imap.IMAPStore;

@Singleton
public class InMemoryIMAPStoreCache implements IMAPStoreCache{

	private Properties props = new Properties();
	private Session session;
	protected Log logger;
	private final Map<String,IMAPStore> pool = new HashMap<String ,IMAPStore>();
	private String address;
	private int port;
	private boolean useSSL = false;
	
	@Inject
	public InMemoryIMAPStoreCache(Log logger,@Named("IMAPServerAddress") String address, @Named("IMAPServerPort") int port, @Named("IMAPS") boolean useSSL,Provider<Session> sessionProvider) {
		this.logger = logger;
		this.address = address;
		this.port = port;
		this.useSSL = useSSL;
		
        props.setProperty("mail.mime.decodetext.strict", "false");
        if (useSSL) {
    		props.setProperty("mail.store.protocol", "imaps");
        }
        session = sessionProvider.get();
        System.setProperty("mail.mime.decodetext.strict", "false");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.hupa.server.IMAPStoreCache#get(org.apache.hupa.shared.data.User)
	 */
	public synchronized IMAPStore get(User user) throws MessagingException {
		return get(user.getName(),user.getPassword());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.hupa.server.IMAPStoreCache#get(java.lang.String, java.lang.String)
	 */
	public synchronized IMAPStore get(String username,String password) throws MessagingException {
		IMAPStore store = pool.get(username);
		if (store == null) {
			logger.debug("No cached store found for user " +username);
			if (useSSL) {
				store = (IMAPStore)session.getStore("imaps");
			} else {
				store = (IMAPStore) session.getStore("imap"); 
			}
		}
		if (store.isConnected() == false) {
			store.connect(address, port, username,password);
		}
		pool.put(username,store);
		return store;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.hupa.server.IMAPStoreCache#delete(org.apache.hupa.shared.data.User)
	 */
	public synchronized void delete(User user) {
		delete(user.getName());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.hupa.server.IMAPStoreCache#delete(java.lang.String)
	 */
	public synchronized void delete(String username) {
		IMAPStore store = pool.get(username);
		if (store != null && store.isConnected()) {
			try {
				store.close();
			} catch (MessagingException e) {
				// Ignore on close
			}
		}
		pool.remove(username);
	}
}
