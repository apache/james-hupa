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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class _CenterComposePanel extends Composite {

    @UiField protected SplitLayoutPanel thisPanel;
    @UiField protected __OutlineComposePanel outlinePanel;
    @UiField protected SimpleLayoutPanel composeContainer;

    public _CenterComposePanel() {
        initWidget(binder.createAndBindUi(this));
//        thisPanel.setWidgetHidden(outlinePanel, true);
    }

    public AcceptsOneWidget getAddressListView() {
        return outlinePanel.getAddressListView();
    }


    interface _CenterPanelUiBinder extends
            UiBinder<SplitLayoutPanel, _CenterComposePanel> {
    }

    private static _CenterPanelUiBinder binder = GWT
            .create(_CenterPanelUiBinder.class);


    public AcceptsOneWidget getComposeView() {
        return new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                composeContainer.setWidget(Widget.asWidgetOrNull(w));
            }
        };
    }

}
