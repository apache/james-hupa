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
import com.google.gwt.query.client.GQuery;
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

    @UiField public SimplePanel topBarContainer;
    @UiField public SimplePanel logoContainer;
    @UiField public SimplePanel notificationContainer;
    @UiField public SimpleLayoutPanel navigationContainer;
    @UiField public _ToolPanel toolPanel;

    @UiField public LayoutPanel mainBox;

    @UiField public _CenterPanel centerPanel;
    @UiField public _CenterComposePanel composePanel;
    @UiField public _CenterSettingPanel settingPanel;
    @UiField public _CenterContactPanel contactPanel;

    protected LayoutPanel hupaMainPanel;

    interface HupaLayoutUiBinder extends UiBinder<LayoutPanel, HupaLayout> {
    }

    @SuppressWarnings("rawtypes")
    protected static UiBinder binder;

    @SuppressWarnings("unchecked")
    public HupaLayout() {
        initBinder();
       hupaMainPanel = (LayoutPanel) binder.createAndBindUi(this);
    }

    protected void initBinder() {
       binder = GWT.create(HupaLayoutUiBinder.class);
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
    public AcceptsOneWidget getAddressListView() {
        return composePanel.getAddressListView();
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
        GQuery.console.log("HupaLayout arrangeLayout " + lyt);
        toolPanel.toggleTo(lyt);
        mainBox.setWidgetVisible(contactPanel, lyt == LAYOUT_CONTACT);
        mainBox.setWidgetVisible(settingPanel, lyt == LAYOUT_SETTING);
        mainBox.setWidgetVisible(composePanel, lyt == LAYOUT_COMPOSE);
        mainBox.setWidgetVisible(centerPanel, lyt == LAYOUT_MESSAGE);
    }

    @Override
    public void arrangeSettingLayout(SettingPlace sp) {
        GQuery.console.log("HupaLayout arrangeSettingLayout " + sp);
        settingPanel.swithTo(sp);
    }

}
