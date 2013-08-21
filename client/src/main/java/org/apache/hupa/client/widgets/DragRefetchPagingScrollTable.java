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

package org.apache.hupa.client.widgets;

import java.util.Iterator;

import org.apache.hupa.client.dnd.PagingScrollTableRowDragController;
import org.apache.hupa.widgets.ui.RefetchPagingScrollTable;

import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.user.client.ui.Widget;

public class DragRefetchPagingScrollTable<RowType> extends RefetchPagingScrollTable<RowType>{
    private DragHandlerFactory factory;
    private int cellIndex =-1;
    private PagingScrollTableRowDragController tableRowDragController;
    public DragRefetchPagingScrollTable(MutableTableModel<RowType> tableModel,
            FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
            DefaultTableDefinition<RowType> tableDefinition, PagingScrollTableRowDragController tableRowDragController,int dragColumn) {
        super(tableModel, dataTable, headerTable, tableDefinition);
        this.tableRowDragController = tableRowDragController;
        tableRowDragController.addDragHandler(new DragHandlerAdapter() {

            @Override
            public void onDragStart(DragStartEvent event) {
                getDataTable().deselectAllRows();
            }
            
        });
    }

    /**
     * Factory which will create new Handler Widgets 
     *      */
    public interface DragHandlerFactory {
        public Widget createHandler();
    }

    public void setDragHandler(int newCellIndex,int cellWidth,DragHandlerFactory factory) {
        this.factory = factory;
        
        if (newCellIndex < 0) {
            throw new IllegalArgumentException("cellIndex needs to be higher then 0");
        }
        
        DefaultTableDefinition<RowType> tableDef = (DefaultTableDefinition<RowType>) getTableDefinition();
        
        // remove old definition 
        if (cellIndex != -1) {
            tableDef.removeColumnDefinition(tableDef.getColumnDefinition(cellIndex));
        }
        
        this.cellIndex = newCellIndex;
        
        // Create new ghost definition which will get used later to add the drag widget
        DragColumnDefinition def = new DragColumnDefinition();
        def.setColumnSortable(false);
        def.setColumnTruncatable(false);
        def.setMaximumColumnWidth(cellWidth);
        def.setPreferredColumnWidth(cellWidth);
        def.setMinimumColumnWidth(cellWidth);
        tableDef.addColumnDefinition(cellIndex,def);
    }
    
    
    @Override
    protected void setData(int firstRow, Iterator<RowType> rows) {
        super.setData(firstRow, rows);
        
        if (getRowValues().size() >0 && factory != null && cellIndex > -1) {
            for (int i = 0; i < getRowValues().size();i++) {
                Widget handler = factory.createHandler();
                getDataTable().setWidget(i, cellIndex, handler);
                tableRowDragController.makeDraggable(handler);
            }
        }
    }
    
    private final class DragColumnDefinition extends AbstractColumnDefinition<RowType, Boolean> {
        
        @Override
        public Boolean getCellValue(RowType rowValue) {
            return true;
        }

        @Override
        public void setCellValue(RowType rowValue, Boolean cellValue) {
        
        }
        
    }
    
}
