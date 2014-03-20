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

package org.apache.hupa.client.activity;

import org.apache.hupa.client.place.SettingPlace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class SettingNavActivity extends AppBaseActivity {

    @Inject protected Displayable display;

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        container.setWidget(display.asWidget());
        itemChangeByPlace();
        bindTo(eventBus);
    }

    protected void itemChangeByPlace() {
        display.singleSelect(1);
    }

    protected void bindTo(EventBus eventBus) {
        registerHandler(display.getLabelsAchor().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                display.singleSelect(1);
                pc.goTo(new SettingPlace("labels"));
            }
        }));
    }

    public interface Displayable extends IsWidget {
        HasClickHandlers getLabelsAchor();
        void singleSelect(int i);
    }
}
