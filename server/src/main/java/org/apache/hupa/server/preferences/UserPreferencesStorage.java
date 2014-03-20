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

import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Abstract class which defines storage operations related
 * with user preferences
 *
 */
public abstract class UserPreferencesStorage {

    protected static final String REGEX_OMITTED_EMAILS = "^.*(reply)[A-z0-9._%\\+\\-]*@.*$";

    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates
     */
    abstract public void addContact(Contact... c);

    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates
     */
    final public void addContact(String... mails) {
        if (mails != null) {
            addContact(Arrays.asList(mails));
        }
    }

    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates
     */
    final public void addContact(List<String> mails) {
        if (mails != null) {
            for (String mail: mails) {
                if (mail != null && !mail.matches(REGEX_OMITTED_EMAILS)) {
                    Contact contact = new Contact(mail);
                    if (!exists(contact)) {
                        addContact(contact);
                    }
                }
            }
        }
    }

    boolean exists (Contact mail) {
        for (Contact c : getContacts()) {
            if (c.mail.equals(mail.mail)) {
                if (c.realname == null || c.realname.isEmpty() || c.realname.equals(c.mail)) {
                    c.realname = mail.realname;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Get the list of contacts
     */
    abstract public Contact[] getContacts();

}
