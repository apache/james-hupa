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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LoginActivity extends AppBaseActivity {
	

	@Inject private HupaLayoutable hupaLayout;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		container.setWidget(display.asWidget());
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
	}

	private void doLogin() {
		String user = display.getUserNameValue().getValue().trim();
		String pass = display.getPasswordValue().getValue().trim();
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

	@Inject private Displayable display;
	@Inject private HupaConstants constants;

	public interface Displayable extends IsWidget {
		public HasClickHandlers getLoginClick();
		public HasValue<String> getUserNameValue();
		public HasValue<String> getPasswordValue();
		public Focusable getUserNameFocus();
		public void setLoading(boolean loading);
		public Widget asWidget();
	}
}
