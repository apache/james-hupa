<<<<<<< HEAD
<<<<<<< HEAD
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

import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.BackEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.ForwardMessageEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.events.NewMessageEventHandler;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.events.ReplyMessageEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
import org.apache.hupa.widgets.ui.HasEditable;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class WestActivity extends AppBaseActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		container.setWidget(display.asWidget());
	}

	public void setCurrentPlace(Place place) {
		this.currentPlace = place;
	}

	public WestActivity with(FolderPlace place) {
		this.currentPlace = place;
//		this.user = place.getUser();
		this.folder = new ImapFolderImpl(user.getSettings().getInboxFolderName());
		return this;
	}

	private void cloneFolder(ImapFolder desc, ImapFolder src) {
	    desc.setChildren(src.getChildren());
		desc.setDelimiter(src.getDelimiter());
		desc.setFullName(src.getFullName());
		desc.setMessageCount(src.getMessageCount());
		desc.setName(src.getName());
		desc.setSubscribed(src.getSubscribed());
		desc.setUnseenMessageCount(src.getUnseenMessageCount());
    }

	private void bind() {
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {
			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				showMessageTable(loadMessagesEvent.getUser(), loadMessagesEvent.getFolder(),
				        loadMessagesEvent.getSearchValue());
			}
		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {
			public void onExpandMessage(ExpandMessageEvent event) {
//				final boolean decreaseUnseen;
				final Message message = event.getMessage();
				// check if the message was already seen in the past
				if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
//					decreaseUnseen = true;//TODO 1209
				} else {
//					decreaseUnseen = false;
				}

				GetMessageDetailsRequest req = rf.messageDetailsRequest();
				GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
				final ImapFolder f = req.create(ImapFolder.class);
//				event.getFolder().setFolderTo(f);
				cloneFolder(f, event.getFolder());
				action.setFolder(f);
				action.setUid(message.getUid());
				req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
					@Override
					public void onSuccess(GetMessageDetailsResult response) {
						/*
						 * TODO if (decreaseUnseen) { eventBus.fireEvent(new
						 * DecreaseUnseenEvent(user, folder)); }
						 */
//						placeController.goTo(messagePlaceProvider.get().with(user, f, message,
//						        response.getMessageDetails()));
					}
				});
			}
		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {
			public void onNewMessageEvent(NewMessageEvent event) {
				showNewMessage();
			}
		});
		eventBus.addHandler(SentMessageEvent.TYPE, new SentMessageEventHandler() {
			public void onSentMessageEvent(SentMessageEvent ev) {
				showMessageTable(user, folder, searchValue);
			}
		});
		eventBus.addHandler(ForwardMessageEvent.TYPE, new ForwardMessageEventHandler() {
			public void onForwardMessageEvent(ForwardMessageEvent event) {
				showForwardMessage(event);
			}
		});
		eventBus.addHandler(ReplyMessageEvent.TYPE, new ReplyMessageEventHandler() {
			public void onReplyMessageEvent(ReplyMessageEvent event) {
				showReplyMessage(event);
			}
		});
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {
			public void onFolderSelectionEvent(FolderSelectionEvent event) {
				user = event.getUser();
				folder = event.getFolder();
				showMessageTable(user, event.getFolder(), searchValue);
			}
		});
		eventBus.addHandler(BackEvent.TYPE, new BackEventHandler() {
			public void onBackEvent(BackEvent event) {
				showMessageTable(user, folder, searchValue);
			}
		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {
			public void onExpandMessage(ExpandMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}
		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {
			public void onNewMessageEvent(NewMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}
		});
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			public void onLogin(LoginEvent event) {
				user = event.getUser();
				searchValue = null;
			}
		});
		exportJSMethods(this);
	}

	public void openLink(String url) {
		Window.open(url, "_blank", "");
	}

	public void mailTo(String mailto) {
		// sendPresenter.revealDisplay(user, mailto);
	}

	private native void exportJSMethods(WestActivity westactivity) /*-{
	                                                               $wnd.openLink = function(url) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::openLink(Ljava/lang/String;) (url);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               $wnd.mailTo = function(mail) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::mailTo(Ljava/lang/String;) (mail);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               }-*/;

	private void showMessageTable(User user, ImapFolder folder, String searchValue) {
		this.user = user;
		this.folder = folder;
		this.searchValue = searchValue;

		// FIXME goto?
//		placeController.goTo(new MailFolderPlace().with(user, folder, searchValue));
	}

	private void showNewMessage() {
		pc.goTo(this.messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
	}

	private void showForwardMessage(ForwardMessageEvent event) {
		pc.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), Type.FORWARD));
	}

	private void showReplyMessage(ReplyMessageEvent event) {
		pc.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), event.getReplyAll() ? Type.REPLY_ALL : Type.REPLY));
	}

	@Inject private Displayable display;
	@Inject private Provider<MessageSendPlace> messageSendPlaceProvider;
