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

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ContactsView extends Composite implements ContactsPresenter.Display{

    private HorizontalPanel panel = new HorizontalPanel();
    private HupaConstants constants;
    @Inject
    public ContactsView(HupaConstants constants) {
        this.constants = constants;
        panel.addStyleName(HupaCSS.C_contacts_container);
        panel.add(new HTML("<center><h1>Contacts view: comming soon<h1></center>"));
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

}
