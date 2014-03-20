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

package org.apache.hupa.client.widgets;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.widgets.ui.Loading;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.ButtonBar;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel with three parts,
 * left has a button bar
 * right has a bunch of links
 * center has a loading widget
 */
public class CommandsBar extends Composite {

    Grid buttonPanel = new Grid(1, 3);
    ButtonBar buttonBarLeft = new ButtonBar();
    ButtonBar buttonBarRight = new ButtonBar();

    public CommandsBar() {

        buttonPanel.addStyleName(HupaCSS.C_commands_bar);
        buttonBarLeft.addStyleName(HupaCSS.C_buttons);
        buttonBarRight.addStyleName(HupaCSS.C_buttons);

        buttonPanel.setWidget(0, 0, buttonBarLeft);
        buttonPanel.setWidget(0, 2, buttonBarRight);

        buttonPanel.getCellFormatter().setWidth(0, 1, "100%");
        buttonPanel.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
        buttonPanel.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);

        initWidget(buttonPanel);
    }

    public void add(Widget w) {
        if (w instanceof Button) {
            addLeft(w);
        } else if ( w instanceof Loading) {
            buttonPanel.setWidget(0, 1, w);
        } else {
            addRight(w);
        }
    }

    public void addLeft(Widget w) {
        buttonBarLeft.add(w);
    }

    public void addRight(Widget w) {
        buttonBarRight.add(w);
    }

    public void clear() {
        buttonBarLeft.clear();
        buttonBarRight.clear();
        buttonPanel.setText(0, 1, "");
    }

    public boolean remove(Widget w) {
        return false;
    }



}
