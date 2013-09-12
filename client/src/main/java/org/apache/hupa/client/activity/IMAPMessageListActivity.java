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

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.Set;

import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.rf.DeleteMessageAllRequest;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.MoveMessageRequest;
import org.apache.hupa.client.rf.SetFlagRequest;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MoveMessageAction;
import org.apache.hupa.shared.domain.SetFlagAction;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.MoveMessageEvent;
import org.apache.hupa.shared.events.MoveMessageEventHandler;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class IMAPMessageListActivity extends AppBaseActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		revealDisplay(user, folder, searchValue);
		container.setWidget(display.asWidget());
	}
	private void bind() {
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
			public void onLogout(LogoutEvent logoutEvent) {
				IMAPMessageListActivity.this.display.reset();
				IMAPMessageListActivity.this.display.getSearchValue().setValue("");
			}
		});
		eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {
			public void onMessagesReceived(MessagesReceivedEvent event) {
				// fill the oracle
				display.fillSearchOracle(event.getMessages());
			}
		});
		registrations.add(display.getSearchClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String searchValue = null;
				if (display.getSearchValue().getValue().trim().length() > 0) {
					searchValue = display.getSearchValue().getValue().trim();
				}
				eventBus.fireEvent(new LoadMessagesEvent(user, folder, searchValue));
			}
		}));
		eventBus.addHandler(MoveMessageEvent.TYPE, new MoveMessageEventHandler() {
			public void onMoveMessageHandler(MoveMessageEvent event) {
				final Message message = event.getMessage();
				MoveMessageRequest req = rf.moveMessageRequest();
				MoveMessageAction action = req.create(MoveMessageAction.class);
				ImapFolder newOne = req.create(ImapFolder.class);
				ImapFolder oldOne = req.create(ImapFolder.class);
				event.getNewFolder().setFolderTo(newOne);
				event.getOldFolder().setFolderTo(oldOne);
				action.setMessageUid(message.getUid());
				action.setNewFolder(newOne);
				action.setOldFolder(oldOne);
				req.move(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						List<Message> messageArray = new ArrayList<Message>();
						messageArray.add(message);
						display.removeMessages(messageArray);
					}
				});
			}
		});
		registrations.add(display.getSelectAllClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				display.deselectAllMessages();
				display.selectAllMessages();
			}
		}));
		registrations.add(display.getSelectNoneClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				display.deselectAllMessages();
			}
		}));
		registrations.add(display.getDeleteClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (folder.getFullName().equals(user.getSettings().getTrashFolderName())) {
					display.getConfirmDeleteDialog().show();
				} else {
					deleteMessages();
				}
			}
		}));
		registrations.add(display.getConfirmDeleteDialogClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteMessages();
			}
		}));
		registrations.add(display.getNewClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				// eventBus.fireEvent(new NewMessageEvent());
				pc.goTo(messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
			}
		}));
		registrations.add(display.getDeleteAllClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				display.getConfirmDeleteAllDialog().center();
			}
		}));
		registrations.add(display.getConfirmDeleteAllDialogClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DeleteMessageAllRequest req = rf.deleteMessageAllRequest();
				DeleteMessageAllAction action = req.create(DeleteMessageAllAction.class);
				ImapFolder f = req.create(ImapFolder.class);
				folder.setFolderTo(f);
				action.setFolder(f);
				req.delete(action).fire(new Receiver<DeleteMessageResult>() {
					@Override
					public void onSuccess(DeleteMessageResult response) {
						redrawTable();
						eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, response.getCount()));
					}
				});
			}
		}));
		registrations.add(display.getMarkSeenClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
				ArrayList<Long> uids = new ArrayList<Long>();
				for (Message m : selectedMessages) {
					if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
						uids.add(m.getUid());
					} else {
						selectedMessages.remove(m);
					}
				}
				SetFlagRequest req = rf.setFlagRequest();
				SetFlagAction action = req.create(SetFlagAction.class);
				ImapFolder f = req.create(ImapFolder.class);
				folder.setFolderTo(f);
				action.setFlag(IMAPFlag.SEEN);
				action.setFolder(f);
				action.setUids(uids);
				action.setValue(true);
				req.set(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						for (Message m : selectedMessages) {
							if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
								m.getFlags().add(IMAPFlag.SEEN);
							}
						}
						display.redraw();
						eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, selectedMessages.size()));
					}
				});
			}
		}));
		registrations.add(display.getMarkUnseenClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
				ArrayList<Long> uids = new ArrayList<Long>();
				for (Message m : selectedMessages) {
					if (m.getFlags().contains(IMAPFlag.SEEN)) {
						uids.add(m.getUid());
					} else {
						selectedMessages.remove(m);
					}
				}
				SetFlagRequest req = rf.setFlagRequest();
				SetFlagAction action = req.create(SetFlagAction.class);
				ImapFolder f = req.create(ImapFolder.class);
				folder.setFolderTo(f);
				action.setFlag(IMAPFlag.SEEN);
				action.setFolder(f);
				action.setUids(uids);
				action.setValue(false);
				req.set(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						for (Message m : selectedMessages) {
							if (m.getFlags().contains(IMAPFlag.SEEN)) {
								m.getFlags().remove(IMAPFlag.SEEN);
							}
						}
						display.redraw();
						eventBus.fireEvent(new IncreaseUnseenEvent(user, folder, selectedMessages.size()));
					}
				});
			}

		}));
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {// TODO
			        public void onFolderSelectionEvent(FolderSelectionEvent event) {
				        folder = event.getFolder();
				        user = event.getUser();
			        }

		        });
		registrations.add(display.getRefreshClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				display.reset();
				display.reloadData();
			}

		}));
		registrations.add(display.getRowsPerPageChange().addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				// firePresenterRevealedEvent(true);
				// firePresenterChangedEvent();
			}

		}));
		// display.addTableListener(tableListener);
	}

	private void deleteMessages() {
		Set<Message> ml = display.getSelectedMessages();
		final List<Message> selectedMessages = new ArrayList<Message>(ml);
		List<Long> uids = new ArrayList<Long>();
		for (Message m : selectedMessages) {
			uids.add(m.getUid());
			display.getTable().getSelectionModel().setSelected(m, false); // FIXME should be deSelected, or removed?
		}
		// maybe its better to just remove the messages from the table and
		// expect the removal will work
		display.removeMessages(selectedMessages);
		DeleteMessageByUidRequest req = rf.deleteMessageByUidRequest();
		DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
		ImapFolder f = req.create(ImapFolder.class);
//		folder.setFolderTo(f); FIXME cannot use any more, for it's already a requestContext assigned.
		clone(f, folder);
		action.setMessageUids(uids);
		action.setFolder(f);
		req.delete(action).fire(new Receiver<DeleteMessageResult>() {
			@Override
			public void onSuccess(DeleteMessageResult response) {
				redrawTable();//TODO presenter
				eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, response.getCount()));
			}
		});
	}
	private void clone(ImapFolder f, ImapFolder folder) {
	    f.setChildren(folder.getChildren());
		f.setDelimiter(folder.getDelimiter());
		f.setFullName(folder.getFullName());
		f.setMessageCount(folder.getMessageCount());
		f.setName(folder.getName());
		f.setSubscribed(folder.getSubscribed());
		f.setUnseenMessageCount(folder.getUnseenMessageCount());
    }

	private void redrawTable() {
        display.getTable().setVisibleRangeAndClearData(display.getTable().getVisibleRange(), true);
    }
	public IMAPMessageListActivity with(FolderPlace place) {
//		this.user = place.getUser();
//		this.folder = place.getFolder();
//		this.searchValue = place.getSearchValue();
		return this;
	}

	protected void onRevealDisplay() {
		if (user != null && folder != null) {
			display.reloadData();
		}
	}
	public void revealDisplay(User user, ImapFolder folder, String searchValue) {
		this.user = user;
		if (this.user == null || !this.user.getName().equals(user.getName()) || this.folder == null
		        || !this.folder.getFullName().equals(folder.getFullName())
		        || (searchValue == null && this.searchValue != null)
		        || (searchValue != null && searchValue.equals(this.searchValue) == false)) {
			display.reset();
			display.deselectAllMessages();
		}
		display.setExpandLoading(false);
		this.searchValue = searchValue;
		this.folder = folder;
		onRevealDisplay();
	}

	private String searchValue;
	private User user;
	private ImapFolder folder;

	@Inject private Displayable display;
	@Inject private Provider<MessageSendPlace> messageSendPlaceProvider;
	
	public interface Displayable extends WidgetDisplayable {
		public void setPostFetchMessageCount(int count);
		public void fillSearchOracle(List<Message> messages);
		public void setExpandLoading(boolean expanding);
		public void redraw();
		public void goToPage(int page);
		public void reloadData();
		public void removeMessages(List<Message> messages);
		public void selectAllMessages();
		public void deselectAllMessages();
		public void reset();
		public int getCurrentPage();
		public int getRowsPerPageIndex();
		public HasClickHandlers getNewClick();
		public Message getData(int rowIndex);
		public HasClickHandlers getDeleteClick();
		public HasClickHandlers getDeleteAllClick();
		public HasEnable getDeleteEnable();
		public Set<Message> getSelectedMessages();
		public HasDialog getConfirmDeleteDialog();
		public HasDialog getConfirmDeleteAllDialog();
		public HasClickHandlers getConfirmDeleteDialogClick();
		public HasClickHandlers getConfirmDeleteAllDialogClick();
		public HasClickHandlers getSelectAllClick();
		public HasClickHandlers getSelectNoneClick();
		public HasClickHandlers getMarkSeenClick();
		public HasClickHandlers getMarkUnseenClick();
		public HasEnable getMarkSeenEnable();
		public HasEnable getMarkUnseenEnable();
		public HasClickHandlers getRefreshClick();
		public HasChangeHandlers getRowsPerPageChange();
		public HasClickHandlers getSearchClick();
		public HasValue<String> getSearchValue();
		public MessagesCellTable getTable();
	}
