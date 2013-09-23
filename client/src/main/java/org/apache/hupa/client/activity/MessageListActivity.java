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
								toolBar.enableAllTools(false);
								hc.hideTopLoading();
								hc.showNotice(error.getMessage(), 10000);
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
}
