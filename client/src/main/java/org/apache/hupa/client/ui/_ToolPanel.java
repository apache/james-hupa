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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class _ToolPanel extends Composite {

    @UiField protected DockLayoutPanel thisPanel;
    @UiField protected SimplePanel toolBarContainer;
    @UiField protected SimplePanel composeToolBarContainer;
    @UiField protected SimplePanel searchBoxContainer;

    public _ToolPanel() {
        initWidget(binder.createAndBindUi(this));
    }

    public void toggleTo(int layout) {
        switch (layout) {
        case HupaLayout.LAYOUT_MESSAGE:
            this.toggleToCompose(false);break;
        case HupaLayout.LAYOUT_COMPOSE:
            this.toggleToCompose(true);break;
            //TODO compose button should be shown when setting
        default:
            hideAll();
        }
    }

    private void hideAll() {
        thisPanel.setWidgetHidden(toolBarContainer, true);
        thisPanel.setWidgetHidden(searchBoxContainer, true);
        thisPanel.setWidgetHidden(composeToolBarContainer, true);
    }

    protected void toggleToCompose(boolean visible) {
        thisPanel.setWidgetHidden(toolBarContainer, visible);
        thisPanel.setWidgetHidden(searchBoxContainer, visible);
        thisPanel.setWidgetHidden(composeToolBarContainer, !visible);
    }

    public AcceptsOneWidget getToolBarView() {
        return new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                toolBarContainer.setWidget(Widget.asWidgetOrNull(w));
            }
        };
    }

    public AcceptsOneWidget getComposeToolBarView() {
        return new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                composeToolBarContainer.setWidget(Widget.asWidgetOrNull(w));
            }
        };
    }

    public AcceptsOneWidget getSearchBoxView() {
        return new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                searchBoxContainer.setWidget(Widget.asWidgetOrNull(w));
            }
        };
    }

    interface _ToolPanelUiBinder extends UiBinder<DockLayoutPanel, _ToolPanel> {
    }

    private static _ToolPanelUiBinder binder = GWT.create(_ToolPanelUiBinder.class);

}
