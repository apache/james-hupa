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

import org.apache.hupa.client.activity.WestActivity;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LoadMessagesEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

/**
 * MainView acts like a container of other widgets which will get displayed
 * after the user successfully logged in
 * 
 * 
 */
public class WestView extends Composite implements WestActivity.Displayable {

	protected User user;
	private CellTree cellTree;

	@Inject
	public WestView(final EventBus eventBus) {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				SingleSelectionModel<ImapFolder> selectionModel = (SingleSelectionModel<ImapFolder>) event.getSource();
				eventBus.fireEvent(new LoadMessagesEvent(user, selectionModel.getSelectedObject()));
			}
		});
//		viewModel.setSelectionModel(selectionModel);

		CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		cellTree = new CellTree(null, res);
		cellTree.setAnimationEnabled(true);
		initWidget(cellTree);

	}

	private final SingleSelectionModel<ImapFolder> selectionModel = new SingleSelectionModel<ImapFolder>(
	        new ProvidesKey<ImapFolder>() {
		        @Override
		        public Object getKey(ImapFolder item) {
			        return item == null ? null : item.getFullName();
		        }
	        });

	public Widget asWidget() {
		return this;
	}
}
