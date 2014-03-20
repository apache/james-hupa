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

import org.apache.hupa.client.activity.SettingNavActivity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class SettingNavView extends Composite implements SettingNavActivity.Displayable {

    @UiField public Element navLabelsItem;
    @UiField public Anchor labelsAnchor;

    @SuppressWarnings("rawtypes")
    protected UiBinder getBinder() {
        return GWT.create(Binder.class);
    }

    @SuppressWarnings("unchecked")
    public SettingNavView() {
        initWidget((Widget)getBinder().createAndBindUi(this));
    }

    interface Binder extends UiBinder<HTMLPanel, SettingNavView> {
    }

    @Override
    public HasClickHandlers getLabelsAchor() {
        return labelsAnchor;
    }

    @Override
    public void singleSelect(int i) {
        selectNavLabelItem();
    }

    protected void selectNavLabelItem() {
        String labelClass = navLabelsItem.getAttribute("class");
        navLabelsItem.setAttribute("class", labelClass + " selected");
    }
}
