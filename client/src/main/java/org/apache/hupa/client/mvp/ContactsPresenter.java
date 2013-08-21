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

import com.google.inject.Inject;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.shared.events.ContactsUpdatedEvent;
import org.apache.hupa.shared.events.ContactsUpdatedEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
import org.apache.hupa.shared.rpc.Contacts;
import org.apache.hupa.shared.rpc.ContactsResult;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

public class ContactsPresenter extends WidgetPresenter<ContactsPresenter.Display>{

    public interface Display extends NameAwareWidgetDisplay, WidgetDisplay {
        public void setContacts(Contact[] contacts);
        public Contact[] getContacts();
    }

    DispatchAsync dispatcher;
    protected Contact[] contacts;
    protected EventBus eventBus;
    
    @Inject
    public ContactsPresenter(Display display, EventBus eventBus, DispatchAsync dispatcher) {
        super(display, eventBus);
        this.dispatcher = dispatcher;
        this.eventBus = eventBus;
    }
    
    @Override
    protected void onBind() {
        registerHandler(eventBus.addHandler(ContactsUpdatedEvent.TYPE, new ContactsUpdatedEventHandler() {
            public void onContactsUpdated(ContactsUpdatedEvent event) {
                contacts = event.getContacts();
                display.setContacts(contacts);
            }
        }));
        registerHandler(eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {
            public void onMessagesReceived(MessagesReceivedEvent event) {
                updateContactsFromServer();
            }
        }));
        registerHandler(eventBus.addHandler(SentMessageEvent.TYPE, new SentMessageEventHandler() {
            public void onSentMessageEvent(SentMessageEvent ev) {
                updateContactsFromServer();
            }
        }));
    }

    @Override
    protected void onRevealDisplay() {
    }

    @Override
    protected void onUnbind() {
    }

    protected void updateContactsFromServer() {
        dispatcher.execute(new Contacts(),  new HupaCallback<ContactsResult>(dispatcher, eventBus) {
            public void callback(ContactsResult result) {
                eventBus.fireEvent(new ContactsUpdatedEvent(result.getContacts()));
            }
        }); 
    }
    
}
