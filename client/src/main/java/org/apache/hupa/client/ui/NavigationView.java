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

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.NavigationActivity;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.SettingPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class NavigationView extends Composite implements NavigationActivity.Displayable {

    @Inject PlaceController placeController;
    @UiField Anchor mail;
    @UiField Anchor setting;
    @UiField SimplePanel mailOuter;
    @UiField SimplePanel settingOuter;

    @UiField Style style;

    interface Style extends CssResource {
        String selected();
        String settingsInnerSelected();
        String mailInnerSelected();
        String contactInnerSelected();
    }

    public NavigationView() {
        initWidget(binder.createAndBindUi(this));
    }

    @UiHandler("mail")
    public void onMailClick(ClickEvent e) {
        select(1);
        placeController.goTo(new FolderPlace(HupaController.user.getSettings().getInboxFolderName()));
    }

    @UiHandler("setting")
    public void onSettingClick(ClickEvent e) {
        select(2);
        placeController.goTo(new SettingPlace("labels"));
    }

    interface NavigationUiBinder extends UiBinder<DockLayoutPanel, NavigationView> {
    }

    private static NavigationUiBinder binder = GWT.create(NavigationUiBinder.class);

    @Override
    public void select(int idx) {
        switch (idx) {
        case 2:
            mailOuter.removeStyleName(style.selected());
            mail.removeStyleName(style.mailInnerSelected());
            settingOuter.addStyleName(style.selected());
            setting.addStyleName(style.settingsInnerSelected());
            break;
        default:
            mailOuter.addStyleName(style.selected());
            mail.addStyleName(style.mailInnerSelected());
            settingOuter.removeStyleName(style.selected());
            setting.removeStyleName(style.settingsInnerSelected());
            break;
        }
    }

}
