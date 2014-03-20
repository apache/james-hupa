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

package org.apache.hupa.client.mock;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;

import java.util.ArrayList;

public class MockWidget implements HasClickHandlers, HasText, HasHTML, Focusable {

    ArrayList<ClickHandler> handlers = new ArrayList<ClickHandler>();
    String text="", html="";

    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        handlers.add(handler);
        return new HandlerRegistration() {
            public void removeHandler() {
                handlers.remove(handler);
            }
        };
    }

    public void fireEvent(GwtEvent<?> event) {
        for(ClickHandler h: handlers) {
            h.onClick(null);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHTML() {
        return html;
    }

    public void setHTML(String html) {
        this.html = html;
    }

    public int getTabIndex() {
        return 0;
    }

    public void setAccessKey(char key) {
    }

    public void setFocus(boolean focused) {
    }

    public void setTabIndex(int index) {
    }
}
