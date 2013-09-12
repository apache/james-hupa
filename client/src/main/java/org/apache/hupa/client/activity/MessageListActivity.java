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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.hupa.client.place.AbstractPlace;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.rf.MoveMessageRequest;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.ToolBarView;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MoveMessageAction;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.DeleteClickEvent;
import org.apache.hupa.shared.events.DeleteClickEventHandler;
import org.apache.hupa.shared.events.MoveMessageEvent;
import org.apache.hupa.shared.events.MoveMessageEventHandler;
import org.apache.hupa.shared.events.RefreshMessagesEvent;
import org.apache.hupa.shared.events.RefreshMessagesEventHandler;
import org.apache.hupa.shared.events.RefreshUnreadEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessageListActivity extends AppBaseActivity {

	@Inject private Displayable display;
	@Inject private ToolBarActivity.Displayable toolBar;
	@Inject private TopBarActivity.Displayable topBar;
	private String folderName;
	// private String searchValue;
	private User user;

	@Override
	public void start(AcceptsOneWidget container, final EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
		display.refresh();
		this.registerHandler(display.getGrid().addCellPreviewHandler(new Handler<Message>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<Message> event) {
				if (hasClickedButFirstCol(event)) {
					hc.showTopLoading("Loading...");
					antiSelectMessages(display.getGrid().getVisibleItems());
					GetMessageDetailsRequest req = rf.messageDetailsRequest();
					GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
					final ImapFolder f = req.create(ImapFolder.class);
					f.setFullName(folderName);
					action.setFolder(f);
					action.setUid(event.getValue().getUid());
					req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
						@Override
						public void onSuccess(GetMessageDetailsResult response) {
							MessagePlace place = new MessagePlace(folderName + AbstractPlace.SPLITTER
									+ event.getValue().getUid());
							pc.goTo(place);
							display.getGrid().getSelectionModel().setSelected(event.getValue(), true);
							toolBar.enableAllTools(true);
							ToolBarView.Parameters p = new ToolBarView.Parameters(user, folderName, event.getValue(),
									response.getMessageDetails());
							toolBar.setParameters(p);
							display.refresh();
							hc.hideTopLoading();
							eventBus.fireEvent(new RefreshUnreadEvent());
						}

						@Override
						public void onFailure(ServerFailure error) {
							if (error.isFatal()) {
								// log.log(Level.SEVERE, error.getMessage());
								// TODO write the error message to
								// status bar.
								toolBar.enableAllTools(false);
								hc.hideTopLoading();
								throw new RuntimeException(error.getMessage());
							}
						}
					});
				}
			}

		}));
	}
	private boolean hasClickedButFirstCol(CellPreviewEvent<Message> event) {
		return "click".equals(event.getNativeEvent().getType()) && 0 != event.getColumn();
	}

	private void bindTo(final EventBus eventBus) {
		eventBus.addHandler(DeleteClickEvent.TYPE, new DeleteClickEventHandler() {
			@Override
			public void onDeleteClickEvent(DeleteClickEvent event) {
				deleteSelectedMessages();
			}
		});

		eventBus.addHandler(RefreshMessagesEvent.TYPE, new RefreshMessagesEventHandler() {
			@Override
			public void onRefresh(RefreshMessagesEvent event) {
				display.setSearchValue(event.getSearchValue());
				display.refresh();
			}
		});

		eventBus.addHandler(MoveMessageEvent.TYPE, new MoveMessageEventHandler() {

			@Override
			public void onMoveMessageHandler(final MoveMessageEvent event) {
				hc.showTopLoading("Moving...");
				MoveMessageRequest req = rf.moveMessageRequest();
				ImapFolder f = req.create(ImapFolder.class);
				ImapFolder newF = req.create(ImapFolder.class);

				String fullName = null;
				if (pc.getWhere() instanceof FolderPlace) {
					fullName = ((FolderPlace) pc.getWhere()).getToken();
				} else {
					fullName = ((MessagePlace) pc.getWhere()).getTokenWrapper().getFolder();
				}
				f.setFullName(fullName);
				newF.setFullName(event.getNewFolder().getFullName());
				MoveMessageAction action = req.create(MoveMessageAction.class);

				final List<Long> uids = display.getSelectedMessagesIds();
				if(uids.isEmpty() || uids.size() > 1){//TODO can move more than one message once.
					hc.hideTopLoading();
					hc.showNotice("Please select one and only one message", 10000);
					return;
				}
				action.setMessageUid(uids.get(0));
				action.setNewFolder(newF);
				action.setOldFolder(f);
				req.move(action).fire(new Receiver<GenericResult>() {

					@Override
					public void onSuccess(GenericResult response) {
						display.refresh();
						eventBus.fireEvent(new RefreshUnreadEvent());
						hc.hideTopLoading();
						hc.showNotice("The conversation has been moved to \"" + event.getNewFolder() + "\"", 10000);
					}

					@Override
					public void onFailure(ServerFailure error) {
						super.onFailure(error);
						hc.hideTopLoading();
						hc.showNotice(error.getMessage(), 10000);
					}

				});
			}

		});

	}

	public MessageListActivity with(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public interface Displayable extends IsWidget {
		MessagesCellTable getGrid();

		void refresh();

		List<Long> getSelectedMessagesIds();

		Set<Message> getSelectedMessages();

		void setSearchValue(String searchValue);
	}

	private void antiSelectMessages(Collection<Message> c) {
		toolBar.enableAllTools(false);
		for (Message msg : c) {
			if (!display.getGrid().getSelectionModel().isSelected(msg))
				continue;
			display.getGrid().getSelectionModel().setSelected(msg, false);
		}
	}
	private void deleteSelectedMessages() {
		hc.showTopLoading("Deleting...");
		String fullName = null;
		if (pc.getWhere() instanceof FolderPlace) {
			fullName = ((FolderPlace) pc.getWhere()).getToken();
		} else {
			fullName = ((MessagePlace) pc.getWhere()).getTokenWrapper().getFolder();
		}
		final List<Long> uids = display.getSelectedMessagesIds();
		DeleteMessageByUidRequest req = rf.deleteMessageByUidRequest();
		DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
		ImapFolder f = req.create(ImapFolder.class);
		f.setFullName(fullName);
		action.setMessageUids(uids);
		action.setFolder(f);
		req.delete(action).fire(new Receiver<DeleteMessageResult>() {
			@Override
			public void onSuccess(DeleteMessageResult response) {
				antiSelectMessages(display.getSelectedMessages());
				display.refresh();
				hc.hideTopLoading();
				eventBus.fireEvent(new RefreshUnreadEvent());
			}
		});
	}
=======
=======
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
>>>>>>> prepare for message content panel
=======
import org.apache.hupa.client.place.MailFolderPlace;
>>>>>>> make reload message content work, use the same place with folder list, while separated with slash, that looks like Gmail's
=======
import java.util.List;

import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.MessagesCellTable;
>>>>>>> let messages list activity make use of mvp
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessageListActivity extends AppBaseActivity {

	@Inject private Displayable display;
	@Inject private HupaRequestFactory requestFactory;
	@Inject private PlaceController placeController;
	@Inject private ToolBarActivity.Displayable toolBarDisplay;
	private ImapFolder folder;
	private String searchValue;
	private User user;
	private boolean pending;

	@Override
	public void start(AcceptsOneWidget container, final EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
		display.getGrid().addCellPreviewHandler(new Handler<Message>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<Message> event) {
				if (hasClickedButFirstCol(event)) {
					List<Message> displayedItems = display.getGrid().getVisibleItems();
					for (Message msg : displayedItems) {
						display.getGrid().getSelectionModel().setSelected(msg, false);
					}
					GetMessageDetailsRequest req = requestFactory.messageDetailsRequest();
					GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
					final ImapFolder f = req.create(ImapFolder.class);
					f.setFullName(folder.getFullName());
					action.setFolder(f);
					action.setUid(event.getValue().getUid());
					req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
						@Override
						public void onSuccess(GetMessageDetailsResult response) {
							eventBus.fireEvent(new ExpandMessageEvent(user, folder, event.getValue(), response
									.getMessageDetails()));
							placeController.goTo(new MailFolderPlace(f.getFullName() + "/" + event.getValue().getUid()));
						}

						@Override
						public void onFailure(ServerFailure error) {
							if (error.isFatal()) {
								// log.log(Level.SEVERE, error.getMessage());
								// TODO write the error message to
								// status bar.
								throw new RuntimeException(error.getMessage());
							}
						}
					});
				} else if (hasChangedFirstCol(event)) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							toolBarDisplay.enableMessageTools();
						}
					});
				}
			}

		});
		display.getGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				fetch(event.getNewRange().getStart());
			}
		});
		if (!pending) {
			pending = true;
			Scheduler.get().scheduleFinally(new ScheduledCommand() {
				@Override
				public void execute() {
					pending = false;
					fetch(0);
				}
			});
		}
	}

	private boolean hasClickedButFirstCol(CellPreviewEvent<Message> event) {
		return "click".equals(event.getNativeEvent().getType()) && 0 != event.getColumn();
	}
	private boolean hasChangedFirstCol(CellPreviewEvent<Message> event) {
		GWT.log(event.getNativeEvent().getType() + " "
				+ ("change".equals(event.getNativeEvent().getType()) && 0 == event.getColumn()));
		return "change".equals(event.getNativeEvent().getType()) && 0 == event.getColumn();
	}
	public void fetch(final int start) {
		FetchMessagesRequest req = requestFactory.messagesRequest();
		FetchMessagesAction action = req.create(FetchMessagesAction.class);
		final ImapFolder f = req.create(ImapFolder.class);
		f.setFullName(folder.getFullName());
		action.setFolder(f);
		action.setOffset(display.getGrid().getPageSize());
		action.setSearchString(searchValue);
		action.setStart(start);
		req.fetch(action).fire(new Receiver<FetchMessagesResult>() {

			@Override
			public void onSuccess(final FetchMessagesResult result) {
				assert result != null;
				display.getGrid().setRowCount(result.getRealCount());
				display.getGrid().setRowData(start, result.getMessages());
			}

			@Override
			public void onFailure(ServerFailure error) {
				placeController.goTo(new DefaultPlace("@"));
				if (error.isFatal()) {
					// FIXME should goto login page regarding the long time
					// session expired.
					throw new RuntimeException(error.getMessage());
				}
			}
		});
	}

	private void bindTo(EventBus eventBus) {
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {
			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				user = loadMessagesEvent.getUser();
				folder = loadMessagesEvent.getFolder();
				searchValue = loadMessagesEvent.getSearchValue();
				fetch(0);

			}
		});
