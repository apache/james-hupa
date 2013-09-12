<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
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

<<<<<<< HEAD
package org.apache.hupa.client.activity;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.rf.LoginUserRequest;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;

=======
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.client.activity;

<<<<<<< HEAD
import net.customware.gwt.dispatch.client.DispatchAsync;

<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.HupaConstants;
<<<<<<< HEAD
import org.apache.hupa.client.evo.HupaEvoCallback;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
=======
import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.place.LoginPlace;
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.mvp.WidgetDisplayable;
import org.apache.hupa.client.place.MailInboxPlace;
>>>>>>> Change to new mvp framework - first step
=======
=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.LoginUserRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
<<<<<<< HEAD
>>>>>>> decorate the theme
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;
=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

import com.google.gwt.activity.shared.AbstractActivity;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.core.client.GWT;
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> Change to new mvp framework - first step
=======
import com.google.gwt.core.client.GWT;
>>>>>>> decorate the theme
=======
import com.google.gwt.core.client.GWT;
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> Change to new mvp framework - first step
=======
import com.google.gwt.core.client.GWT;
>>>>>>> decorate the theme
=======
>>>>>>> fix issue 2&3. 	Handle exceptions thrown in async blocks & Simply injection code
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.google.gwt.place.shared.PlaceController;
<<<<<<< HEAD
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
import com.google.gwt.user.client.Window;
=======
>>>>>>> introduce the top activity
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
<<<<<<< HEAD
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LoginActivity extends AppBaseActivity {
	

	@Inject private HupaLayoutable hupaLayout;
=======
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginActivity extends AbstractActivity {

<<<<<<< HEAD
	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	private DispatchAsync dispatcher;
	private HupaConstants constants = GWT.create(HupaConstants.class);

	@Inject
	public LoginActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			DispatchAsync dispatcher) {
=======
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LoginActivity extends AbstractActivity {

	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	private DispatchAsync dispatcher;
	private HupaConstants constants = GWT.create(HupaConstants.class);

	@Inject
<<<<<<< HEAD
	public LoginActivity(Display display, EventBus eventBus, PlaceController placeController, DispatchAsync dispatcher,
			Provider<LoginPlace> newGoToPlaceProvider) {
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
	public LoginActivity(Displayable display, EventBus eventBus, PlaceController placeController,
<<<<<<< HEAD
			Provider<MailInboxPlace> mailInboxPlaceProvider, DispatchAsync dispatcher) {
>>>>>>> Change to new mvp framework - first step
=======
			DispatchAsync dispatcher) {
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
		this.display = display;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.dispatcher = dispatcher;
<<<<<<< HEAD
<<<<<<< HEAD
	}
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
		this.newGoToPlaceProvider = newGoToPlaceProvider;
		// this.loginRpcService = loginRpcService;
	}

	public void init(LoginPlace place) {
		this.place = place;
=======
>>>>>>> Change to new mvp framework - first step
	}
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	@Inject private HupaConstants constants;
<<<<<<< HEAD
>>>>>>> fix issue 2&3. 	Handle exceptions thrown in async blocks & Simply injection code
=======
	@Inject private HupaRequestFactory requestFactory;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		container.setWidget(display.asWidget());
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		display.setLoading(false);
	}

	public void bind() {
		registerHandler(display.getLoginClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doLogin();// FIXME double run if press "ENTER" key in the login
						  // page
			}
		}));
		eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {
			public void onSessionExpireEvent(SessionExpireEvent event) {
				eventBus.fireEvent(new FlashEvent(constants.sessionTimedOut(), 4000));
			}
		});
=======
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
		display.setLoading(false);
>>>>>>> fix bugs, including 1)folders appending on west panel; 2)unread email folder's been frozen exception; 3)back, logout, ...buttons wired behavior.

	}

	public void bind() {
		display.getLoginClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doLogin();// FIXME double run if press "ENTER" key in the login page
			}
		});
<<<<<<< HEAD
<<<<<<< HEAD
		display.getResetClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doReset();
			}

		});
		eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {

			public void onSessionExpireEvent(SessionExpireEvent event) {
				eventBus.fireEvent(new FlashEvent(constants.sessionTimedOut(), 4000));
			}
=======
		display.getResetClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doReset();
			}

		});
		eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {

<<<<<<< HEAD
            public void onSessionExpireEvent(SessionExpireEvent event) {
                eventBus.fireEvent(new FlashEvent(constants.sessionTimedOut(), 4000));
            }
            
        });
>>>>>>> decorate the theme
=======
			public void onSessionExpireEvent(SessionExpireEvent event) {
				eventBus.fireEvent(new FlashEvent(constants.sessionTimedOut(), 4000));
			}
>>>>>>> introduce the top activity

		});

>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======