//	@Inject private Provider<IMAPMessagePlace> messagePlaceProvider;
	private User user;
	private ImapFolder folder;
	private IMAPTreeItem tItem;
	private HasEditable editableTreeItem;
	private String searchValue;
	private Place currentPlace;

	public interface Displayable extends WidgetDisplayable {
	}
=======
=======
>>>>>>> Change to new mvp framework - first step
package org.apache.hupa.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

<<<<<<< HEAD

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> try to change fetch messages to use RF
import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.rf.CreateFolderRequest;
import org.apache.hupa.client.rf.DeleteFolderRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.ImapFolderRequest;
import org.apache.hupa.client.rf.RenameFolderRequest;
import org.apache.hupa.client.ui.WidgetContainerDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.BackEventHandler;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.DecreaseUnseenEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.ForwardMessageEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.IncreaseUnseenEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.events.NewMessageEventHandler;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.events.ReplyMessageEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
<<<<<<< HEAD
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.DeleteFolder;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.GetMessageDetails;
import org.apache.hupa.shared.rpc.GetMessageDetailsResult;
=======
import org.apache.hupa.client.HupaEvoCallback;
=======
>>>>>>> Make the evo more clear.
import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.evo.HupaEvoCallback;
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.IMAPFolderRequestContext;
import org.apache.hupa.client.ui.WidgetContainerDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.BackEventHandler;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.DecreaseUnseenEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.ForwardMessageEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.IncreaseUnseenEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.events.NewMessageEventHandler;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.events.ReplyMessageEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.DeleteFolder;
import org.apache.hupa.shared.rpc.GenericResult;
<<<<<<< HEAD
>>>>>>> Change to new mvp framework - first step
=======
import org.apache.hupa.shared.rpc.GetMessageDetails;
import org.apache.hupa.shared.rpc.GetMessageDetailsResult;
>>>>>>> 
import org.apache.hupa.shared.rpc.RenameFolder;
=======
>>>>>>> try to change fetch messages to use RF
import org.apache.hupa.widgets.event.EditEvent;
import org.apache.hupa.widgets.event.EditHandler;
import org.apache.hupa.widgets.ui.HasEditable;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;
import com.google.inject.Provider;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.web.bindery.requestfactory.shared.Receiver;
=======
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> 
=======
import com.google.web.bindery.requestfactory.shared.Receiver;
<<<<<<< HEAD
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
=======
import com.google.web.bindery.requestfactory.shared.ServerFailure;
>>>>>>> Succeed creating new folder

public class WestActivity extends AbstractActivity {

<<<<<<< HEAD
	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	private final Provider<IMAPMessagePlace> IMAPMessagePlaceProvider;
	private final Provider<MessageSendPlace> messageSendPlaceProvider;
	private final Provider<IMAPMessagePlace> messagePlaceProvider;
<<<<<<< HEAD
	
    private DispatchAsync dispatcher;
    private User user;
    private IMAPFolderProxy folder;
    private IMAPTreeItem tItem;
    private HasEditable editableTreeItem;
    private String searchValue;
    
    private Place currentPlace;
    
    public void setCurrentPlace(Place place){
    	this.currentPlace = place;
    }
    
    @Inject
    public WestActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			DispatchAsync dispatcher,Provider<IMAPMessagePlace> IMAPMessagePlaceProvider,Provider<MessageSendPlace> messageSendPlaceProvider,Provider<IMAPMessagePlace> messagePlaceProvider){
=======
=======
	private final Provider<MailInboxPlace> mailInboxPlaceProvider;
=======
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
	private final Provider<IMAPMessagePlace> IMAPMessagePlaceProvider;
	private final Provider<MessageSendPlace> messageSendPlaceProvider;
>>>>>>> 
=======
>>>>>>> 1. improve the inbox folder place.
=======
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		display.setUser(user);
		loadTreeItems();
		bind();
		container.setWidget(display.asWidget());
	}

	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	@Inject private Provider<IMAPMessagePlace> IMAPMessagePlaceProvider;
	@Inject private Provider<MessageSendPlace> messageSendPlaceProvider;
	@Inject private Provider<IMAPMessagePlace> messagePlaceProvider;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> fix issue 2&3. 	Handle exceptions thrown in async blocks & Simply injection code
	
    private User user;
    private ImapFolder folder;
    private IMAPTreeItem tItem;
    private HasEditable editableTreeItem;
    private String searchValue;
    
    private Place currentPlace;
    
    public void setCurrentPlace(Place place){
    	this.currentPlace = place;
    }
