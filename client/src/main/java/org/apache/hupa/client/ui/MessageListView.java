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
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.Set;
=======
import java.util.logging.Level;
import java.util.logging.Logger;
>>>>>>> scrub code

import org.apache.hupa.client.activity.MessageListActivity;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
import java.util.List;

>>>>>>> add click handler to every message row
import org.apache.hupa.client.activity.MessageListActivity;
=======
=======
import org.apache.hupa.client.place.DefaultPlace;
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list
import org.apache.hupa.client.place.MailFolderPlace;
>>>>>>> make reload message content work, use the same place with folder list, while separated with slash, that looks like Gmail's
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
=======
=======
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

>>>>>>> fixed issue#59, coupled with fixing some UI refreshment issues in toolsbar
import org.apache.hupa.client.activity.MessageListActivity;
>>>>>>> scrub code
import org.apache.hupa.client.rf.HupaRequestFactory;
=======
>>>>>>> change message list view to make it not refresh the whole list when click one of the messages
import org.apache.hupa.shared.domain.Message;

>>>>>>> integrate all of the views to their corresponding activities and mappers
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;

public class MessageListView extends Composite implements MessageListActivity.Displayable {

	@UiField SimplePanel thisView;
	private MessagesCellTable grid;

	@Inject
	public MessageListView(final EventBus eventBus, final MessagesCellTable table) {
		initWidget(binder.createAndBindUi(this));
		grid = table;
		thisView.add(grid);
	}
<<<<<<< HEAD
<<<<<<< HEAD

	public void fetch(final int start) {
		FetchMessagesRequest req = requestFactory.messagesRequest();
		FetchMessagesAction action = req.create(FetchMessagesAction.class);
		final ImapFolder f = req.create(ImapFolder.class);
		f.setFullName(folder.getFullName());
		action.setFolder(f);
		action.setOffset(grid.getPageSize());
		action.setSearchString(searchValue);
		action.setStart(start);
		req.fetch(action).fire(new Receiver<FetchMessagesResult>() {

			@Override
			public void onSuccess(final FetchMessagesResult result) {
				assert result != null;
				grid.setRowCount(result.getRealCount());
				grid.setRowData(start, result.getMessages());
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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
	interface MessageListUiBinder extends UiBinder<DataGrid, MessageListView> {
>>>>>>> deal with onResizeEvent of folder list panel, but found issue #25
=======
	interface MessageListUiBinder extends
			UiBinder<DataGrid<Message>, MessageListView> {
>>>>>>> prepare for place management and history controller
=======
=======
>>>>>>> let messages list activity make use of mvp
=======

<<<<<<< HEAD
>>>>>>> fixed issue#59, coupled with fixing some UI refreshment issues in toolsbar
	interface MessageListUiBinder extends UiBinder<DataGrid<Message>, MessageListView> {
>>>>>>> coping with reply and forward sending message
=======
	interface MessageListUiBinder extends UiBinder<SimpleLayoutPanel, MessageListView> {
>>>>>>> change message list view to make it not refresh the whole list when click one of the messages
	}

	private static MessageListUiBinder binder = GWT.create(MessageListUiBinder.class);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
=======

>>>>>>> let messages list activity make use of mvp
=======
>>>>>>> fixed issue#59, coupled with fixing some UI refreshment issues in toolsbar
	@Override
	public MessagesCellTable getGrid() {
		return grid;
	}
	
	@Override
	public void refresh(){
		grid.refresh();
	}

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> prepare for place management and history controller
=======
=======
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
>>>>>>> fixed issue#59, coupled with fixing some UI refreshment issues in toolsbar

>>>>>>> let messages list activity make use of mvp
}
