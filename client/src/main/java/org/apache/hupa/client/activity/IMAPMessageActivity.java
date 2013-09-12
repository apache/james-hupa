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

package org.apache.hupa.client.activity;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import org.apache.hupa.client.CachingDispatchAsync;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.evo.HupaEvoCallback;
=======
>>>>>>> other RFs
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.SConsts;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.mvp.WidgetDisplayable;
=======
import org.apache.hupa.client.evo.HupaEvoCallback;
>>>>>>> Make the evo more clear.
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.IMAPFolder;
>>>>>>> 1. improve the inbox folder place.
=======
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import org.apache.hupa.shared.data.Message;
=======
>>>>>>> try to change fetch messages to use RF
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
=======
>>>>>>> try to get message details, problem is:
=======
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
>>>>>>> other RFs
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.ReplyMessageEvent;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> 1. improve the inbox folder place.
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
>>>>>>> Allow client can use the domain entity interface.
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.DeleteMessageResult;
=======
>>>>>>> other RFs

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class IMAPMessageActivity extends AbstractActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		updateDisplay();
		bind();
		container.setWidget(display.asWidget());
	}

	public IMAPMessageActivity with(IMAPMessagePlace place) {
		this.message = place.getMessage();
		this.messageDetails = place.getMessageDetails();
		this.folder = place.getFolder();
		this.user = place.getUser();
		return this;
	}

	private void updateDisplay() {
		display.setAttachments(messageDetails.getMessageAttachments(), folder.getFullName(), message.getUid());
		display.setHeaders(message);
		display.setContent(messageDetails.getText());
	}

<<<<<<< HEAD
    private void updateDisplay() {
        display.setAttachments(messageDetails.getMessageAttachments(), folder.getFullName(),message.getUid());
        display.setHeaders(message);
        display.setContent(messageDetails.getText());
    }
    
    protected void bind(){
    	display.getDeleteButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ArrayList<Long> uidList = new ArrayList<Long>();
                uidList.add(message.getUid());
//                dispatcher.execute(new DeleteMessageByUid(folder, uidList), new HupaEvoCallback<DeleteMessageResult>(dispatcher, eventBus) {
//                    public void callback(DeleteMessageResult result) {
//                        eventBus.fireEvent(new LoadMessagesEvent(user,folder));
//                    }
//                }); 
            }

        });
    	display.getForwardButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ForwardMessageEvent(user,folder,message, messageDetails));
            }
            
        });
    	display.getReplyButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ReplyMessageEvent(user,folder,message, messageDetails, false));
            }
            
        });
    	display.getReplyAllButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new ReplyMessageEvent(user,folder,message, messageDetails, true));
            }
            
        });
    	display.getBackButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new BackEvent());
            }
            
        });
    	display.getShowRawMessageClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String message_url = GWT.getModuleBaseURL() + SConsts.SERVLET_SOURCE + 
                "?" + SConsts.PARAM_UID + "=" + message.getUid() + 
                "&" + SConsts.PARAM_FOLDER + "=" + folder.getFullName();
                Window.open(message_url, "_blank", "");
            }
            
        });
    	
    }
    
    private MessageDetails messageDetails;
    private Message message;
<<<<<<< HEAD
    private CachingDispatchAsync dispatcher;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private IMAPFolderProxy folder;
=======
    private IMAPFolder folder;
>>>>>>> 1. improve the inbox folder place.
=======
    private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
=======
>>>>>>> fix issue 2&3. 	Handle exceptions thrown in async blocks & Simply injection code
    private ImapFolder folder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
    private User user;
//    @Inject private CachingDispatchAsync dispatcher;
    @Inject private Displayable display;
    @Inject private EventBus eventBus;
    @Inject private PlaceController placeController;
	public interface Displayable extends WidgetDisplayable{
        public void setHeaders(Message msg);
        public void setAttachments(List<MessageAttachment> attachements, String folder,  long uid);
        public void setContent(String content);
        
        public HasClickHandlers getShowRawMessageClick();
        public HasClickHandlers getDeleteButtonClick();
        public HasClickHandlers getReplyButtonClick();
        public HasClickHandlers getReplyAllButtonClick();
        public HasClickHandlers getForwardButtonClick();
        public HasClickHandlers getBackButtonClick();
=======
	protected void bind() {
		display.getDeleteButtonClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ArrayList<Long> uidList = new ArrayList<Long>();
				uidList.add(message.getUid());
				DeleteMessageByUidRequest req = requestFactory.deleteMessageByUidRequest();
				DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
				action.setMessageUids(uidList);
				action.setFolder(folder);
				req.delete(action).fire(new Receiver<DeleteMessageResult>() {
					@Override
					public void onSuccess(DeleteMessageResult response) {
						eventBus.fireEvent(new LoadMessagesEvent(user, folder));
					}
				});
			}

		});
		display.getForwardButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ForwardMessageEvent(user, folder, message, messageDetails));
			}

		});
		display.getReplyButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ReplyMessageEvent(user, folder, message, messageDetails, false));
			}

		});
		display.getReplyAllButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ReplyMessageEvent(user, folder, message, messageDetails, true));
			}

		});
		display.getBackButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new BackEvent());
			}

		});
		display.getShowRawMessageClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				String message_url = GWT.getModuleBaseURL() + SConsts.SERVLET_SOURCE + "?" + SConsts.PARAM_UID + "="
				        + message.getUid() + "&" + SConsts.PARAM_FOLDER + "=" + folder.getFullName();
				Window.open(message_url, "_blank", "");
			}

		});

	}

	private MessageDetails messageDetails;
	private Message message;
	private ImapFolder folder;
	private User user;
	// @Inject private CachingDispatchAsync dispatcher;
	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	@Inject private HupaRequestFactory requestFactory;

	public interface Displayable extends WidgetDisplayable {
		public void setHeaders(Message msg);
		public void setAttachments(List<MessageAttachment> attachements, String folder, long uid);
		public void setContent(String content);

		public HasClickHandlers getShowRawMessageClick();
		public HasClickHandlers getDeleteButtonClick();
		public HasClickHandlers getReplyButtonClick();
		public HasClickHandlers getReplyAllButtonClick();
		public HasClickHandlers getForwardButtonClick();
		public HasClickHandlers getBackButtonClick();
>>>>>>> other RFs
	}
}
