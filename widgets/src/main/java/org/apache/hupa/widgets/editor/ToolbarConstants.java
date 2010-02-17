/*
 * Copyright 2010 Manuel Carrasco Moñino. (manuel_carrasco at users.sourceforge.net) 
 * http://code.google.com/p/gwtchismes
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.code.p.gwtchismes.client.editor;

import com.google.gwt.i18n.client.Constants;

/**
 * This {@link Constants} interface is used to make the toolbar's strings
 * internationalizable.
 */
public interface ToolbarConstants extends Constants {
    
    @DefaultStringValue("Toggle Bold")
    public String editor_bold();

    @DefaultStringValue("Create Link")
    public String editor_createLink();

    @DefaultStringValue("Insert Horizontal Rule")
    public String editor_hr();

    @DefaultStringValue("Indent Right")
    public String editor_indent();

    @DefaultStringValue("Insert Image")
    public String editor_insertImage();

    @DefaultStringValue("Toggle Italic")
    public String editor_italic();

    @DefaultStringValue("Center")
    public String editor_justifyCenter();

    @DefaultStringValue("Left Justify")
    public String editor_justifyLeft();

    @DefaultStringValue("Right Justify")
    public String editor_justifyRight();

    @DefaultStringValue("Insert Ordered List")
    public String editor_ol();

    @DefaultStringValue("Indent Left")
    public String editor_outdent();

    @DefaultStringValue("Remove Formatting")
    public String editor_removeFormat();

    @DefaultStringValue("Remove Link")
    public String editor_removeLink();

    @DefaultStringValue("Toggle Strikethrough")
    public String editor_strikeThrough();

    @DefaultStringValue("Toggle Subscript")
    public String editor_subscript();

    @DefaultStringValue("Toggle Superscript")
    public String editor_superscript();

    @DefaultStringValue("Insert Unordered List")
    public String editor_ul();

    @DefaultStringValue("Toggle Underline")
    public String editor_underline();

    @DefaultStringValue("Background color")
    public String editor_background();
    
    @DefaultStringValue("Foreground color")
    public String editor_foreground();
    
    @DefaultStringValue("Font name")
    public String editor_font();
    
    @DefaultStringValue("Font size")
    public String editor_size();
    

}
