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

import com.google.inject.Inject;
import com.google.inject.Provider;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.shared.rpc.Contacts;
import org.apache.hupa.shared.rpc.ContactsResult;

import javax.servlet.http.HttpSession;

/**
 * Handler for getting the list of contacts
 */
public class ContactsHandler implements ActionHandler<Contacts, ContactsResult> {

    UserPreferencesStorage userPreferences;

    @Inject
    public ContactsHandler(IMAPStoreCache cache, Log logger, Provider<HttpSession> sessionProvider, UserPreferencesStorage preferences) {
        this.userPreferences = preferences;
    }

    public ContactsResult execute(Contacts action, ExecutionContext context) throws ActionException {
        return new ContactsResult(userPreferences.getContacts());
    }

    public Class<Contacts> getActionType() {
        return Contacts.class;
    }

    public void rollback(Contacts action, ContactsResult result, ExecutionContext context) throws ActionException {
    }

}
