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
/**
 * Much of this code has been taken from the GWT Showcase example
 * which is licensed under Apache License v2.0
 *
 * This class is necessary while GWT library doesn't provide any
 * toolbar to be used with its RichTextArea widget.
 */
package org.apache.hupa.widgets.editor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Color picker for rich editor
 */
public class ColorPicker extends PopupPanel implements ClickHandler, HasValueChangeHandlers<ColorPicker>  {

    private class ColorCell extends Label {
        String rgbColor;
        public ColorCell(long color) {
            this(Long.toHexString(color));
        }

        public ColorCell(String color) {
            super();
            setColor(color);
            setTitle(rgbColor);
            setSize("14px", "12px");
            DOM.setStyleAttribute(getElement(), "backgroundColor", rgbColor);
            setBorderColor("#cccccc");
            addMouseOverHandler(new MouseOverHandler() {
                public void onMouseOver(MouseOverEvent event) {
                  setBorderColor("#ffffff");
                }
            });
            addMouseOutHandler(new MouseOutHandler() {
                public void onMouseOut(MouseOutEvent event) {
                    setBorderColor("#cccccc");
                }
            });
        }

        public String getColor() {
            return rgbColor;
        }

        public void setBorderColor(String color) {
            DOM.setStyleAttribute(getElement(), "border", "1px solid " + color);
        }

        void setColor(String s){
            while(s.length()<6) s= "0" + s;
            rgbColor="#" + s;
        }
    }

    ValueChangeHandler<ColorPicker> changeHandler = null;

    private String color = "";

    long[] colors = new long[] {
        0xffffff, 0xcccccc, 0xc0c0c0, 0x999999, 0x666666, 0x333333, 0x000000,
        0xffcccc, 0xff6666, 0xff0000, 0xcc0000, 0x990000, 0x660000, 0x330000,
        0xffcc99, 0xff9966, 0xff9900, 0xfd6500, 0xcb6500, 0x983200, 0x653200,
        0xffff99, 0xffff66, 0xffcc66, 0xfdcb32, 0xcb9832, 0x986532, 0x653232,
        0xffffcc, 0xffff33, 0xffff00, 0xfdcb00, 0x989800, 0x656500, 0x323200,
        0x99ff99, 0x66ff99, 0x33ff33, 0x32cb00, 0x009800, 0x006500, 0x003200,
        0x99ffff, 0x33ffff, 0x66cccc, 0x00cbcb, 0x329898, 0x326565, 0x003232,
        0xccffff, 0x66ffff, 0x33ccff, 0x3265fd, 0x3232fd, 0x000098, 0x000065,
        0xccccff, 0x9999ff, 0x6666cc, 0x6532fd, 0x6500cb, 0x323298, 0x320098,
        0xffccff, 0xff99ff, 0xcc66cc, 0xcb32cb, 0x983298, 0x653265, 0x320032,
    };

    public ColorPicker() {
        super(true);
        FlexTable t = new FlexTable();
        t.setCellPadding(0);
        t.setCellSpacing(0);
        DOM.setStyleAttribute(t.getElement(), "border", "1px solid #cccccc");

        int i=0;
        for (int r=0; i<colors.length; r++) {
            for (int c=0; c<7 && i<colors.length; c++, i++) {
                ColorCell cell = new ColorCell(colors[i]);
                cell.addClickHandler(this);
                t.setWidget(r, c, cell);
            }
        }

        add(t);
        setAnimationEnabled(true);
        setStyleName("hupa-color-picker");
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ColorPicker> handler) {
        assert changeHandler == null : "Change handler is already defined";
        changeHandler = handler;
        return new HandlerRegistration() {
            public void removeHandler() {
                changeHandler = null;
            }
        };
    }

    public String getColor() {
        return color;
    }

    public void onClick(ClickEvent event) {
        ColorCell cell = (ColorCell)event.getSource();
        this.color = cell.getColor();
        if (changeHandler != null)
            changeHandler.onValueChange(new ValueChangeEvent<ColorPicker>(this){});
    }

}
