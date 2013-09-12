<<<<<<< HEAD
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
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.rf.LoginUserRequest;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;

=======
package org.apache.hupa.client.activity;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.mvp.WidgetDisplayable;
import org.apache.hupa.client.place.MailInboxPlace;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;

import com.google.gwt.activity.shared.AbstractActivity;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.core.client.GWT;
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> Change to new mvp framework - first step
=======
import com.google.gwt.core.client.GWT;
>>>>>>> decorate the theme
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
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
import com.google.inject.Provider;

public class LoginActivity extends AbstractActivity {

	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	private DispatchAsync dispatcher;
	private HupaConstants constants = GWT.create(HupaConstants.class);

	@Inject
	public LoginActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			DispatchAsync dispatcher) {
		this.display = display;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.dispatcher = dispatcher;
	}
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		container.setWidget(display.asWidget());
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

	}

	public void bind() {
		display.getLoginClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doLogin();
			}
		});
		display.getResetClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doReset();
			}

		});
		eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {

			public void onSessionExpireEvent(SessionExpireEvent event) {
				eventBus.fireEvent(new FlashEvent(constants.sessionTimedOut(), 4000));
			}

		});

>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
	}

	private void doLogin() {
		String user = display.getUserNameValue().getValue().trim();
		String pass = display.getPasswordValue().getValue().trim();
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

		if (user.isEmpty() || pass.isEmpty())
			return;

		display.setLoading(true);
		dispatcher.execute(new LoginUser(user, pass), new HupaEvoCallback<LoginUserResult>(dispatcher, eventBus,
				display) {
			public void callback(LoginUserResult result) {
				display.setLoading(false);
				// eventBus.fireEvent(new LoginEvent(result.getUser()));
//				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get().with(result.getUser()));
				LoginActivity.this.placeController.goTo(new MailInboxPlace("!").with(result.getUser()));
				doReset();
			}

			public void callbackError(Throwable caught) {
				display.setLoading(false);
<<<<<<< HEAD
				Window.alert("error");
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
				doReset();
			}
		});
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
		public HasClickHandlers getLoginClick();

		public HasClickHandlers getResetClick();

		public HasValue<String> getUserNameValue();

		public HasValue<String> getPasswordValue();

		public Focusable getUserNameFocus();

		public void setLoading(boolean loading);

>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
		public Widget asWidget();
	}
}
