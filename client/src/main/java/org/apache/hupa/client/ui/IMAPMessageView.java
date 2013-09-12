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
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.client.mvp.IMAPMessagePresenter.Display;
import org.apache.hupa.client.widgets.CommandsBar;
import org.apache.hupa.client.widgets.MessageHeaders;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.Util;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.widgets.ui.Loading;
import org.cobogw.gwt.user.client.ui.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.apache.hupa.client.activity.IMAPMessageActivity;

public class IMAPMessageView extends Composite implements IMAPMessageActivity.Displayable{

    public final static int DELETE_BUTTON = 0;
    public final static int REPLY_BUTTON = 1;
    public final static int REPLY_ALL_BUTTON = 2;
    
    private HupaImageBundle imageBundle;

    private VerticalPanel messageContainer = new VerticalPanel();
    private MessageHeaders headers;
    private CommandsBar buttonsBar = new CommandsBar();
    private HTML msgArea = new HTML();
    
    private Label from = new Label();
    private Label cc = new Label();
    private Label to = new Label();
    private Label subject = new Label();
    
    private Button deleteMsgButton = new Button();
    private Button replyMsgButton = new Button();
    private Button replyAllMsgButton = new Button();
    private Button forwardMsgButton = new Button();
    private Hyperlink showRawButton;
    private Hyperlink backButton;
    private FlowPanel attachments = new FlowPanel();
    
    private Loading loading;

    @Inject
    public IMAPMessageView(HupaConstants constants, HupaImageBundle imageBundle) {
        this.imageBundle = imageBundle;
        
        loading = new Loading(constants.loading());
        showRawButton = new Hyperlink(constants.rawButton(),"");
        backButton = new Hyperlink(constants.backButton(),"");
        headers = new MessageHeaders(constants);
        deleteMsgButton.setText(constants.deleteMailButton());
        replyMsgButton.setText(constants.replyMailButton());
        replyAllMsgButton.setText(constants.replyAllMailButton());
        forwardMsgButton.setText(constants.forwardMailButton());    
        
        messageContainer.addStyleName(HupaCSS.C_msgview_container);
        
        buttonsBar.add(replyMsgButton);
        buttonsBar.add(replyAllMsgButton);
        buttonsBar.add(deleteMsgButton);
        buttonsBar.add(forwardMsgButton);
        buttonsBar.add(loading);
        buttonsBar.add(showRawButton);
        buttonsBar.add(backButton);
        
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.addStyleName(HupaCSS.C_msgview_content);
        scrollPanel.setAlwaysShowScrollBars(false);
        scrollPanel.add(msgArea);
        
        messageContainer.add(headers);
        messageContainer.add(buttonsBar);
        messageContainer.add(scrollPanel);
        
        loading.hide();
        
        initWidget(messageContainer);
    }

    public Widget asWidget() {
        return this;
    }

    public void startProcessing() {
        loading.show();
    }

    public void stopProcessing() {
        loading.show();
    }

    public HasClickHandlers getDeleteButtonClick() {
        return deleteMsgButton;
    }

    public void setAttachments(List<MessageAttachment> attachements,
            final String folder,
            final long uid) {
        
        attachments.clear();
        final Element downloadIframe = RootPanel.get("__download").getElement();
        if (attachements != null) {
            for (final MessageAttachment messageAttachment : attachements) {
                Label link = new Label(messageAttachment.getName() + " (" + messageAttachment.getSize() / 1024 + "kB)");
                link.setStyleName(HupaCSS.C_hyperlink);
                link.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        String url = GWT.getModuleBaseURL() + SConsts.SERVLET_DOWNLOAD 
                                    + "?" + SConsts.PARAM_NAME + "=" + messageAttachment.getName() 
                                    + "&" + SConsts.PARAM_FOLDER + "=" + folder
                                    + "&" + SConsts.PARAM_UID + "=" + uid;
                        if (downloadIframe == null)
                            Window.open(url,"_blank", "");
                        else
                            DOM.setElementAttribute(downloadIframe, "src", url);
                    }
                });
                HorizontalPanel aPanel = new HorizontalPanel();
                aPanel.addStyleName(HupaCSS.C_attachment);
                aPanel.add(new Image(imageBundle.attachmentIcon()));
                aPanel.add(link);
                attachments.add(aPanel);
            }
        }
    }

    public HasClickHandlers getForwardButtonClick() {
        return forwardMsgButton;
    }

    public HasClickHandlers getReplyAllButtonClick() {
        return replyAllMsgButton;
    }

    public HasClickHandlers getReplyButtonClick() {
        return replyMsgButton;
    }

    public HasClickHandlers getBackButtonClick() {
        return backButton;
    }

    public HasClickHandlers getShowRawMessageClick() {
        return showRawButton;
    }

    public void setHeaders(Message message) {
        from.setText(message.getFrom());
        cc.setText(Util.listToString(message.getCc()));
        to.setText(Util.listToString(message.getTo()));
        subject.setText(message.getSubject());
        headers.setValues(from, to, cc, null, subject, attachments);
    }
    
    public void setContent(String content) {
        msgArea.setHTML(content);
    }
    
}