=======
=======
>>>>>>> Change to new mvp framework - first step
package org.apache.hupa.client.activity;
=======
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.

<<<<<<< HEAD
import java.util.ArrayList;

<<<<<<< HEAD
import net.customware.gwt.dispatch.client.DispatchAsync;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
import org.apache.hupa.client.HandlerRegistrationAdapter;
=======
>>>>>>> remove gwt-incubator dependency in Messages List Model; 
import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.rf.DeleteMessageAllRequest;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.MoveMessageRequest;
import org.apache.hupa.client.rf.SetFlagRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MoveMessageAction;
import org.apache.hupa.shared.domain.SetFlagAction;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.MoveMessageEvent;
import org.apache.hupa.shared.events.MoveMessageEventHandler;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
import org.apache.hupa.shared.rpc.DeleteAllMessages;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.DeleteMessageResult;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.MoveMessage;
import org.apache.hupa.shared.rpc.MoveMessageResult;
import org.apache.hupa.shared.rpc.SetFlag;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
=======
=======
import org.apache.hupa.client.HandlerRegistrationAdapter;
<<<<<<< HEAD
import org.apache.hupa.client.HupaEvoCallback;
<<<<<<< HEAD
>>>>>>> 
=======
import org.apache.hupa.client.activity.MessageSendActivity.Type;
>>>>>>> 1. improve the inbox folder place.
import org.apache.hupa.client.mvp.WidgetDisplayable;
import org.apache.hupa.client.place.IMAPMessagePlace;
=======
import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.evo.HupaEvoCallback;
import org.apache.hupa.client.place.MailFolderPlace;
>>>>>>> Make the evo more clear.
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.MoveMessageEvent;
import org.apache.hupa.shared.events.MoveMessageEventHandler;
import org.apache.hupa.shared.proxy.ImapFolder;
=======
>>>>>>> Allow client can use the domain entity interface.
import org.apache.hupa.shared.rpc.DeleteAllMessages;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.DeleteMessageResult;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.MoveMessage;
import org.apache.hupa.shared.rpc.MoveMessageResult;
import org.apache.hupa.shared.rpc.SetFlag;
=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
<<<<<<< HEAD
>>>>>>> Change to new mvp framework - first step
=======
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
>>>>>>> 
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
<<<<<<< HEAD
import com.google.gwt.gen2.table.event.client.HasPageChangeHandlers;
import com.google.gwt.gen2.table.event.client.HasPageLoadHandlers;
import com.google.gwt.gen2.table.event.client.HasRowSelectionHandlers;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 
import com.google.gwt.gen2.table.event.client.PageChangeEvent;
import com.google.gwt.gen2.table.event.client.PageChangeHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
<<<<<<< HEAD
=======
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> 
=======
>>>>>>> remove gwt-incubator dependency in Messages List Model; 
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class IMAPMessageListActivity extends AbstractActivity {

<<<<<<< HEAD
    private String searchValue;
    private User user;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private IMAPFolderProxy folder;
=======
    private IMAPFolder folder;
>>>>>>> Change to new mvp framework - first step
=======
    private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private ImapFolder folder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
    private ShowMessageTableListener tableListener = new ShowMessageTableListener();
=======
	private String searchValue;
	private User user;
	private ImapFolder folder;
<<<<<<< HEAD
	private ShowMessageTableListener tableListener = new ShowMessageTableListener();
>>>>>>> delete messages, make WestActivity Singleton
=======
>>>>>>> clean some code. Pager issue remain

	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	@Inject private Provider<MessageSendPlace> messageSendPlaceProvider;
	@Inject private HupaRequestFactory requestFactory;

<<<<<<< HEAD
<<<<<<< HEAD
	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
<<<<<<< HEAD
<<<<<<< HEAD
	private DispatchAsync dispatcher;
	private final Provider<MessageSendPlace> messageSendPlaceProvider;
=======
    @Inject private Displayable display;
    @Inject private EventBus eventBus;
    @Inject private PlaceController placeController;
    @Inject private Provider<MessageSendPlace> messageSendPlaceProvider;
>>>>>>> fix issue 2&3. 	Handle exceptions thrown in async blocks & Simply injection code
    
=======
>>>>>>> delete messages, make WestActivity Singleton
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		revealDisplay(user, folder, searchValue);
		container.setWidget(display.asWidget());
	}
