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

package org.apache.hupa.client.ui;

import java.util.List;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.activity.MessageContentActivity;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.widgets.editor.Editor;
import org.apache.hupa.widgets.editor.Toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class MessageContentView extends Composite implements MessageContentActivity.Displayable {


    private Editor editor = new Editor();

    // TODO should use a scrolled panel which can contain multiple children
    @UiField HTML messageContent = new HTML();
    @UiField FlowPanel attachments;
    @UiField DockLayoutPanel thisPanel;
    @UiField FlowPanel tmpWrapper;
    @UiField DockLayoutPanel messageUpdateContent;
    @UiField SimplePanel updateToolBar;
    @UiField SimplePanel updateEditor;
    @UiField SimpleLayoutPanel centerPanel;

    String messageDetail = "";

    @Inject
    public MessageContentView(HupaConstants constants) {
        initWidget(binder.createAndBindUi(this));

        Toolbar toolbar = new Toolbar(editor.getArea(), constants);
        toolbar.ensureDebugId("hupa-editor-toolbar");

        tmpWrapper.removeFromParent();


        updateToolBar.setWidget(toolbar);
        updateEditor.setWidget(editor);

        clearContent();
    }

    @Override
    public void fillMessageContent(String messageDetail, boolean isEditable) {
        this.messageDetail = messageDetail;
        if (isEditable) {
            editor.setHTML(messageDetail);
            centerPanel.setWidget(messageUpdateContent);
        } else {
            messageContent.setHTML(messageDetail);
            centerPanel.setWidget(messageContent);
        }
    }

    @Override
    public void setAttachments(List<MessageAttachment> attachements, final String folder, final long uid) {
        thisPanel.setWidgetHidden(attachments, false);

        attachments.clear();
        final Element downloadIframe = RootPanel.get("__download").getElement();
        if (attachements != null) {
            for (final MessageAttachment messageAttachment : attachements) {
                int s = messageAttachment.getSize() / 1024;
                String size = s < 100 ? "" : ("(" + s + " kB)");
                String name = messageAttachment.getName() + size;
                Label link = new Label(name);
                link.setTitle(name);
                link.setStyleName(HupaCSS.C_hyperlink);
                link.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        String url = getUrl(messageAttachment, folder, uid, false);
                        if (downloadIframe == null)
                            Window.open(url, "_blank", "");
                        else
                            DOM.setElementAttribute(RootPanel.get("__download").getElement(), "src", url);
                    }
                });
                HorizontalPanel aPanel = new HorizontalPanel();
                aPanel.addStyleName(HupaCSS.C_attachment);
                // aPanel.add(new Image(imageBundle.attachmentIcon()));
                aPanel.add(link);
                if (messageAttachment.isImage()) {
                    Anchor viewImageLink = new Anchor("View", getUrl(messageAttachment, folder, uid, true), "_blank");
                    viewImageLink.setStyleName(HupaCSS.C_attachment_view);
                    aPanel.add(viewImageLink);
                }
                attachments.add(aPanel);
            }
        }
    }

    private String getUrl(MessageAttachment messageAttachment, String folder, long uid, boolean inline) {
        return GWT.getModuleBaseURL() + SConsts.SERVLET_DOWNLOAD + "?" + SConsts.PARAM_NAME + "="
                + messageAttachment.getName() + "&" + SConsts.PARAM_FOLDER + "=" + folder + "&" + SConsts.PARAM_UID
                + "=" + uid + (inline ? "&" + SConsts.PARAM_MODE + "=inline" : "");
    }

    @Override
    public void clearContent() {
        fillMessageContent("<div class='emptyMsg' style='position absolute; height: 100%'/>", false);
        thisPanel.setWidgetHidden(attachments, true);
    }

    interface Binder extends UiBinder<DockLayoutPanel, MessageContentView> {
    }

    private static Binder binder = GWT.create(Binder.class);

    @Override
    public HasHTML getMessageHTML() {
        return editor;
    }
}
