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

import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A ToolTip which is shown a configured time before get destroyed
 *
 *
 */
public class ToolTip extends Label {
    private int y = 0;
    private int x = 0;
    private final PopupPanel popup = new PopupPanel();
    private final Timer showTimer = new Timer() {

        @Override
        public void run() {
            popup.setPopupPosition(y,x);
            popup.show();
        }

    };

    public <T extends Widget & HasMouseOverHandlers & HasMouseOutHandlers & HasMouseMoveHandlers> ToolTip(final T w) {
        w.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(MouseOverEvent event) {
                showTimer.schedule(1000);
            }

        });

        w.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(MouseOutEvent event) {
                showTimer.cancel();
                popup.hide();
            }

        });

        w.addMouseMoveHandler(new MouseMoveHandler() {

            public void onMouseMove(MouseMoveEvent event) {
                y = event.getScreenY();
                x = w.getAbsoluteTop() + w.getOffsetHeight();
            }

        });
        popup.addCloseHandler(new CloseHandler<PopupPanel>() {

            public void onClose(CloseEvent<PopupPanel> event) {
                showTimer.cancel();
            }

        });
        addStyleName("hupa-ToolTip");
        popup.addStyleName("hupa-ToolTip");
        popup.setAnimationEnabled(true);
        popup.setAutoHideEnabled(true);
    }

    public void setText(String text) {
        super.setText(text);
        popup.setWidget(this);
    }


}