<<<<<<< HEAD
	
	private void bind(){
=======
	private final Provider<MailInboxPlace> mailInboxPlaceProvider;
=======
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
	private DispatchAsync dispatcher;
	private final Provider<MessageSendPlace> messageSendPlaceProvider;
    
	@Inject
    public IMAPMessageListActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			 DispatchAsync dispatcher,Provider<MessageSendPlace> messageSendPlaceProvider){
		this.display = display;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.dispatcher = dispatcher;
		this.messageSendPlaceProvider = messageSendPlaceProvider;
		

        // add this event on constructor because we don't want to remove it on unbind
>>>>>>> Change to new mvp framework - first step
        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

            public void onLogout(LogoutEvent logoutEvent) {
            	IMAPMessageListActivity.this.display.reset();
            	IMAPMessageListActivity.this.display.getSearchValue().setValue("");
            }
            
        });
<<<<<<< HEAD
=======

	private void bind() {
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			public void onLogout(LogoutEvent logoutEvent) {
				IMAPMessageListActivity.this.display.reset();
				IMAPMessageListActivity.this.display.getSearchValue().setValue("");
			}

		});
>>>>>>> delete messages, make WestActivity Singleton
		eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {

			public void onMessagesReceived(MessagesReceivedEvent event) {

				// fill the oracle
				display.fillSearchOracle(event.getMessages());
			}

		});
		display.getSearchClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				String searchValue = null;
				if (display.getSearchValue().getValue().trim().length() > 0) {
					searchValue = display.getSearchValue().getValue().trim();
				}
				eventBus.fireEvent(new LoadMessagesEvent(user, folder, searchValue));
			}

		});
		eventBus.addHandler(MoveMessageEvent.TYPE, new MoveMessageEventHandler() {
			public void onMoveMessageHandler(MoveMessageEvent event) {
				final Message message = event.getMessage();
				MoveMessageRequest req = requestFactory.moveMessageRequest();
				MoveMessageAction action = req.create(MoveMessageAction.class);
				action.setMessageUid(message.getUid());
				action.setNewFolder(event.getNewFolder());
				action.setOldFolder(event.getOldFolder());
				req.move(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						List<Message> messageArray = new ArrayList<Message>();
						messageArray.add(message);
						display.removeMessages(messageArray);
					}
				});
			}

		});
		display.getSelectAllClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.deselectAllMessages();
				display.selectAllMessages();
			}

		});
		display.getSelectNoneClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.deselectAllMessages();
			}

		});
		display.getDeleteClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (folder.getFullName().equals(user.getSettings().getTrashFolderName())) {
					display.getConfirmDeleteDialog().show();
				} else {
					deleteMessages();
				}

			}

		});
		display.getConfirmDeleteDialogClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				deleteMessages();
			}

		});
		display.getNewClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				// eventBus.fireEvent(new NewMessageEvent());
				placeController.goTo(messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
			}

		});
		display.getDeleteAllClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.getConfirmDeleteAllDialog().center();
			}

		});
		display.getConfirmDeleteAllDialogClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				DeleteMessageAllRequest req = requestFactory.deleteMessageAllRequest();
				DeleteMessageAllAction action = req.create(DeleteMessageAllAction.class);
				action.setFolder(folder);
				req.delete(action).fire(new Receiver<DeleteMessageResult>() {
					@Override
					public void onSuccess(DeleteMessageResult response) {
						display.reset();
						display.reloadData();
						eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, response.getCount()));
					}
				});
			}

		});
		display.getMarkSeenClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
				ArrayList<Long> uids = new ArrayList<Long>();
				for (Message m : selectedMessages) {
					if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
						uids.add(m.getUid());
					} else {
						selectedMessages.remove(m);
					}
				}

				SetFlagRequest req = requestFactory.setFlagRequest();
				SetFlagAction action = req.create(SetFlagAction.class);
				action.setFlag(IMAPFlag.SEEN);
				action.setFolder(folder);
				action.setUids(uids);
				action.setValue(true);
				req.set(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						for (Message m : selectedMessages) {
							if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
								m.getFlags().add(IMAPFlag.SEEN);
							}
						}
						display.redraw();
						eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, selectedMessages.size()));
					}
				});
			}

		});
		display.getMarkUnseenClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
				ArrayList<Long> uids = new ArrayList<Long>();
				for (Message m : selectedMessages) {
					if (m.getFlags().contains(IMAPFlag.SEEN)) {
						uids.add(m.getUid());
					} else {
						selectedMessages.remove(m);
					}
				}
				SetFlagRequest req = requestFactory.setFlagRequest();
				SetFlagAction action = req.create(SetFlagAction.class);
				action.setFlag(IMAPFlag.SEEN);
				action.setFolder(folder);
				action.setUids(uids);
				action.setValue(false);
				req.set(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						for (Message m : selectedMessages) {
							if (m.getFlags().contains(IMAPFlag.SEEN)) {
								m.getFlags().remove(IMAPFlag.SEEN);
							}
						}
						display.redraw();
						eventBus.fireEvent(new IncreaseUnseenEvent(user, folder, selectedMessages.size()));

					}
				});
			}

		});
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {// TODO

			        public void onFolderSelectionEvent(FolderSelectionEvent event) {
				        folder = event.getFolder();
				        user = event.getUser();
			        }

		        });
		display.getRefreshClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.reset();
				display.reloadData();
			}

		});
		display.getRowsPerPageChange().addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				// firePresenterRevealedEvent(true);
				// firePresenterChangedEvent();
			}

		});