<<<<<<< HEAD
    
    @Inject
    public WestActivity(Displayable display, EventBus eventBus, PlaceController placeController,
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
			DispatchAsync dispatcher){
>>>>>>> Change to new mvp framework - first step
=======
			DispatchAsync dispatcher,Provider<MailInboxPlace> mailInboxPlaceProvider,Provider<IMAPMessagePlace> IMAPMessagePlaceProvider,Provider<MessageSendPlace> messageSendPlaceProvider){
>>>>>>> 
=======
			DispatchAsync dispatcher,Provider<IMAPMessagePlace> IMAPMessagePlaceProvider,Provider<MessageSendPlace> messageSendPlaceProvider){
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
=======
			DispatchAsync dispatcher,Provider<IMAPMessagePlace> IMAPMessagePlaceProvider,Provider<MessageSendPlace> messageSendPlaceProvider,Provider<IMAPMessagePlace> messagePlaceProvider){
>>>>>>> 1. improve the inbox folder place.
    	this.dispatcher = dispatcher;
    	this.display = display;
    	this.eventBus = eventBus;
    	this.placeController = placeController;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    	this.IMAPMessagePlaceProvider = IMAPMessagePlaceProvider;
    	this.messageSendPlaceProvider = messageSendPlaceProvider;
    	this.messagePlaceProvider = messagePlaceProvider;
<<<<<<< HEAD
=======
>>>>>>> Change to new mvp framework - first step
=======
    	this.mailInboxPlaceProvider = mailInboxPlaceProvider;
=======
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
    	this.IMAPMessagePlaceProvider = IMAPMessagePlaceProvider;
    	this.messageSendPlaceProvider = messageSendPlaceProvider;
>>>>>>> 
=======
>>>>>>> 1. improve the inbox folder place.
    	
    }

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		display.setUser(user);
		loadTreeItems();
		bind();
		container.setWidget(display.asWidget());
	}
	
