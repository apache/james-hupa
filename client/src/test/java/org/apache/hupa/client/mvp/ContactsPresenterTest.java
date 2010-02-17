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
package org.apache.hupa.client.mvp;

import org.apache.hupa.client.HupaMvpTestCase;
import org.apache.hupa.shared.events.ContactsUpdatedEvent;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;
import org.easymock.EasyMock;

public class ContactsPresenterTest extends HupaMvpTestCase {

    ContactsPresenter presenter = injector.getInstance(ContactsPresenter.class);
    ContactsPresenter.Display display = presenter.getDisplay();
    
    public void testRevealDisplayWhenListIsEmpty() throws Exception {
        // Register an event listener
        presenter.onBind();
        Contact[] contacts = new Contact[]{};
        
        EasyMock.reset(display);
        display.setContacts(EasyMock.aryEq(contacts));
        EasyMock.replay(display);
        // send an event
        eventBus.fireEvent(new ContactsUpdatedEvent(contacts));
        assertNotNull(presenter.contacts);
        assertEquals(0, presenter.contacts.length);
        EasyMock.verify(display);
        EasyMock.reset(display);
    }

    public void testRevealDisplayWhenListHasData() throws Exception {
        Contact c = new Contact("Somebody <somebody@foo.com>");
        assertEquals("Somebody", c.realname);
        assertEquals("somebody@foo.com", c.mail);
        Contact[] contacts = new Contact[]{c};
        
        // Register an event listener
        presenter.onBind();
        
        EasyMock.reset(display);
        display.setContacts(EasyMock.aryEq(contacts));
        EasyMock.replay(display);
        // send an event
        eventBus.fireEvent(new ContactsUpdatedEvent(contacts));
        assertNotNull(presenter.contacts);
        assertEquals(1, presenter.contacts.length);
        EasyMock.verify(display);
        EasyMock.reset(display);
    }

    public void testUpdateContactsFromServer() throws Exception {
        Contact c = new Contact("Somebody <somebody@foo.com>");
        assertEquals("Somebody", c.realname);
        assertEquals("somebody@foo.com", c.mail);
        Contact[] contacts = new Contact[]{c};
        
        assertNull(presenter.contacts);
        // Register the event listener
        presenter.onBind();
        // Put contacts in server side
        userPreferences.addContact(contacts);
        // Call to the server
        presenter.updateContactsFromServer();
        assertNotNull(presenter.contacts);
        assertEquals(1, presenter.contacts.length);
    }
}

