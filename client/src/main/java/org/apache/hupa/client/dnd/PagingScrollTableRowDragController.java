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

package org.apache.hupa.client.dnd;

import org.apache.hupa.client.HupaMessages;
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.widgets.ui.RndPanel;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class PagingScrollTableRowDragController extends PickupDragController {


    private HupaMessages messages = GWT.create(HupaMessages.class);
    private HupaImageBundle bundle =GWT.create(HupaImageBundle.class);
    private FixedWidthGrid draggableTable;
    @SuppressWarnings("rawtypes")
    private PagingScrollTable parentTable;
    private int dragRow;
    private RowDragProxy proxyWidget;

    public PagingScrollTableRowDragController() {
        this(RootPanel.get());

    }
    public PagingScrollTableRowDragController(AbsolutePanel boundaryPanel) {
        super(boundaryPanel, false);
        setBehaviorDragProxy(true);
        setBehaviorMultipleSelection(false);
    }

    @Override
    public void dragEnd() {
        super.dragEnd();

        // cleanup
        draggableTable = null;
        parentTable = null;
        dragRow = -1;
        proxyWidget = null;
    }

    @Override
    public void setBehaviorDragProxy(boolean dragProxyEnabled) {
        if (!dragProxyEnabled) {
            // TODO implement drag proxy behavior
            throw new IllegalArgumentException();
        }
        super.setBehaviorDragProxy(dragProxyEnabled);
    }

    @Override
    protected BoundaryDropController newBoundaryDropController(
            AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
        if (allowDroppingOnBoundaryPanel) {
            throw new IllegalArgumentException();
        }
        return super.newBoundaryDropController(boundaryPanel,
                allowDroppingOnBoundaryPanel);
    }


    @SuppressWarnings("rawtypes")
    protected Widget newDragProxy(DragContext context) {

        draggableTable = (FixedWidthGrid) context.draggable.getParent();
        parentTable = (PagingScrollTable)draggableTable.getParent();

        dragRow = getWidgetRow(context.draggable);

        proxyWidget = new RowDragProxy();
        return proxyWidget;
    }

    private int getWidgetRow(Widget widget) {
        FixedWidthGrid grid = draggableTable;
        for (int row = 0; row < grid.getRowCount(); row++) {
            for (int col = 0; col < grid.getCellCount(row); col++) {
                Widget w = grid.getWidget(row, col);
                if (w == widget) {
                    return row;
                }
            }
        }
        throw new RuntimeException("Unable to determine widget row");
    }



    public RowDragProxy getCurrentProxy() {
        return proxyWidget;
    }

    public Object getDragValue() {
        return parentTable.getRowValue(dragRow);
    }

    public class RowDragProxy extends Composite {
        private String styleName = "hupa-droptarget-invalid";
        private HorizontalPanel proxy = new HorizontalPanel();
        public RowDragProxy() {
            RndPanel proxyPanel = new RndPanel();
            setIsValid(false);
            proxy.add(new Image(bundle.moveMailIcon()));
            proxy.add(new Label(" " + messages.moveMessage()));
            proxyPanel.add(proxy);
            proxyPanel.setWidth("150px");
            initWidget(proxyPanel);
        }


        public void setIsValid(boolean valid) {
            if (valid) {
                proxy.removeStyleName(styleName);
            } else {
                proxy.addStyleName(styleName);
            }
        }
    }

}