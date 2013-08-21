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

package org.apache.hupa.client.ui;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.activity.TopActivity;
import org.apache.hupa.shared.events.ServerStatusEvent.ServerStatus;
import org.apache.hupa.widgets.ui.RndPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

public class TopView extends Composite implements TopActivity.Displayable {
	@UiField HorizontalPanel buttonContainer;
	@UiField HorizontalPanel infoContainer;
	@UiField HorizontalPanel loginInfoPanel;
	private Label loginLabel = new Label();
	@UiField Label loginUserLabel;
	@UiField Anchor mainButton;
	private Anchor contactsButton;
	@UiField Anchor logoutButton;
	@UiField Label appnameLabel;

	private RndPanel flashContainer = new RndPanel();
	private Label flashLabel = new Label();
	@UiField Label extraLabel;

	@Inject
	public TopView(HupaConstants constants) {
		initWidget(binder.createAndBindUi(this));
		mainButton.setText(constants.mailTab());
		contactsButton = new Anchor(constants.contactsTab());
		logoutButton.setText(constants.logoutButton());
		loginLabel.addStyleName(HupaCSS.C_login_info_label);
		logoutButton.addStyleName(HupaCSS.C_menu_button);
		appnameLabel.addStyleName(HupaCSS.C_header);
		flashContainer.addStyleName(HupaCSS.C_flash);
		loginLabel.setText(constants.loginAs() + ": ");
		appnameLabel.setText(constants.productName());
		infoContainer.add(appnameLabel);
		infoContainer.add(extraLabel);
		flashContainer.add(flashLabel);
		showTopNavigation(false);
	}

	@Override
	public HasClickHandlers getLogoutClick() {
		return logoutButton;
	}

	@Override
	public HasClickHandlers getContactsClick() {
		return contactsButton;
	}

	@Override
	public HasClickHandlers getMainClick() {
		return mainButton;
	}

	@Override
	public void showTopNavigation(boolean show) {
		buttonContainer.setVisible(show);
	}

	@Override
	public void showContactsButton() {
		mainButton.setVisible(false);
		contactsButton.setVisible(true);
	}

	@Override
	public void showMainButton() {
		mainButton.setVisible(true);
		contactsButton.setVisible(false);
	}

	@Override
	public HasText getUserText() {
		return loginUserLabel;
	}

	@Override
	public void setServerStatus(ServerStatus status) {
		if (status == ServerStatus.Available)
			showMessage("Server available.", 20000);
		else
			showMessage("Server unavailable", 0);
	}

	@Override
	public void showMessage(String message, int millisecs) {
		flashLabel.setText(message);
		flashContainer.setVisible(true);
		if (millisecs > 0)
			hideMessage.schedule(millisecs);
	}

	private final Timer hideMessage = new Timer() {
		public void run() {
			flashContainer.setVisible(false);
			flashLabel.setText("");
		}
	};

	interface TopViewUiBinder extends UiBinder<FlowPanel, TopView> {
	}

	private static TopViewUiBinder binder = GWT.create(TopViewUiBinder.class);

}
