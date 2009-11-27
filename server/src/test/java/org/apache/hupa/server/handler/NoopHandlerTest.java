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

import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.Noop;
import org.apache.hupa.shared.rpc.NoopResult;

public class NoopHandlerTest extends AbstractHandlerTest{

    public void testNoop() {
        NoopHandler handler = new NoopHandler(storeCache, logger, httpSessionProvider);
        User user = createUser();
        Noop action = new Noop();
        storeCache.addValidUser(user.getName(), user.getPassword());
        httpSession.setAttribute("user", user);
        try {
            NoopResult result = handler.execute(action, null);
            assertNotNull(result);
        } catch (ActionException e) {
            e.printStackTrace();
            fail();

        }
        
    }
}
