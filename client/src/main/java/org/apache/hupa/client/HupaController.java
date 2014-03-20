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

package org.apache.hupa.client;

import org.apache.hupa.client.activity.NotificationActivity;
import org.apache.hupa.client.activity.TopBarActivity;
import org.apache.hupa.client.mapper.ActivityManagerInitializer;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.place.ContactPlace;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.HupaPlace;
import org.apache.hupa.client.place.SettingPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.storage.HupaStorage;
import org.apache.hupa.client.ui.HupaLayout;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.client.ui.LoginLayoutable;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class HupaController {

    private final PlaceController placeController;
    private final HupaLayoutable hupaLayout;
    private final LoginLayoutable loginLayout;

    @Inject private NotificationActivity.Displayable noticeRegion;
    @Inject private TopBarActivity.Displayable topBar;

    public static User user = null;

    @Inject
    public HupaController(final PlaceController placeController, final PlaceHistoryHandler placeHistoryHandler,
            final EventBus eventBus, ActivityManagerInitializer initializeActivityManagerByGin, HupaRequestFactory requestFactory,
            HupaStorage storage, final HupaLayoutable hupaLayout, final LoginLayoutable loginLayout) {

        this.placeController = placeController;
        this.hupaLayout = hupaLayout;
        this.loginLayout = loginLayout;

        eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                adjustLayout(event);
            }
        });

        requestFactory.sessionRequest().getUser().fire(new Receiver<User>() {
            @Override
            public void onSuccess(User u) {
                if (u == null) {
                    placeController.goTo(new DefaultPlace(""));
                    onFailure(null);
                } else {
                    user = u;
                    eventBus.fireEvent(new LoginEvent(user));
                    showScreen(false);
                    eventBus.fireEvent(new LoginEvent(user));
                    placeHistoryHandler.handleCurrentHistory();
                }
            }
            @Override
            public void onFailure(ServerFailure error) {
                showScreen(true);
            }
        });

        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
            public void onLogout(LogoutEvent logoutEvent) {
                RootLayoutPanel.get().clear();
//              RootLayoutPanel.get().add(loginLayout.get());
//              pc.goTo(new DefaultPlace(""));
              Window.Location.reload();
            }
        });
    }

    private void showScreen(boolean login) {
        RootLayoutPanel.get().clear();
        RootLayoutPanel.get().add(login ? loginLayout.get() : hupaLayout.get());
    }

    private void adjustLayout(PlaceChangeEvent event) {
        Place place = event.getNewPlace();
        if (place instanceof ComposePlace) {
            ComposePlace here = (ComposePlace) place;
            if (here.getParameters() != null) {
                hupaLayout.switchTo(HupaLayout.LAYOUT_COMPOSE);
            } else {
                //FIXME using configure one
                if(GWT.isProdMode()){
                    placeController.goTo(new FolderPlace("INBOX"));
                }else{
                    placeController.goTo(new FolderPlace("Mock-Inbox"));
                }
            }
        } else if (place instanceof ContactPlace) {
            hupaLayout.switchTo(HupaLayout.LAYOUT_CONTACT);
        }  else if (place instanceof SettingPlace) {
            hupaLayout.switchTo(HupaLayout.LAYOUT_SETTING);
            SettingPlace sp = (SettingPlace)place;
            hupaLayout.arrangeSettingLayout(sp);
        } else if(place instanceof HupaPlace){
            hupaLayout.switchTo(HupaLayout.LAYOUT_MESSAGE);
        } else {
            return;
        }
    }

    public void showNotice(String html, int millis) {
        noticeRegion.notice(html);
        if (millis > 0)
            hideNotice.schedule(millis);
    }

    public void showTopLoading(String message) {
        topBar.showLoading(message);
    }

    public void hideTopLoading() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                topBar.hideLoading();
            }
        });
    }

    private final Timer hideNotice = new Timer() {
        public void run() {
            noticeRegion.hideNotification();
        }
    };
}
