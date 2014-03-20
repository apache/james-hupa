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

package org.apache.hupa.widgets.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Tree which can holds EditableTreeItem instances
 *
 *
 */
public class EditableTree extends Tree {
    public EditableTree(Tree.Resources images, boolean leaf) {
        super(images, leaf);
    }

    public EditableTree() {
        super();
    }

    public EditableTree(Tree.Resources images) {
        super(images);
    }

    /**
     * Prevent  Event.ONCLICK, Event.ONMOUSEDOWN, Event.ONKEYDOWN from bubble down if the item is in editing mode
     */
    public void onBrowserEvent(Event event) {
        TreeItem item = getSelectedItem();

        // Check if the selectedItem is Editable and if so make sure the events are not fired
        if (item instanceof HasEditable) {
            if (item != null && ((HasEditable) item).isEdit()) {
                int type = DOM.eventGetType(event);
                switch (type) {
                case Event.ONCLICK:
                    return;
                case Event.ONMOUSEDOWN:
                    return;
                case Event.ONKEYDOWN:
                    return;
                default:
                    break;
                }
            }
        }
        super.onBrowserEvent(event);
    }
}
