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

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.widgets.Loading;
import org.apache.hupa.client.widgets.MyButton;
import org.apache.hupa.widgets.ui.EnableHyperlink;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessageSendView extends Composite implements
        MessageSendPresenter.Display {
    private HupaConstants constants = GWT.create(HupaConstants.class);
    private Grid detailGrid = new Grid(6, 3);
    private Label from = new Label();
    private TextBox to = new TextBox();
    private TextBox cc = new TextBox();
    private TextBox bcc = new TextBox();
    private TextBox subject = new TextBox();
    private BaseUploadStatus uploadStatus = new BaseUploadStatus();
    private MultiUploader uploader = new MultiUploader(uploadStatus);
    private TextArea text = new TextArea();
    private MyButton sendButton = new MyButton(constants.sendButton());
    private EnableHyperlink backButton = new EnableHyperlink(constants.backButton(),"");
    private Loading sendProgress = new Loading(true);

    public MessageSendView() {
        final VerticalPanel mPanel = new VerticalPanel();
        //mPanel.setWidth("100%");
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
        detailGrid.setText(3, 0, constants.headerBcc() + ":");
        detailGrid.setText(4, 0, constants.headerSubject() + ":");
        detailGrid.setText(5, 0, constants.attachments() + ":");
        detailGrid.setWidget(0, 1, from);
        detailGrid.setWidget(1, 1, to);
        detailGrid.setWidget(2, 1, cc);
        detailGrid.setWidget(3, 1, bcc);
        detailGrid.setWidget(4, 1, subject);
        detailGrid.setWidget(5, 1, uploader);
        detailGrid.getCellFormatter().setHorizontalAlignment(0, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(1, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(2, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(3, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(4, 0, HorizontalPanel.ALIGN_RIGHT);
        detailGrid.getCellFormatter().setHorizontalAlignment(5, 0, HorizontalPanel.ALIGN_RIGHT);

        detailGrid.getCellFormatter().setStyleName(0, 0,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(0, 1,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(1, 0,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(1, 1,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(2, 0,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(2, 1,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(3, 0,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(3, 1,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(4, 0,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(4, 1,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(5, 0,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setStyleName(5, 1,
                "hupa-IMAPMessageWidget-Header-Content");
        detailGrid.getCellFormatter().setVerticalAlignment(0, 0,
                VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(1, 0,
                VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(2, 0,
                VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(3, 0,
                VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(4, 0,
                VerticalPanel.ALIGN_MIDDLE);
        detailGrid.getCellFormatter().setVerticalAlignment(5, 0,
                VerticalPanel.ALIGN_TOP);
        detailGrid.getCellFormatter().setWidth(0, 0, "100px");
        detailGrid.getCellFormatter().setWidth(1, 0, "100px");
        detailGrid.getCellFormatter().setWidth(2, 0, "100px");
        detailGrid.getCellFormatter().setWidth(3, 0, "100px");
        detailGrid.getCellFormatter().setWidth(4, 0, "100px");
        detailGrid.getCellFormatter().setWidth(5, 0, "100px");

        from.setWidth("100%");
        cc.setWidth("100%");
        bcc.setWidth("100%");
        to.setWidth("100%");

        subject.setWidth("100%");

        text.setWidth("100%");
        text.setHeight("400px");
        text.setVisibleLines(50);

        uploadStatus.setCancelConfiguration(IUploadStatus.GMAIL_CANCEL_CFG);
        uploader.setServletPath(GWT.getModuleBaseURL() + "uploadAttachmentServlet");
        uploader.avoidRepeatFiles(true);
        uploader.setI18Constants(constants);
        
        mPanel.add(detailGrid);

        HorizontalPanel buttonBar = new HorizontalPanel();
        buttonBar.addStyleName("hupa-IMAPMessageWidget-ButtonBar");
        buttonBar.setWidth("100%");
        buttonBar.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        buttonBar.add(sendButton);
        buttonBar.add(sendProgress);
        buttonBar.add(backButton);
        buttonBar.setCellHorizontalAlignment(backButton, HorizontalPanel.ALIGN_RIGHT);

        mPanel.add(buttonBar);

        mPanel.add(text);
        initWidget(mPanel);

    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    public Widget asWidget() {
        return this;
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    public void startProcessing() {
        sendProgress.show();
        sendButton.setEnabled(false);
        backButton.setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    public void stopProcessing() {
        sendProgress.hide();
        sendButton.setEnabled(true);
        backButton.setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getBccText()
     */
    public HasText getBccText() {
        return bcc;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getCcText()
     */
    public HasText getCcText() {
        return cc;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getFromText()
     */
    public HasText getFromText() {
        return from;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getMessageText()
     */
    public HasText getMessageText() {
        return text;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getSubjectText()
     */
    public HasText getSubjectText() {
        return subject;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getToText()
     */
    public HasText getToText() {
        return to;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getSendClick()
     */
    public HasClickHandlers getSendClick() {
        return sendButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getUploader()
     */
    public IUploader getUploader() {
        return uploader;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#resetUploader()
     */
    public void resetUploader() {
        uploader.removeFromParent();
        uploader = new MultiUploader();
        uploader.setServletPath(GWT.getModuleBaseURL()
                + "uploadAttachmentServlet");
        uploader.avoidRepeatFiles(true);
        detailGrid.setWidget(5, 1, uploader);        
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getSendEnable()
     */
    public HasEnable getSendEnable() {
        return sendButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getBackButtonClick()
     */
    public HasClickHandlers getBackButtonClick() {
        return backButton;
    }
}
