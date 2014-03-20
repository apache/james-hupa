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
import org.apache.hupa.client.storage.HupaStorage;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.query.client.Function;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class FolderListView extends Composite implements FolderListActivity.Displayable {
    @UiField SimplePanel thisView;
    @Inject private HupaController controller;
    @Inject private ToolBarActivity.Displayable toolBar;
    @Inject private MessageListActivity.Displayable msgListDisplay;
    @Inject private PlaceController placeController;
    @Inject private HupaStorage hupaStorage;

    private CellList<LabelNode> cellList;
    private ShowMorePagerPanel pagerPanel;

    public interface Resources extends CellList.Resources {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("res/CssLabelListView.css")
        public CellList.Style cellListStyle();
    }

    public static final ProvidesKey<LabelNode> KEY_PROVIDER = new ProvidesKey<LabelNode>() {
          @Override
          public Object getKey(LabelNode item) {
            return item == null ? null : item.getPath();
          }
        };

    protected void onAttach() {
        super.onAttach();
        // Delay getting data until the widget has been attached, to use injected objects.
        if (data.getDataDisplays().size() == 0) {
            data.addDataDisplay(cellList);
        }
    };

    public FolderListView() {
        initWidget(binder.createAndBindUi(this));

        data = new ImapLabelListDataProvider();
        pagerPanel = new ShowMorePagerPanel();
        cellList = new CellList<LabelNode>(new FolderCell(), Resources.INSTANCE, KEY_PROVIDER);
        cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
        cellList.setPageSize(100);// ShowMorePagerPanel does not work at present. Therefore, assume one's labels are under one hundred
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                controller.showTopLoading("Loading...");
                toolBar.enableAllTools(false);
                placeController.goTo(new FolderPlace(selectionModel.getSelectedObject().getFolder().getFullName()));
                msgListDisplay.refresh();
            }
        });
        pagerPanel.setDisplay(cellList);
        thisView.setWidget(pagerPanel);
    }

    @Override
    public void refresh() {
        data.refresh();
    }

    public final SingleSelectionModel<LabelNode> selectionModel = new SingleSelectionModel<LabelNode>(
            new ProvidesKey<LabelNode>() {
                @Override
                public Object getKey(LabelNode item) {
                    return item == null ? null : item.getPath();
                }
            });

    class FolderCell extends AbstractCell<LabelNode> {
        public FolderCell(String... consumedEvents) {
            super(consumedEvents);
        }
        // TODO different images for each folder
        @Override
        public void render(Context context, LabelNode value, SafeHtmlBuilder sb) {
            if (value != null) {
//                if (value.getFolder().getUnseenMessageCount() > 0) {
//                    sb.appendHtmlConstant("<span style='right:6px;top:3px;font-weight:bold;'>");
//                    sb.appendHtmlConstant(value.getNameForDisplay());
//                    sb.appendHtmlConstant(" (" + value.getFolder().getUnseenMessageCount());
//                    sb.appendHtmlConstant(")</span>");
//                } else {
                    sb.appendHtmlConstant(value.getNameForDisplay());
//                }
            }
        }
    }

    private final ImapLabelListDataProvider data;

    public class ImapLabelListDataProvider extends AsyncDataProvider<LabelNode> implements HasRefresh {

        private List<LabelNode> folderNodes = new ArrayList<LabelNode>();
        HasData<LabelNode> display;

        public List<LabelNode> getDataList() {
            return folderNodes;
        }

        @Override
        public void addDataDisplay(HasData<LabelNode> display) {
            super.addDataDisplay(display);
            this.display = display;
        }

        protected void onRangeChanged(HasData<LabelNode> display) {
            onRangeChanged(display, false);
        }

        protected void onRangeChanged(HasData<LabelNode> display, boolean skipCache) {
            hupaStorage
                .gettingFolders(skipCache)
                .done(new Function(){public void f() {
                    List<ImapFolder> response = arguments(0);
                    folderNodes.clear();
                    for (ImapFolder folder : response) {
                        fillCellList(folderNodes, folder, LabelNode.ROOT, "");
                    }
                    // For some reason removing a row does not update the display correctly
                    updateRowCount(folderNodes.size(), true);
                    updateRowData(0, folderNodes);
                 }});
        }

        private void fillCellList(List<LabelNode> folderNodes, ImapFolder curFolder, LabelNode parent, String intents) {
            LabelNode labelNode = new LabelNode();
            labelNode.setFolder(curFolder);
            labelNode.setName(curFolder.getName());
            labelNode.setNameForDisplay(intents + curFolder.getName());
            labelNode.setParent(parent);
            labelNode.setPath(curFolder.getFullName());
            folderNodes.add(labelNode);
            if (curFolder.getHasChildren()) {
                for (ImapFolder subFolder : curFolder.getChildren()) {
                    // FIXME: don't use intents, it will be much better user experience to use cellTree
                    fillCellList(folderNodes, subFolder, labelNode, intents + "&nbsp;&nbsp;&nbsp;&nbsp;");
                }
            }
        }

        Double last = 0d;
        @Override
        public void refresh(){
            Double now = Duration.currentTimeMillis();
            if (now - last > 1 * 60  * 1000) {
                this.onRangeChanged(display, true);
                last = now;
            }
        }
    }



    interface FolderListUiBinder extends UiBinder<SimplePanel, FolderListView> {
    }

    private static FolderListUiBinder binder = GWT.create(FolderListUiBinder.class);

}
