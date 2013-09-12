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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel.Callback;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.Response;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.gen2.table.event.client.TableEvent.Row;

/**
 * PagingScrollTable which refetch a row after one is deleted. This ensure that always the configured page
 * size is shown. 
 * 
 * 
 *
 */
public class RefetchPagingScrollTable<RowType> extends PagingScrollTable<RowType>{
    private ArrayList<RowType> selectedRows = new ArrayList<RowType>();

    
    public RefetchPagingScrollTable(MutableTableModel<RowType> tableModel,
            FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
            TableDefinition<RowType> tableDefinition) {
        super(tableModel, dataTable, headerTable, tableDefinition);

        getDataTable().addRowSelectionHandler(new RowSelectionHandler() {

            public void onRowSelection(RowSelectionEvent event) {
                Iterator<Row> rowIndexIt = event.getSelectedRows().iterator();
                while(rowIndexIt.hasNext()) {
                    RowType row = getRowValue(rowIndexIt.next().getRowIndex());
                    if (selectedRows.contains(row) == false) {
                        selectedRows.add(row);
                    }
                }
                
                Iterator<Row> rowDeselectIndexIt = event.getDeselectedRows().iterator();
                while(rowDeselectIndexIt.hasNext()) {
                    RowType row = getRowValue(rowDeselectIndexIt.next().getRowIndex());
                    selectedRows.remove(row);
                }
            }
            
        });
    }
    
    /**
     * Get selected rows
     * 
     * @return rows
     */
    public List<RowType> getSelectedRows() {
        return selectedRows;
    }

    /**
     * Remove the given rows from the underlying dataTable 
     * 
     * @param rows
     */
    public void removeRows(List<RowType> rows) {
        ArrayList<Integer> rowsIndex = new ArrayList<Integer>();
        for (RowType rowType : rows) {
            int rowIndex = getRowValues().indexOf(rowType);
            if (rowsIndex.contains(rowIndex) == false) {
                rowsIndex.add(rowIndex);
            }
        }
        // Check if we found any rows to remove
        if (rowsIndex.isEmpty() == false) {
            // remove the row value on deletion
            for (int i = 0; i <rowsIndex.size();i++) {
                int index = rowsIndex.get(i) -i;
                selectedRows.remove(getRowValue(index));
                getRowValues().remove(index);

                ((MutableTableModel<RowType>) getTableModel()).removeRow(index);
            }
            
            // Check if we need to refetch rows
            if (getTableModel().getRowCount() >= getPageSize()) {
                // request new rows to fill the table again
                Request r = new Request(getAbsoluteLastRowIndex() +1,rowsIndex.size());
                getTableModel().requestRows(r, new Callback<RowType>() {

                    public void onFailure(Throwable caught) {
                        // Nothing todo
                    }

                    public void onRowsReady(Request request,
                            Response<RowType> response) {
                        // Add the new row values
                        Iterator<RowType> it = response.getRowValues();
                        while (it.hasNext()) {
                            getRowValues().add(it.next());
                        }
                        // copy the selected rows to reset it after reloading the data
                        Iterator<Integer> selected = new HashSet<Integer>(getDataTable().getSelectedRows()).iterator();
                    
                        // set the data
                        setData(getAbsoluteFirstRowIndex(), getRowValues().iterator());
                    
                        // select the rows again
                        while (selected.hasNext()) {
                            getDataTable().selectRow(selected.next(), false);
                        }
                    }
                
                });
            } else {
                // redraw the table to eliminate empty rows
                redraw();
            }
        }
    }
}
