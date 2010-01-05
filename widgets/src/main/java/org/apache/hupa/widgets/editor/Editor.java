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
package org.apache.hupa.widgets.editor;

import com.google.gwt.user.client.ui.HasHTML;

import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Wysiwyg editor for composing and editing emails in Hupa
 */
public class Editor extends VerticalPanel implements HasHTML {

    RichTextArea area = new RichTextArea();
    
    public Editor() {
        area.ensureDebugId("hupa-editor-area");
        // Note: rich-area is created in an iframe, so Hupa's style sheets 
        // are not available, unless we inject them to the generated iframe
        area.setSize("100%", "200em");
        
        Toolbar toolbar = new Toolbar(area);
        toolbar.ensureDebugId("hupa-editor-toolbar");
        
        super.setStyleName("hupa-editor");
        super.add(toolbar);
        super.add(area);
        super.setWidth("100%");

    }
    
    @Override
    public void setSize(String width, String height) {
        area.setSize(width, height);
    }
    
    @Override
    public void setWidth(String width){
        area.setWidth(width);
    }

    @Override
    public void setHeight(String height){
        area.setHeight(height);
    }

    public String getHTML() {
        return area.getHTML();
    }

    public void setHTML(String html) {
        area.setHTML("<font style='font-family: arial' size=2>" + html + "</font>");
    }

    public String getText() {
        return area.getText();
    }

    public void setText(String text) {
        area.setText(text);
    }
    
}