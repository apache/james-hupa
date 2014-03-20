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

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
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

public class _CenterContactPanel extends Composite {

    @UiField SplitLayoutPanel thisPanel;

    @UiField SimpleLayoutPanel settingsTab;

    @UiField SimpleLayoutPanel labelListContainer;
    @UiField SimplePanel labelPropertiesContainer;

    public _CenterContactPanel() {

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

    interface _CeterSettingPanelUiBinder extends UiBinder<SplitLayoutPanel, _CenterContactPanel> {
    }

    private static _CeterSettingPanelUiBinder binder = GWT.create(_CeterSettingPanelUiBinder.class);

    public AcceptsOneWidget getContactListView() {
        return new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                labelListContainer.setWidget(Widget.asWidgetOrNull(w));
            }
        };
    }

    public AcceptsOneWidget getContactPropertiesView() {
        return new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                labelPropertiesContainer.setWidget(Widget.asWidgetOrNull(w));
            }
        };
    }
}
