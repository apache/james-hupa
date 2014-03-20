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

package org.apache.hupa.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.mail.MessagingException;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.exception.HupaException;
import org.junit.Test;

public class LoginUserServiceTest extends HupaGuiceTestCase {

    @Test public void invalidLogin() throws MessagingException {
        httpSession.setAttribute("Attribute", "Value");
        try {
            loginUserService.login("invalid", "invalid", null);
            fail("Should throw an exception");
        } catch (Throwable t) {
        }
        assertNull("No user should be stored in session", httpSession.getAttribute(SConsts.USER_SESS_ATTR));
        assertNull("Attributes should be removed", httpSession.getAttribute("Attribute"));
    }

    @Test public void validLogin() throws MessagingException {
        try {
            System.out.println(testUser.getName() + " " + testUser.getPassword());
            User u = loginUserService.login(testUser.getName(), testUser.getPassword(), null);
            assertEquals("Authenticated", true, u.getAuthenticated());
            assertEquals("Authenticated", testUser.getName(), u.getName());
            assertEquals("Authenticated", testUser.getPassword(), u.getPassword());
            assertEquals("User stored in session", u, httpSession.getAttribute(SConsts.USER_SESS_ATTR));
        } catch (HupaException e) {
            e.printStackTrace();
            fail("Should throw an exception");
        }
    }
}
