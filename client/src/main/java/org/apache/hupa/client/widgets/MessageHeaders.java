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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import gwtupload.client.IUploader;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.widgets.ui.EnableHyperlink;
import org.apache.hupa.widgets.ui.MultiValueSuggestArea;

/**
 * A panel which displays the headers of a message.
 */
public class MessageHeaders extends Composite {

    private FlexTable detailGrid = new FlexTable();
    private String headerNames[];
    private int rowCounter = 0;

    public MessageHeaders (HupaConstants constants) {
       headerNames = new String[] {
                constants.headerFrom(),
                constants.headerTo(),
                constants.headerCc(),
                constants.headerBcc(),
                constants.headerSubject(),
                constants.attachments(),
                "nothing"
        };
        detailGrid.setWidth("100%");
        detailGrid.addStyleName(HupaCSS.C_msg_headers);
        initWidget(detailGrid);
    }

    public void setValues(Widget from, Widget to, Widget cc, Widget bcc, Widget subject, Widget attachments) {
       addValues(from, to, cc, bcc, subject, attachments);
    }

    private void addValues(Widget...widgets) {
        detailGrid.clearAll();
        rowCounter = 0;
        for (int i=0; i<widgets.length; i++)
            addRow(headerNames[i], widgets[i]);
    }

    private Widget createLinkToShow(String name, final Widget widget) {
        final EnableHyperlink link = new EnableHyperlink(name + ":");
        link.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                widget.setVisible(true);
                link.setEnabled(false);
            }
        });
        widget.setVisible(false);
        return link;
    }

    private void addRow(String name, final Widget widget) {
        if (widget == null)
            return;

        Widget label = new Label(name + ":");
        if (widget instanceof MultiValueSuggestArea){
            widget.setWidth("100%");
            if (((HasText)widget).getText().trim().length() == 0) {
                label = createLinkToShow(name, widget);
            }
        } else if (widget instanceof TextBox) {
            widget.setWidth("100%");
        } else if (widget instanceof HasText) {
            if (((HasText)widget).getText().trim().length() == 0)
                return;
        } else if (widget instanceof IUploader) {
            label = createLinkToShow(name, widget);
        } else if (widget instanceof Panel) {
            if (((Panel)widget).iterator().hasNext() == false)
                return;
        }

        detailGrid.setWidget(rowCounter, 0, label);
        detailGrid.setWidget(rowCounter, 1, widget);
        detailGrid.getCellFormatter().setStyleName(rowCounter, 0, "label");
        detailGrid.getCellFormatter().setStyleName(rowCounter, 1, "value");
        rowCounter ++;
    }

}
