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

import org.apache.hupa.widgets.editor.FontPicker.FontPickerType;
import org.apache.hupa.widgets.editor.bundles.ToolbarImages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Toolbar for use with {@link RichTextArea}. It provides a simple UI for all
 * rich text formatting, dynamically displayed only for the available
 * functionality.
 */
public class Toolbar extends Composite {

    private class EventHandler implements ClickHandler, KeyUpHandler, KeyDownHandler {

        public void onClick(ClickEvent event) {
            Widget sender = (Widget) event.getSource();

            if (sender == bold) {
                formatter.toggleBold();
            } else if (sender == italic) {
                formatter.toggleItalic();
            } else if (sender == underline) {
                formatter.toggleUnderline();
            } else if (sender == subscript) {
                formatter.toggleSubscript();
            } else if (sender == superscript) {
                formatter.toggleSuperscript();
            } else if (sender == strikethrough) {
                formatter.toggleStrikethrough();
            } else if (sender == indent) {
                formatter.rightIndent();
            } else if (sender == outdent) {
                formatter.leftIndent();
            } else if (sender == justifyLeft) {
                formatter.setJustification(RichTextArea.Justification.LEFT);
            } else if (sender == justifyCenter) {
                formatter.setJustification(RichTextArea.Justification.CENTER);
            } else if (sender == justifyRight) {
                formatter.setJustification(RichTextArea.Justification.RIGHT);
            } else if (sender == insertImage) {
                String url = Window.prompt("Enter an image URL:", "http://");
                if (url != null) {
                    formatter.insertImage(url);
                }
            } else if (sender == createLink) {
                String url = Window.prompt("Enter a link URL:", "http://");
                if (url != null) {
                    formatter.createLink(url);
                }
            } else if (sender == removeLink) {
                formatter.removeLink();
            } else if (sender == hr) {
                formatter.insertHorizontalRule();
            } else if (sender == ol) {
                formatter.insertOrderedList();
            } else if (sender == ul) {
                formatter.insertUnorderedList();
            } else if (sender == removeFormat) {
                formatter.removeFormat();
            } else if (sender == richText) {
                updateStatus();
            } else if (sender == backColors) {
                backColorsPicker.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop()+20);
                backColorsPicker.show();
            } else if (sender == foreColors) {
                foreColorsPicker.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop()+20);
                foreColorsPicker.show();
            } else if (sender == fontFamily) {
                fontFamilyPicker.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop()+20);
                fontFamilyPicker.show();
            } else if (sender == fontSize) {
                fontSizePicker.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop()+20);
                fontSizePicker.show();
            }
        }

        public void onKeyDown(KeyDownEvent event) {
        }

        public void onKeyUp(KeyUpEvent event) {
            Widget sender = (Widget) event.getSource();
            if (sender == richText) {
                updateStatus();
            }
        }

    }

    private ValueChangeHandler<ColorPicker> colorHandler = new ValueChangeHandler<ColorPicker>() {
        public void onValueChange(ValueChangeEvent<ColorPicker> event) {
            ColorPicker sender = event.getValue();
            if (sender == backColorsPicker) {
                formatter.setBackColor(sender.getColor());
            } else if (sender == foreColorsPicker) {
                formatter.setForeColor(sender.getColor());
            }
            sender.hide();
        }
    };
    private ValueChangeHandler<FontPicker> fontHandler = new ValueChangeHandler<FontPicker>() {
        public void onValueChange(ValueChangeEvent<FontPicker> event) {
            FontPicker sender = event.getValue();
            if (sender == fontFamilyPicker) {
               formatter.setFontName(sender.getFontName());
            } else if (sender == fontSizePicker) {
               formatter.setFontSize(sender.getFontSize());
            }
            sender.hide();
        }
    };

    private static final ToolbarImages images = (ToolbarImages) GWT.create(ToolbarImages.class);
    private EventHandler handler = new EventHandler();


    private RichTextArea richText;
    private RichTextArea.Formatter formatter;

    private HorizontalPanel topPanel = new HorizontalPanel();
    private ToggleButton bold;
    private ToggleButton italic;
    private ToggleButton underline;
    private ToggleButton subscript;
    private ToggleButton superscript;
    private ToggleButton strikethrough;
    private PushButton indent;
    private PushButton outdent;
    private PushButton justifyLeft;
    private PushButton justifyCenter;
    private PushButton justifyRight;
    private PushButton hr;
    private PushButton ol;
    private PushButton ul;
    private PushButton insertImage;
    private PushButton createLink;
    private PushButton removeLink;
    private PushButton removeFormat;

    private PushButton fontFamily;
    private FontPicker fontFamilyPicker = new FontPicker(FontPickerType.FONT_FAMILY);
    private PushButton fontSize;
    private FontPicker fontSizePicker = new FontPicker(FontPickerType.FONT_SIZE);

    private PushButton backColors;
    private PushButton foreColors;
    private ColorPicker backColorsPicker = new ColorPicker();
    private ColorPicker foreColorsPicker = new ColorPicker();

    /**
     * Creates a new toolbar that drives the given rich text area.
     *
     * @param richText
     *            the rich text area to be controlled
     */
    public Toolbar(RichTextArea richText, ToolbarConstants strings) {
        this.richText = richText;
        this.formatter = richText.getFormatter();
//        this.extended = richText.getFormatter();

        topPanel.setWidth("100%");

        initWidget(topPanel);
        setStyleName("gwt-RichTextToolbar");
        richText.addStyleName("hasRichTextToolbar");

        if (formatter != null) {
            topPanel.add(bold = createToggleButton(images.bold(), strings.editor_bold()));
            topPanel.add(italic = createToggleButton(images.italic(), strings.editor_italic()));
            topPanel.add(underline = createToggleButton(images.underline(), strings.editor_underline()));
            topPanel.add(backColors = createPushButton(images.backColors(), strings.editor_background()));
            topPanel.add(foreColors = createPushButton(images.foreColors(), strings.editor_foreground()));
            topPanel.add(fontFamily = createPushButton(images.fonts(), strings.editor_font()));
            topPanel.add(fontSize = createPushButton(images.fontSizes(), strings.editor_size()));
            topPanel.add(subscript = createToggleButton(images.subscript(), strings.editor_subscript()));
            topPanel.add(superscript = createToggleButton(images.superscript(), strings.editor_superscript()));
            topPanel.add(justifyLeft = createPushButton(images.justifyLeft(), strings.editor_justifyLeft()));
            topPanel.add(justifyCenter = createPushButton(images.justifyCenter(), strings.editor_justifyCenter()));
            topPanel.add(justifyRight = createPushButton(images.justifyRight(), strings.editor_justifyRight()));
        }

        if (formatter != null) {
            topPanel.add(strikethrough = createToggleButton(images.strikeThrough(), strings.editor_strikeThrough()));
            topPanel.add(indent = createPushButton(images.indent(), strings.editor_indent()));
            topPanel.add(outdent = createPushButton(images.outdent(), strings.editor_outdent()));
            topPanel.add(hr = createPushButton(images.hr(), strings.editor_hr()));
            topPanel.add(ol = createPushButton(images.ol(), strings.editor_ol()));
            topPanel.add(ul = createPushButton(images.ul(), strings.editor_ul()));
            topPanel.add(insertImage = createPushButton(images.insertImage(), strings.editor_insertImage()));
            topPanel.add(createLink = createPushButton(images.createLink(), strings.editor_createLink()));
            topPanel.add(removeLink = createPushButton(images.removeLink(), strings.editor_removeLink()));
            topPanel.add(removeFormat = createPushButton(images.removeFormat(), strings.editor_removeFormat()));
        }


        HTML topEmtyCell = new HTML("");
        topPanel.add(topEmtyCell);
        topPanel.setCellWidth(topEmtyCell, "100%");

        richText.addKeyDownHandler(handler);
        richText.addKeyUpHandler(handler);
        richText.addClickHandler(handler);
        backColorsPicker.addValueChangeHandler(colorHandler);
        foreColorsPicker.addValueChangeHandler(colorHandler);
        fontFamilyPicker.addValueChangeHandler(fontHandler);
        fontSizePicker.addValueChangeHandler(fontHandler);
    }

    private PushButton createPushButton(ImageResource img, String tip) {
        PushButton pb = new PushButton(new Image(img));
        pb.addClickHandler(handler);
        pb.setTitle(tip);
        return pb;
    }

    private ToggleButton createToggleButton(ImageResource img, String tip) {
        ToggleButton tb = new ToggleButton(new Image(img));
        tb.addClickHandler(handler);
        tb.setTitle(tip);
        return tb;
    }

    /**
     * Updates the status of all the stateful buttons.
     */
    private void updateStatus() {
        if (formatter != null) {
            bold.setDown(formatter.isBold());
            italic.setDown(formatter.isItalic());
            underline.setDown(formatter.isUnderlined());
            subscript.setDown(formatter.isSubscript());
            superscript.setDown(formatter.isSuperscript());
            foreColorsPicker.hide();
            backColorsPicker.hide();
            fontFamilyPicker.hide();
            fontSizePicker.hide();
        }

        if (formatter != null) {
            strikethrough.setDown(formatter.isStrikethrough());
        }
    }
}
