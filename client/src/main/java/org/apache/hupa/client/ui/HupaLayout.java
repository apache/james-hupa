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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.place.SettingPlace;
=======
import org.apache.hupa.client.evo.ActivityManagerInitializer;
>>>>>>> try to fix some issues by reorganize the activity mapper and place controller

=======
>>>>>>> add loading and notification bar(finishing the folder list click event), related to the issue#18
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
<<<<<<< HEAD
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
	@Override
	public void arrangeSettingLayout(SettingPlace sp) {
		settingPanel.swithTo(sp);
	}

	interface HupaLayoutUiBinder extends UiBinder<LayoutPanel, HupaLayout> {
	}

	private static HupaLayoutUiBinder binder = GWT.create(HupaLayoutUiBinder.class);


=======
=======
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.uibinder.client.UiField;
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
>>>>>>> integrate them as a whole one - first: make the default place work
=======
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
>>>>>>> integrate all of the views to their corresponding activities and mappers
import com.google.gwt.user.client.ui.LayoutPanel;
=======
>>>>>>> deal with onResizeEvent of folder list panel, but found issue #25
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HupaLayout implements HupaLayoutable {

	@UiField SimplePanel topBarContainer;

	@UiField SimplePanel logoContainer;
	@UiField SimplePanel notificationContainer;
	@UiField SimpleLayoutPanel navigationContainer;

	@UiField _ToolPanel toolPanel;

	@UiField _CenterPanel centerPanel;
	@UiField DockLayoutPanel mainBox;
	
	private _CenterSettingPanel settingPanel;

	private LayoutPanel hupaMainPanel;

	@Inject
	public HupaLayout(_CenterSettingPanel settingPanel) {
		this.settingPanel = settingPanel;
		hupaMainPanel = binder.createAndBindUi(this);
	}

	@Override
	public LayoutPanel get() {
		return hupaMainPanel;
	}

	@Override
	public void switchToCompose() {
		if (isMessageOccupied()) {
			changeToCompose();
		}
	}

	@Override
	public void switchToMessage() {
		if (isMessageOccupied())
			return;
		if (isComposeOccupied()) {
			changeToMessage();
		}
	}

	private void changeToCompose() {
		centerPanel.thisPanel.remove(centerPanel.contentPanel);
		centerPanel.thisPanel.add(centerPanel.composeContainer);
		toolPanel.toggleToCompose(true);
		centerPanel.temporarilyHiddenTheUnimplementedContactPanel(true);
	}

	private boolean isMessageOccupied() {
		return centerPanel.thisPanel.getWidgetIndex(centerPanel.contentPanel) >= 0;
	}

	private void changeToMessage() {
		centerPanel.thisPanel.remove(centerPanel.composeContainer);
		centerPanel.thisPanel.add(centerPanel.contentPanel);
		toolPanel.toggleToCompose(false);
		centerPanel.temporarilyHiddenTheUnimplementedContactPanel(false);
	}

	private boolean isComposeOccupied() {
		return centerPanel.thisPanel.getWidgetIndex(centerPanel.composeContainer) >= 0;
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
		return centerPanel.getComposeView();
	}
	

	@Override
	public AcceptsOneWidget getComposeToolBarView() {
		return toolPanel.getComposeToolBarView();
	}

	@Override
	public AcceptsOneWidget getFolderListView() {
		return centerPanel.getFolderListView();
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

<<<<<<< HEAD
	@Override
	public AcceptsOneWidget getComposeHeader() {
		return centerPanel.getComposeHeader();
	}

<<<<<<< HEAD
<<<<<<< HEAD
public interface HupaLayout {
	LayoutPanel get();
>>>>>>> move new theme ui from experiment to hupa evo
=======
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
	@Override
	public AcceptsOneWidget getComposeContent() {
		return centerPanel.getComposeContent();
	}

	@Override
<<<<<<< HEAD
	public void switchToMessage() {
		if (isMessageOccupied())
			return;
		if (isComposeOccupied()) {
			changeToMessage();
		}
	}

<<<<<<< HEAD
>>>>>>> could change main panel dynamically currently by clicking the compose button
=======
	private void changeToMessage() {
		centerPanel.thisPanel.remove(centerPanel.composePanel);
		centerPanel.thisPanel.add(centerPanel.contentPanel);
		centerPanel.temporarilyHiddenTheUnimplementedContactPanel(false);
=======
	public AcceptsOneWidget getComposeStatus() {
		return centerPanel.getComposeStatus();
>>>>>>> toggle to display/hide the tool bar view to adjust the compose and message panel
	}

=======
>>>>>>> make send text mail work excellently
	interface HupaLayoutUiBinder extends UiBinder<LayoutPanel, HupaLayout> {
	}

<<<<<<< HEAD
>>>>>>> scrub code
=======
	private static HupaLayoutUiBinder binder = GWT
			.create(HupaLayoutUiBinder.class);
<<<<<<< HEAD
>>>>>>> toggle to display/hide the tool bar view to adjust the compose and message panel
=======

	@Override
	public void switchToSetting() {
		centerPanel.removeFromParent();
		mainBox.add(settingPanel);
	}
>>>>>>> attempt to add label setting feature
}
