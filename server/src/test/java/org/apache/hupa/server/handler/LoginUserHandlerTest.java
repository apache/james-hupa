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

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;

public class LoginUserHandlerTest extends HupaGuiceTestCase {
    
    public void testInvalidLogin() {
        httpSession.setAttribute("Attribute", "Value");
        try {
            loginUser.execute(new LoginUser("invalid","invalid"), null);
            fail("Should throw an exception");
        } catch (ActionException e) {
        }
        assertNull("No user should be stored in session", httpSession.getAttribute(SConsts.USER_SESS_ATTR));
        assertNull("Attributes should be removed", httpSession.getAttribute("Attribute"));
    }
    
    public void testValidLogin() {
        try {
            LoginUserResult result = loginUser.execute(new LoginUser(testUser.getName(), testUser.getPassword()), null);
            User u = result.getUser();
            assertEquals("Authenticated", true, u.getAuthenticated());
            assertEquals("Authenticated", testUser.getName(), u.getName());
            assertEquals("Authenticated", testUser.getPassword(), u.getPassword());
            assertEquals("User stored in session", u, httpSession.getAttribute(SConsts.USER_SESS_ATTR));
        } catch (ActionException e) {
            e.printStackTrace();
            fail("Should throw an exception");
        }
    }
}
