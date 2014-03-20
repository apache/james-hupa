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

package org.apache.hupa.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hupa.client.activity.AddressListActivity;
import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.shared.events.AddressClickEvent;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class AddressListView extends Composite implements AddressListActivity.Displayable {

    @Inject EventBus eventBus;

    @UiField SimplePanel thisView;

    private static final String EMAIL_PATTERN = "\\<(.+?)\\>";

    private CellList<AddressNode> addrList;
    private ListDataProvider<AddressNode> dataProvider = new ListDataProvider<AddressNode>();

    public interface Resources extends CellList.Resources {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("res/CssLabelListView.css")
        public CellList.Style cellListStyle();
    }

    public final SingleSelectionModel<AddressNode> selectionModel = new SingleSelectionModel<AddressNode>(
            new ProvidesKey<AddressNode>() {
                @Override
                public Object getKey(AddressNode item) {
                    return item == null ? null : item.getEmail();
                }
            });

    class AddressCell extends AbstractCell<AddressNode> {
        public AddressCell(String... consumedEvents) {
            super(consumedEvents);
        }
        @Override
        public void render(Context context, AddressNode value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendEscaped(value.getEmail());
            }
        }
    }

    public static final ProvidesKey<AddressNode> KEY_PROVIDER = new ProvidesKey<AddressNode>() {
        @Override
        public Object getKey(AddressNode item) {
            return item == null ? null : item.getEmail();
        }
    };

    public static class AddressNode {
        private String name;
        private String email;

        public AddressNode(String name, String email) {
            super();
            this.name = name;
            this.email = email;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

    }

    public AddressListView() {
        initWidget(binder.createAndBindUi(this));
        dataProvider.setList(getContactsFromCache());

        addrList = new CellList<AddressNode>(new AddressCell(), Resources.INSTANCE, KEY_PROVIDER);
        addrList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        addrList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
        // default page size -> max int value
        addrList.setPageSize(Integer.MAX_VALUE);
        addrList.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                eventBus.fireEvent(new AddressClickEvent(selectionModel.getSelectedObject().getEmail()));
            }
        });
        dataProvider.addDataDisplay(addrList);
        thisView.setWidget(addrList);
    }


    List<AddressNode> getContactsFromCache() {
        String[] contacts = null;
        Storage contactStore = Storage.getLocalStorageIfSupported();
        if (contactStore != null) {
            String contactsString = contactStore.getItem(MessageListActivity.CONTACTS_STORE);
            System.out.println(contactsString);
            if (contactsString != null)
                contacts = contactsString.replace("[", "").replace("]", "").trim().split(",");
        }
        List<AddressNode> addrs = new ArrayList<AddressNode>();
        if (contacts == null || contacts.length == 0) {
            return null;
        }


        // Compile and use regular expression
//        RegExp regExp = RegExp.compile(EMAIL_PATTERN);
        for (String contact : contacts) {
            addrs.add(new AddressNode(contact, contact));
//            MatchResult matcher = regExp.exec(contact);
//            boolean matchFound = (matcher != null); // equivalent to regExp.test(inputStr);
//            if (matchFound) {
//                // Get all groups for this match
//                for (int i=0; i<=matcher.getGroupCount(); i++) {
//                    String groupStr = matcher.getGroup(i);
//                    addrs.add(new AddressNode(contact, groupStr == null?contact:groupStr.substring(1, groupStr.length()-1)));
//                }
//            }
        }

        return addrs;

    }

    interface Binder extends UiBinder<SimplePanel, AddressListView> {
    }

    private static Binder binder = GWT.create(Binder.class);

}
