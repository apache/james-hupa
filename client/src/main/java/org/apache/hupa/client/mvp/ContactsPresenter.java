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
import org.apache.hupa.shared.rpc.Contacts;
import org.apache.hupa.shared.rpc.ContactsResult;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

public class ContactsPresenter extends WidgetPresenter<ContactsPresenter.Display>{

    DispatchAsync dispatcher;
    
    @Inject
    public ContactsPresenter(Display display, EventBus eventBus, DispatchAsync dispatcher) {
        super(display, eventBus);
        this.dispatcher = dispatcher;
    }

    public interface Display extends NameAwareWidgetDisplay, WidgetDisplay {
        public void setContacts(Contact[] contacts);
    }
    
    @Override
    protected void onBind() {
    }

    @Override
    protected void onRevealDisplay() {
        dispatcher.execute(new Contacts(),  new HupaCallback<ContactsResult>(dispatcher, eventBus) {
            public void callback(ContactsResult result) {
                display.setContacts(result.getContacts());
            }
        }); 
    }

    @Override
    protected void onUnbind() {
        // TODO Auto-generated method stub
        
    }

}
