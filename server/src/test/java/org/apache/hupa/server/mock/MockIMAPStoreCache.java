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

package org.apache.hupa.server.mock;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.sun.mail.imap.IMAPStore;

import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

public class MockIMAPStoreCache implements IMAPStoreCache {
  
    private Provider<Session> provider = new Provider<Session>() {
        public Session get() {
            Session session = Session.getDefaultInstance(new Properties());
            session.addProvider(MockIMAPStore.getProvider());
            return session;
        }
    };
    
    private Map<String, String> users = new HashMap<String, String>();
    private Map<String, IMAPStore> stores = new HashMap<String, IMAPStore>();

    @Inject
    public MockIMAPStoreCache(User user) {
        addValidUser(user);
    }

    public void addValidUser(User user, IMAPStore store) {
        addValidUser(user.getName(), user.getPassword(), store);
    }

    public void addValidUser(String username, String password, IMAPStore store) {
        users.put(username, password);
        stores.put(username, store);
    }

    public void addValidUser(User user) {
        addValidUser(user.getName(), user.getPassword());
    }

    public void addValidUser(String username, String password) {
        try {
            addValidUser(username, password, (IMAPStore)provider.get().getStore("mockimap"));
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid store", e);
        }
    }

    public void clear() {
        users.clear();
    }

    public void delete(User user) {
        users.remove(user.getName());
    }

    public void delete(String username) {
        users.remove(username);
    }

    public IMAPStore get(User user) throws MessagingException {
        return get(user.getName(), user.getPassword());
    }

    public IMAPStore get(String username, String password) throws MessagingException {
        String pass = users.get(username);
        if (pass != null && pass.equals(password)) {
            return stores.get(username);
        } else {
            throw new MessagingException("Invalid user");
        }
    }

}
