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
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.SessionAsyncCallback;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
import org.apache.hupa.shared.rpc.LoginSession;
import org.apache.hupa.shared.rpc.LoginUserResult;
import org.apache.hupa.shared.rpc.LogoutUser;
import org.apache.hupa.shared.rpc.LogoutUserResult;
import org.apache.hupa.shared.rpc.Noop;
import org.apache.hupa.shared.rpc.NoopResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AppPresenter extends WidgetPresenter<AppPresenter.Display>{

	public interface Display extends WidgetDisplay {
		public HasClickHandlers getLogoutClick();
		public void showTopNavigation(boolean show);
		public void setMain(Widget w);
		public HasText getUserText();
	}
	
	private LoginPresenter loginPresenter;
	private MainPresenter mainPresenter;
	private Timer noopTimer;
	private DispatchAsync dispatcher;
	private User user;
	

	@Inject
	public AppPresenter(Display display, DispatchAsync dispatcher,final EventBus bus, LoginPresenter loginPresenter, MainPresenter mainPresenter) {
		super(display,bus);
		this.dispatcher = dispatcher;
		this.loginPresenter = loginPresenter;
		this.mainPresenter = mainPresenter;		
	}

	private void showMain(User user) {
		loginPresenter.unbind();
		mainPresenter.bind(user);
		display.showTopNavigation(true);
		display.setMain(mainPresenter.getDisplay().asWidget());
	}
	
	
	private void showLogin() {
		mainPresenter.unbind();
		loginPresenter.bind();
		display.showTopNavigation(false);
		display.setMain(loginPresenter.getDisplay().asWidget());

	}

	@Override
	public Place getPlace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onBind() {
		registerHandler(eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

			public void onLogin(LoginEvent event) {
				user = event.getUser();
				display.getUserText().setText(event.getUser().getName());

				Cookies.setCookie("sessionId", user.getSessionId());
				noopTimer = new Timer() {

					@Override
					public void run() {
						dispatcher.execute(new Noop(user.getSessionId()), new SessionAsyncCallback<NoopResult>(new AsyncCallback<NoopResult>() {

							public void onFailure(Throwable caught) {
								GWT.log("Error while NOOP", caught);
							}

							public void onSuccess(NoopResult result) {
							}
							
						}, eventBus, user));
					}
					
				};
				noopTimer.scheduleRepeating(30000);
				showMain(user);
			}
		}));
		registerHandler(eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			public void onLogout(LogoutEvent event) {
				Cookies.removeCookie("sessionId");
				showLogin();
				if (noopTimer != null) {
					noopTimer.cancel();
				}
			}
			
		}));
		registerHandler(display.getLogoutClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doLogout();
			}
			
		}));
		registerHandler(Window.addWindowClosingHandler(new ClosingHandler() {

			public void onWindowClosing(ClosingEvent event) {
				doLogout();
			}
			
		}));
		registerHandler(eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {

			public void onSessionExpireEvent(SessionExpireEvent event) {
				doLogout();
			}
			
		}));
		
		checkForCookie();
	}
	
	private void doLogout() {
		if (user != null) {
			dispatcher.execute(new LogoutUser(user.getSessionId()), new AsyncCallback<LogoutUserResult>() {

				public void onFailure(Throwable caught) {
					GWT.log("ERROR",caught);
				}

				public void onSuccess(LogoutUserResult result) {
					Cookies.removeCookie("sessionId");

					eventBus.fireEvent(new LogoutEvent(result.getUser()));
				}
			
			});
		}
	}
	

	private void checkForCookie() {
		String sessionId = Cookies.getCookie("sessionId");
		if (sessionId != null) {
			dispatcher.execute(new LoginSession(sessionId), new AsyncCallback<LoginUserResult>() {

				public void onFailure(Throwable caught) {
					// show login and remove cookie
					Cookies.removeCookie("sessionId");
					showLogin();

				}

				public void onSuccess(LoginUserResult result) {
					eventBus.fireEvent(new LoginEvent(result.getUser()));
				}
				
			});
		} else {
			showLogin();
		}
	}
	@Override
	protected void onPlaceRequest(PlaceRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onUnbind() {
		loginPresenter.unbind();
		mainPresenter.unbind();
	}

	public void refreshDisplay() {
		// TODO Auto-generated method stub
		
	}

	public void revealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
