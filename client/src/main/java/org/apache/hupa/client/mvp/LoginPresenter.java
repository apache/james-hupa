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
import net.customware.gwt.presenter.client.DisplayCallback;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;

public class LoginPresenter extends WidgetPresenter<LoginPresenter.Display>{
    private HupaConstants constants = GWT.create(HupaConstants.class);

	public interface Display extends WidgetDisplay{
		public HasClickHandlers getLoginClick();
		public HasClickHandlers getResetClick();
		public HasValue<String> getUserNameValue();
		public HasValue<String> getPasswordValue();
		public Focusable getUserNameFocus();
		public HasText getErrorText();
	}
	
	private DispatchAsync dispatcher;
	public static final Place PLACE = new Place("Login");

	@Inject
	public LoginPresenter(LoginPresenter.Display display,EventBus bus,DispatchAsync dispatcher) {
		super(display,bus);
		this.dispatcher = dispatcher;
	}

	
	/**
	 * Try to login the user
	 */
	private void doLogin() {
		dispatcher.execute(new LoginUser(display.getUserNameValue().getValue(),display.getPasswordValue().getValue()), new DisplayCallback<LoginUserResult>(display) {


			@Override
			protected void handleFailure(Throwable e) {
				doReset();
				display.getErrorText().setText(constants.loginInvalid());
			}

			@Override
			protected void handleSuccess(LoginUserResult result) {
				eventBus.fireEvent(new LoginEvent(result.getUser()));
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
		display.getErrorText().setText("");
		display.getUserNameFocus().setFocus(true);
	}

	@Override
	public Place getPlace() {
		return PLACE;
	}

	@Override
	protected void onBind() {
		registerHandler(display.getLoginClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doLogin();
			}
			
		}));
		registerHandler(display.getResetClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doReset();
			}
			
		}));
		
		registerHandler(eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {

			public void onSessionExpireEvent(SessionExpireEvent event) {
				display.getErrorText().setText(constants.sessionTimedOut());
			}
			
		}));
	}

	@Override
	protected void onPlaceRequest(PlaceRequest request) {
		String username = request.getParameter("username",null);
		if (username != null) {
			display.getUserNameValue().setValue(username);
		}
	}

	@Override
	protected void onUnbind() {
		// TODO Auto-generated method stub
		
	}

	public void refreshDisplay() {
		// TODO Auto-generated method stub
	
	}

	public void revealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