<<<<<<< HEAD
<<<<<<< HEAD
    public WestActivity with(MailFolderPlace place){
    	this.currentPlace = place;
    	this.user = place.getUser();
=======
    public WestActivity with(User user){
    	this.user = user;
>>>>>>> Change to new mvp framework - first step
=======
=======
    	
>>>>>>> fix issue 2&3. 	Handle exceptions thrown in async blocks & Simply injection code
    public WestActivity with(MailFolderPlace place){
    	this.currentPlace = place;
    	this.user = place.getUser();
>>>>>>> 1. improve the inbox folder place.
    	return this;
    }

    protected void loadTreeItems() {
        display.setLoadingFolders(true);
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
        
        HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
=======
=======
	@Inject private HupaRequestFactory requestFactory;
>>>>>>> Succeed creating new folder

	private User user;
	private ImapFolder folder;
	private IMAPTreeItem tItem;
	private HasEditable editableTreeItem;
	private String searchValue;

	private Place currentPlace;

	public void setCurrentPlace(Place place) {
		this.currentPlace = place;
	}

	public WestActivity with(MailFolderPlace place) {
		this.currentPlace = place;
		this.user = place.getUser();
		return this;
	}

	protected void loadTreeItems() {
		display.setLoadingFolders(true);
		HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
>>>>>>> try to change fetch messages to use RF
		rf.initialize(eventBus);
		ImapFolderRequest folderRequest = rf.folderRequest();
		folderRequest.requestFolders().fire(new Receiver<List<ImapFolder>>() {
			@Override
<<<<<<< HEAD
			public void onSuccess(List<IMAPFolderProxy> response) {
<<<<<<< HEAD
<<<<<<< HEAD
=======
			public void onSuccess(List<ImapFolder> response) {
<<<<<<< HEAD
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
              display.bindTreeItems(createTreeNodes(response));
<<<<<<< HEAD
=======
System.out.println("1111111"+response);
              display.bindTreeItems(null);
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
=======
              display.bindTreeItems(createTreeNodes(response));
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
//              // disable
=======
>>>>>>> Allow client can use the domain entity interface.
              display.getDeleteEnable().setEnabled(false);
              display.getRenameEnable().setEnabled(false);
              display.setLoadingFolders(false);
				
=======
				display.bindTreeItems(createTreeNodes(response));
				display.getDeleteEnable().setEnabled(false);
				display.getRenameEnable().setEnabled(false);
				display.setLoadingFolders(false);
>>>>>>> try to change fetch messages to use RF
			}
		});
<<<<<<< HEAD
        
//        dispatcher.execute(new FetchFolders(), new HupaEvoCallback<FetchFoldersResult>(dispatcher, eventBus, display) {
//            public void callback(FetchFoldersResult result) {
//                display.bindTreeItems(createTreeNodes(result.getFolders()));
//                // disable
//                display.getDeleteEnable().setEnabled(false);
//                display.getRenameEnable().setEnabled(false);
//                display.setLoadingFolders(false);
//
//            }
//        });
        
<<<<<<< HEAD
=======
        dispatcher.execute(new FetchFolders(), new HupaEvoCallback<FetchFoldersResult>(dispatcher, eventBus, display) {
            public void callback(FetchFoldersResult result) {
                display.bindTreeItems(createTreeNodes(result.getFolders()));
                // disable
                display.getDeleteEnable().setEnabled(false);
                display.getRenameEnable().setEnabled(false);
                display.setLoadingFolders(false);

            }
        });
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
    }

    /**
     * Create recursive the TreeNodes with all childs
     * 
     * @param list
     * @return
     */
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolderProxy> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolderProxy iFolder : list) {
=======
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolder> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolder iFolder : list) {
>>>>>>> Change to new mvp framework - first step
=======
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolderProxy> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolderProxy iFolder : list) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private List<IMAPTreeItem> createTreeNodes(List<ImapFolder> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (ImapFolder iFolder : list) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.

            final IMAPTreeItem record = new IMAPTreeItem(iFolder);
//            record.addEditHandler(new EditHandler() {
//
//                public void onEditEvent(EditEvent event) {
//                    if (event.getEventType().equals(EditEvent.EventType.Stop)) {
//                        ImapFolderImpl iFolder = new ImapFolderImpl((String) event.getOldValue());
//                        final String newName = (String) event.getNewValue();
//                        if (iFolder.getFullName().equalsIgnoreCase(newName) == false) {
//                            dispatcher.execute(new RenameFolder(iFolder, newName), new HupaEvoCallback<GenericResult>(dispatcher, eventBus) {
//                                public void callback(GenericResult result) {
//                                    folder.setFullName(newName);
//                                }
//                                public void callbackError(Throwable caught) {
//                                    record.cancelEdit();
//                                }
//                            }); 
//                        }
//                    }
//                }
//
//            });
            record.setUserObject(iFolder);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            List<IMAPFolderProxy> childFolders = iFolder.getChildIMAPFolders();
=======
            List<IMAPFolder> childFolders = iFolder.getChildIMAPFolders();
>>>>>>> Change to new mvp framework - first step
=======
            List<IMAPFolderProxy> childFolders = iFolder.getChildIMAPFolders();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
            List<IMAPFolderProxy> childFolders = iFolder.getChildren();
>>>>>>> 
=======
            List<ImapFolder> childFolders = iFolder.getChildren();
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
            List<IMAPTreeItem> items = createTreeNodes(childFolders);
            for (IMAPTreeItem item : items) {
                record.addItem(item);
            }

            // Store the INBOX as starting point after first loading
            if (iFolder.getFullName().equals(user.getSettings().getInboxFolderName())) {
                folder = iFolder;
                tItem = record;
            }

            tList.add(record);
        }

        // Sort tree
        Collections.sort(tList, new Comparator<TreeItem>() {

            public int compare(TreeItem o1, TreeItem o2) {
                return o1.getText().compareTo(o2.getText());
            }

        });
        return tList;
    }
	private void bind(){
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 
=======
	}

	/**
	 * Create recursive the TreeNodes with all childs
	 * 
	 * @param list
	 * @return
	 */
	private List<IMAPTreeItem> createTreeNodes(List<ImapFolder> list) {
		List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

		for (ImapFolder iFolder : list) {

			final IMAPTreeItem record = new IMAPTreeItem(iFolder);
			record.addEditHandler(new EditHandler() {

				public void onEditEvent(EditEvent event) {
					if (event.getEventType().equals(EditEvent.EventType.Stop)) {
						ImapFolderImpl iFolder = new ImapFolderImpl((String) event.getOldValue());
						final String newName = (String) event.getNewValue();
						if (iFolder.getFullName().equalsIgnoreCase(newName) == false) {
							RenameFolderRequest req = requestFactory.renameFolderRequest();
							RenameFolderAction action = req.create(RenameFolderAction.class);
							action.setNewName(newName);
							action.setFolder(iFolder);
							req.rename(action).fire(new Receiver<GenericResult>() {
								@Override
								public void onSuccess(GenericResult response) {
									folder.setFullName(newName);
								}
								@Override
								public void onFailure(ServerFailure error) {
									record.cancelEdit();
									GWT.log("Error while renaming" + error.getStackTraceString());
								}
							});
						}
					}
				}

			});
			record.setUserObject(iFolder);

			List<ImapFolder> childFolders = iFolder.getChildren();
			List<IMAPTreeItem> items = createTreeNodes(childFolders);
			for (IMAPTreeItem item : items) {
				record.addItem(item);
			}

			// Store the INBOX as starting point after first loading
			if (iFolder.getFullName().equals(user.getSettings().getInboxFolderName())) {
				folder = iFolder;
				tItem = record;
			}

			tList.add(record);
		}

		// Sort tree
		Collections.sort(tList, new Comparator<TreeItem>() {

			public int compare(TreeItem o1, TreeItem o2) {
				return o1.getText().compareTo(o2.getText());
			}

		});
		return tList;
	}

	private void bind() {
>>>>>>> try to change fetch messages to use RF
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {

			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				showMessageTable(loadMessagesEvent.getUser(), loadMessagesEvent.getFolder(),
				        loadMessagesEvent.getSearchValue());
			}

		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

<<<<<<< HEAD
            public void onExpandMessage(ExpandMessageEvent event) {
                final boolean decreaseUnseen;
                final Message message = event.getMessage();
                // check if the message was already seen in the past
                if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
                    decreaseUnseen = true;
                } else {
                    decreaseUnseen = false;
                }

                display.setLoadingMessage(true);
<<<<<<< HEAD
                dispatcher.execute(new GetMessageDetails(event.getFolder(), message.getUid()), new HupaEvoCallback<GetMessageDetailsResult>(dispatcher, eventBus, display) {
                    public void callback(GetMessageDetailsResult result) {
                        if (decreaseUnseen) {
                            eventBus.fireEvent(new DecreaseUnseenEvent(user, folder));
                        }
                        display.setLoadingMessage(false);
<<<<<<< HEAD
<<<<<<< HEAD
//                        showMessage(user, folder, message, result.getMessageDetails());

                        placeController.goTo(messagePlaceProvider.get().with(user,folder, message,result.getMessageDetails()));
=======
                        showMessage(user, folder, message, result.getMessageDetails());
>>>>>>> 
=======
//                        showMessage(user, folder, message, result.getMessageDetails());

                        placeController.goTo(messagePlaceProvider.get().with(user,folder, message,result.getMessageDetails()));
>>>>>>> 1. improve the inbox folder place.
                    }
                });
=======
//                dispatcher.execute(new GetMessageDetails(event.getFolder(), message.getUid()), new HupaEvoCallback<GetMessageDetailsResult>(dispatcher, eventBus, display) {
//                    public void callback(GetMessageDetailsResult result) {
//                        if (decreaseUnseen) {
//                            eventBus.fireEvent(new DecreaseUnseenEvent(user, folder));
//                        }
//                        display.setLoadingMessage(false);
////                        showMessage(user, folder, message, result.getMessageDetails());
//
//                        placeController.goTo(messagePlaceProvider.get().with(user,folder, message,result.getMessageDetails()));
//                    }
//                });
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
            }
=======
			public void onExpandMessage(ExpandMessageEvent event) {
				final boolean decreaseUnseen;
				final Message message = event.getMessage();
				// check if the message was already seen in the past
				if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
					decreaseUnseen = true;
				} else {
					decreaseUnseen = false;
				}

				display.setLoadingMessage(true);
				// dispatcher.execute(new
				// GetMessageDetails(event.getFolder(),
				// message.getUid()), new
				// HupaEvoCallback<GetMessageDetailsResult>(dispatcher,
				// eventBus, display) {
				// public void callback(GetMessageDetailsResult result)
				// {
				// if (decreaseUnseen) {
				// eventBus.fireEvent(new DecreaseUnseenEvent(user,
				// folder));
				// }
				// display.setLoadingMessage(false);
				// // showMessage(user, folder, message,
				// result.getMessageDetails());
				//
				// placeController.goTo(messagePlaceProvider.get().with(user,folder,
				// message,result.getMessageDetails()));
				// }
				// });
			}
>>>>>>> try to change fetch messages to use RF

		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {

			public void onNewMessageEvent(NewMessageEvent event) {
				showNewMessage();
			}

		});
		eventBus.addHandler(SentMessageEvent.TYPE, new SentMessageEventHandler() {

			public void onSentMessageEvent(SentMessageEvent ev) {
				showMessageTable(user, folder, searchValue);
			}

		});
		eventBus.addHandler(ForwardMessageEvent.TYPE, new ForwardMessageEventHandler() {

			public void onForwardMessageEvent(ForwardMessageEvent event) {
				showForwardMessage(event);
			}

		});
		eventBus.addHandler(ReplyMessageEvent.TYPE, new ReplyMessageEventHandler() {

			public void onReplyMessageEvent(ReplyMessageEvent event) {
				showReplyMessage(event);
			}

		});
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {

			public void onFolderSelectionEvent(FolderSelectionEvent event) {
				user = event.getUser();
				folder = event.getFolder();
				showMessageTable(user, event.getFolder(), searchValue);
			}

		});
		eventBus.addHandler(BackEvent.TYPE, new BackEventHandler() {

			public void onBackEvent(BackEvent event) {
				showMessageTable(user, folder, searchValue);
			}

		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

			public void onExpandMessage(ExpandMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}

		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {

			public void onNewMessageEvent(NewMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}

		});
		eventBus.addHandler(DecreaseUnseenEvent.TYPE, new DecreaseUnseenEventHandler() {

			public void onDecreaseUnseenEvent(DecreaseUnseenEvent event) {
				display.decreaseUnseenMessageCount(event.getFolder(), event.getAmount());
			}

		});
		eventBus.addHandler(IncreaseUnseenEvent.TYPE, new IncreaseUnseenEventHandler() {

			public void onIncreaseUnseenEvent(IncreaseUnseenEvent event) {
				display.increaseUnseenMessageCount(event.getFolder(), event.getAmount());
			}

		});
		display.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

<<<<<<< HEAD
            public void onSelection(SelectionEvent<TreeItem> event) {
                tItem = (IMAPTreeItem) event.getSelectedItem();
                if (tItem.isEdit()) 
                    return;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                folder = (IMAPFolderProxy) tItem.getUserObject();
=======
                folder = (IMAPFolder) tItem.getUserObject();
>>>>>>> 
=======
                folder = (IMAPFolderProxy) tItem.getUserObject();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
                folder = (ImapFolder) tItem.getUserObject();
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
                eventBus.fireEvent(new LoadMessagesEvent(user, folder));
            }
=======
			public void onSelection(SelectionEvent<TreeItem> event) {
				tItem = (IMAPTreeItem) event.getSelectedItem();
				if (tItem.isEdit())
					return;
				folder = (ImapFolder) tItem.getUserObject();
				eventBus.fireEvent(new LoadMessagesEvent(user, folder));
			}
>>>>>>> try to change fetch messages to use RF

		});
		display.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

