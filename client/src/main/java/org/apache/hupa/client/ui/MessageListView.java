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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.client.ui.MessagesCellTable.MessageListDataProvider;
import org.apache.hupa.shared.domain.Message;

import com.google.gwt.core.client.Duration;
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
    }

    private static MessageListUiBinder binder = GWT.create(MessageListUiBinder.class);

    @Override
    public MessagesCellTable getGrid() {
        return grid;
    }

    @Override
    public MessageListDataProvider getDataProvider() {
        return grid.dataProvider;
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

}
