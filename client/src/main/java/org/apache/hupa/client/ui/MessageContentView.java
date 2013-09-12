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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.List;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.activity.MessageContentActivity;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.MessageAttachment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class MessageContentView extends Composite implements MessageContentActivity.Displayable {
	@UiField ScrollPanel messageContent;
	HTML messageContentHTML = new HTML();

	//TODO should use a scrolled panel which can contain multiple children
	@UiField FlowPanel attachments;
	@UiField DockLayoutPanel thisPanel;
	@UiField Anchor rawButton;
	@UiField SimplePanel rawPanel;

	@Inject
=======
=======
import org.apache.hupa.client.activity.MessageContentActivity;

>>>>>>> integrate all of the views to their corresponding activities and mappers
import com.google.gwt.core.client.GWT;
=======
import java.util.List;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
=======
>>>>>>> make message content work as expected partly
import org.apache.hupa.client.activity.MessageContentActivity;

import com.google.gwt.core.client.GWT;
<<<<<<< HEAD
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
>>>>>>> prepare for message content panel
=======
>>>>>>> make message content work as expected partly
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

<<<<<<< HEAD
public class MessageContentView extends Composite implements
		MessageContentActivity.Displayable {

<<<<<<< HEAD
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
    public final static int DELETE_BUTTON = 0;
    public final static int REPLY_BUTTON = 1;
    public final static int REPLY_ALL_BUTTON = 2;
    
    private HupaImageBundle imageBundle;

    @UiField
    VerticalPanel messageContainer;
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
    private Anchor showRawButton;
    private Anchor backButton;
    private FlowPanel attachments = new FlowPanel();
    
    private Loading loading;

    @Inject
    public MessageContentView(HupaConstants constants, HupaImageBundle imageBundle) {
        this.imageBundle = imageBundle;
        
        loading = new Loading(constants.loading());
        showRawButton = new Anchor(constants.rawButton());
        backButton = new Anchor(constants.backButton());
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
//        buttonsBar.add(showRawButton); TODO
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
    
=======
public class MessageContentView extends Composite implements MessageContentActivity.Displayable {
>>>>>>> make message content work as expected partly

	@UiField
	HTML messageContent;

<<<<<<< HEAD
>>>>>>> prepare for message content panel
=======
	@Inject
>>>>>>> make message content work as expected partly
	public MessageContentView() {
		initWidget(binder.createAndBindUi(this));
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	@Override
	public void fillMessageContent(String messageDetail) {
		messageContentHTML.setHTML(messageDetail);
		messageContent.add(messageContentHTML);
	}
	
	@Override
	public void showAttachmentPanel(boolean is){
		if(is){
			thisPanel.setWidgetSize(attachments, 216);
		}else{
			thisPanel.setWidgetSize(attachments, 0);
		}
	}

	@Override
	public void setAttachments(List<MessageAttachment> attachements, final String folder, final long uid) {
		attachments.clear();
		final Element downloadIframe = RootPanel.get("__download").getElement();
		if (attachements != null) {
			for (final MessageAttachment messageAttachment : attachements) {
				Label link = new Label(messageAttachment.getName() + " (" + messageAttachment.getSize() / 1024 + "kB)");
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
		messageContentHTML.setHTML("");
	}


	interface Binder extends UiBinder<DockLayoutPanel, MessageContentView> {
	}

	private static Binder binder = GWT.create(Binder.class);

	@Override
	public HasClickHandlers getRaw() {
		return rawButton;
	}

	@Override
	public HasVisibility getRawPanel() {
		return rawPanel;
	}
=======
	interface MessageContentUiBinder extends UiBinder<HTMLPanel, MessageContentView> {
=======
	interface MessageContentUiBinder extends
<<<<<<< HEAD
			UiBinder<HTMLPanel, MessageContentView> {
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
			UiBinder<VerticalPanel, MessageContentView> {
>>>>>>> prepare for message content panel
=======
	interface MessageContentUiBinder extends UiBinder<ScrollPanel, MessageContentView> {
>>>>>>> make message content work as expected partly
=======
	interface MessageContentUiBinder extends UiBinder<SimplePanel, MessageContentView> {
>>>>>>> beautify message list and content
	}

	private static MessageContentUiBinder binder = GWT.create(MessageContentUiBinder.class);

	@Override
	public void fillMessageContent(String messageDetail) {
		messageContent.setHTML(messageDetail);
	}

>>>>>>> make login page as one part of the overall layout & splite layout to little one
}
