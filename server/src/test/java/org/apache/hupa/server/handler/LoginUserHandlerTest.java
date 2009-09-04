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

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.server.mock.MockLog;
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;

import com.google.inject.Provider;

public class LoginUserHandlerTest extends AbstractHandlerTest{
	private LoginUserHandler handler = new LoginUserHandler(storeCache, new MockLog(),sessionProvider, new Provider<Settings>() {

		public Settings get() {
			return new Settings();
		}
	});
	public void testInvalidLogin() {
		try {
			handler.execute(new LoginUser("invalid","invalid"), null);
			fail("Should throw an exception");
		} catch (ActionException e) {
			e.printStackTrace();
		}
		assertNull("no user stored in session", session.getAttribute("user"));
	}
	
	public void testValidLogin() {
		String username = "valid";
		String password = "valid";
		storeCache.addValidUser(username, password);
		
		try {
			LoginUserResult result = handler.execute(new LoginUser(username,password), null);
			User u = result.getUser();
			
			assertEquals("Authenticated", true, u.getAuthenticated());
			assertEquals("Authenticated", username, u.getName());
			assertEquals("Authenticated", password, u.getPassword());
			assertEquals("Authenticated", VALID_ID, u.getSessionId());
			assertEquals("User stored in session", u, session.getAttribute("user"));
		} catch (ActionException e) {
			e.printStackTrace();
			fail("Should throw an exception");
		}
	}
	
}