//		display.addTableListener(tableListener);
	}

	private void deleteMessages() {
		List<Message> ml = display.getSelectedMessages();
		final List<Message> selectedMessages = new ArrayList<Message>(ml);
		List<Long> uids = new ArrayList<Long>();
		for (Message m : selectedMessages) {
			uids.add(m.getUid());
		}
		// maybe its better to just remove the messages from the table and
		// expect the removal will work
		display.removeMessages(selectedMessages);
		DeleteMessageByUidRequest req = requestFactory.deleteMessageByUidRequest();
		DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
		action.setMessageUids(uids);
		action.setFolder(folder);
		req.delete(action).fire(new Receiver<DeleteMessageResult>() {
			@Override
			public void onSuccess(DeleteMessageResult response) {
				eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, response.getCount()));
			}
		});
	}
	public IMAPMessageListActivity with(MailFolderPlace place) {
		this.user = place.getUser();
		this.folder = place.getFolder();
		this.searchValue = place.getSearchValue();
<<<<<<< HEAD
<<<<<<< HEAD
=======
	}
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		revealDisplay(user, folder, searchValue);
		container.setWidget(display.asWidget());
	}
	
	private void bind(){
		eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {

            public void onMessagesReceived(MessagesReceivedEvent event) {

                // fill the oracle
                display.fillSearchOracle(event.getMessages());
            }

        });
		display.getSearchClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String searchValue = null;
                if (display.getSearchValue().getValue().trim().length() > 0) {
                    searchValue = display.getSearchValue().getValue().trim();
                }
                eventBus.fireEvent(new LoadMessagesEvent(user, folder, searchValue));
            }

        });
		eventBus.addHandler(MoveMessageEvent.TYPE, new MoveMessageEventHandler() {

            public void onMoveMessageHandler(MoveMessageEvent event) {
                final Message message = event.getMessage();
                dispatcher.execute(new MoveMessage(event.getOldFolder(), event.getNewFolder(), message.getUid()), new HupaEvoCallback<MoveMessageResult>(dispatcher, eventBus) {
                    public void callback(MoveMessageResult result) {
                        ArrayList<Message> messageArray = new ArrayList<Message>();
                        messageArray.add(message);
                        display.removeMessages(messageArray);
                    }
                }); 
            }
            
        });
		display.getSelectAllClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.deselectAllMessages();
                display.selectAllMessages();
            }
            
        });
		display.getSelectNoneClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.deselectAllMessages();
            }
            
        });
		display.getDeleteClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                if (folder.getFullName().equals(user.getSettings().getTrashFolderName())) {
                    display.getConfirmDeleteDialog().show();
                } else {
                    deleteMessages();
                }
                
            }
            
        });
		display.getConfirmDeleteDialogClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                deleteMessages();
            }
            
        });
		display.getNewClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
