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

package org.apache.hupa.client.mvp;

import net.customware.gwt.presenter.client.widget.WidgetContainerDisplay;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The View which acts as container for Hupa sub parts
 * 
 *
 */
public class ContainerView extends Composite implements WidgetContainerDisplay {

    private TabPanel tabPanel = new TabPanel();
    
    public ContainerView () {
        tabPanel.getDeckPanel().setStyleName("hupa-Container-Navigation");
        initWidget(tabPanel);
    }
    
    /**
     * Widgets which should get add to the Container needs to implements the NameAwareWidgetContainerDisplay interface
     * 
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetContainerDisplay#addWidget(com.google.gwt.user.client.ui.Widget)
     */
    public void addWidget(Widget widget) {
        if (widget instanceof NameAwareWidgetContainerDisplay) {
            tabPanel.add(widget,((NameAwareWidgetContainerDisplay) widget).getName());
        } else {
            throw new IllegalArgumentException("Given widget must implement NameAwareWidgetContainerDisplay");
        }
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetContainerDisplay#removeWidget(com.google.gwt.user.client.ui.Widget)
     */
    public void removeWidget(Widget widget) {
        tabPanel.remove(widget);
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetContainerDisplay#showWidget(com.google.gwt.user.client.ui.Widget)
     */
    public void showWidget(Widget widget) {
        int index = tabPanel.getWidgetIndex(widget);
        tabPanel.selectTab(index);
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    public Widget asWidget() {
        return this;
    }

}
