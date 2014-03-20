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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RichTextArea.FontSize;

/**
 * Font picker for rich editor
 */
public class FontPicker extends PopupPanel implements ClickHandler, HasValueChangeHandlers<FontPicker> {

    private class FontCell extends HTML {
        String cellFont;

        public FontCell(String font) {
            super(font);
            this.cellFont = font;
            DOM.setStyleAttribute(getElement(), "backgroundColor", "#D8ECFD");
            DOM.setStyleAttribute(getElement(), "padding", "2px 4px 2px 8px");
            addMouseOverHandler(new MouseOverHandler() {
                public void onMouseOver(MouseOverEvent event) {
                    DOM.setStyleAttribute(getElement(), "backgroundColor", "#7FAAFF");
                }
            });
            addMouseOutHandler(new MouseOutHandler() {
                public void onMouseOut(MouseOutEvent event) {
                    DOM.setStyleAttribute(getElement(), "backgroundColor", "#D8ECFD");
                }
            });
        }

        public String getFont() {
            return cellFont;
        }

    }

    public enum FontPickerType {
        FONT_FAMILY, FONT_SIZE
    }

    private static final String[] fontFamilies = new String[] { "Times New Roman", "Arial", "Courier New", "Georgia", "Trebuchet", "Verdana", "Comic Sans MS" };

    private static final String[] fontSizes = new String[] { "xx-small", "x-small", "small", "medium", "large", "x-large", "xx-large" };

    private String font = "";

    ValueChangeHandler<FontPicker> changeHandler = null;

    public FontPicker(FontPickerType type) {
        super(true);
        VerticalPanel container = new VerticalPanel();
        DOM.setStyleAttribute(container.getElement(), "border", "1px solid  #7FAAFF");
        DOM.setStyleAttribute(container.getElement(), "backgroundColor", "#D8ECFD");
        DOM.setStyleAttribute(container.getElement(), "cursor", "pointer");

        String[] fonts = type == FontPickerType.FONT_SIZE ? fontSizes : fontFamilies;

        for (int i = 0; i < fonts.length; i++) {
            FontCell cell;
            if (type == FontPickerType.FONT_SIZE) {
                cell = new FontCell("" + (i + 1));
                DOM.setStyleAttribute(cell.getElement(), "fontSize", fonts[i]);
            } else {
                cell = new FontCell(fonts[i]);
                DOM.setStyleAttribute(cell.getElement(), "fontFamily", fonts[i]);
            }
            cell.addClickHandler(this);
            container.add(cell);
        }

        add(container);
        setAnimationEnabled(true);
        setStyleName("hupa-color-picker");
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<FontPicker> handler) {
        assert changeHandler == null : "Change handler is already defined";
        changeHandler = handler;
        return new HandlerRegistration() {
            public void removeHandler() {
                changeHandler = null;
            }
        };
    }

    public String getFontName() {
        return font;
    }

    public FontSize getFontSize() {
        switch (Integer.valueOf(font).intValue()) {
        case 1:
            return FontSize.XX_SMALL;
        case 2:
            return FontSize.X_SMALL;
        case 4:
            return FontSize.MEDIUM;
        case 5:
            return FontSize.LARGE;
        case 6:
            return FontSize.X_LARGE;
        case 7:
            return FontSize.XX_LARGE;
        case 3:
        default:
            return FontSize.SMALL;
        }
    }

    public void onClick(ClickEvent event) {
        FontCell cell = (FontCell) event.getSource();
        this.font = cell.getFont();
        if (changeHandler != null)
            changeHandler.onValueChange(new ValueChangeEvent<FontPicker>(this) {
            });
    }

}
