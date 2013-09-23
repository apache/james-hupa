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
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.activity.TopBarActivity;
import org.apache.hupa.client.mapper.ActivityManagerInitializer;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.place.ContactPlace;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.HupaPlace;
import org.apache.hupa.client.place.SettingPlace;
import org.apache.hupa.client.rf.CheckSessionRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.IdleRequest;
import org.apache.hupa.client.ui.HupaLayout;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.client.ui.LoginLayoutable;
import org.apache.hupa.client.ui.LoginView;
import org.apache.hupa.shared.domain.IdleAction;
import org.apache.hupa.shared.domain.IdleResult;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LoginEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class HupaController {

	private static final int IDLE_INTERVAL = 15000;
	private PlaceController placeController;
	private PlaceHistoryHandler placeHistoryHandler;
	@Inject private HupaLayoutable hupaLayout;
	@Inject private HupaRequestFactory requestFactory;
	@Inject private LoginLayoutable loginLayout;
	@Inject private NotificationActivity.Displayable noticeRegion;
	@Inject private TopBarActivity.Displayable topBar;
	@Inject private ToolBarActivity.Displayable toolBar;
	private EventBus eventBus;

	private Timer noopTimer = new IdleTimer();

	@Inject
	public HupaController(PlaceController placeController, PlaceHistoryHandler placeHistoryHandler, EventBus eventBus,
			ActivityManagerInitializer initializeActivityManagerByGin) {
		this.placeController = placeController;
		this.placeHistoryHandler = placeHistoryHandler;
		this.eventBus = eventBus;
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangHandler());
	}

	public void start() {
		bindCss();
		placeHistoryHandler.handleCurrentHistory();
	}

	private void bindCss() {
		// TODO:replace with a more gentle approach
		StyleInjector.inject(LoginView.Resources.INSTANCE.stylesheet().getText());
	}

	private final class PlaceChangHandler implements PlaceChangeEvent.Handler {
		@Override
		public void onPlaceChange(PlaceChangeEvent event) {
			checkSession();
			adjustLayout(event);
		}
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
		} else if(place instanceof HupaPlace){
			hupaLayout.switchTo(HupaLayout.LAYOUT_MESSAGE);
		}
	}

	private void checkSession() {
		CheckSessionRequest checkSession = requestFactory.sessionRequest();
		checkSession.getUser().fire(new Receiver<User>() {
			@Override
			public void onSuccess(User user) {
				if (user == null) {
					RootLayoutPanel.get().clear();
					RootLayoutPanel.get().add(loginLayout.get());
					noopTimer.cancel();
				} else {
					RootLayoutPanel.get().clear();
					RootLayoutPanel.get().add(hupaLayout.get());
					eventBus.fireEvent(new LoginEvent(user));
					noopTimer.scheduleRepeating(IDLE_INTERVAL);
				}
			}

			@Override
			public void onFailure(ServerFailure error) {
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(loginLayout.get());
				noopTimer.cancel();
			}
		});
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

	private class IdleTimer extends Timer {
		boolean running = false;

		public void run() {
			if (!running) {
				running = true;
				IdleRequest idle = requestFactory.idleRequest();
				IdleAction action = idle.create(IdleAction.class);
				idle.idle(action).fire(new Receiver<IdleResult>() {
					@Override
					public void onSuccess(IdleResult response) {
						running = false;
						// check if the server is not supporting the Idle
						// command. if so cancel this Timer
						if (response.isSupported() == false) {
							IdleTimer.this.cancel();
						}
						// Noop
						// TODO: put code here to read new events from server
						// (new messages ...)
					}
				});
			}
		}
	}
}
