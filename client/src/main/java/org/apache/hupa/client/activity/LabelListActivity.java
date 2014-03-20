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

package org.apache.hupa.client.activity;

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.rf.DeleteFolderRequest;
import org.apache.hupa.client.ui.LabelNode;
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.events.DeleteFolderEvent;
import org.apache.hupa.shared.events.DeleteFolderEventHandler;
import org.apache.hupa.shared.events.RefreshLabelListEvent;
import org.apache.hupa.shared.events.RefreshLabelListEventHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LabelListActivity extends AppBaseActivity {

    @Inject private HupaController hupaController;
    @Inject private Displayable display;
    @Inject private LabelPropertiesActivity.Displayable labelProperties;


    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        container.setWidget(display.asWidget());
        bindTo(eventBus);
    }

    private void bindTo(final EventBus eventBus) {
        this.registerHandler(display.getDelete().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Are you sure?")) {
                    eventBus.fireEvent(new DeleteFolderEvent());
                }
            }
        }));
        eventBus.addHandler(DeleteFolderEvent.TYPE, new DeleteFolderEventHandler() {
            @Override
            public void onDeleteFolderEvent(DeleteFolderEvent event) {
                deleteSelected();
            }
        });
        eventBus.addHandler(RefreshLabelListEvent.TYPE, new RefreshLabelListEventHandler(){
            @Override
            public void onRefreshEvent(RefreshLabelListEvent event) {
                display.refresh();
            }
        });
    }

    public interface Displayable extends IsWidget {
        final int CASCADE_TYPE_ADD = 0x01;
        final int CASCADE_TYPE_RENAME = 0x02;
        SingleSelectionModel<LabelNode> getSelectionModel();
        HasClickHandlers getAdd();
        HasClickHandlers getDelete();
        void refresh();
    }

    public void deleteSelected() {
        hupaController.showTopLoading("Deleting...");
        SingleSelectionModel<LabelNode> selectionModel = display.getSelectionModel();
        LabelNode labelNode = selectionModel.getSelectedObject();
        DeleteFolderRequest req = rf.deleteFolderRequest();
        DeleteFolderAction action = req.create(DeleteFolderAction.class);
        final ImapFolder f = req.create(ImapFolder.class);
        f.setFullName(labelNode.getFolder().getFullName());
        action.setFolder(f);
        req.delete(action).fire(new Receiver<GenericResult>() {
            @Override
            public void onSuccess(GenericResult response) {
                hupaController.hideTopLoading();
                display.refresh();
                hupaController.showNotice("The label \"" + f.getFullName() + "\" was deleted.", 10000);
            }
            @Override
            public void onFailure(ServerFailure error) {
                hupaController.hideTopLoading();
                hupaController.showNotice(error.getMessage(), 10000);
            }
        });
    }
}