<<<<<<< HEAD
            public void onSelection(SelectionEvent<TreeItem> event) {
                tItem = (IMAPTreeItem) event.getSelectedItem();
                if (tItem.isEdit()) 
                    return;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                folder = (IMAPFolderProxy) tItem.getUserObject();
=======
                folder = (IMAPFolder) tItem.getUserObject();
>>>>>>> 
=======
                folder = (IMAPFolderProxy) tItem.getUserObject();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
                folder = (ImapFolder) tItem.getUserObject();
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
                if (folder.getFullName().equalsIgnoreCase(user.getSettings().getInboxFolderName())) {
                    display.getDeleteEnable().setEnabled(false);
                    display.getRenameEnable().setEnabled(false);
                } else {
                    display.getDeleteEnable().setEnabled(true);
                    display.getRenameEnable().setEnabled(true);
                }
            }

        });
=======
			public void onSelection(SelectionEvent<TreeItem> event) {
				tItem = (IMAPTreeItem) event.getSelectedItem();
				if (tItem.isEdit())
					return;
				folder = (ImapFolder) tItem.getUserObject();
				if (folder.getFullName().equalsIgnoreCase(user.getSettings().getInboxFolderName())) {
					display.getDeleteEnable().setEnabled(false);
					display.getRenameEnable().setEnabled(false);
				} else {
					display.getDeleteEnable().setEnabled(true);
					display.getRenameEnable().setEnabled(true);
				}
			}

		});
