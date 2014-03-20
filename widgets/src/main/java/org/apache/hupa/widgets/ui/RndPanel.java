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

package org.apache.hupa.widgets.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import org.apache.hupa.widgets.ui.impl.RndPanelGenerator;

/**
 * Widget which renders a rounded panel.
 *
 * This is here because IE doesn't support rounded borders in css,
 * so it is needed to wrap the container with additional html elements.
 *
 * For other browsers this class just produces a class-named FlowPanel.
 *
 */
public class RndPanel extends Composite {

    private static final RndPanelGenerator impl = GWT.create(RndPanelGenerator.class);

    private FlowPanel panel = new FlowPanel();

    public RndPanel() {
        panel = impl.createPanel();
        initWidget(impl.roundPanel(panel));
    }

    public void add(Widget child) {
        panel.add(child);
    }

    public void insert(Widget w, int beforeIndex) {
        panel.insert(w, beforeIndex);
    }

    public void clear() {
        panel.clear();
    }

    public boolean remove(Widget w) {
        return panel.remove(w);
    }

    public void setWidget(Widget w) {
        panel.clear();
        panel.add(w);
    }

    @Override
    public void addStyleName(String style) {
        getWidget().addStyleName(style);
    }

}
