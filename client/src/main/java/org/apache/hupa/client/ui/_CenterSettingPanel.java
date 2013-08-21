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
import java.util.Arrays;
import java.util.List;

import org.apache.hupa.client.ui.FolderListView.Resources;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
=======
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
<<<<<<< HEAD
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

=======

public class _CenterSettingPanel extends Composite {
	

	@UiField SplitLayoutPanel thisPanel;

	@UiField SimpleLayoutPanel settingNavContainer;

	@UiField SimpleLayoutPanel labelListContainer;
	@UiField SimplePanel labelPropertiesContainer;
	
	@UiField protected Style style;

	interface Style extends CssResource {
		
	}
	
	public _CenterSettingPanel() {

		initWidget(binder.createAndBindUi(this));
	}

>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
	public interface Resources extends CellList.Resources {

		Resources INSTANCE = GWT.create(Resources.class);

		@Source("res/CssLabelListView.css")
		public CellList.Style cellListStyle();
	}
<<<<<<< HEAD
	private CellList<String> createTabList() {
		CellList<String> cellList = new CellList<String>(new SpanCell(), Resources.INSTANCE);
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

	static class SpanCell extends AbstractCell<String> {

		public SpanCell() {
		}

		@Override
		public void render(Context context, String value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			sb.appendHtmlConstant("<span >");
			sb.appendHtmlConstant(value);
			sb.appendHtmlConstant("</span>");
		}
	}
=======
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f

	interface _CeterSettingPanelUiBinder extends UiBinder<SplitLayoutPanel, _CenterSettingPanel> {
	}

	private static _CeterSettingPanelUiBinder binder = GWT.create(_CeterSettingPanelUiBinder.class);

	public AcceptsOneWidget getLabelListView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				labelListContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	public AcceptsOneWidget getLabelPropertiesView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				labelPropertiesContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}
<<<<<<< HEAD
=======

	public AcceptsOneWidget getSettingNavView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				settingNavContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
}
