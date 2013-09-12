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
import org.apache.hupa.client.place.SettingPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class _CenterSettingPanel extends Composite {

	public static final int LAYOUT_LABEL = 0x01;
	public static final int LAYOUT_ECS = 0x02;

	@UiField SplitLayoutPanel thisPanel;

	@UiField SimpleLayoutPanel settingNavContainer;

	@UiField SettingLabelPanel settingLabelPanel;
	@UiField SettingEcsPanel settingEcsPanel;

	@UiField LayoutPanel settingBox;

	@UiField protected Style style;

	interface Style extends CssResource {

	}

	public void arrangeLayout(int lyt) {
		showOrHideLabel(lyt);
		showOrHideEcs(lyt);
	}

	private void showOrHideLabel(int lyt) {
		settingBox.setWidgetLeftWidth(settingLabelPanel, 0, Unit.PCT, (lyt & LAYOUT_LABEL) / LAYOUT_LABEL * 100,
				Unit.PCT);
		settingBox.setWidgetTopHeight(settingLabelPanel, 0, Unit.PCT, (lyt & LAYOUT_LABEL) / LAYOUT_LABEL * 100,
				Unit.PCT);
	}

	private void showOrHideEcs(int lyt) {
		settingBox.setWidgetLeftWidth(settingEcsPanel, 0, Unit.PCT, (lyt & LAYOUT_ECS) / LAYOUT_ECS * 100, Unit.PCT);
		settingBox.setWidgetTopHeight(settingEcsPanel, 0, Unit.PCT, (lyt & LAYOUT_ECS) / LAYOUT_ECS * 100, Unit.PCT);
	}

	public _CenterSettingPanel() {

		initWidget(binder.createAndBindUi(this));
	}

	public interface Resources extends CellList.Resources {

		Resources INSTANCE = GWT.create(Resources.class);

		@Source("res/CssLabelListView.css")
		public CellList.Style cellListStyle();
	}

	interface _CeterSettingPanelUiBinder extends UiBinder<SplitLayoutPanel, _CenterSettingPanel> {
=======
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class _CenterSettingPanel extends Composite {
	
	@UiField SplitLayoutPanel thisPanel;

	public _CenterSettingPanel() {
		initWidget(binder.createAndBindUi(this));
	}

<<<<<<< HEAD
	interface _CeterSettingPanelUiBinder extends UiBinder<DockLayoutPanel, _CenterSettingPanel> {
>>>>>>> attempt to add label setting feature
=======
	interface _CeterSettingPanelUiBinder extends UiBinder<SplitLayoutPanel, _CenterSettingPanel> {
>>>>>>> add layout styles to three part
	}

	private static _CeterSettingPanelUiBinder binder = GWT.create(_CeterSettingPanelUiBinder.class);

<<<<<<< HEAD
	public AcceptsOneWidget getLabelListView() {
		return settingLabelPanel.getLabelListView();
	}

	public AcceptsOneWidget getLabelPropertiesView() {
		return settingLabelPanel.getLabelPropertiesView();
	}

	public AcceptsOneWidget getSettingNavView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				settingNavContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	public void swithTo(SettingPlace sp) {
		int lyt;
		if ("ecs".equals(sp.getToken())) {
			lyt = LAYOUT_ECS;
		} else {
			lyt = LAYOUT_LABEL;
		}
		showOrHideEcs(lyt);
		showOrHideLabel(lyt);

	}
=======
>>>>>>> attempt to add label setting feature
}
