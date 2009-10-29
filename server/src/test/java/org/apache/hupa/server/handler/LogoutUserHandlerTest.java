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
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.LogoutUser;
import org.apache.hupa.shared.rpc.LogoutUserResult;

public class LogoutUserHandlerTest extends AbstractHandlerTest{

    
    public void testLogout() {
        String username = "test";
        String password = "pass";
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setAuthenticated(true);
        httpSession.setAttribute("user", user);
        LogoutUserHandler handler = new LogoutUserHandler(storeCache,new MockLog(),httpSessionProvider);
        try {
            LogoutUserResult result = handler.execute(new LogoutUser(), null);
            assertFalse("Not authenticated anymore", result.getUser().getAuthenticated());
            assertNull("User removed", httpSession.getAttribute("user"));
            
        } catch (ActionException e) {
            e.printStackTrace();
            fail();
        }
        
    }
}