//		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
//			public void onLogin(LoginEvent event) {
//				user = event.getUser();
//				if (folder == null) {
//					folder = new ImapFolderImpl(user.getSettings().getInboxFolderName());
//					searchValue = null;
//					if (!pending) {
//						pending = true;
//						Scheduler.get().scheduleFinally(new ScheduledCommand() {
//							@Override
//							public void execute() {
//								pending = false;
//								fetch(0);
//							}
//						});
//					}
//				}
//			}
//		});

	}

	public MessageListActivity with(MailFolderPlace place) {
		setFolder(new ImapFolderImpl(place.getFullName()));
		return this;
	}

<<<<<<< HEAD
<<<<<<< HEAD
	@Inject private Displayable display;
<<<<<<< HEAD
	
	public interface Displayable extends WidgetDisplayable {}
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
=======
	@Inject
	private Displayable display;
>>>>>>> make message content work as expected partly

=======
>>>>>>> make reload message content work, use the same place with folder list, while separated with slash, that looks like Gmail's
	public interface Displayable extends WidgetDisplayable {
		MessagesCellTable getGrid();
	}

	public void setFolder(ImapFolder folder) {
		this.folder = folder;
		// if (folder != null)
		// fetch(0);
	}
>>>>>>> prepare for message content panel
}
