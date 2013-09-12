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

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.client.activity.NotificationActivity;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
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
	@Inject private HupaController controller;
	@Inject private PlaceController placeController;
	@Inject private NotificationActivity.Displayable notice;
	@Inject private ToolBarActivity.Displayable toolBar;
	@Inject private MessageListActivity.Displayable msgListDisplay;

	@Inject
	public FoldersTreeViewModel() {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				controller.showTopLoading("Loading...");
				SingleSelectionModel<ImapFolder> selectionModel = (SingleSelectionModel<ImapFolder>) event.getSource();
				toolBar.enableAllTools(false);
				placeController.goTo(new FolderPlace(selectionModel.getSelectedObject().getFullName()));
				msgListDisplay.refresh();
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
	private ImapFolderListDataProvider dataProvider;

	public void refresh() {
		dataProvider.refresh();
	}

	/**
	 * Get the {@link NodeInfo} that provides the children of the specified
	 * value.
	 */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		dataProvider = new ImapFolderListDataProvider(rf, (ImapFolder) value);
		return new DefaultNodeInfo<ImapFolder>(dataProvider, new FolderCell(ClickEvent.getType().getName()),
				selectionModel, null);
	}

	class FolderCell extends AbstractCell<ImapFolder> {
		public FolderCell(String... consumedEvents) {
			super(consumedEvents);
		}
		// TODO different images for each folder
		@Override
		public void render(Context context, ImapFolder value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped(value.getName());
			}
			if (value.getUnseenMessageCount() > 0) {
				sb.appendHtmlConstant("<span style='position:absolute;right:6px;top:3px;font-weight:bold;'>(");
				sb.appendHtmlConstant("" + value.getUnseenMessageCount());
				sb.appendHtmlConstant(")</span>");
			}
		}

		// TODO is this a click event?
//		@Override
//		public void onBrowserEvent(Context context, Element parent, ImapFolder value, NativeEvent event,
//				ValueUpdater<ImapFolder> valueUpdater) {
//			super.onBrowserEvent(context, parent, value, event, valueUpdater);
//				placeController.goTo(new FolderPlace(value.getFullName()));
//
//		}

	}

	public class ImapFolderListDataProvider extends AsyncDataProvider<ImapFolder> implements HasRefresh{

		private HupaRequestFactory rf;
		private ImapFolder folder;
		private HasData<ImapFolder> display;

		public ImapFolderListDataProvider(HupaRequestFactory rf, ImapFolder folder) {
			this.rf = rf;
			this.folder = folder;
		}

		@Override
		public void addDataDisplay(HasData<ImapFolder> display) {
			super.addDataDisplay(display);
			this.display = display;
		}

		@Override
		protected void onRangeChanged(HasData<ImapFolder> display) {
			rf.fetchFoldersRequest().fetch(folder, Boolean.FALSE).fire(new Receiver<List<ImapFolder>>() {
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

		@Override
		public void refresh() {
			this.onRangeChanged(display);
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
