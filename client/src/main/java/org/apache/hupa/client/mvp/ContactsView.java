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

import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import java.util.ArrayList;

public class ContactsView extends Composite implements ContactsPresenter.Display{

    private VerticalPanel panel = new VerticalPanel();
    FlexTable ctable = new FlexTable();
    
    private HupaConstants constants;
    
    @Inject
    public ContactsView(HupaConstants constants) {
        this.constants = constants;
        panel.addStyleName(HupaCSS.C_contacts_container);
        panel.add(new HTML("<h1>Contacts view: comming soon<h1>"));
        panel.add(ctable);
        initWidget(panel);
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.NameAwareWidgetDisplay#getName()
     */
    public String getName() {
        return constants.contactsTab();
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    public Widget asWidget() {
        return this;
    }

    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.ContactsPresenter.Display#setContacts(org.apache.hupa.shared.rpc.ContactsResult.Contact[])
     */
    public void setContacts(Contact[] contacts) {
        ctable.clearAll();
        for(int i=0; i<contacts.length; i++) {
            ctable.setText(i, 0, contacts[i].realname);
            ctable.setText(i, 1, contacts[i].mail);
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.ContactsPresenter.Display#getContacts()
     */
    public Contact[] getContacts() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        for (int i=0; i < ctable.getRowCount(); i++) {
            contacts.add(new Contact(ctable.getText(i, 0), ctable.getText(i, 1)));
        }
        return contacts.toArray(new Contact[contacts.size()]);
    }

}
