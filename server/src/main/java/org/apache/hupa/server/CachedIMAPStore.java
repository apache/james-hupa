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

package org.apache.hupa.server;

import javax.mail.MessagingException;
import javax.mail.Session;

import com.sun.mail.imap.IMAPStore;

public class CachedIMAPStore {
    private long validUntil;
    private int validForMillis;
    private IMAPStore store;
    private Session session;

    private CachedIMAPStore() {
    }

    public static CachedIMAPStore getInstance() {
        return new CachedIMAPStore();
    }

    public CachedIMAPStore(IMAPStore store, int validForSeconds) {
        this.store = store;
        this.validForMillis = validForSeconds * 1000;
        this.validUntil = System.currentTimeMillis() + validForMillis;
    }

    public boolean isExpired() {
        if (validUntil > System.currentTimeMillis() && store.isConnected()) {
            return false;
        }
        return true;
    }

    public void validate() throws MessagingException {
        validUntil = System.currentTimeMillis() + validForMillis;
    }

    public IMAPStore getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}