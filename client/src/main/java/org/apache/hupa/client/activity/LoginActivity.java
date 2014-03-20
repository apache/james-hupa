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

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.rf.LoginUserRequest;
import org.apache.hupa.client.storage.HupaStorage;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
import org.apache.hupa.widgets.dialog.Dialog;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LoginActivity extends AppBaseActivity {


    @Inject private HupaLayoutable hupaLayout;
    @Inject private Displayable display;
    @Inject private HupaConstants constants;
    @Inject private HupaStorage storage;

    private Settings settings;

    @Override
    public void start(AcceptsOneWidget container, final EventBus eventBus) {
        container.setWidget(display.asWidget());
        display.setActivity(this);
        display.setLoading(false);

        registerHandler(eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {
            public void onSessionExpireEvent(SessionExpireEvent event) {
                eventBus.fireEvent(new FlashEvent(constants.sessionTimedOut(), 4000));
            }
        }));
    }

    public void doLogin() {
        final String user = display.getUserNameValue().getValue().trim();
        final String pass = display.getPasswordValue().getValue().trim();
        if (user.isEmpty() || pass.isEmpty())
            return;
        display.setLoading(true);

        LoginUserRequest loginRequest = rf.loginRequest();
        if (settings != null) {
             settings = display.getSettings(loginRequest.edit(settings));
        }

        loginRequest.login(user, pass, settings).fire(new Receiver<User>() {
            @Override
            public void onSuccess(User response) {
                HupaController.user = response;

                RootLayoutPanel.get().clear();
                RootLayoutPanel.get().add(hupaLayout.get());
                pc.goTo(new FolderPlace(response.getSettings().getInboxFolderName()));

                eventBus.fireEvent(new LoginEvent(response));
                display.setLoading(false);

                storage.saveSettings(user, settings);
            }
            @Override
            public void onFailure(ServerFailure error) {
                Dialog.alert("Unable to login, verify that your user, password and settings are correct.");
                display.setLoading(false);
            }
        });
    }

    public interface Displayable extends IsWidget {
        public HasValue<String> getUserNameValue();
        public Settings getSettings(Settings edit);
        public void setActivity(LoginActivity loginActivity);
        public HasValue<String> getPasswordValue();
        public void setLoading(boolean loading);
        public Widget asWidget();
        void setSettings(Settings s);
    }

    public void loadSettings() {
      System.out.println("Load settings");
      String email = display.getUserNameValue().getValue();
      if (!email.isEmpty()) {
          settings = storage.getSettingsByEmail(email);
          if (settings == null) {
              rf.loginRequest().getSettings(email).fire(new Receiver<Settings>() {
                  public void onSuccess(Settings response) {
                      settings = response;
                      display.setSettings(settings);
                  }
              });
          } else {
              display.setSettings(settings);
          }
      }
    }
}
