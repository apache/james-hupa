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

import org.apache.hupa.client.place.SettingPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.query.client.GQuery;
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

    @UiField public SplitLayoutPanel thisPanel;

    @UiField public SimpleLayoutPanel settingNavContainer;

    @UiField public SettingLabelPanel settingLabelPanel;

    @UiField public LayoutPanel settingBox;

    @UiField public Style style;

    public interface Style extends CssResource {
    }

    public void arrangeLayout(int lyt) {
        settingBox.setWidgetLeftWidth(settingLabelPanel, 0, Unit.PCT, 100, Unit.PCT);
        settingBox.setWidgetTopHeight(settingLabelPanel, 0, Unit.PCT, 100, Unit.PCT);
    }

    interface _CenterSettingPanelUiBinder extends UiBinder<SplitLayoutPanel, _CenterSettingPanel> {
    }

    @SuppressWarnings("rawtypes")
    protected static UiBinder binder;

    @SuppressWarnings("unchecked")
    public _CenterSettingPanel() {
        binder = createBinder();
        initWidget((SplitLayoutPanel)binder.createAndBindUi(this));
    }

    @SuppressWarnings("rawtypes")
    protected UiBinder createBinder() {
        return GWT.create(_CenterSettingPanelUiBinder.class);
    }

    public interface Resources extends CellList.Resources {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("res/CssLabelListView.css")
        public CellList.Style cellListStyle();
    }

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
        GQuery.console.log("_CenterSetting... swithTo " + sp + " " + sp.getToken());
    }
}
