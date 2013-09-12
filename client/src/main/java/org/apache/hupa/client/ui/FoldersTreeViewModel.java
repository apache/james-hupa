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

import java.util.List;

import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LoadMessagesEvent;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class FoldersTreeViewModel implements TreeViewModel {

	@Inject private HupaRequestFactory rf;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	// @Inject private Provider<MailFolderPlace> folderPlaceProvider;
	private User user;
	private ImapFolder currentFolder;

	public FoldersTreeViewModel() {

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@SuppressWarnings("unchecked")
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						SingleSelectionModel<ImapFolder> selectionModel = (SingleSelectionModel<ImapFolder>) event
								.getSource();
						currentFolder = selectionModel.getSelectedObject();
						eventBus.fireEvent(new LoadMessagesEvent(user,
								selectionModel.getSelectedObject()));
						placeController.goTo(new MailFolderPlace(selectionModel
								.getSelectedObject().getFullName()));
					}
				});
	}

	private final SingleSelectionModel<ImapFolder> selectionModel = new SingleSelectionModel<ImapFolder>(
			new ProvidesKey<ImapFolder>() {
				@Override
				public Object getKey(ImapFolder item) {
					return item == null ? null : item.getFullName();
				}
			});

	/**
	 * Get the {@link NodeInfo} that provides the children of the specified
	 * value.
	 */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		return new DefaultNodeInfo<ImapFolder>(new ImapFolderListDataProvider(
				(ImapFolder) value), new AbstractCell<ImapFolder>(ClickEvent
				.getType().getName()) {
			// TODO different images for each folder
			@Override
			public void render(Context context, ImapFolder value,
					SafeHtmlBuilder sb) {
				if (value != null) {
					sb.appendEscaped(value.getName());
				}
			}

			// TODO is this a click event?
			@Override
			public void onBrowserEvent(Context context, Element parent,
					ImapFolder value, NativeEvent event,
					ValueUpdater<ImapFolder> valueUpdater) {
				if (clickSameFolder(value)) {
					eventBus.fireEvent(new LoadMessagesEvent(user, value));
					placeController.goTo(new MailFolderPlace(value
							.getFullName()));
				}
			}

			private boolean clickSameFolder(ImapFolder value) {
				return value == currentFolder;
			}
		}, selectionModel, null);
	}

	class ImapFolderListDataProvider extends AsyncDataProvider<ImapFolder> {

		private ImapFolder folder;

		public ImapFolderListDataProvider(ImapFolder folder) {
			this.folder = folder;
		}

		@Override
		public void addDataDisplay(HasData<ImapFolder> display) {
			super.addDataDisplay(display);
		}

		@Override
		protected void onRangeChanged(HasData<ImapFolder> display) {
			rf.fetchFoldersRequest().fetch(folder)
					.fire(new Receiver<List<ImapFolder>>() {
						@Override
						public void onSuccess(List<ImapFolder> response) {
							if (response == null || response.size() == 0) {
								updateRowCount(-1, true);
							} else {
								updateRowData(0, response);
							}
						}

						@Override
						public void onFailure(ServerFailure error) {
							if (error.isFatal()) {
								throw new RuntimeException(error.getMessage());
							}
						}

					});

		}

	}

	/**
	 * Check if the specified value represents a leaf node. Leaf nodes cannot be
	 * opened.
	 */
	@Override
	public boolean isLeaf(Object value) {
		if (value == null)
			return false;
		if (value instanceof ImapFolder) {
			ImapFolder folder = (ImapFolder) value;
			if (!folder.getHasChildren())
				return true;
		}
		return false;
	}

}
