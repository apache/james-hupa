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

package org.apache.hupa.server.preferences;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

/**
 * A preferences storage which uses session as repository data
 */
public class InSessionUserPreferencesStorage extends UserPreferencesStorage {

    private final Provider<HttpSession> sessionProvider;

    @Inject
    public InSessionUserPreferencesStorage(IMAPStoreCache cache, Log logger, Provider<HttpSession> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void addContact(Contact... contacts) {
        HttpSession session = sessionProvider.get();

        @SuppressWarnings("unchecked")
        HashMap<String, Contact> sessionContacts = (HashMap<String, Contact>)session.getAttribute(SConsts.CONTACTS_SESS_ATTR);
        if (sessionContacts==null) {
            sessionContacts=new HashMap<String, Contact>();
            session.setAttribute(SConsts.CONTACTS_SESS_ATTR, sessionContacts);
        }

        for(Contact contact: contacts) {
            if (!sessionContacts.containsKey(contact.toKey())) {
                sessionContacts.put(contact.toKey(), contact);
            }
        }
    }

    public Contact[] getContacts() {
        HttpSession session = sessionProvider.get();

        @SuppressWarnings("unchecked")
        HashMap<String, Contact> sessionContacts = (HashMap<String, Contact>)session.getAttribute(SConsts.CONTACTS_SESS_ATTR);

        return sessionContacts == null ? new Contact[]{} : sessionContacts.values().toArray(new Contact[sessionContacts.size()]);
    }
}