//                eventBus.fireEvent(new NewMessageEvent());
            	placeController.goTo(messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
            }
            
        });
		display.getDeleteAllClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.getConfirmDeleteAllDialog().center();
            }
            
        });
		display.getConfirmDeleteAllDialogClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                dispatcher.execute(new DeleteAllMessages(folder), new HupaEvoCallback<DeleteMessageResult>(dispatcher, eventBus) {
                    public void callback(DeleteMessageResult result) {
                        display.reset();
                        display.reloadData();
//                        eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,result.getCount()));
                    }
                });
            }
            
        });
		display.getMarkSeenClick().addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
                ArrayList<Long> uids = new ArrayList<Long>();
                for (Message m : selectedMessages) {
                    if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
                        uids.add(m.getUid());
                    } else {
                        selectedMessages.remove(m);
                    }
                }
                dispatcher.execute(new SetFlag(folder, IMAPFlag.SEEN, true, uids), new HupaEvoCallback<GenericResult>(dispatcher, eventBus) {
                    public void callback(GenericResult result) {
                        for (Message m : selectedMessages) {
                            if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
                                m.getFlags().add(IMAPFlag.SEEN);
                            }
                        }
                        display.redraw();
                        eventBus.fireEvent(new DecreaseUnseenEvent(user, folder,selectedMessages.size()));
                    }
                });
            }

        });
		display.getMarkUnseenClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
                ArrayList<Long> uids = new ArrayList<Long>();
                for (Message m : selectedMessages) {
                    if (m.getFlags().contains(IMAPFlag.SEEN)) {
                        uids.add(m.getUid());
                    } else {
                        selectedMessages.remove(m);
                    }
                }
                
                dispatcher.execute(new SetFlag(folder, IMAPFlag.SEEN, false, uids), new HupaEvoCallback<GenericResult>(dispatcher, eventBus) {
                    public void callback(GenericResult result) {
                        for (Message m : selectedMessages) {
                            if (m.getFlags().contains(IMAPFlag.SEEN)) {
                                m.getFlags().remove(IMAPFlag.SEEN);
                            }
                        }
                        display.redraw();
                        eventBus.fireEvent(new IncreaseUnseenEvent(user, folder,selectedMessages.size()));
                    }
                });
            }
            
            
        });
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {//TODO

            public void onFolderSelectionEvent(FolderSelectionEvent event) {
                folder = event.getFolder();
                user = event.getUser();
            }
            
        });
		new HandlerRegistrationAdapter(display.getDataTableSelection().addRowSelectionHandler(new RowSelectionHandler() {
            public void onRowSelection(RowSelectionEvent event) {
                if (event.getSelectedRows().size() == 0) {
                    display.getDeleteEnable().setEnabled(false);
                    display.getMarkSeenEnable().setEnabled(false);
                    display.getMarkUnseenEnable().setEnabled(false);
                } else {
                    display.getDeleteEnable().setEnabled(true);
                    display.getMarkSeenEnable().setEnabled(true);
                    display.getMarkUnseenEnable().setEnabled(true);
                }
            }
            
        
        
		}));
		display.getRefreshClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.reset();
                display.reloadData();
            }
            
        });
		new HandlerRegistrationAdapter(display.getDataTablePageChange().addPageChangeHandler(new PageChangeHandler() {//TODO

            public void onPageChange(PageChangeEvent event) {
                //firePresenterRevealedEvent(true);
//                firePresenterChangedEvent();
            }
            
        }));
		display.getRowsPerPageChange().addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                //firePresenterRevealedEvent(true);
