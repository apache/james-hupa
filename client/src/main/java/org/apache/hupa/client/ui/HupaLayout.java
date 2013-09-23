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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class HupaLayout implements HupaLayoutable {

	/*
	 * The flag of layout can be defined as any single bit. such as: 0b0001
	 * 0b0010 0b0100, namely, 0x01 0x02 0x04 respectively
	 */
	public static final int LAYOUT_MESSAGE = 0x01;
	public static final int LAYOUT_COMPOSE = 0x02;
	public static final int LAYOUT_CONTACT = 0x04;
	public static final int LAYOUT_SETTING = 0x08;

	@UiField SimplePanel topBarContainer;
	@UiField SimplePanel logoContainer;
	@UiField SimplePanel notificationContainer;
	@UiField SimpleLayoutPanel navigationContainer;
	@UiField _ToolPanel toolPanel;

	@UiField LayoutPanel mainBox;

	@UiField _CenterPanel centerPanel;
	@UiField _CenterComposePanel composePanel;
	@UiField _CenterSettingPanel settingPanel;
	@UiField _CenterContactPanel contactPanel;

	private LayoutPanel hupaMainPanel;

	public HupaLayout() {
		hupaMainPanel = binder.createAndBindUi(this);
	}

	@Override
	public LayoutPanel get() {
		return hupaMainPanel;
	}

	@Override
	public void switchTo(int layout) {
		arrangeLayout(layout);
	}

	@Override
	public AcceptsOneWidget getTopBarView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				topBarContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getLogoView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				logoContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getNotificationView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				notificationContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}
	@Override
	public AcceptsOneWidget getNavigationView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				navigationContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getToolBarView() {
		return toolPanel.getToolBarView();
	}

	@Override
	public AcceptsOneWidget getComposeView() {
		return composePanel.getComposeView();
	}

	@Override
	public AcceptsOneWidget getComposeToolBarView() {
		return toolPanel.getComposeToolBarView();
	}

	@Override
	public AcceptsOneWidget getSearchBoxView() {
		return toolPanel.getSearchBoxView();
	}
	@Override
	public AcceptsOneWidget getFolderListView() {
		return centerPanel.getFolderListView();
	}
	@Override
	public AcceptsOneWidget getContactListView() {
		return composePanel.getContactListView();
	}

	@Override
	public AcceptsOneWidget getMessageListView() {
		return centerPanel.getMessageListView();
	}

	@Override
	public AcceptsOneWidget getMessageListFooterView() {
		return centerPanel.getMessageListFooterView();
	}

	@Override
	public AcceptsOneWidget getMessageContentView() {
		return centerPanel.getMessageContentView();
	}

	@Override
	public AcceptsOneWidget getStatusView() {
		return centerPanel.getStatusView();
	}

	@Override
	public AcceptsOneWidget getLabelListView() {
		return settingPanel.getLabelListView();
	}

	@Override
	public AcceptsOneWidget getSettingNavView() {
		return settingPanel.getSettingNavView();
	}
	
	@Override
	public AcceptsOneWidget getLabelPropertiesView() {
		return settingPanel.getLabelPropertiesView();
	}

	@Override
	public AcceptsOneWidget getContactsListView() {
		return contactPanel.getContactListView();
	}

	@Override
	public AcceptsOneWidget getContactPropertiesView() {
		return contactPanel.getContactPropertiesView();
	}

	private void arrangeLayout(int lyt) {
		toolPanel.toggleTo(lyt);
		showOrHideMessage(lyt);
		showOrHideCompose(lyt);
		showOrHideSetting(lyt);
		showOrHideContact(lyt);
	}

	private void showOrHideContact(int lyt) {
		mainBox.setWidgetLeftWidth(contactPanel, 0, Unit.PCT, (lyt & LAYOUT_CONTACT) / LAYOUT_CONTACT * 100, Unit.PCT);
		mainBox.setWidgetTopHeight(contactPanel, 0, Unit.PCT, (lyt & LAYOUT_CONTACT) / LAYOUT_CONTACT * 100, Unit.PCT);
	}

	private void showOrHideSetting(int lyt) {
		mainBox.setWidgetLeftWidth(settingPanel, 0, Unit.PCT, (lyt & LAYOUT_SETTING) / LAYOUT_SETTING * 100, Unit.PCT);
		mainBox.setWidgetTopHeight(settingPanel, 0, Unit.PCT, (lyt & LAYOUT_SETTING) / LAYOUT_SETTING * 100, Unit.PCT);
	}

	private void showOrHideCompose(int lyt) {
		mainBox.setWidgetLeftWidth(composePanel, 0, Unit.PCT, (lyt & LAYOUT_COMPOSE) / LAYOUT_COMPOSE * 100, Unit.PCT);
		mainBox.setWidgetTopHeight(composePanel, 0, Unit.PCT, (lyt & LAYOUT_COMPOSE) / LAYOUT_COMPOSE * 100, Unit.PCT);
	}

	private void showOrHideMessage(int lyt) {
		mainBox.setWidgetLeftWidth(centerPanel, 0, Unit.PCT, (lyt & LAYOUT_MESSAGE) / LAYOUT_MESSAGE * 100, Unit.PCT);
		mainBox.setWidgetTopHeight(centerPanel, 0, Unit.PCT, (lyt & LAYOUT_MESSAGE) / LAYOUT_MESSAGE * 100, Unit.PCT);
	}

	interface HupaLayoutUiBinder extends UiBinder<LayoutPanel, HupaLayout> {
	}

	private static HupaLayoutUiBinder binder = GWT.create(HupaLayoutUiBinder.class);

}
