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

package org.apache.hupa.client.mvp;

import java.util.List;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.client.mvp.IMAPMessagePresenter.Display;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.HasURL;
import org.apache.hupa.client.widgets.Iframe;
import org.apache.hupa.client.widgets.Loading;
import org.apache.hupa.client.widgets.MyDialogBox;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.MessageAttachment;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.ButtonBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IMAPMessageView extends Composite implements Display{
    
    private HupaImageBundle imageBundle = GWT.create(HupaImageBundle.class);
    private HupaConstants constants = GWT.create(HupaConstants.class);
    private Grid detailGrid = new Grid(5, 2);
    private Label from = new Label();
    private Label cc = new Label();
    private Label to = new Label();
    private Label subject = new Label();
    private HTML msgArea = new HTML();
    private Button deleteMsgButton = new Button();
    private Button replyMsgButton = new Button();
    private Button replyAllMsgButton = new Button();
    private Button forwardMsgButton = new Button();
    private Hyperlink showRawButton = new Hyperlink(constants.rawButton(),"");
    private Hyperlink backButton = new Hyperlink(constants.backButton(),"");
    private FlowPanel attachments = new FlowPanel();
    private MyDialogBox rawDialogBox = new MyDialogBox();
    private Iframe rawFrame = new Iframe();
    public final static int DELETE_BUTTON = 0;
    public final static int REPLY_BUTTON = 1;
    public final static int REPLY_ALL_BUTTON = 2;
    private Loading loading  = new Loading(true);
    private SimplePanel container = new SimplePanel();
    public IMAPMessageView() {
        final VerticalPanel mPanel = new VerticalPanel();
        mPanel.setWidth(Window.getClientWidth() -200 +"px");
        
        Window.addResizeHandler(new ResizeHandler() {

            public void onResize(ResizeEvent event) {
                mPanel.setWidth(Window.getClientWidth() -200+"px");
            }
            
        });
        mPanel.setSpacing(5);

        detailGrid.setWidth("100%");
        detailGrid.setStyleName("hupa-IMAPMessageWidget-Header");
        detailGrid.setText(0, 0, constants.headerFrom() + ":");
        detailGrid.setText(1, 0, constants.headerTo() + ":");
        detailGrid.setText(2, 0, constants.headerCc() + ":");
        detailGrid.setText(3, 0, constants.headerSubject() + ":");
        detailGrid.setText(4, 0, constants.attachments() + ":");
        detailGrid.setWidget(0, 1, from);
        detailGrid.setWidget(1, 1, to);
        detailGrid.setWidget(2, 1, cc);
        detailGrid.setWidget(3, 1, subject);
        detailGrid.setWidget(4, 1, attachments);
        
        detailGrid.getCellFormatter().setHorizontalAlignment(0, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(1, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(2, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(3, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(4, 0, HorizontalPanel.ALIGN_RIGHT);
        
        detailGrid.getCellFormatter().setStyleName(0,0,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(0,1,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(1,0,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(1,1,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(2,0,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(2,1,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(3,0,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(3,1,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(4,0,"hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(4,1,"hupa-IMAPMessageWidget-Header-Content");

        detailGrid.getCellFormatter().setVerticalAlignment(0, 0, VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(1, 0, VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(2, 0, VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(3, 0, VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(4, 0, VerticalPanel.ALIGN_TOP);
        detailGrid.getCellFormatter().setWidth(0, 0, "100px");
        detailGrid.getCellFormatter().setWidth(1, 0, "100px");
        detailGrid.getCellFormatter().setWidth(2, 0, "100px");
        detailGrid.getCellFormatter().setWidth(3, 0, "100px");
        detailGrid.getCellFormatter().setWidth(4, 0, "100px");
        
        mPanel.add(detailGrid);

        deleteMsgButton.setText(constants.deleteMailButton());
        replyMsgButton.setText(constants.replyMailButton());
        replyAllMsgButton.setText(constants.replyAllMailButton());
        forwardMsgButton.setText(constants.forwardMailButton());    
        
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setWidth("100%");
        buttonPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        buttonPanel.addStyleName("hupa-IMAPMessageWidget-ButtonBar");

        
        container.setWidget(showRawButton);
        
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.add(replyMsgButton);
        buttonBar.add(replyAllMsgButton);
        buttonBar.add(deleteMsgButton);
        buttonBar.add(forwardMsgButton);
        buttonBar.setWidth("100%");
        buttonPanel.add(buttonBar);
        
        buttonPanel.add(container);
        buttonPanel.setCellHorizontalAlignment(container, HorizontalPanel.ALIGN_RIGHT);
        buttonPanel.add(backButton);
        buttonPanel.setCellHorizontalAlignment(backButton, HorizontalPanel.ALIGN_RIGHT);
        mPanel.add(buttonPanel);
        msgArea.setWidth("100%");
        ScrollPanel sPanel = new ScrollPanel();
        sPanel.setAlwaysShowScrollBars(false);

        sPanel.add(msgArea);
        mPanel.add(sPanel);

        // TODO: put this in css
        rawFrame.setHeight("600px");
        rawFrame.setWidth("600px");
        rawDialogBox.setText(constants.rawTitle());
        rawDialogBox.add(rawFrame);
        rawDialogBox.setAnimationEnabled(true);
        rawDialogBox.setAutoHideEnabled(true);
        initWidget(mPanel);
    }

    public HasText getCc() {
        return cc;
    }

    public HasHTML getContent() {
        return msgArea;
    }

    public HasText getFrom() {
        return from;
    }

    public HasText getSubject() {
        return subject;
    }

    public HasText getTo() {
        return to;
    }

    public Widget asWidget() {
        return this;
    }

    public void startProcessing() {
        container.setWidget(loading);
        loading.show();
    }

    public void stopProcessing() {
        container.setWidget(showRawButton);
        loading.hide();
    }

    public HasClickHandlers getDeleteButtonClick() {
        return deleteMsgButton;
    }

    public void setAttachments(List<MessageAttachment> attachements,
            final String folder,
            final long uid) {
        attachments.clear();
        if (attachements != null) {
            for (int i = 0; i < attachements.size(); i++) {
                final MessageAttachment a = attachements.get(i);
                Hyperlink link = new Hyperlink(a.getName() + " (" + a.getSize() / 1024
                        + "kB)", true, "");
                link.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                         DOM.setElementAttribute(RootPanel.get("__download")
                                 .getElement(), "src", 
                                 GWT.getModuleBaseURL() + SConsts.SERVLET_DOWNLOAD 
                                 + "?" + SConsts.PARAM_NAME + "=" + a.getName() 
                                 + "&" + SConsts.PARAM_FOLDER + "=" + folder
                                 + "&" + SConsts.PARAM_UID + "=" + uid);
                    }

                });
                HorizontalPanel aPanel = new HorizontalPanel();
                aPanel.add(imageBundle.attachmentIcon().createImage());
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

    public HasDialog getRawMessageDialog() {
        return rawDialogBox;
    }

    public HasURL getRawMessageURL() {
        return rawFrame;
    }
    
}