//                firePresenterChangedEvent();
            }
            
        });
		display.addTableListener(tableListener);
	}

    private void deleteMessages() {
        final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
        ArrayList<Long> uids = new ArrayList<Long>();
        for (Message m : selectedMessages) {
            uids.add(m.getUid());
        }
        // maybe its better to just remove the messages from the table and expect the removal will work
        display.removeMessages(selectedMessages);

        dispatcher.execute(new DeleteMessageByUid(folder,uids), new HupaEvoCallback<DeleteMessageResult>(dispatcher, eventBus) {
            public void callback(DeleteMessageResult result) {
                eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,result.getCount()));
            }
        }); 
    }
<<<<<<< HEAD
	public IMAPMessageListActivity with(User user, IMAPFolder imapFolder, String string){
		this.user = user;
<<<<<<< HEAD
		this.folder = new IMAPFolder(user.getSettings().getInboxFolderName());
>>>>>>> Change to new mvp framework - first step
=======
		this.folder = imapFolder;
		this.searchValue = string;
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
=======
	public IMAPMessageListActivity with(MailFolderPlace place){
		this.user = place.getUser();
		this.folder = place.getFolder();
		this.searchValue = place.getSearchValue();
>>>>>>> Make the evo more clear.
=======
		display.setImapFolder(folder);
>>>>>>> use CellTable to deal with MessageTableModel, even a few issues
=======
>>>>>>> fix the CellTable display
		return this;
	}

<<<<<<< HEAD
    protected void onRevealDisplay() {
        if (user != null && folder != null) {
            display.reloadData();  
        }
    }
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public void revealDisplay(User user, IMAPFolderProxy folder, String searchValue) {
=======
    public void revealDisplay(User user, IMAPFolder folder, String searchValue) {
>>>>>>> Change to new mvp framework - first step
=======
    public void revealDisplay(User user, IMAPFolderProxy folder, String searchValue) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public void revealDisplay(User user, ImapFolder folder, String searchValue) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        this.user = user;
       
        if (this.user == null 
            || !this.user.getName().equals(user.getName()) 
            || this.folder == null 
            || !this.folder.getFullName().equals(folder.getFullName()) 
            || (searchValue == null && this.searchValue != null) 
            || (searchValue != null && searchValue.equals(this.searchValue) == false)) {
            display.reset();
            display.deselectAllMessages();
        }
        display.setExpandLoading(false);
        this.searchValue = searchValue;
        this.folder = folder;
        
        onRevealDisplay();
    }
	public interface Displayable extends WidgetDisplayable{
        public HasRowSelectionHandlers getDataTableSelection();
        public HasPageLoadHandlers getDataTableLoad();
        public void addTableListener(TableListener listener) ;
        public void removeTableListener(TableListener listener) ;
        public void setPostFetchMessageCount(int count);
        public HasClickHandlers getNewClick();
        public Message getData(int rowIndex);
        public HasClickHandlers getDeleteClick();
        public HasClickHandlers getDeleteAllClick();
        public HasEnable getDeleteEnable();
        public void reloadData();
        public void removeMessages(ArrayList<Message> messages);
        public List<Message> getSelectedMessages();
        public void reset();
        public HasDialog getConfirmDeleteDialog();
        public HasDialog getConfirmDeleteAllDialog();
        public HasClickHandlers getConfirmDeleteDialogClick();
        public HasClickHandlers getConfirmDeleteAllDialogClick();
        public void selectAllMessages();
        public void deselectAllMessages();
        public HasClickHandlers getSelectAllClick();
        public HasClickHandlers getSelectNoneClick();
        public HasClickHandlers getMarkSeenClick();
        public HasClickHandlers getMarkUnseenClick();
        public HasEnable getMarkSeenEnable();
        public HasEnable getMarkUnseenEnable();
        public HasClickHandlers getRefreshClick();
        public void redraw();
        public HasPageChangeHandlers getDataTablePageChange();
        public void goToPage(int page);
        public int getCurrentPage();
        public int getRowsPerPageIndex();
        public HasChangeHandlers getRowsPerPageChange();     
        public HasClickHandlers getSearchClick();
        public HasValue<String> getSearchValue();
        public void fillSearchOracle(List<Message> messages);
        public void setExpandLoading(boolean expanding);

    }

    
    private final class ShowMessageTableListener implements TableListener {

        public void onCellClicked(SourcesTableEvents sender, int row,
                int cell) {
            
            display.setExpandLoading(true);
            Message message = display.getData(row);
            
            // mark the message as seen and redraw the table to reflect this
            if (message.getFlags().contains(MessageImpl.IMAPFlag.SEEN) == false) {
                // add flag, fire event and redraw
                message.getFlags().add(MessageImpl.IMAPFlag.SEEN);
                eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,1));
                
                display.redraw();

            }
<<<<<<< HEAD
<<<<<<< HEAD
=======
            
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> 1. improve the inbox folder place.
            eventBus.fireEvent(new ExpandMessageEvent(user,folder,message));
        }

    }
