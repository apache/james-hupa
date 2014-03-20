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

import org.apache.hupa.widgets.event.EditEvent;
import org.apache.hupa.widgets.event.EditHandler;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * TreeItem which supports editing
 *
 *
 */
public class EditableTreeItem extends TreeItem implements HasEditable,HasEditHandlers{
    protected TextBox editBox = new TextBox();
    protected String oldValue;
    protected Widget normalItem = new Label();
    protected HandlerManager manager = new HandlerManager(this);
    public EditableTreeItem() {
        editBox.setWidth("100px");
        editBox.addKeyDownHandler(new KeyDownHandler() {

            public void onKeyDown(KeyDownEvent event) {
                int code = event.getNativeKeyCode();
                switch (code) {
                // handle ENTER and ESCAPE keys
                case KeyCodes.KEY_ENTER:
                    stopEdit();
                    break;
                case KeyCodes.KEY_ESCAPE:
                    cancelEdit();
                    break;

                default:
                    break;
                }

            }

        });

        // Just cancel the editing if the user click outside the TextBox
        editBox.addBlurHandler(new BlurHandler() {

            public void onBlur(BlurEvent event) {
                cancelEdit();
            }

        });
        super.setWidget(normalItem);
    }


    @Override
    public void setText(String text) {
        editBox.setText(text);
        ((HasText)normalItem).setText(text);
    }


    @Override
    public void setWidget(Widget newWidget) {
        if (newWidget instanceof HasText) {
            normalItem = newWidget;
            super.setWidget(newWidget);
        } else {
            throw new IllegalArgumentException("Widget need to implement HasText");
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.widgets.HasEditable#cancelEdit()
     */
    public void cancelEdit() {
        showItem(oldValue);
        manager.fireEvent(new EditEvent(EditEvent.EventType.Start,oldValue,null));
    }
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.widgets.HasEditable#isEdit()
     */
    public boolean isEdit() {
        return getWidget().equals(editBox);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.widgets.HasEditable#startEdit()
     */
    public void startEdit() {
        oldValue = getText();
        showEditBox(oldValue);
        manager.fireEvent(new EditEvent(EditEvent.EventType.Start,oldValue,null));
    }

    /**
     * Show the editbox filled with the given value
     *
     * @param value
     */
    protected void showEditBox(String value) {
        super.setWidget(editBox);
        editBox.setText(value);
        editBox.setCursorPos(value.length());
        editBox.setFocus(true);
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.widgets.HasEditable#stopEdit()
     */
    public void stopEdit() {
        showItem(editBox.getText());
        manager.fireEvent(new EditEvent(EditEvent.EventType.Stop,oldValue,editBox.getText()));
    }

    /**
     * Show the "normal" item with the given text
     *
     * @param text
     */
    protected void showItem(String text) {
        ((HasText)normalItem).setText(text);
        setWidget(normalItem);
    }

    @Override
    public String getText() {
        return ((HasText)normalItem).getText();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.widgets.HasEditHandlers#addEditHandler(org.apache.hupa.client.widgets.EditHandler)
     */
    public HandlerRegistration addEditHandler(EditHandler handler) {
        return manager.addHandler(EditEvent.TYPE, handler);
    }



}
