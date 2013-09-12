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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.activity.NotificationActivity;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> fixed issue#18
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.activity.TopBarActivity;
=======
import org.apache.hupa.client.activity.NotificationActivity;
>>>>>>> make a notification timer to be able to schedule the notice with millis time.
=======
import org.apache.hupa.client.activity.TopBarActivity;
>>>>>>> fixed issue#61; add loading to mark, unmark.
import org.apache.hupa.client.mapper.ActivityManagerInitializer;
import org.apache.hupa.client.place.ComposePlace;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.place.ContactPlace;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.HupaPlace;
import org.apache.hupa.client.place.SettingPlace;
=======
import org.apache.hupa.client.mapper.ActivityManagerInitializer;
>>>>>>> use GinFactoryModuleBuilder to inject multiple displayable instances of some activities
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
=======
=======
import java.util.logging.Logger;

>>>>>>> remove some warnings and create the AbstractPlace that can give place infomation
=======
>>>>>>> scrub code
import org.apache.hupa.client.bundles.HupaResources;
=======
>>>>>>> scrub login view code, use css by a unique way 
=======
>>>>>>> could change main panel dynamically currently by clicking the compose button
=======
import org.apache.hupa.client.place.DefaultPlace;
=======
>>>>>>> fixed issue#48, and add the original IdleTimer
import org.apache.hupa.client.place.MailFolderPlace;
<<<<<<< HEAD
>>>>>>> prepare to make composeView's reload work
=======
=======
=======
import org.apache.hupa.client.place.ContactPlace;
>>>>>>> prepared for issue#73, established the UI layout
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.HupaPlace;
>>>>>>> change place management and make refresh folder and message list more gentle
import org.apache.hupa.client.place.SettingPlace;
>>>>>>> attempt to add label setting feature
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

<<<<<<< HEAD
>>>>>>> move new theme ui from experiment to hupa evo
=======
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
>>>>>>> try to rearrange the places and history managment.
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
=======
=======
import com.google.gwt.safehtml.shared.SafeHtml;
>>>>>>> make a notification timer to be able to schedule the notice with millis time.
=======
>>>>>>> make the notification be able to cope with link
import com.google.gwt.user.client.Timer;
>>>>>>> fixed issue#48, and add the original IdleTimer
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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	public HupaController(EventBus eventBus) {
>>>>>>> move new theme ui from experiment to hupa evo
=======
	public HupaController(EventBus eventBus,
<<<<<<< HEAD
			HupaActivityManagerInitializer initializeActivityManagerByGin) {
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
			ActivityManagerInitializer initializeActivityManagerByGin) {
>>>>>>> use GinFactoryModuleBuilder to inject multiple displayable instances of some activities
=======
	public HupaController(PlaceController placeController,
			PlaceHistoryHandler placeHistoryHandler,
			EventBus eventBus, 
=======
	public HupaController(PlaceController placeController, PlaceHistoryHandler placeHistoryHandler, EventBus eventBus,
>>>>>>> fixed issue#48, and add the original IdleTimer
			ActivityManagerInitializer initializeActivityManagerByGin) {
		this.placeController = placeController;
		this.placeHistoryHandler = placeHistoryHandler;
		this.eventBus = eventBus;
>>>>>>> fixed issue#46 and issue#32
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangHandler());
	}

	public void start() {
<<<<<<< HEAD
<<<<<<< HEAD
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
			SettingPlace sp = (SettingPlace)place;
			hupaLayout.arrangeSettingLayout(sp);
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
=======
		StyleInjector.inject(HupaResources.INSTANCE.stylesheet().getText());// TODO
																			// need
																			// this?
		RootLayoutPanel.get().add(hupaLayout.get());
=======
		bindCss();
<<<<<<< HEAD
		checkSession();
>>>>>>> integrate them as a whole one - first: make the default place work
=======
>>>>>>> cope with issue #36, the default place and and the mail folder place, we would always come up with the default place whenever giving a empty string token
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
				//FIXME when gmail mode
				this.placeController.goTo(new FolderPlace("INBOX"));
			}
		} else if (place instanceof ContactPlace) {
			hupaLayout.switchTo(HupaLayout.LAYOUT_CONTACT);
		}  else if (place instanceof SettingPlace) {
			hupaLayout.switchTo(HupaLayout.LAYOUT_SETTING);
		} else if(place instanceof HupaPlace){
			hupaLayout.switchTo(HupaLayout.LAYOUT_MESSAGE);
		}
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> move new theme ui from experiment to hupa evo
=======
=======
	private User user;
	
>>>>>>> prepare to make composeView's reload work
=======
>>>>>>> fixed issue#48, and add the original IdleTimer
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
>>>>>>> integrate them as a whole one - first: make the default place work
}