>>>>>>> try to change fetch messages to use RF
		display.getRenameClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				tItem.startEdit();
			}

		});
		display.getDeleteClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.getDeleteConfirmDialog().show();
			}

		});
		display.getDeleteConfirmClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				DeleteFolderRequest req = requestFactory.deleteFolderRequest();
				final DeleteFolderAction action = req.create(DeleteFolderAction.class);
				action.setFolder(folder);
				req.delete(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						display.deleteSelectedFolder();
					}
					@Override
					public void onFailure(ServerFailure error) {
						GWT.log("Error while deleting" + error.getStackTraceString());
					}
				});
			}

		});
		display.getNewClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				editableTreeItem = display.createFolder(new EditHandler() {
					public void onEditEvent(EditEvent event) {
						final IMAPTreeItem item = (IMAPTreeItem) event.getSource();
						final String newValue = (String) event.getNewValue();
						if (event.getEventType().equals(EditEvent.EventType.Stop)) {
							CreateFolderRequest req = requestFactory.createFolderRequest();
							final CreateFolderAction action = req.create(CreateFolderAction.class);
							ImapFolder folder = req.create(ImapFolder.class);
							folder.setFullName(newValue.trim());
							action.setFolder(folder);
							req.create(action).fire(new Receiver<GenericResult>() {
								@Override
								public void onSuccess(GenericResult response) {
									// Nothing todo
								}
								@Override
								public void onFailure(ServerFailure error) {
									GWT.log("Error while create folder" + error.getStackTraceString());
									item.cancelEdit();
								}
							});
						}
					}

				});
			}

		});
		eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {

			public void onMessagesReceived(MessagesReceivedEvent event) {
				ImapFolder f = event.getFolder();
				display.updateTreeItem(f);
			}

		});
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

			public void onLogin(LoginEvent event) {
				user = event.getUser();
				// folder = (IMAPFolderProxy)new
				// IMAPFolder(user.getSettings().getInboxFolderName());;
				searchValue = null;
				// showMessageTable(user, folder, searchValue);
			}

