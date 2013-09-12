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

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetContainerDisplay;
import net.customware.gwt.presenter.client.widget.WidgetContainerPresenter;

import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.FlashEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.ServerStatusEvent;
import org.apache.hupa.shared.events.ServerStatusEventHandler;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
import org.apache.hupa.shared.events.ServerStatusEvent.ServerStatus;
import org.apache.hupa.shared.rpc.CheckSession;
import org.apache.hupa.shared.rpc.CheckSessionResult;
import org.apache.hupa.shared.rpc.Idle;
import org.apache.hupa.shared.rpc.IdleResult;
import org.apache.hupa.shared.rpc.LogoutUser;
import org.apache.hupa.shared.rpc.LogoutUserResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;

/**
 * Presenter which flips the in the LoginPresenter or the LoginPresenter depending on if the user is 
 * logged in or not
 *
 */
public class AppPresenter extends WidgetContainerPresenter<AppPresenter.Display>{

    private static final int IDLE_INTERVAL = 150000;
    HupaConstants constants;

    public interface Display extends WidgetContainerDisplay {
        public HasClickHandlers getLogoutClick();
        public HasClickHandlers getContactsClick();
        public HasClickHandlers getMainClick();
        public void showTopNavigation(boolean show);
        public void showContactsButton();
        public void showMainButton();
        public HasText getUserText();
        public void setServerStatus(ServerStatus status);
        public void showMessage(String message, int millisecs);
    }

    private Timer noopTimer = new IdleTimer();

    private DispatchAsync dispatcher;
    private User user;
    private ServerStatus serverStatus = ServerStatus.Available;
    private MainPresenter mainPresenter;
    private LoginPresenter loginPresenter;
    private ContactsPresenter contactsPresenter;
    
    @Inject
    public AppPresenter(Display display, DispatchAsync dispatcher, final EventBus bus, HupaConstants constants, LoginPresenter loginPresenter, MainPresenter mainPresenter, ContactsPresenter contactsPresenter) {
        super(display, bus, loginPresenter, mainPresenter, contactsPresenter);
        this.mainPresenter = mainPresenter;
        this.loginPresenter = loginPresenter;
        this.contactsPresenter = contactsPresenter;
        this.dispatcher = dispatcher;  
        this.constants = constants;
    }

    private void showMain(User user) {
        display.showTopNavigation(true);
        display.showContactsButton();
        mainPresenter.revealDisplay(user);
    }
    
    private void showLogin(String username) {
        display.showTopNavigation(false);
        loginPresenter.revealDisplay();
    }

    private void showContacts() {
        display.showTopNavigation(true);
        display.showMainButton();
        contactsPresenter.revealDisplay();
    }

    @Override
    protected void onBind() {
        super.onBind();
        registerHandler(eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

            public void onLogin(LoginEvent event) {
                user = event.getUser();
                display.getUserText().setText(event.getUser().getName());
                noopTimer.scheduleRepeating(IDLE_INTERVAL);
                showMain(user);
                display.showMessage(constants.welcome(), 3000);
            }

        }));
        
        registerHandler(eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

            public void onLogout(LogoutEvent event) {
                User u = event.getUser();
                String username = null;
                if (u != null) {
                    username = u.getName();
                }
                showLogin(username);
                noopTimer.cancel();
            }
            
        }));
        registerHandler(display.getLogoutClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                doLogout();
            }
            
        }));
        registerHandler(display.getContactsClick().addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent event) {
                showContacts();
            }
            
        }));
        registerHandler(display.getMainClick().addClickHandler(new ClickHandler() {
            
            public void onClick(ClickEvent event) {
                showMain(user);
            }
            
        }));
        registerHandler(eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {

            public void onSessionExpireEvent(SessionExpireEvent event) {
                doLogout();
            }
            
        }));
        registerHandler(eventBus.addHandler(ServerStatusEvent.TYPE, new ServerStatusEventHandler() {
            
            public void onServerStatusChange(ServerStatusEvent event) {
                if (event.getStatus() != serverStatus) {
                    GWT.log("Server status has hanged from " + serverStatus + " to" + event.getStatus(), null);
                    serverStatus = event.getStatus();
                    display.setServerStatus(serverStatus);
                }
            }
            
        }));
        registerHandler(eventBus.addHandler(FlashEvent.TYPE, new FlashEventHandler() {
            
            public void onFlash(FlashEvent event) {
                display.showMessage(event.getMessage(), event.getMillisec());
            }
            
        }));

        checkSession();
    }
    

    private void doLogout() {
        if (user != null) {
            dispatcher.execute(new LogoutUser(), new HupaCallback<LogoutUserResult>(dispatcher, eventBus) {
                public void callback(LogoutUserResult result) {
                    eventBus.fireEvent(new LogoutEvent(result.getUser()));
                }
            });
        }
    }

    private void checkSession() {
        dispatcher.execute(new CheckSession(), new AsyncCallback<CheckSessionResult>() {
            public void onFailure(Throwable caught) {
                serverStatus = ServerStatus.Unavailable;
                display.setServerStatus(serverStatus);
                showLogin(null);
            }
            public void onSuccess(CheckSessionResult result) {
                serverStatus = ServerStatus.Available;
                display.setServerStatus(serverStatus);
                if (result.isValid()) {
                    eventBus.fireEvent(new LoginEvent(result.getUser()));
                } else {
                    showLogin(null);
                }
            }
        });
    }
    
    private class IdleTimer extends Timer {
        boolean running = false;
        public void run() {
            if (!running) {
                running = true;
                dispatcher.execute(new Idle(), new HupaCallback<IdleResult>(dispatcher, eventBus) {
                    public void callback(IdleResult result) {
                        running = false;
                        // check if the server is not supporting the Idle command.
                        // if so cancel this Timer
                        if (result.isSupported() == false) {
                            IdleTimer.this.cancel();
                        }
                        // Noop
                        // TODO: put code here to read new events from server (new messages ...)
                    }
                });
            }
        }
    }

    
}
