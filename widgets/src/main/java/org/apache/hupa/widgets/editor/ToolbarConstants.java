package org.apache.hupa.widgets.editor;

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
