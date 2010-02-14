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
package org.apache.hupa.client;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.sun.mail.imap.IMAPStore;

import junit.framework.TestCase;

import org.apache.hupa.client.guice.GuiceMvpTestModule;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.GuiceServerTestModule;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.User;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

/**
 * Base class for testing presenters in hupa
 * 
 * @author manolo
 *
 */
public abstract class HupaMvpTestCase extends TestCase {

    // Create an injector containing both, the server and the client modules.
    protected Injector injector = Guice.createInjector(new GuiceServerTestModule(), new GuiceMvpTestModule());

    protected HttpSession httpSession = injector.getInstance(HttpSession.class);
    protected Session session = injector.getInstance(Session.class);

    protected UserPreferencesStorage userPreferences = injector.getInstance(UserPreferencesStorage.class);
    protected IMAPStoreCache storeCache = injector.getInstance(IMAPStoreCache.class);

    protected User testUser;
    protected IMAPStore store;

    @Override
    protected void setUp() throws Exception {
        try {
            testUser = injector.getInstance(User.class);
            store = storeCache.get(testUser);
            httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
        } catch (Exception e) {
        }
    }

}
