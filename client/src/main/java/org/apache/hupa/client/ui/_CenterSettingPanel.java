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
=======
import java.util.Arrays;
import java.util.List;

<<<<<<< HEAD
import com.google.gwt.cell.client.TextCell;
>>>>>>> make label settings prototype
=======
import com.google.gwt.cell.client.AbstractCell;
>>>>>>> try to rearrange the places and history managment.
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class _CenterSettingPanel extends Composite {

	@UiField SplitLayoutPanel thisPanel;

	@UiField SimpleLayoutPanel settingsTab;

	@UiField SimpleLayoutPanel labelListContainer;
	@UiField SimplePanel labelPropertiesContainer;

	public _CenterSettingPanel() {

		initWidget(binder.createAndBindUi(this));
		settingsTab.setWidget(createTabList());
	}

	private static final List<String> TABS = Arrays.asList("Folders");

	private CellList<String> createTabList() {
		CellList<String> cellList = new CellList<String>(new SpanCell());
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();
				if (selected != null) {
					// Window.alert("You selected: " + selected);
				}
			}
		});
		cellList.setRowCount(TABS.size(), true);

		// Push the data into the widget.
		cellList.setRowData(0, TABS);
		return cellList;
	}

<<<<<<< HEAD
<<<<<<< HEAD
	interface _CeterSettingPanelUiBinder extends UiBinder<DockLayoutPanel, _CenterSettingPanel> {
>>>>>>> attempt to add label setting feature
=======
=======
	static class SpanCell extends AbstractCell<String> {

		public SpanCell() {
		}

		@Override
		public void render(Context context, String value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			sb.appendHtmlConstant("<span style='display: block;color: #376572;text-shadow: 0px 1px 1px #fff;text-decoration: none;cursor: default;padding: 6px 8px 2px 8px;height: 17px;white-space: nowrap;'>");
			sb.appendHtmlConstant(value);
			sb.appendHtmlConstant("</span>");
		}
	}

>>>>>>> try to rearrange the places and history managment.
	interface _CeterSettingPanelUiBinder extends UiBinder<SplitLayoutPanel, _CenterSettingPanel> {
>>>>>>> add layout styles to three part
	}

	private static _CeterSettingPanelUiBinder binder = GWT.create(_CeterSettingPanelUiBinder.class);

<<<<<<< HEAD
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
=======
	public AcceptsOneWidget getLabelListView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				labelListContainer.setWidget(Widget.asWidgetOrNull(w));
>>>>>>> make label settings prototype
			}
		};
	}

<<<<<<< HEAD
<<<<<<< HEAD
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
=======
>>>>>>> make label settings prototype
=======
	public AcceptsOneWidget getLabelPropertiesView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				labelPropertiesContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}
>>>>>>> add rename RF to label setting feature
}
