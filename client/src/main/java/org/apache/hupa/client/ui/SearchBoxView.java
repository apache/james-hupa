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

import java.util.List;

import org.apache.hupa.client.activity.SearchBoxActivity;
import org.apache.hupa.shared.domain.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

public class SearchBoxView extends Composite implements SearchBoxActivity.Displayable {

    private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(" ,@");
    private TextBox searchBox = new TextBox();
    private Button searchButton = new Button("Search");
    @UiField protected HorizontalPanel thisPanel;

    // @SuppressWarnings("deprecation")
    public SearchBoxView() {
        initWidget(binder.createAndBindUi(this));

        // searchBox.addStyleName(HupaCSS.C_msg_search);

        // searchBox.setAnimationEnabled(true);
        // searchBox.setAutoSelectEnabled(false);
        // searchBox.setLimit(20);
        searchBox.getElement().setAttribute("type", "search");
        searchBox.getElement().setAttribute("placeholder", "Search...");
        searchBox.getElement().setAttribute("results", "10");
        searchBox.getElement().setAttribute("incremental", "incremental");
        searchBox.getElement().setAttribute("name", "s");
        searchBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE && searchBox.getText().trim().equals(""))) {
                    searchButton.click();
                }
            }
        });
        thisPanel.add(searchBox);
        thisPanel.add(searchButton);
    }

    @Override
    public HasClickHandlers getSearchClick() {
        return searchButton;
    }

    @Override
    public HasValue<String> getSearchValue() {
        return searchBox;
    }

    @Override
    public void fillSearchOracle(List<Message> messages) {
        for (Message m : messages) {
            String subject = m.getSubject();
            String from = m.getFrom();
            if (subject != null && subject.trim().length() > 0) {
                oracle.add(subject.trim());
            }
            if (from != null && from.trim().length() > 0) {
                oracle.add(from.trim());
            }
        }
        // searchBox.setText("");
    }

    interface SearchBoxUiBinder extends UiBinder<HorizontalPanel, SearchBoxView> {
    }

    private static SearchBoxUiBinder binder = GWT.create(SearchBoxUiBinder.class);

}