<<<<<<< HEAD
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> Change to new mvp framework - first step
=======
	protected void onRevealDisplay() {
		if (user != null && folder != null) {
			display.reloadData();
		}
	}
	public void revealDisplay(User user, ImapFolder folder, String searchValue) {
		this.user = user;

		if (this.user == null || !this.user.getName().equals(user.getName()) || this.folder == null
		        || !this.folder.getFullName().equals(folder.getFullName())
		        || (searchValue == null && this.searchValue != null)
		        || (searchValue != null && searchValue.equals(this.searchValue) == false)) {
			display.reset();
			display.deselectAllMessages();
		}
		display.setExpandLoading(false);
		this.searchValue = searchValue;
		this.folder = folder;

		onRevealDisplay();
	}

	public interface Displayable extends WidgetDisplayable {
		public void setPostFetchMessageCount(int count);
		public HasClickHandlers getNewClick();
		public Message getData(int rowIndex);
		public HasClickHandlers getDeleteClick();
		public HasClickHandlers getDeleteAllClick();
		public HasEnable getDeleteEnable();
		public void reloadData();
		public void removeMessages(List<Message> messages);
		public List<Message> getSelectedMessages();
		public void reset();
		public HasDialog getConfirmDeleteDialog();
		public HasDialog getConfirmDeleteAllDialog();
		public HasClickHandlers getConfirmDeleteDialogClick();
		public HasClickHandlers getConfirmDeleteAllDialogClick();
		public void selectAllMessages();
		public void deselectAllMessages();
		public HasClickHandlers getSelectAllClick();
		public HasClickHandlers getSelectNoneClick();
		public HasClickHandlers getMarkSeenClick();
		public HasClickHandlers getMarkUnseenClick();
		public HasEnable getMarkSeenEnable();
		public HasEnable getMarkUnseenEnable();
		public HasClickHandlers getRefreshClick();
		public void redraw();
		public void goToPage(int page);
		public int getCurrentPage();
		public int getRowsPerPageIndex();
		public HasChangeHandlers getRowsPerPageChange();
		public HasClickHandlers getSearchClick();
		public HasValue<String> getSearchValue();
		public void fillSearchOracle(List<Message> messages);
		public void setExpandLoading(boolean expanding);

	}
<<<<<<< HEAD

	private final class ShowMessageTableListener implements TableListener {

		public void onCellClicked(SourcesTableEvents sender, int row, int cell) {

			display.setExpandLoading(true);
			Message message = display.getData(row);

			// mark the message as seen and redraw the table to reflect this
			if (message.getFlags().contains(MessageImpl.IMAPFlag.SEEN) == false) {
				// add flag, fire event and redraw
				message.getFlags().add(MessageImpl.IMAPFlag.SEEN);
				eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, 1));

				display.redraw();

			}
			eventBus.fireEvent(new ExpandMessageEvent(user, folder, message));
		}

	}
>>>>>>> delete messages, make WestActivity Singleton
=======
>>>>>>> clean some code. Pager issue remain
}
