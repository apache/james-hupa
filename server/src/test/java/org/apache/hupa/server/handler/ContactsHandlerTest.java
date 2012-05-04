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

import junit.framework.Assert;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.shared.rpc.Contacts;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

public class ContactsHandlerTest extends HupaGuiceTestCase {
	
	private Contact[] getContacts() throws Exception {
		return contactsHandler.execute(new Contacts(), null).getContacts();
	}
    
    public void testContactsHandler() throws Exception {
        Assert.assertEquals(0, getContacts().length);
        userPreferences.addContact("Somebody <somebody@foo.com>");
        userPreferences.addContact(" Some.body   <somebody@foo.com>  ");
        userPreferences.addContact("\"somebody\" <somebody@foo.com>");
        Assert.assertEquals(1, getContacts().length);
        userPreferences.addContact("<somebody@foo.com>");
        userPreferences.addContact("somebody@foo.com");
        userPreferences.addContact("\"somebody@foo.com\" <somebody@foo.com>");
        Assert.assertEquals(2, getContacts().length);
    }
    
}
