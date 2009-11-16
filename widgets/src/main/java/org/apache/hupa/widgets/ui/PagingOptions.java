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

import org.apache.hupa.widgets.PagingOptionsConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.event.client.PageChangeEvent;
import com.google.gwt.gen2.table.event.client.PageChangeHandler;
import com.google.gwt.gen2.table.event.client.PageCountChangeEvent;
import com.google.gwt.gen2.table.event.client.PageCountChangeHandler;
import com.google.gwt.gen2.table.event.client.PageLoadEvent;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.gen2.table.event.client.PagingFailureEvent;
import com.google.gwt.gen2.table.event.client.PagingFailureHandler;
import com.google.gwt.gen2.table.event.client.RowCountChangeEvent;
import com.google.gwt.gen2.table.event.client.RowCountChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * PagingOptions for using to control the PagingScrollTable
 *
 */
public class PagingOptions extends Composite {    
    private HorizontalPanel pagingPanel = new HorizontalPanel();
    private EnableHyperlink firstLink;
    private EnableHyperlink prevLink;
    private EnableHyperlink lastLink;
    private EnableHyperlink nextLink;
    private Label text = new Label();
    private int currentPage = 1;
    private Loading loading = new Loading();
    private SimplePanel panel = new SimplePanel();
    
    public PagingOptions(final PagingScrollTable<?> table, PagingOptionsConstants constants) {
        firstLink = new EnableHyperlink("<< " + constants.pageFirst(),"");
        prevLink = new EnableHyperlink("< " + constants.pagePrev(),"");
        lastLink = new EnableHyperlink(constants.pageLast() + " >>","");
        nextLink = new EnableHyperlink(constants.pageNext() + " >","");
        pagingPanel.setSpacing(3);

        pagingPanel.add(panel);
        pagingPanel.add(firstLink);
        pagingPanel.add(prevLink);
        pagingPanel.add(nextLink);
        pagingPanel.add(lastLink);
        loading.hide();
        panel.setWidget(text);
        panel.setWidth("100px");
        pagingPanel.setCellHorizontalAlignment(panel, HorizontalPanel.ALIGN_CENTER);
        firstLink.setEnabled(false);
        prevLink.setEnabled(false);
        lastLink.setEnabled(false);
        nextLink.setEnabled(false);
        
        table.addPageCountChangeHandler(new PageCountChangeHandler() {

            public void onPageCountChange(PageCountChangeEvent event) {
                int startCount =  currentPage * table.getPageSize();
                
                if (currentPage == 0) {
                    startCount = 0;
                }
                
                int endCount  = startCount + table.getPageSize();
                
                int rows = table.getTableModel().getRowCount();
                updateControl(startCount, endCount, rows);
            }
            
        });
        
        
        table.addPageChangeHandler(new PageChangeHandler() {

            public void onPageChange(PageChangeEvent event) {
                loading(true);
            
                currentPage = event.getNewPage();
                int startCount =  currentPage * table.getPageSize();
                
                if (currentPage == 0) {
                    startCount = 0;
                }
                
                int endCount  = startCount + table.getPageSize();
                int rows = table.getTableModel().getRowCount();
            
                
                updateControl(startCount, endCount, rows);
            }
        });
        
        table.addPageLoadHandler(new PageLoadHandler() {

            public void onPageLoad(PageLoadEvent event) {
                loading(false);
            }
            
        });
        
        table.addPagingFailureHandler(new PagingFailureHandler() {

            public void onPagingFailure(PagingFailureEvent event) {
                loading(false);
            }
            
        });
        
        firstLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                table.gotoFirstPage();
            }
            
        });
        
        prevLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                table.gotoPreviousPage();
            }
            
        });
        
        nextLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                table.gotoNextPage();
            }
            
        });
        
        lastLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                table.gotoLastPage();
            }
            
        });
        
        table.getTableModel().addRowCountChangeHandler(new RowCountChangeHandler() {

            public void onRowCountChange(RowCountChangeEvent event) {
                int startCount =  currentPage * table.getPageSize();
                
                if (currentPage == 0) {
                    startCount = 0;
                }
                
                int endCount  = startCount + table.getPageSize();
                
                int rows =event.getNewRowCount();
                updateControl(startCount, endCount, rows);

            }
            
        });
        initWidget(pagingPanel);
    }
    

    protected void loading(boolean isLoading) {
        if (isLoading) {
            loading.show();
            panel.setWidget(loading);
        } else {
            loading.hide();
            panel.setWidget(text);
        }
    
    }
    
    protected void updateControl(int startCount, int endCount, int rows) {
        if (rows == TableModel.UNKNOWN_ROW_COUNT) {
            startCount = 0;
            endCount = 0;
            rows = 0;
        } 
        
        if (rows < endCount) {
            endCount = rows;
        }
        
        if (endCount == 0) {
            startCount = 0;
        }
        
        if (startCount <= 0) {
            firstLink.setEnabled(false);
            prevLink.setEnabled(false);
        } else {
            firstLink.setEnabled(true);
            prevLink.setEnabled(true);
        }
        
        if (rows > endCount) {
            lastLink.setEnabled(true);
            nextLink.setEnabled(true);
        } else {
            lastLink.setEnabled(false);
            nextLink.setEnabled(false);
        }
        text.setText(startCount + " - " + endCount + " of " + rows);

    }
    
    /**
     * Reset the currentPage to 0 
     */
    public void reset() {
        currentPage = 0;
        text.setText("0 - 0 of 0");
    }
    
    /**
     * Return the Link to navigate to the first page
     * 
     * @return firstLink
     */
    public EnableHyperlink getFirstLink() {
        return firstLink;
    }
    
    /**
     * Return the Link to navigate to the previous page
     * 
     * @return prevLink
     */
    public EnableHyperlink getPrevLink() {
        return prevLink;
    }
    
    /**
     * Return the Link to navigate to the next page
     * 
     * @return nextLink
     */
    public EnableHyperlink getNextLink() {
        return nextLink;
    }
    
    /**
     * Return the Link to navigate to the last page
     * 
     * @return lastLink
     */
    public EnableHyperlink getLastLink() {
        return lastLink;
    }

}
