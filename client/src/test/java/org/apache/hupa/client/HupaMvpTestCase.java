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

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.hupa.client.guice.GuiceClientTestModule;
import org.apache.hupa.client.guice.GuiceMvpTestModule;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.junit.GWTMockUtilities;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.mail.imap.IMAPStore;

/**
 * Base class for testing presenters in hupa.
 * Tests extending this class only work in jvm.
 * 
 * @author manolo
 *
 */
public abstract class HupaMvpTestCase extends TestCase {
    
    protected Injector injector = Guice.createInjector(getModules());

    protected HttpSession httpSession;
    protected Session session;
    protected UserPreferencesStorage userPreferences;
    protected IMAPStoreCache storeCache;
    protected User testUser;
    protected IMAPStore store;
    protected EventBus eventBus;
    
    protected Module[] getModules() {
        return new Module[]{new GuiceClientTestModule(), new GuiceMvpTestModule()};
    }

    @Override
    protected void setUp() throws Exception {
        try {
            GWTMockUtilities.disarm();
            httpSession = injector.getInstance(HttpSession.class);
            session = injector.getInstance(Session.class);
            userPreferences = injector.getInstance(UserPreferencesStorage.class);
            storeCache = injector.getInstance(IMAPStoreCache.class);
            eventBus = injector.getInstance(EventBus.class);
            
            SessionUtils.cleanSessionAttributes(httpSession);
            testUser = injector.getInstance(User.class);
            store = storeCache.get(testUser);
            httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
