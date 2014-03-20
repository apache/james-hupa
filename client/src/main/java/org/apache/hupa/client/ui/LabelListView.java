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

import org.apache.hupa.client.activity.LabelListActivity;
import org.apache.hupa.client.activity.LabelPropertiesActivity;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.storage.HupaStorage;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class LabelListView extends Composite implements LabelListActivity.Displayable {

    @Inject LabelPropertiesActivity.Displayable labelProperties;
    @Inject private HupaStorage hupaStorage;
    @UiField ScrollPanel thisView;

    @UiField Button add;
    @UiField Button delete;
    private CellList<LabelNode> cellList;

    public interface Resources extends CellList.Resources {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("res/CssLabelListView.css")
        public CellList.Style cellListStyle();
    }
    @UiHandler("add")
    public void handleAdd(ClickEvent e) {
        labelProperties.cascade(selectionModel.getSelectedObject(), data.getDataList(), CASCADE_TYPE_ADD);
    }

    private final ImapLabelListDataProvider data;

    protected void onAttach() {
        super.onAttach();
        // Delay getting data until the widget has been attached, to use injected objects.
        if (data.getDataDisplays().size() == 0) {
            data.addDataDisplay(cellList);
        }
    };

    @Inject
    public LabelListView(final HupaRequestFactory rf) {
        initWidget(binder.createAndBindUi(this));
        data = new ImapLabelListDataProvider();
        cellList = new CellList<LabelNode>(new LabelCell(), Resources.INSTANCE);
        cellList.setPageSize(100);// assume one's labels are under one hundred, otherwise we need a pager
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                labelProperties.cascade(selectionModel.getSelectedObject(), data.getDataList(), CASCADE_TYPE_RENAME);
            }
        });
        thisView.setWidget(cellList);
    }

    @Override
    public SingleSelectionModel<LabelNode> getSelectionModel() {
        return this.selectionModel;
    }

    public final SingleSelectionModel<LabelNode> selectionModel = new SingleSelectionModel<LabelNode>(
            new ProvidesKey<LabelNode>() {
                @Override
                public Object getKey(LabelNode item) {
                    return item == null ? null : item.getPath();
                }
            });

    // FIXME: almost the code in this class is identical to FolderListView, duplicated code in GWT is bad
    // because explodes js size !!!
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

        @Override
        protected void onRangeChanged(HasData<LabelNode> display) {
            hupaStorage
                .gettingFolders()
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

        @Override
        public void refresh() {
            this.onRangeChanged(display);
        }
    }

    interface LabelListUiBinder extends UiBinder<DockLayoutPanel, LabelListView> {
    }

    private static LabelListUiBinder binder = GWT.create(LabelListUiBinder.class);

    @Override
    public HasClickHandlers getAdd() {
        return add;
    }

    @Override
    public HasClickHandlers getDelete() {
        return delete;
    }

    @Override
    public void refresh() {
        System.out.println("REFRESH");
        hupaStorage.expireFolders();
        data.refresh();
    }

}
