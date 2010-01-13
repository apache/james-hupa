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

import org.apache.hupa.widgets.editor.bundles.ToolbarImages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RichTextArea.FontSize;

/**
 * Toolbar for use with {@link RichTextArea}. It provides a simple UI for all
 * rich text formatting, dynamically displayed only for the available
 * functionality.
 */
public class Toolbar extends Composite {

    private class EventHandler implements ClickHandler, ChangeHandler, KeyUpHandler, KeyDownHandler {
        public void onChange(ChangeEvent event) {
            Widget sender = (Widget) event.getSource();

            if (sender == backColors) {
                basic.setBackColor(backColors.getValue(backColors.getSelectedIndex()));
                backColors.setSelectedIndex(0);
            } else if (sender == foreColors) {
                basic.setForeColor(foreColors.getValue(foreColors.getSelectedIndex()));
                foreColors.setSelectedIndex(0);
            } else if (sender == fonts) {
                basic.setFontName(fonts.getValue(fonts.getSelectedIndex()));
                fonts.setSelectedIndex(0);
            } else if (sender == fontSizes) {
                basic.setFontSize(fontSizesConstants[fontSizes.getSelectedIndex() - 1]);
                fontSizes.setSelectedIndex(0);
            }
        }

        public void onClick(ClickEvent event) {
            Widget sender = (Widget) event.getSource();

            if (sender == bold) {
                basic.toggleBold();
            } else if (sender == italic) {
                basic.toggleItalic();
            } else if (sender == underline) {
                basic.toggleUnderline();
            } else if (sender == subscript) {
                basic.toggleSubscript();
            } else if (sender == superscript) {
                basic.toggleSuperscript();
            } else if (sender == strikethrough) {
                extended.toggleStrikethrough();
            } else if (sender == indent) {
                extended.rightIndent();
            } else if (sender == outdent) {
                extended.leftIndent();
            } else if (sender == justifyLeft) {
                basic.setJustification(RichTextArea.Justification.LEFT);
            } else if (sender == justifyCenter) {
                basic.setJustification(RichTextArea.Justification.CENTER);
            } else if (sender == justifyRight) {
                basic.setJustification(RichTextArea.Justification.RIGHT);
            } else if (sender == insertImage) {
                String url = Window.prompt("Enter an image URL:", "http://");
                if (url != null) {
                    extended.insertImage(url);
                }
            } else if (sender == createLink) {
                String url = Window.prompt("Enter a link URL:", "http://");
                if (url != null) {
                    extended.createLink(url);
                }
            } else if (sender == removeLink) {
                extended.removeLink();
            } else if (sender == hr) {
                extended.insertHorizontalRule();
            } else if (sender == ol) {
                extended.insertOrderedList();
            } else if (sender == ul) {
                extended.insertUnorderedList();
            } else if (sender == removeFormat) {
                extended.removeFormat();
            } else if (sender == richText) {
                updateStatus();
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

    private static final RichTextArea.FontSize[] fontSizesConstants = new RichTextArea.FontSize[] { RichTextArea.FontSize.XX_SMALL, RichTextArea.FontSize.X_SMALL, RichTextArea.FontSize.SMALL,
            RichTextArea.FontSize.MEDIUM, RichTextArea.FontSize.LARGE, RichTextArea.FontSize.X_LARGE, RichTextArea.FontSize.XX_LARGE };

    private ToolbarImages images = (ToolbarImages) GWT.create(ToolbarImages.class);
    private ToolbarConstants strings = (ToolbarConstants) GWT.create(ToolbarConstants.class);
    private EventHandler handler = new EventHandler();

    private RichTextArea richText;
    private RichTextArea.BasicFormatter basic;
    private RichTextArea.ExtendedFormatter extended;

    private VerticalPanel outer = new VerticalPanel();
    private HorizontalPanel topPanel = new HorizontalPanel();
    private HorizontalPanel bottomPanel = new HorizontalPanel();
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

    private ListBox backColors;
    private ListBox foreColors;
    private ListBox fonts;
    private ListBox fontSizes;

    /**
     * Creates a new toolbar that drives the given rich text area.
     * 
     * @param richText
     *            the rich text area to be controlled
     */
    public Toolbar(RichTextArea richText) {
        this.richText = richText;
        this.basic = richText.getBasicFormatter();
        this.extended = richText.getExtendedFormatter();

        outer.add(topPanel);
        outer.add(bottomPanel);
        topPanel.setWidth("100%");
        bottomPanel.setWidth("100%");

        initWidget(outer);
        setStyleName("gwt-RichTextToolbar");
        richText.addStyleName("hasRichTextToolbar");

        if (basic != null) {
            topPanel.add(bold = createToggleButton(images.bold(), strings.editor_bold()));
            topPanel.add(italic = createToggleButton(images.italic(), strings.editor_italic()));
            topPanel.add(underline = createToggleButton(images.underline(), strings.editor_underline()));
            topPanel.add(subscript = createToggleButton(images.subscript(), strings.editor_subscript()));
            topPanel.add(superscript = createToggleButton(images.superscript(), strings.editor_superscript()));
            topPanel.add(justifyLeft = createPushButton(images.justifyLeft(), strings.editor_justifyLeft()));
            topPanel.add(justifyCenter = createPushButton(images.justifyCenter(), strings.editor_justifyCenter()));
            topPanel.add(justifyRight = createPushButton(images.justifyRight(), strings.editor_justifyRight()));
        }

        if (extended != null) {
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

        if (basic != null) {
            bottomPanel.add(backColors = createColorList("Background"));
            bottomPanel.add(foreColors = createColorList("Foreground"));
            bottomPanel.add(fonts = createFontList());
            bottomPanel.add(fontSizes = createFontSizes());

            richText.addKeyDownHandler(handler);
            richText.addKeyUpHandler(handler);
            richText.addClickHandler(handler);
        }
        
        outer.setWidth("100%");
        
        HTML topEmtyCell = new HTML("");
        topPanel.add(topEmtyCell);
        topPanel.setCellWidth(topEmtyCell, "100%");

        HTML bottomEmtyCell = new HTML("");
        bottomPanel.add(bottomEmtyCell);
        bottomPanel.setCellWidth(bottomEmtyCell, "100%");
    }

    private ListBox createColorList(String caption) {
        ListBox lb = new ListBox();
        lb.addChangeHandler(handler);
        lb.setVisibleItemCount(1);

        lb.addItem(caption);
        lb.addItem(strings.editor_white(), "white");
        lb.addItem(strings.editor_black(), "black");
        lb.addItem(strings.editor_red(), "red");
        lb.addItem(strings.editor_green(), "green");
        lb.addItem(strings.editor_yellow(), "yellow");
        lb.addItem(strings.editor_blue(), "blue");
        return lb;
    }

    private ListBox createFontList() {
        ListBox lb = new ListBox();
        lb.addChangeHandler(handler);
        lb.setVisibleItemCount(1);

        lb.addItem(strings.editor_font(), "");
        lb.addItem(strings.editor_normal(), "");
        lb.addItem("Times New Roman", "Times New Roman");
        lb.addItem("Arial", "Arial");
        lb.addItem("Courier New", "Courier New");
        lb.addItem("Georgia", "Georgia");
        lb.addItem("Trebuchet", "Trebuchet");
        lb.addItem("Verdana", "Verdana");
        return lb;
    }

    private ListBox createFontSizes() {
        ListBox lb = new ListBox();
        lb.addChangeHandler(handler);
        lb.setVisibleItemCount(1);

        lb.addItem(strings.editor_size());
        lb.addItem(strings.editor_xxsmall());
        lb.addItem(strings.editor_xsmall());
        lb.addItem(strings.editor_small());
        lb.addItem(strings.editor_medium());
        lb.addItem(strings.editor_large());
        lb.addItem(strings.editor_xlarge());
        lb.addItem(strings.editor_xxlarge());
        return lb;
    }

    private PushButton createPushButton(AbstractImagePrototype img, String tip) {
        PushButton pb = new PushButton(img.createImage());
        pb.addClickHandler(handler);
        pb.setTitle(tip);
        return pb;
    }

    private ToggleButton createToggleButton(AbstractImagePrototype img, String tip) {
        ToggleButton tb = new ToggleButton(img.createImage());
        tb.addClickHandler(handler);
        tb.setTitle(tip);
        return tb;
    }

    /**
     * Updates the status of all the stateful buttons.
     */
    private void updateStatus() {
        if (basic != null) {
            bold.setDown(basic.isBold());
            italic.setDown(basic.isItalic());
            underline.setDown(basic.isUnderlined());
            subscript.setDown(basic.isSubscript());
            superscript.setDown(basic.isSuperscript());
        }

        if (extended != null) {
            strikethrough.setDown(extended.isStrikethrough());
        }
    }
}
