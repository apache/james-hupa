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

package org.apache.hupa.client.activity;

import java.util.List;

import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.events.RefreshMessagesEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class SearchBoxActivity extends AppBaseActivity {

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        bindTo(eventBus);
        container.setWidget(display.asWidget());

    }

    private void bindTo(final EventBus eventBus) {

        registerHandler(display.getSearchClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String searchValue = null;
                if (display.getSearchValue().getValue().trim().length() > 0) {
                    searchValue = display.getSearchValue().getValue().trim();
                }
                eventBus.fireEvent(new RefreshMessagesEvent(searchValue));
                hc.showTopLoading("Searching...");
            }

        }));

//        registerHandler(eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {
//
//            public void onMessagesReceived(MessagesReceivedEvent event) {
//
//                // fill the oracle
//                display.fillSearchOracle(event.getMessages());
//            }
//
//        }));
    }

    @Inject private Displayable display;

    public interface Displayable extends IsWidget {
        HasClickHandlers getSearchClick();

        HasValue<String> getSearchValue();

        void fillSearchOracle(List<Message> messages);
    }
}
