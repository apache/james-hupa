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

import com.google.inject.Provider;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.mock.MockHttpSession;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.server.mock.MockLog;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import java.util.Properties;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

public abstract class AbstractHandlerTest extends TestCase{
    
    public final static String VALID_ID ="VALID_ID";
    
    protected Log logger = new MockLog();
    
    User user = createUser();
    
    protected Session session = Session.getInstance(new Properties());
    protected Provider<Session> provider = new Provider<Session> () {
        public Session get() {
            return session;
        }
    };

    protected HttpSession httpSession = new MockHttpSession(VALID_ID);
    protected Provider<HttpSession> httpSessionProvider = new Provider<HttpSession>() {
        public HttpSession get() {
            return httpSession;
        }
        
    };
    
    protected Provider<Settings> settingsProvider = new Provider<Settings> () {
        public Settings get() {
            return DemoModeConstants.mockSettings;
        }
    };
    
    protected MockIMAPStoreCache storeCache = new MockIMAPStoreCache(user);
    
    protected UserPreferencesStorage preferences = new UserPreferencesStorage(){
        public void addContact(Contact... c) {
        }

        public Contact[] getContacts() {
            return null;
        }
    };
    
    public void setUp() {
        storeCache.addValidUser(user.getName(), user.getPassword());
    }
    
    public void tearDown() {
        storeCache.clear();
    }
    
    protected User createUser() {
        User user = new User();
        user.setName("test");
        user.setPassword("password");
        user.setSettings(new Settings());
        return user;
    }


}
