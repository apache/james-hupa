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


package org.apache.hupa.server.handler;

import java.util.Properties;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.apache.hupa.server.mock.MockHttpSession;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;

import com.google.inject.Provider;

import junit.framework.TestCase;

public abstract class AbstractHandlerTest extends TestCase{
	public final static String VALID_ID ="VALID_ID";
	
	protected HttpSession session = new MockHttpSession(VALID_ID);
	
	protected MockIMAPStoreCache storeCache = new MockIMAPStoreCache( new Provider<Session>() {

		public Session get() {
			Session session = Session.getDefaultInstance(new Properties());
			session.addProvider(MockIMAPStore.getProvider());
			return session;
		}
		
	});
	
	protected Provider<HttpSession> sessionProvider = new Provider<HttpSession>() {
		public HttpSession get() {
			return session;
		}
		
	};
	
	public void tearDown() {
		storeCache.clear();
	}
	
	protected User createUser() {
		User user = new User();
		user.setName("test");
		user.setPassword("password");
		user.setSessionId(VALID_ID);
		user.setSettings(new Settings());
		return user;
	}
}
