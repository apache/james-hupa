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

import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.events.MoveMessageEvent;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;

public class MessageListFooterActivity extends AppBaseActivity {

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus) {
        container.setWidget(display.asWidget());
        bindTo(eventBus);
    }

    private void bindTo(final EventBus eventBus) {
        final ListBox labels = display.getLabels();
        labels.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                int selectedIndex = labels.getSelectedIndex();
                if (selectedIndex > 0){
                    String newFolderName = labels.getItemText(labels.getSelectedIndex()).replace(".", "").trim();
                    eventBus.fireEvent(new MoveMessageEvent(new ImapFolderImpl(newFolderName)));
                }
            }
        });
    }

    @Inject private Displayable display;

    public interface Displayable extends IsWidget {
        SimplePager getPager();
        HasVisibility getLabelsPanel();
        ListBox getLabels();
    }
}
