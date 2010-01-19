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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
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
public class MultiValueSuggestArea extends Composite implements HasText {

    /**
     * It is necessary to modify the behavior of the default SuggestBox, because
     * it look for items which match the entire text in the box.
     * 
     * @author manolo
     */
    private class CustomSuggestBox extends SuggestBox {

        public CustomSuggestBox(SuggestOracle oracle) {
            // this is a hack, It is necessary to override the TextBoxBase passed to the constructor
            // instead of override getText and setText from SuggestBox because a bug in the implementation
            // I've sent a patch to gwt.
            super(oracle, new TextArea() {
                @Override
                public String getText() {
                    return super.getText().replaceAll("^.*[,; \r\n]+", "");
                }

                @Override
                public void setText(String text) {
                    String actual = super.getText().replaceFirst("[\r\n]+", "").replaceFirst(".$", "").replaceFirst("[,; \r\n]+[^,; \r\n]*$", "");
                    super.setText(actual + (actual.isEmpty() ? "" : ", ") + text + (text.isEmpty() ? "" : ", "));
                }
            });
        }

        // We have to use getValue and setValue to get/set the entire text of the textarea
        // because setText and getText have different behavior since we have modified 
        // this methods in the the box implementation
        @Override
        public String getValue() {
            return DOM.getElementProperty(getTextBox().getElement(), "value");
        }

        @Override
        public void setValue(String text) {
            DOM.setElementProperty(getTextBox().getElement(), "value", text);
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
}
