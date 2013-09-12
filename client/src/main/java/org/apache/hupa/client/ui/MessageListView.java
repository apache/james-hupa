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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.shared.domain.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;

public class MessageListView extends Composite implements MessageListActivity.Displayable, RequiresResize {

	@UiField SimpleLayoutPanel thisView;
	private MessagesCellTable grid;

	@Inject
	public MessageListView(final EventBus eventBus, final MessagesCellTable table) {
		initWidget(binder.createAndBindUi(this));
		grid = table;
		thisView.add(grid);
	}

	interface MessageListUiBinder extends UiBinder<SimpleLayoutPanel, MessageListView> {
=======
=======
import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;

>>>>>>> integrate all of the views to their corresponding activities and mappers
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessageListView extends Composite implements MessageListActivity.Displayable {

	@UiField(provided = true)
	MessagesCellTable table;
	private HupaRequestFactory requestFactory;
	private EventBus eventBus;
	private ImapFolder folder;
	private String searchValue;
	private User user;
	private boolean pending;

	@Inject
	public MessageListView(final EventBus eventBus, final HupaRequestFactory requestFactory,
			final MessagesCellTable table) {
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;
		this.table = table;
		initWidget(binder.createAndBindUi(this));
		table.addCellPreviewHandler(new Handler<Message>() {
			@Override
			public void onCellPreview(CellPreviewEvent<Message> event) {
				if (hasClickedButFirstCol(event)) {
					eventBus.fireEvent(new ExpandMessageEvent(user, folder, event.getValue()));
				}
			}

			private boolean hasClickedButFirstCol(CellPreviewEvent<Message> event) {
				return "click".equals(event.getNativeEvent().getType()) && 0 != event.getColumn();
			}

		});
		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				fetch(event.getNewRange().getStart());
			}
		});
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {
			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				user = loadMessagesEvent.getUser();
				folder = loadMessagesEvent.getFolder();
				searchValue = loadMessagesEvent.getSearchValue();
				fetch(0);

			}
		});
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			public void onLogin(LoginEvent event) {
				user = event.getUser();
				folder = new ImapFolderImpl(user.getSettings().getInboxFolderName());
				searchValue = null;
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
		});
	}

	public void fetch(final int start) {
		FetchMessagesRequest messagesRequest = requestFactory.messagesRequest();
		FetchMessagesAction action = messagesRequest.create(FetchMessagesAction.class);
		final ImapFolder folder1 = messagesRequest.create(ImapFolder.class);
		folder1.setChildren(folder.getChildren());
		folder1.setDelimiter(folder.getDelimiter());
		folder1.setFullName(folder.getFullName());
		folder1.setMessageCount(folder.getMessageCount());
		folder1.setName(folder.getName());
		folder1.setSubscribed(folder.getSubscribed());
		folder1.setUnseenMessageCount(folder.getUnseenMessageCount());
		action.setFolder(folder1);
		action.setOffset(table.getPageSize());
		action.setSearchString(searchValue);
		action.setStart(start);
		messagesRequest.fetch(action).fire(new Receiver<FetchMessagesResult>() {

			@Override
			public void onFailure(ServerFailure error) {
				if (error.isFatal()) {
					throw new RuntimeException(error.getMessage());
				}
			}

			@Override
			public void onSuccess(final FetchMessagesResult result) {
				assert result != null;
				// folder.setMessageCount(result.getRealCount());// TODO if do
				// this, there will be auto bean has been frozen.
				// folder.setUnseenMessageCount(result.getRealUnreadCount());
				table.setRowCount(result.getRealCount());
				table.setRowData(start, result.getMessages());

				// pager.setPageStart(start);
//				eventBus.fireEvent(new MessagesReceivedEvent(folder1, result.getMessages()));
			}
		});
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	interface MessageListUiBinder extends UiBinder<HTMLPanel, MessageListView> {
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
	interface MessageListUiBinder extends UiBinder<SimplePanel, MessageListView> {
>>>>>>> try to integrate the messages list's panel
=======
	interface MessageListUiBinder extends UiBinder<LayoutPanel, MessageListView> {
>>>>>>> make message list view panel work as expected partly
=======
	interface MessageListUiBinder extends UiBinder<MessagesCellTable, MessageListView> {
>>>>>>> adjust to the ui of folder list panel
	}

	private static MessageListUiBinder binder = GWT.create(MessageListUiBinder.class);

<<<<<<< HEAD
	@Override
	public MessagesCellTable getGrid() {
		return grid;
	}
	
	@Override
	public void refresh(){
		grid.refresh();
	}

	@Override
	public List<Long> getSelectedMessagesIds() {
		List<Long> selecteds = new ArrayList<Long>();
		MultiSelectionModel<? super Message> selectionModel = (MultiSelectionModel<? super Message>) grid
				.getSelectionModel();
		selectionModel.getSelectedSet();
		for (Message msg : getSelectedMessages()) {
			selecteds.add(msg.getUid());
		}
		return selecteds;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Message> getSelectedMessages() {
		MultiSelectionModel<? super Message> selectionModel = (MultiSelectionModel<? super Message>) grid
				.getSelectionModel();
		return (Set<Message>) selectionModel.getSelectedSet();
	}

    @Override
    public void onResize() {
        grid.onResize();
    }
    
    @Override
    public void setSearchValue(String searchValue){
    	grid.setSearchValue(searchValue);
    	
    }

=======
>>>>>>> make login page as one part of the overall layout & splite layout to little one
}