<<<<<<< HEAD
            public void onMessagesReceived(MessagesReceivedEvent event) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            	IMAPFolderProxy f = event.getFolder();
=======
                IMAPFolder f = event.getFolder();
>>>>>>> 
=======
            	IMAPFolderProxy f = event.getFolder();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
            	ImapFolder f = event.getFolder();
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
                display.updateTreeItem(f);
            }
=======
		});
>>>>>>> try to change fetch messages to use RF

		exportJSMethods(this);
	}

<<<<<<< HEAD
            public void onLogin(LoginEvent event) {
                user = event.getUser();
<<<<<<< HEAD
<<<<<<< HEAD
//                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());;
                searchValue = null;
//                showMessageTable(user, folder, searchValue);
=======
                folder = new IMAPFolder(user.getSettings().getInboxFolderName());;
=======
//                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
                searchValue = null;
<<<<<<< HEAD
                showMessageTable(user, folder, searchValue);
>>>>>>> 
=======
//                showMessageTable(user, folder, searchValue);
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
            }
            
        });

        exportJSMethods(this);
<<<<<<< HEAD
=======
	public void openLink(String url) {
		Window.open(url, "_blank", "");
	}

	public void mailTo(String mailto) {
		// sendPresenter.revealDisplay(user, mailto);
	}

	private native void exportJSMethods(WestActivity westactivity) /*-{
	                                                               $wnd.openLink = function(url) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::openLink(Ljava/lang/String;) (url);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               $wnd.mailTo = function(mail) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::mailTo(Ljava/lang/String;) (mail);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               }-*/;

	private void showMessageTable(User user, ImapFolder folder, String searchValue) {
		this.user = user;
		this.folder = folder;
		this.searchValue = searchValue;
		placeController.goTo(new MailFolderPlace().with(user, folder, searchValue));
		// placeController.goTo(mailInboxPlaceProvider.get().with(user));
		// System.out.println("111");
		// placeController.goTo(new
		// MailInboxPlace(folder.getName()).with(user));
	}

	private void showMessage(User user, ImapFolderImpl folder, Message message, MessageDetails details) {
		placeController.goTo(IMAPMessagePlaceProvider.get());
>>>>>>> try to change fetch messages to use RF
	}

	private void showNewMessage() {
		placeController.goTo(this.messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
	}

	private void showForwardMessage(ForwardMessageEvent event) {
		placeController.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), Type.FORWARD));
	}

	private void showReplyMessage(ReplyMessageEvent event) {
		placeController.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), event.getReplyAll() ? Type.REPLY_ALL : Type.REPLY));
	}

	public interface Displayable extends WidgetContainerDisplayable {

		public HasSelectionHandlers<TreeItem> getTree();

<<<<<<< HEAD
    private void showReplyMessage(ReplyMessageEvent event) {
        placeController.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(), event.getMessage(), event.getMessageDetails(), event.getReplyAll()?Type.REPLY_ALL:Type.REPLY));
    }
