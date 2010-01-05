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

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.CachingDispatchAsync;
import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.DeleteMessageResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class IMAPMessagePresenter extends WidgetPresenter<IMAPMessagePresenter.Display>{

    public interface Display extends WidgetDisplay{
        
        public void setHeaders(Message msg);
        public void setAttachments(List<MessageAttachment> attachements, String folder,  long uid);
        public void setContent(String content);
        
        public HasClickHandlers getShowRawMessageClick();
        public HasClickHandlers getDeleteButtonClick();
        public HasClickHandlers getReplyButtonClick();
        public HasClickHandlers getReplyAllButtonClick();
        public HasClickHandlers getForwardButtonClick();
        public HasClickHandlers getBackButtonClick();
    }

    private MessageDetails messageDetails;
    private Message message;
    private CachingDispatchAsync dispatcher;
    private IMAPFolder folder;
    private User user;

    @Inject
    private IMAPMessagePresenter(IMAPMessagePresenter.Display display,EventBus bus, CachingDispatchAsync dispatcher) {
        super(display,bus);
        this.dispatcher = dispatcher;
    }

    
    public void revealDisplay(User user, IMAPFolder folder, Message message, MessageDetails messageDetails) {
        this.message = message;
        this.messageDetails = messageDetails;
        this.folder = folder;
        this.user = user;
        updateDisplay();
        firePresenterChangedEvent();
        revealDisplay();
    }

    private void updateDisplay() {
        display.setAttachments(messageDetails.getMessageAttachments(), folder.getFullName(),message.getUid());
        display.setHeaders(message);
        display.setContent(messageDetails.getText());
    }

    @Override
    protected void onBind() {
        registerHandler(display.getDeleteButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ArrayList<Long> uidList = new ArrayList<Long>();
                uidList.add(message.getUid());
                dispatcher.execute(new DeleteMessageByUid(folder, uidList), new HupaCallback<DeleteMessageResult>(dispatcher, eventBus) {
                    public void callback(DeleteMessageResult result) {
                        eventBus.fireEvent(new LoadMessagesEvent(user,folder));
                    }
                }); 
            }

        }));
        
        registerHandler(display.getForwardButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ForwardMessageEvent(user,folder,message, messageDetails));
            }
            
        }));
        
        registerHandler(display.getReplyButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ReplyMessageEvent(user,folder,message, messageDetails, false));
            }
            
        }));
        
        registerHandler(display.getReplyAllButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ReplyMessageEvent(user,folder,message, messageDetails, true));
            }
            
        }));
        registerHandler(display.getBackButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new BackEvent());
            }
            
        }));
        registerHandler(display.getShowRawMessageClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String message_url = GWT.getModuleBaseURL() + SConsts.SERVLET_SOURCE + 
                "?" + SConsts.PARAM_UID + "=" + message.getUid() + 
                "&" + SConsts.PARAM_FOLDER + "=" + folder.getFullName();
                Window.open(message_url, "_blank", "");
            }
            
        }));
    }


    @Override
    protected void onUnbind() {
    }


    @Override
    protected void onRevealDisplay() {

    }
}
