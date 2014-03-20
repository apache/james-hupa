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


import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import org.apache.hupa.widgets.WidgetsCSS;

/**
 * Hyperlink which can get enabled/disabled.
 *
 * CSS rules:
 * <pre>
    .hupa-hyperlink .gwt-Hyperlink {
        color: #0d0eb0;
        text-decoration: underline;
        cursor: default;
    }
    .hupa-hyperlink .gwt-Hyperlink-disabled {
        color: #8d8d8d;
    }
 * </pre>
 */
public class EnableHyperlink extends Composite implements HasClickHandlers,HasHTML,HasText, HasEnable{

    private SimplePanel panel = new SimplePanel();
    private Widget link;
    private HTML html;

    public EnableHyperlink(String text) {
        this(text, false, null);
    }

    public EnableHyperlink(String text, String historyToken) {
        this(text, false, historyToken);
    }

    public EnableHyperlink(String text, boolean asHTML, String historyToken) {

        link = historyToken != null ? new Hyperlink(text, asHTML, historyToken) : new Anchor(text);
        html = new HTML();

        panel.setStyleName(WidgetsCSS.C_hyperlink);
        html.setStyleName(link.getStyleName());
        html.addStyleDependentName("disabled");

        if (asHTML) {
            html.setHTML(text);
        } else {
            html.setText(text);
        }

        panel.setWidget(link);
        initWidget(panel);
    }
    /*
     * (non-Javadoc)
     * @see com.google.gwt.event.dom.client.HasClickHandlers#addClickHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return ((HasClickHandlers)link).addClickHandler(handler);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.HasText#getText()
     */
    public String getText() {
        return ((HasHTML)link).getText();
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
     */
    public void setText(String text) {
        ((HasHTML)link).setText(text);
        html.setText(text);
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.HasHTML#getHTML()
     */
    public String getHTML() {
        return ((HasHTML)link).getHTML();
    }

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
     */
    public void setHTML(String html) {
        this.html.setHTML(html);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.widgets.HasEnable#setEnabled(boolean)
     */
    public void setEnabled(boolean enable) {
        if (enable) {
            panel.setWidget(link);
        } else {
            panel.setWidget(html);
        }
    }

}
