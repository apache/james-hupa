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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
=======
>>>>>>> first commit
=======
>>>>>>> first commit
=======
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
>>>>>>> Avoid entering a new-line in the textarea when selecting a suggestion element
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextArea;

/**
 * A text-area which shows a pop-up with suggestions.
 * Different values in the text area are separated by comma.
 * 
 * @author manolo
 */
public class MultiValueSuggestArea extends Composite implements HasText, Focusable {

    /**
     * It is necessary to modify the behavior of the default SuggestBox, because
     * it look for items which match the entire text in the box.
     * 
     * @author manolo
     */
    private class CustomSuggestBox extends SuggestBox {

        public CustomSuggestBox(SuggestOracle oracle) {
            // this is a hack, It is necessary to override the TextBoxBase passed to the constructor
            // instead of overriding getText and setText from SuggestBox because a bug in the implementation
            // I've sent a patch to gwt.
            super(oracle, new TextArea() {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Avoid entering a new-line in the textarea when selecting a suggestion element
                
                {
                    // Avoid entering a new-line when selecting a suggestion element
                    // TODO: I think this is a bug in GWT SuggestBox which should be reported.
                    addKeyDownHandler(new KeyDownHandler() {
                        public void onKeyDown(KeyDownEvent event) {
                            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                                event.preventDefault();
                            }
                        }
                    });
                }
<<<<<<< HEAD
=======
>>>>>>> first commit
=======
>>>>>>> first commit
=======
>>>>>>> Avoid entering a new-line in the textarea when selecting a suggestion element

                String search = null;
                
                @Override
                public String getText() {
                    return search = super.getText().replaceFirst("\\s+$", "").replaceFirst("^\\s+","").replaceAll("[\\s;]", ",").replaceFirst("^.+,", "");
                }
                
                @Override
                public void setText(String text) {
                    if (text.trim().length() > 0) {
                        String actual = super.getText().replaceFirst("\\s+$", "").replaceFirst(search + "[\\s]*$", "");
                        super.setText(actual + text + ", ");
                    }
                }
                
            });
        }

        // We have to use getValue and setValue to get/set the entire text of the textarea
        // because setText and getText have different behavior since we have modified 
        // this methods in the the box implementation
        @Override
        public String getValue() {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            return DOM.getElementProperty(getValueBox().getElement(), "value");
=======
            return DOM.getElementProperty(getTextBox().getElement(), "value");
>>>>>>> first commit
=======
            return DOM.getElementProperty(getTextBox().getElement(), "value");
>>>>>>> first commit
=======
            return DOM.getElementProperty(getValueBox().getElement(), "value");
>>>>>>> remove some warnings and create the AbstractPlace that can give place infomation
        }

        @Override
        public void setValue(String text) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            DOM.setElementProperty(getValueBox().getElement(), "value", text);
=======
            DOM.setElementProperty(getTextBox().getElement(), "value", text);
>>>>>>> first commit
=======
            DOM.setElementProperty(getTextBox().getElement(), "value", text);
>>>>>>> first commit
=======
            DOM.setElementProperty(getValueBox().getElement(), "value", text);
>>>>>>> remove some warnings and create the AbstractPlace that can give place infomation
        }
    }

    private SuggestBox box;

    private MultiWordSuggestOracle oracle;

    public MultiValueSuggestArea(SuggestOracle oracle) {
        box = new CustomSuggestBox(oracle);
        initWidget(box);
    }

    public MultiValueSuggestArea(String separators) {
        this(null, separators);
    }

    public MultiValueSuggestArea(Object[] list, String separators) {
        oracle = new MultiWordSuggestOracle(separators);
        box = new CustomSuggestBox(oracle);
        initWidget(box);
        fillOracle(list);
    }

    public String getText() {
        return box.getValue();
    }

    public void setText(String text) {
        box.setValue(text);
    }

    public void fillOracle(Object[] list) {
        oracle.clear();
        if (list != null)
            for (Object o : list)
                oracle.add(o.toString());
    }

    public SuggestOracle getOracle() {
        return oracle;
    }

    public int getTabIndex() {
        return box.getTabIndex();
    }

    public void setAccessKey(char key) {
        box.setAccessKey(key);
    }

    public void setFocus(boolean focused) {
        box.setFocus(focused);
    }

    public void setTabIndex(int index) {
        box.setTabIndex(index);
    }
}