=======
		
	}

>>>>>>> Change to new mvp framework - first step
=======
	}

    
    public void openLink(String url) {
        Window.open(url, "_blank", "");
    }

    public void mailTo(String mailto) {
//        sendPresenter.revealDisplay(user, mailto);
    }
    private native void exportJSMethods(WestActivity westactivity) /*-{
      $wnd.openLink = function(url) {
        try {
           westactivity.@org.apache.hupa.client.activity.WestActivity::openLink(Ljava/lang/String;) (url);
        } catch(e) {}
        return false;
      };
      $wnd.mailTo = function(mail) {
        try {
           westactivity.@org.apache.hupa.client.activity.WestActivity::mailTo(Ljava/lang/String;) (mail);
        } catch(e) {}
        return false;
      };
    }-*/;
    private void showMessageTable(User user, IMAPFolderProxy folder, String searchValue) {
        this.user = user;
        this.folder = folder;
        this.searchValue = searchValue;
        placeController.goTo(new MailFolderPlace().with(user,folder, searchValue));
//        placeController.goTo(mailInboxPlaceProvider.get().with(user));
//        System.out.println("111");
//        placeController.goTo(new MailInboxPlace(folder.getName()).with(user));
    }

    private void showMessage(User user, IMAPFolder folder, Message message, MessageDetails details) {
    	placeController.goTo(IMAPMessagePlaceProvider.get());
    }

    private void showNewMessage() {
    	placeController.goTo(this.messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
    }

    private void showForwardMessage(ForwardMessageEvent event) {
    	placeController.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(), event.getMessage(), event.getMessageDetails(), Type.FORWARD));
    }

    private void showReplyMessage(ReplyMessageEvent event) {
        placeController.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(), event.getMessage(), event.getMessageDetails(), event.getReplyAll()?Type.REPLY_ALL:Type.REPLY));
    }
>>>>>>> 
    public interface Displayable extends WidgetContainerDisplayable {
        
        public HasSelectionHandlers<TreeItem> getTree();
=======
		public void bindTreeItems(List<IMAPTreeItem> treeList);
>>>>>>> try to change fetch messages to use RF

		public HasClickHandlers getRenameClick();

		public HasClickHandlers getDeleteClick();

		public HasClickHandlers getNewClick();

		public HasDialog getDeleteConfirmDialog();

		public HasClickHandlers getDeleteConfirmClick();

		public HasEnable getRenameEnable();

		public HasEnable getDeleteEnable();

		public HasEnable getNewEnable();

		public void updateTreeItem(ImapFolder folder);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        public void updateTreeItem(IMAPFolderProxy folder);
=======
        public void updateTreeItem(IMAPFolder folder);
>>>>>>> Change to new mvp framework - first step
=======
        public void updateTreeItem(IMAPFolderProxy folder);
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
        public void updateTreeItem(ImapFolder folder);
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
		public void deleteSelectedFolder();
>>>>>>> try to change fetch messages to use RF

		public HasEditable createFolder(EditHandler handler);

		public void increaseUnseenMessageCount(ImapFolder folder, int amount);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        public void increaseUnseenMessageCount(IMAPFolderProxy folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolderProxy folder, int amount);
=======
        public void increaseUnseenMessageCount(IMAPFolder folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolder folder, int amount);
>>>>>>> Change to new mvp framework - first step
=======
        public void increaseUnseenMessageCount(IMAPFolderProxy folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolderProxy folder, int amount);
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
        public void increaseUnseenMessageCount(ImapFolder folder, int amount);

        public void decreaseUnseenMessageCount(ImapFolder folder, int amount);
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        
        public void setLoadingFolders(boolean loading);
        public void setLoadingMessage(boolean loading);
        
        public void setUser(User user);
=======
		public void decreaseUnseenMessageCount(ImapFolder folder, int amount);

		public void setLoadingFolders(boolean loading);
>>>>>>> try to change fetch messages to use RF

		public void setLoadingMessage(boolean loading);

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> Change to new mvp framework - first step
=======
		public void setUser(User user);

	}
>>>>>>> try to change fetch messages to use RF

}
