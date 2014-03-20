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

import org.apache.hupa.client.activity.TopBarActivity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class TopBarView extends Composite implements TopBarActivity.Displayable {

    @UiField Anchor about;
    @UiField Anchor logout;
    @UiField HTMLPanel userLabel;
    @UiField SimplePanel loading;
    @UiField HTML loadingRegion;

    @UiField Style style;

    interface Style extends CssResource {
        String hideLoading();
    }

    public TopBarView() {
        initWidget(binder.createAndBindUi(this));
    }

    @UiHandler("about")
    void handleAboutClick(ClickEvent e){
        Window.alert("// TODO show about model view");
    }

    @Override
    public void showLoading(String message){
        loadingRegion.setHTML(message);
        loading.removeStyleName(style.hideLoading());
    }

    @Override
    public void hideLoading(){
        loadingRegion.setHTML("");
        loading.addStyleName(style.hideLoading());
    }

    @Override
    public HasClickHandlers getLogoutClick() {
        return logout;
    }

    @Override
    public HTMLPanel getUserLabel() {
        return userLabel;
    }

    @Override
    public void showUserName(String userName){
        userLabel.clear();
        userLabel.add(new HTML(userName));
    }

    interface TopBarUiBinder extends UiBinder<DockLayoutPanel, TopBarView> {
    }

    private static TopBarUiBinder binder = GWT.create(TopBarUiBinder.class);

}
