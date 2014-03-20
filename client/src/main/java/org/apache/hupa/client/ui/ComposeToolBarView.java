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

import org.apache.hupa.client.activity.ComposeToolBarActivity;
import org.apache.hupa.shared.events.SendClickEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ComposeToolBarView extends Composite implements ComposeToolBarActivity.Displayable {

    @Inject protected EventBus eventBus;

    @UiField public Anchor send;
    @UiField public Anchor cancel;

    @UiHandler("cancel")
    public void handleClickCancel(ClickEvent e) {
        History.back();
    }

    @UiHandler("send")
    public void handleClickSend(ClickEvent e) {
       eventBus.fireEvent(new SendClickEvent());
    }

    @Override
    public HasClickHandlers getSendClick() {
        return send;
    }

    interface ComposeToolBarUiBinder extends UiBinder<HorizontalPanel, ComposeToolBarView> {
    }

    @SuppressWarnings("rawtypes")
    private static UiBinder binder;

    @SuppressWarnings("rawtypes")
    protected UiBinder initBinder() {
      return  GWT.create(ComposeToolBarUiBinder.class);
    }

    @SuppressWarnings("unchecked")
    public ComposeToolBarView() {
        binder = initBinder();
        initWidget((Widget)binder.createAndBindUi(this));
    }

}
