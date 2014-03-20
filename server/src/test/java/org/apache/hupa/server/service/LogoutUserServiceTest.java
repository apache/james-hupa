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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.LogoutUserResult;
import org.apache.hupa.shared.exception.HupaException;
import org.junit.Test;

public class LogoutUserServiceTest extends HupaGuiceTestCase {

    @Test public void logout() {
        try {
            httpSession.setAttribute("Attribute", "Value");
            LogoutUserResult result = logoutUserService.logout();
            assertFalse("Not authenticated anymore", result.getUser().getAuthenticated());
            assertNull("User should be removed", httpSession.getAttribute(SConsts.USER_SESS_ATTR));
            assertNull("Attributes should be removed", httpSession.getAttribute("Attribute"));
        } catch (HupaException e) {
            e.printStackTrace();
            fail();
        }

    }
}
