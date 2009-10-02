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

import net.customware.gwt.presenter.client.DisplayCallback;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.CachingDispatchAsync;
import org.apache.hupa.client.SessionAsyncCallback;
import org.apache.hupa.client.mvp.MessageSendPresenter.Type;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.BackEventHandler;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.ForwardMessageEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.events.NewMessageEventHandler;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.events.ReplyMessageEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
import org.apache.hupa.shared.rpc.GetMessageDetails;
import org.apache.hupa.shared.rpc.GetMessageDetailsResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainPresenter extends WidgetPresenter<MainPresenter.Display>{

	
	public interface Display extends WidgetDisplay{
		public HasClickHandlers getSearchClick();
		public HasValue<String> getSearchValue();
		public void fillOracle(ArrayList<Message> messages);
		public void setCenter(Widget widget);
		public void setWest(Widget widget);
	}
	
	private CachingDispatchAsync cachingDispatcher;
	private User user;
	private IMAPFolder folder;
	private String searchValue;
	private IMAPMessageListPresenter messageListPresenter;
	private IMAPMessagePresenter messagePresenter;
	private MessageSendPresenter sendPresenter;
	private IMAPFolderPresenter folderPresenter;
	public static final Place PLACE = new Place("Main");
	
	@Inject
	public MainPresenter(MainPresenter.Display display, EventBus bus, CachingDispatchAsync cachingDispatcher,IMAPFolderPresenter folderPresenter, IMAPMessageListPresenter messageListPresenter, IMAPMessagePresenter messagePresenter, MessageSendPresenter sendPresenter) {
		super(display,bus);
		this.cachingDispatcher = cachingDispatcher;
		this.messageListPresenter = messageListPresenter;
		this.messagePresenter = messagePresenter;
		this.sendPresenter = sendPresenter;
		this.folderPresenter = folderPresenter;
	}
	
	

	private void showMessageTable(User user, IMAPFolder folder, String searchValue,boolean refresh) {
		this.user = user;
		this.folder = folder;
		this.searchValue = searchValue;
		
		messagePresenter.unbind();
		sendPresenter.unbind();
		
		messageListPresenter.bind(user, folder, searchValue);
		if (refresh) {
			messageListPresenter.refreshDisplay();
		}
		display.setCenter(messageListPresenter.getDisplay().asWidget());
	}
	
	private void showMessage(User user, IMAPFolder folder, Message message, MessageDetails details) {
		sendPresenter.unbind();
		messageListPresenter.unbind();
		
		messagePresenter.bind(user,folder,message,details);
		display.setCenter(messagePresenter.getDisplay().asWidget());
	}
	
	
	private void showNewMessage() {
		messagePresenter.unbind();
		messageListPresenter.unbind();
		
		sendPresenter.bind(user, Type.NEW);
		display.setCenter(sendPresenter.getDisplay().asWidget());
	}
	
	private void showForwardMessage(ForwardMessageEvent event) {
		messagePresenter.unbind();
		messageListPresenter.unbind();

		sendPresenter.bind(event.getUser(),event.getFolder(),event.getMessage(), event.getMessageDetails(), Type.FORWARD);
		display.setCenter(sendPresenter.getDisplay().asWidget());
	}
	
	private void showReplyMessage(ReplyMessageEvent event) {
		messagePresenter.unbind();
		messageListPresenter.unbind();

		if (event.getReplyAll()) {
			sendPresenter.bind(event.getUser(),event.getFolder(),event.getMessage(),event.getMessageDetails(), Type.REPLY_ALL);
		} else {
			sendPresenter.bind(event.getUser(),event.getFolder(),event.getMessage(),event.getMessageDetails(), Type.REPLY);

		}
		display.setCenter(sendPresenter.getDisplay().asWidget());
	}
	private void reset() {
		display.getSearchValue().setValue("");
		cachingDispatcher.clear();
	}
	

	@Override
	public Place getPlace() {
		return PLACE;
	}

	private void showIMAPFolders(User user) {
		folderPresenter.bind(user);
		display.setWest(folderPresenter.getDisplay().asWidget());

	}
	
	public void bind(User user) {
		this.user = user;
		folder = new IMAPFolder(user.getSettings().getInboxFolderName());

		bind();
		refreshDisplay();
	}
	
	@Override
	protected void onBind() {
	
		registerHandler(eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {

			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				showMessageTable(loadMessagesEvent.getUser(), loadMessagesEvent.getFolder(), loadMessagesEvent.getSearchValue(), true);
			}
			
		}));
		registerHandler(eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {

			public void onMessagesReceived(MessagesReceivedEvent event) {
				
				// fill the oracle
				display.fillOracle(event.getMessages());
			}
			
		}));
		
		registerHandler(eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

			public void onExpandMessage(ExpandMessageEvent event) {
				final boolean decreaseUnseen;
				final Message message = event.getMessage();
				// check if the message was already seen in the past
				if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
					decreaseUnseen = true;
				} else {
					decreaseUnseen = false;
				}
				cachingDispatcher.executeWithCache(new GetMessageDetails(event.getFolder(),message.getUid()), new SessionAsyncCallback<GetMessageDetailsResult>(new DisplayCallback<GetMessageDetailsResult>(display) {

					@Override
					protected void handleFailure(Throwable e) {
						GWT.log("ERROR", e);
					}

					@Override
					protected void handleSuccess(GetMessageDetailsResult result) {
						// decrease the unseen count if we were able to expose the message
						if (decreaseUnseen) {
							eventBus.fireEvent(new DecreaseUnseenEvent(user,folder));
						}
						
						showMessage(user, folder, message, result.getMessageDetails());
					}

					
				}, eventBus, user));
			}
			
		}));
		registerHandler(eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {

			public void onNewMessageEvent(NewMessageEvent event) {
				showNewMessage();
			}
			
		}));
		
		registerHandler(eventBus.addHandler(SentMessageEvent.TYPE, new SentMessageEventHandler() {

			public void onSentMessageEvent(SentMessageEvent ev) {
				showMessageTable(user,folder,searchValue, false);
			}
			
		}));
		
		registerHandler(eventBus.addHandler(ForwardMessageEvent.TYPE, new ForwardMessageEventHandler() {

			public void onForwardMessageEvent(ForwardMessageEvent event) {
				showForwardMessage(event);
			}
			
		}));
		registerHandler(eventBus.addHandler(ReplyMessageEvent.TYPE, new ReplyMessageEventHandler() {

			public void onReplyMessageEvent(ReplyMessageEvent event) {
				showReplyMessage(event);
			}
			
		}));
		registerHandler(eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {

			public void onFolderSelectionEvent(FolderSelectionEvent event) {
				showMessageTable(user,event.getFolder(),searchValue, true);
			}
			
		}));		
		
		registerHandler(display.getSearchClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				String searchValue = null;
				if (display.getSearchValue().getValue().trim().length() >0) {
					searchValue = display.getSearchValue().getValue().trim();
				}
				eventBus.fireEvent(new LoadMessagesEvent(user,folder,searchValue));
			}
			
		}));
		
		registerHandler(eventBus.addHandler(BackEvent.TYPE, new BackEventHandler() {

			public void onBackEvent(BackEvent event) {
				showMessageTable(user, folder, searchValue, false);
			}
			
		}));
		
	}


	@Override
	protected void onPlaceRequest(PlaceRequest request) {
	}

	@Override
	protected void onUnbind() {
		messagePresenter.unbind();
		sendPresenter.unbind();
		messageListPresenter.unbind();
		folderPresenter.unbind();
		reset();
	
	}

	public void refreshDisplay() {
		showIMAPFolders(user);
		showMessageTable(user,folder,null,true);
	}

	public void revealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
