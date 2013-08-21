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

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.FolderListActivity;
import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
<<<<<<< HEAD
import org.apache.hupa.client.ui.LabelListView.ImapLabelListDataProvider;
import org.apache.hupa.client.ui.LabelListView.LabelCell;
import org.apache.hupa.client.ui.LabelListView.Resources;
import org.apache.hupa.client.ui.RightCellTree.Css;
=======
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
<<<<<<< HEAD
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
=======
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
<<<<<<< HEAD
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
=======
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class FolderListView extends Composite implements FolderListActivity.Displayable {
<<<<<<< HEAD
	@UiField ScrollPanel thisView;
=======
	@UiField SimplePanel thisView;
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
	@Inject private HupaController controller;
	@Inject private ToolBarActivity.Displayable toolBar;
	@Inject private MessageListActivity.Displayable msgListDisplay;
	@Inject private PlaceController placeController;
<<<<<<< HEAD
	private CellTree cellTree;
//	private FoldersTreeViewModel viewModel; 
=======
	private CellList<LabelNode> cellList;
	private ShowMorePagerPanel pagerPanel;
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f

	public interface Resources extends CellList.Resources {

		Resources INSTANCE = GWT.create(Resources.class);

		@Source("res/CssLabelListView.css")
		public CellList.Style cellListStyle();
	}
<<<<<<< HEAD
	@Inject
	public FolderListView(final HupaRequestFactory rf) {
		initWidget(binder.createAndBindUi(this));
		

		data = new ImapLabelListDataProvider(rf);
		CellList<LabelNode> cellList = new CellList<LabelNode>(new LabelCell(), Resources.INSTANCE);
=======
	
	public static final ProvidesKey<LabelNode> KEY_PROVIDER = new ProvidesKey<LabelNode>() {
	      @Override
	      public Object getKey(LabelNode item) {
	        return item == null ? null : item.getPath();
	      }
	    };

	@Inject
	public FolderListView(final HupaRequestFactory rf) {
		initWidget(binder.createAndBindUi(this));

		data = new ImapLabelListDataProvider(rf);
		pagerPanel = new ShowMorePagerPanel();
		cellList = new CellList<LabelNode>(new FolderCell(), Resources.INSTANCE, KEY_PROVIDER);
	    cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
	    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
	    cellList.setPageSize(100);// ShowMorePagerPanel does not work at present. Therefore, assume one's labels are under one hundred
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				controller.showTopLoading("Loading...");
				toolBar.enableAllTools(false);
				placeController.goTo(new FolderPlace(selectionModel.getSelectedObject().getFolder().getFullName()));
				msgListDisplay.refresh();
			}
		});
		data.addDataDisplay(cellList);
<<<<<<< HEAD
		thisView.setWidget(cellList);
		
//		this.viewModel = viewModel;
		
//		cellTree = new CellTree(viewModel,null,Resources.INSTANCE);
		
		
		
		
//		cellTree.setAnimationEnabled(true);
//		thisView.add(cellTree);
	}
//	public interface Resources extends CellTree.Resources {
//
//		Resources INSTANCE = GWT.create(Resources.class);
//
//		@Source("res/CssFolderListView.css")
//		public Css cellTreeStyle();
//
//		@Source("res/listicons.png")
//		public ImageResource listicons();
//	}
	
	@Override
	public void refresh(){
//		viewModel.refresh();
	}


=======
		pagerPanel.setDisplay(cellList);
		thisView.setWidget(pagerPanel);
	}

	@Override
	public void refresh() {
		data.refresh();
	}

>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
	public final SingleSelectionModel<LabelNode> selectionModel = new SingleSelectionModel<LabelNode>(
			new ProvidesKey<LabelNode>() {
				@Override
				public Object getKey(LabelNode item) {
					return item == null ? null : item.getPath();
				}
			});

<<<<<<< HEAD
	static class LabelCell extends AbstractCell<LabelNode> {

		public LabelCell() {
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, LabelNode value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}

			if (value.getFolder().getSubscribed()) {
				sb.appendHtmlConstant(value.getNameForDisplay());
			} else {
				sb.appendHtmlConstant("<span style='color:gray;'>");
				sb.appendHtmlConstant(value.getNameForDisplay());
				sb.appendHtmlConstant("</span>");
=======
	class FolderCell extends AbstractCell<LabelNode> {
		public FolderCell(String... consumedEvents) {
			super(consumedEvents);
		}
		// TODO different images for each folder
		@Override
		public void render(Context context, LabelNode value, SafeHtmlBuilder sb) {
			if (value != null) {
				if (value.getFolder().getUnseenMessageCount() > 0) {
					sb.appendHtmlConstant("<span style='right:6px;top:3px;font-weight:bold;'>");
					sb.appendHtmlConstant(value.getNameForDisplay());
					sb.appendHtmlConstant(" (" + value.getFolder().getUnseenMessageCount());
					sb.appendHtmlConstant(")</span>");
				} else {
					sb.appendHtmlConstant(value.getNameForDisplay());
				}
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
			}
		}
	}

	private final ImapLabelListDataProvider data;
<<<<<<< HEAD
=======

>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
	public class ImapLabelListDataProvider extends AsyncDataProvider<LabelNode> implements HasRefresh {

		private HupaRequestFactory rf;
		private List<LabelNode> folderNodes = new ArrayList<LabelNode>();
		HasData<LabelNode> display;

		public List<LabelNode> getDataList() {
			return folderNodes;
		}

		public ImapLabelListDataProvider(HupaRequestFactory rf) {
			this.rf = rf;
		}

		@Override
		public void addDataDisplay(HasData<LabelNode> display) {
			super.addDataDisplay(display);
			this.display = display;
		}

		@Override
		protected void onRangeChanged(HasData<LabelNode> display) {

			final int start = display.getVisibleRange().getStart();

			rf.fetchFoldersRequest().fetch(null, Boolean.TRUE).fire(new Receiver<List<ImapFolder>>() {

				private String INTENTS = "&nbsp;&nbsp;&nbsp;&nbsp;";

				@Override
				public void onSuccess(List<ImapFolder> response) {
					folderNodes.clear();
					if (response == null || response.size() == 0) {
						updateRowCount(-1, true);
					} else {
						for (ImapFolder folder : response) {
							fillCellList(folderNodes, folder, LabelNode.ROOT, "");
						}
						updateRowData(start, folderNodes);
					}
				}

				private void fillCellList(List<LabelNode> folderNodes, ImapFolder curFolder, LabelNode parent,
						String intents) {
					LabelNode labelNode = new LabelNode();
					labelNode.setFolder(curFolder);
					labelNode.setName(curFolder.getName());
					labelNode.setNameForDisplay(intents + curFolder.getName());
					labelNode.setParent(parent);
					labelNode.setPath(curFolder.getFullName());
					folderNodes.add(labelNode);
					if (curFolder.getHasChildren()) {
						for (ImapFolder subFolder : curFolder.getChildren()) {
							fillCellList(folderNodes, subFolder, labelNode, intents + INTENTS);
						}
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

	interface FolderListUiBinder extends UiBinder<SimplePanel, FolderListView> {
	}

	private static FolderListUiBinder binder = GWT.create(FolderListUiBinder.class);

}