>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
	}

	private void doLogin() {
		String user = display.getUserNameValue().getValue().trim();
		String pass = display.getPasswordValue().getValue().trim();
<<<<<<< HEAD
<<<<<<< HEAD
		if (user.isEmpty() || pass.isEmpty())
			return;
		display.setLoading(true);
		LoginUserRequest loginRequest = rf.loginRequest();
		loginRequest.login(user, pass).fire(new Receiver<User>() {
			@Override
			public void onSuccess(User response) {
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(hupaLayout.get());
				pc.goTo(new FolderPlace(response.getSettings().getInboxFolderName()));
				eventBus.fireEvent(new LoginEvent(response));
				display.setLoading(false);
			}
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());// TODO a more gentle way
				display.setLoading(false);
=======
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter

		if (user.isEmpty() || pass.isEmpty())
			return;

		display.setLoading(true);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
//		HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
//		rf.initialize(eventBus);
//		UserRequest userRequest = rf.userRequest();
//		UserProxy user1 = userRequest.create(UserProxy.class);
//		user1.setName(user);
//		user1.setPassword(pass);
//		userRequest.save(user1).fire(new Receiver<UserProxy>(){
//
//			@Override
//			public void onSuccess(UserProxy user) {
//				display.setLoading(false);
//				LoginActivity.this.placeController.goTo(new MailFolderPlace().with(null));
//				doReset();
//			}
//			
//		});
		
		
		
<<<<<<< HEAD
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
		dispatcher.execute(new LoginUser(user, pass), new HupaEvoCallback<LoginUserResult>(dispatcher, eventBus,
				display) {
			public void callback(LoginUserResult result) {
				display.setLoading(false);
<<<<<<< HEAD
<<<<<<< HEAD
				// eventBus.fireEvent(new LoginEvent(result.getUser()));
//				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get().with(result.getUser()));
				LoginActivity.this.placeController.goTo(new MailFolderPlace().with(result.getUser()));
<<<<<<< HEAD
=======
				Window.alert("success");
				// eventBus.fireEvent(new LoginEvent(result.getUser()));
//				LoginActivity.this.placeController.goTo(newGoToPlaceProvider.get());
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
				// eventBus.fireEvent(new LoginEvent(result.getUser()));
<<<<<<< HEAD
				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get().with(result.getUser()));
>>>>>>> Change to new mvp framework - first step
=======
//				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get().with(result.getUser()));
				LoginActivity.this.placeController.goTo(new MailInboxPlace("!").with(result.getUser()));
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
=======
>>>>>>> 1. improve the inbox folder place.
				doReset();
			}

			public void callbackError(Throwable caught) {
				display.setLoading(false);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
				Window.alert("error");
<<<<<<< HEAD
<<<<<<< HEAD
				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get());
				// eventBus.fireEvent(new FlashEvent(constants.loginInvalid(),
				// 4000));
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
				// eventBus.fireEvent(new FlashEvent(constants.loginInvalid(),4000));
>>>>>>> decorate the theme
=======
				eventBus.fireEvent(new FlashEvent(constants.loginInvalid(), 4000));
>>>>>>> introduce the top activity
=======
				// eventBus.fireEvent(new FlashEvent(constants.loginInvalid(),4000));
>>>>>>> decorate the theme
=======
				eventBus.fireEvent(new FlashEvent(constants.loginInvalid(), 4000));
>>>>>>> introduce the top activity
				doReset();
			}
		});
		
<<<<<<< HEAD
=======
				Window.alert("failure");
//				LoginActivity.this.placeController.goTo(newGoToPlaceProvider.get());
=======
				Window.alert("error");
				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get());
>>>>>>> Change to new mvp framework - first step
				// eventBus.fireEvent(new FlashEvent(constants.loginInvalid(),
				// 4000));
				doReset();
			}
		});
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
=======

		LoginUserRequest loginRequest = requestFactory.loginRequest();
		loginRequest.login(user, pass).fire(new Receiver<User>() {
			@Override
			public void onSuccess(User response) {
				placeController.goTo(new MailFolderPlace().with(response, useDefaultInboxFolder(response), null));
                eventBus.fireEvent(new LoginEvent(response));
			}
			@Override
			public void onFailure(ServerFailure error){
				placeController.goTo(new DefaultPlace());
			}
		});

>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
	}
	
	private ImapFolder useDefaultInboxFolder(User user){
		return new ImapFolderImpl(user.getSettings().getInboxFolderName());
	}

	/**
	 * Reset display
	 */
	private void doReset() {
		display.getUserNameValue().setValue("");
		display.getPasswordValue().setValue("");
		display.getUserNameFocus().setFocus(true);
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	@Inject private Displayable display;
	@Inject private HupaConstants constants;

	public interface Displayable extends IsWidget {
		public HasClickHandlers getLoginClick();
		public HasValue<String> getUserNameValue();
		public HasValue<String> getPasswordValue();
		public Focusable getUserNameFocus();
		public void setLoading(boolean loading);
=======
	public interface Display {
=======
	public interface Displayable extends WidgetDisplayable {
>>>>>>> Change to new mvp framework - first step
=======
	public interface Display {
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
	public interface Displayable extends WidgetDisplayable {
>>>>>>> Change to new mvp framework - first step
		public HasClickHandlers getLoginClick();

		public HasClickHandlers getResetClick();

		public HasValue<String> getUserNameValue();

		public HasValue<String> getPasswordValue();

		public Focusable getUserNameFocus();

		public void setLoading(boolean loading);

<<<<<<< HEAD
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
		public Widget asWidget();
	}
}
