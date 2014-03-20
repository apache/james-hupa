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

import org.apache.hupa.client.activity.ContactsListActivity;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class ContactsListView extends Composite implements ContactsListActivity.Displayable {

    @UiField ScrollPanel thisView;

    @UiField Button add;
    @UiField Button delete;

    public interface Resources extends CellList.Resources {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("res/CssLabelListView.css")
        public CellList.Style cellListStyle();
    }
    @UiHandler("add")
    public void handleAdd(ClickEvent e) {
    }

    private final ImapLabelListDataProvider data;

    @Inject
    public ContactsListView(final HupaRequestFactory rf) {
        initWidget(binder.createAndBindUi(this));
        data = new ImapLabelListDataProvider(rf);
        CellList<LabelNode> cellList = new CellList<LabelNode>(new LabelCell(), Resources.INSTANCE);
        cellList.setPageSize(100);// assume one's labels are under one hundred, otherwise we need a pager
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
            }
        });
        data.addDataDisplay(cellList);
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
            }
        }
    }

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
                    if("inbox".equalsIgnoreCase(curFolder.getName())){
                        if(selectionModel.getSelectedObject() == null){
                            selectionModel.setSelected(labelNode, true);
                        }
                    }
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

    interface Binder extends UiBinder<DockLayoutPanel, ContactsListView> {
    }

    private static Binder binder = GWT.create(Binder.class);

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
        data.refresh();
    }

}
