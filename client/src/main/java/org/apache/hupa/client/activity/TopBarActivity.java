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

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.rf.LogoutUserRequest;
import org.apache.hupa.client.ui.LoginLayoutable;
import org.apache.hupa.shared.domain.LogoutUserResult;
import org.apache.hupa.shared.events.LogoutEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class TopBarActivity extends AppBaseActivity {

    @Inject private Displayable display;
    @Inject private LoginLayoutable loginLayout;

    @UiField protected HTMLPanel userLabel;

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        container.setWidget(display.asWidget());
        bindTo(eventBus);
        String user = HupaController.user != null ? HupaController.user.getName() : "Not logged in";
        display.getUserLabel().clear();
        display.getUserLabel().add(new HTML(user));
    }

    private void bindTo(EventBus eventBus) {
        registerHandler(display.getLogoutClick().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                doLogout();
            }
        }));
    }

    private void doLogout() {
        LogoutUserRequest req = rf.logoutRequest();
        hc.showTopLoading("Logging out ...");

        req.logout().fire(new Receiver<LogoutUserResult>() {
            public void onSuccess(LogoutUserResult response) {
                eventBus.fireEvent(new LogoutEvent(response.getUser()));
            }

            public void onFailure(ServerFailure error) {
                eventBus.fireEvent(new LogoutEvent(null));
            }
        });
    }

    public interface Displayable extends IsWidget {
        HasClickHandlers getLogoutClick();
        void showUserName(String userName);
        HTMLPanel getUserLabel();
        void showLoading(String message);
        void hideLoading();
    }
}
