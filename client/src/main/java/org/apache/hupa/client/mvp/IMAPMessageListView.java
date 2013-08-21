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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaMessages;
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.client.dnd.PagingScrollTableRowDragController;
import org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display;
import org.apache.hupa.client.widgets.CommandsBar;
import org.apache.hupa.client.widgets.ConfirmDialogBox;
import org.apache.hupa.client.widgets.DragRefetchPagingScrollTable;
import org.apache.hupa.client.widgets.EnableButton;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.DragRefetchPagingScrollTable.DragHandlerFactory;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.widgets.ui.HasEnable;
import org.apache.hupa.widgets.ui.Loading;
import org.apache.hupa.widgets.ui.PagingOptions;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.ButtonBar;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.CachedTableModel;
import com.google.gwt.gen2.table.client.CellRenderer;
import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ColumnResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollPolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView;
import com.google.gwt.gen2.table.event.client.HasPageChangeHandlers;
import com.google.gwt.gen2.table.event.client.HasPageLoadHandlers;
import com.google.gwt.gen2.table.event.client.HasRowSelectionHandlers;
import com.google.gwt.gen2.table.event.client.PageLoadEvent;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.gen2.table.event.client.RowCountChangeEvent;
import com.google.gwt.gen2.table.event.client.RowCountChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


@SuppressWarnings("deprecation")
public class IMAPMessageListView extends Composite implements Display{

    private static final int DEFAULT_MSG_PAGE_SIZE = 25;
    
    @SuppressWarnings("unused")
    private HupaMessages messages;
    private HupaImageBundle imageBundle;

    private PagingOptions pagingBar;
    private DragRefetchPagingScrollTable<Message> mailTable;
    private CachedTableModel<Message> cTableModel;

    private FixedWidthGrid dataTable = createDataTable();
    private EnableButton deleteMailButton;
    private Button newMailButton;
    private Button deleteAllMailButton;
    private ConfirmDialogBox confirmBox = new ConfirmDialogBox();
    private ConfirmDialogBox confirmDeleteAllBox = new ConfirmDialogBox();
    private EnableButton markSeenButton;
    private EnableButton markUnSeenButton;

    private ListBox pageBox = new ListBox();
    private Hyperlink allLink;    
    private Hyperlink noneLink;
    private Hyperlink refreshLink;
    private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(" ,@");
    private SuggestBox searchBox = new SuggestBox(oracle);
    private Button searchButton;
    private Loading loading;
    
    @Inject
    public IMAPMessageListView(final PagingScrollTableRowDragController controller, final MessageTableModel mTableModel, final HupaConstants constants, final HupaMessages messages, final HupaImageBundle imageBundle) {
        this.messages = messages;
        this.imageBundle = imageBundle;
        
        deleteMailButton = new EnableButton(constants.deleteMailButton());
        newMailButton = new Button(constants.newMailButton());
        deleteAllMailButton = new Button(constants.deleteAll());
        markSeenButton = new EnableButton(constants.markSeen());
        markUnSeenButton = new EnableButton(constants.markUnseen());
        allLink = new Hyperlink(constants.all(),"");    
        noneLink = new Hyperlink(constants.none(),"");
        refreshLink = new Hyperlink(constants.refresh(),"");
        searchButton = new Button(constants.searchButton());
        loading = new Loading(constants.loading());
        this.cTableModel = new CachedTableModel<Message>(mTableModel);
        cTableModel.setRowCount(MutableTableModel.UNKNOWN_ROW_COUNT);
        mTableModel.addRowCountChangeHandler(new RowCountChangeHandler() {
            
            public void onRowCountChange(RowCountChangeEvent event) {
                cTableModel.setRowCount(event.getNewRowCount());
            }
        });
        
        VerticalPanel msgListContainer = new VerticalPanel();
        msgListContainer.addStyleName(HupaCSS.C_msg_list_container);
        mailTable = new DragRefetchPagingScrollTable<Message>(
                cTableModel, dataTable, new FixedWidthFlexTable(),
                createTableDefinitation(),controller,1);
        mailTable.setPageSize(20);
        mailTable.setDragHandler(0,30, new DragHandlerFactory() {

            public Widget createHandler() {
                return new Image(imageBundle.readyToMoveMailIcon());
            }
            
        });
        
        HTML emptyTable = new HTML(constants.emptyMailTable());
        emptyTable.addStyleName(HupaCSS.C_msg_table_empty);
        mailTable.setEmptyTableWidget(emptyTable);
        FixedWidthGridBulkRenderer<Message> bulkRenderer = new FixedWidthGridBulkRenderer<Message>(mailTable.getDataTable(),mailTable);
        mailTable.setBulkRenderer(bulkRenderer);
        mailTable.addStyleName(HupaCSS.C_msg_table);
        mailTable.setCellPadding(0);
        mailTable.setResizePolicy(ResizePolicy.FILL_WIDTH);
        mailTable.setColumnResizePolicy(ColumnResizePolicy.MULTI_CELL);
        mailTable.setScrollPolicy(ScrollPolicy.DISABLED);
        mailTable.addPageLoadHandler(onMessagePageLoadHandler);
        mailTable.setPageSize(DEFAULT_MSG_PAGE_SIZE);
        mailTable.getDataTable().setCellSpacing(0);
        mailTable.setSortPolicy(SortPolicy.DISABLED);

        mailTable.fillWidth();
        
        pagingBar = new PagingOptions(mailTable, constants, loading);
        
        HorizontalPanel buttonBar = new HorizontalPanel();
        buttonBar.addStyleName(HupaCSS.C_buttons);
        
        ButtonBar navigatorBar = new ButtonBar();
        navigatorBar.add(newMailButton);
        deleteMailButton.setEnabled(false);
        navigatorBar.add(deleteMailButton);
        buttonBar.add(navigatorBar);
        buttonBar.add(deleteAllMailButton);
        
        ButtonBar markButtonBar = new ButtonBar();
        markButtonBar.add(markSeenButton);
        markButtonBar.add(markUnSeenButton);
        buttonBar.add(markButtonBar);
        buttonBar.add(refreshLink);
        pageBox.addItem("" + DEFAULT_MSG_PAGE_SIZE);
        pageBox.addItem("" + (DEFAULT_MSG_PAGE_SIZE * 2));
        pageBox.addItem("" + (DEFAULT_MSG_PAGE_SIZE * 4));
        pageBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                if (pageBox.getSelectedIndex() > 0)
                    mailTable.setPageSize(Integer.parseInt(pageBox.getItemText(pageBox.getSelectedIndex())));
            }
        });
      
        
        HorizontalPanel searchPanel = new HorizontalPanel();
        searchPanel.addStyleName(HupaCSS.C_buttons);

        searchBox.addStyleName(HupaCSS.C_msg_search);
        searchBox.setAnimationEnabled(true);
        searchBox.setAutoSelectEnabled(false);
        searchBox.setLimit(20);
        searchBox.addKeyUpHandler(new KeyUpHandler() {

            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    searchButton.click();
                }
            }

        });
        searchPanel.add(searchBox);
        searchPanel.add(searchButton);
        searchPanel.add(pageBox);

        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.addStyleName(HupaCSS.C_msg_top_bar);
        hPanel.add(buttonBar);
        hPanel.add(searchPanel);
        hPanel.setCellHorizontalAlignment(searchPanel, HorizontalPanel.ALIGN_RIGHT);        

        msgListContainer.add(hPanel);
        
        CommandsBar commandsBar = new CommandsBar();
        commandsBar.addLeft(new HTML(constants.select() +":"));
        commandsBar.addLeft(allLink);
        commandsBar.addLeft(noneLink);
        commandsBar.add(loading);
        commandsBar.addRight(pagingBar);
        
        msgListContainer.add(commandsBar);
        msgListContainer.add(mailTable);
        
        confirmBox.setText(messages.confirmDeleteMessages());
        confirmDeleteAllBox.setText(messages.confirmDeleteAllMessages());
        initWidget(msgListContainer);
    }
    
    
    PageLoadHandler onMessagePageLoadHandler = new PageLoadHandler() {

        public void onPageLoad(PageLoadEvent event) {

            for (int i = 0; i < mailTable.getDataTable().getRowCount(); i++) {
                mailTable.getDataTable().getRowFormatter().setStyleName(i, HupaCSS.C_msg_table_row);
                Message msg = mailTable.getRowValue(i);
                if (msg != null) {
                    if (msg.getFlags().contains(IMAPFlag.SEEN) == false) {
                        mailTable.getDataTable().getRowFormatter().addStyleName(i,HupaCSS.C_msg_table_unseen);
                    } else {
                        mailTable.getDataTable().getRowFormatter().removeStyleName(i, HupaCSS.C_msg_table_seen);
                    }
                }
            }
            
            String nrows = String.valueOf(mailTable.getPageSize());
            for (int i = 0; i<pageBox.getItemCount(); i++) {
                if (nrows.equals(pageBox.getItemText(i)))
                    pageBox.setSelectedIndex(i);
            }
        }
        
    };
    
    private DefaultTableDefinition<Message> createTableDefinitation() {
        DefaultTableDefinition<Message> def = new DefaultTableDefinition<Message>(createColumnDefinitionList());
        
        return def;
    }

    
    /**
       * @return the newly created data table.
       */
      private FixedWidthGrid createDataTable() {
        FixedWidthGrid dataTable = new FixedWidthGrid();
        dataTable.setSelectionPolicy(SelectionPolicy.CHECKBOX);
        return dataTable;
      }

    /**
     * Create a new List which holds all needed ColumnDefinitions 
     * 
     */
    private List<ColumnDefinition<Message, ?>> createColumnDefinitionList() {
        List<ColumnDefinition<Message, ?>> cList = new ArrayList<ColumnDefinition<Message, ?>>();

        FromColumnDefination from = new FromColumnDefination();
        from.setCellRenderer(new WhiteSpaceCellRenderer<Message>());
        from.setColumnTruncatable(true);
        from.setPreferredColumnWidth(250);
        from.setMinimumColumnWidth(150);
        from.setMaximumColumnWidth(300);
        cList.add(from);

        
        SubjectColumnDefination subject =new SubjectColumnDefination();
        subject.setCellRenderer(new WhiteSpaceCellRenderer<Message>());
        subject.setColumnTruncatable(true);
        subject.setPreferredColumnWidth(800);
        subject.setMinimumColumnWidth(200);
        cList.add(subject);
        
        AttachmentColumnDefination attachment = new AttachmentColumnDefination();
        attachment.setColumnTruncatable(false);
        // display an image if the message contains an attachment
        attachment.setCellRenderer(new CellRenderer<Message, Boolean>() {

            public void renderRowValue(Message rowValue,
                    ColumnDefinition<Message, Boolean> columnDef,
                    AbstractCellView<Message> view) {
                if (columnDef.getCellValue(rowValue)) {
                    view.setWidget(new Image(imageBundle.attachmentIcon()));
                } else {
                    view.setHTML("&nbsp");
                }
                
            }
            
        });
        
        attachment.setPreferredColumnWidth(20);
        attachment.setMinimumColumnWidth(15);
        attachment.setMaximumColumnWidth(25);
        cList.add(attachment);
        
        DateColumnDefination date = new DateColumnDefination();
        date.setColumnTruncatable(true);
        // set a special renderer for the date
        date.setCellRenderer(new CellRenderer<Message, Date>() {

            public void renderRowValue(Message rowValue,
                    ColumnDefinition<Message, Date> columnDef,
                    AbstractCellView<Message> view) {
                DateTimeFormat dtformat;
                Date rDate = rowValue.getReceivedDate();
                int rYear = rDate.getYear();
                int rMonth = rDate.getMonth();
                int rDay = rDate.getDate();
                
                Date now = new Date();
                int nowYear = now.getYear();
                int nowMonth = now.getMonth();
                int nowDay = now.getDate();
                
                if (rYear < nowYear) {
                    dtformat = DateTimeFormat.getFormat("dd.MMM.yyyy");
                } else if (rMonth < nowMonth || (rMonth == nowMonth && rDay < nowDay)) {
                    dtformat = DateTimeFormat.getFormat("dd.MMM.");
                } else if (rDay == nowDay){
                    dtformat = DateTimeFormat.getFormat("HH:mm");
                } else {

                    dtformat = DateTimeFormat.getFormat("dd.MMM.yyyy HH:mm");
                }
            
                view.setHTML(dtformat.format(rDate));
                view.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
            }
            
        });
        date.setPreferredColumnWidth(100);
        date.setMinimumColumnWidth(100);
        date.setMaximumColumnWidth(150);
        
        cList.add(date);
        
        return cList;
    }
    
   

    
    /**
     * ColumnDefination which display if the message contains an attachment
     * @author Norman
     *
     */
    private static final class AttachmentColumnDefination extends AbstractColumnDefinition<Message, Boolean> {

        @Override
        public Boolean getCellValue(Message rowValue) {
            return rowValue.hasAttachment();
        }

        @Override
        public void setCellValue(Message rowValue, Boolean cellValue) {
        }
        
    }

    /**
     * ColumnDefination which display the From 
     *
     */
    private static final class FromColumnDefination extends AbstractColumnDefinition<Message, String> {

        @Override
        public String getCellValue(Message rowValue) {
            return rowValue.getFrom();
        }

        @Override
        public void setCellValue(Message rowValue, String cellValue) {
            rowValue.setFrom(cellValue);
        }
        
    }
    
    /**
     * ColumnDefination which display the Subject
     *
     */
    private static final class SubjectColumnDefination extends AbstractColumnDefinition<Message, String> {

        @Override
        public String getCellValue(Message rowValue) {
            return rowValue.getSubject();
        }

        @Override
        public void setCellValue(Message rowValue, String cellValue) {
            rowValue.setSubject(cellValue);

        }
        
    }
    
    /**
     * ColumnDefination which display the Date
     * 
     */
    private static final class DateColumnDefination extends AbstractColumnDefinition<Message, Date> {

        @Override
        public Date getCellValue(Message rowValue) {
            return rowValue.getReceivedDate();
        }

        @Override
        public void setCellValue(Message rowValue, Date cellValue) {
            rowValue.setReceivedDate(cellValue);
        }
        
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDataTableSelection()
     */
    public HasRowSelectionHandlers getDataTableSelection() {
        return mailTable.getDataTable();
    }
    

    public void reloadData() {
        mailTable.reloadPage();
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#reset()
     */
    public void reset() {
        pageBox.setSelectedIndex(0);
        cTableModel.clearCache();
        cTableModel.setRowCount(CachedTableModel.UNKNOWN_ROW_COUNT);
        mailTable.gotoFirstPage();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDataTableLoad()
     */
    public HasPageLoadHandlers getDataTableLoad() {
        return mailTable;
    }


    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    public Widget asWidget() {
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDeleteClick()
     */
    public HasClickHandlers getDeleteClick() {
        return deleteMailButton;
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getNewClick()
     */
    public HasClickHandlers getNewClick() {
        return newMailButton;
    }
    
    /**
     * Renderer which fill empty rows with a whitespace 
     *
     * @param <E> RowType
     */
    private static final class WhiteSpaceCellRenderer<E> implements CellRenderer<E, String> {

        /*
         * (non-Javadoc)
         * @see com.google.gwt.gen2.table.client.CellRenderer#renderRowValue(java.lang.Object, com.google.gwt.gen2.table.client.ColumnDefinition, com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView)
         */
        public void renderRowValue(E rowValue,
                ColumnDefinition<E, String> columnDef, AbstractCellView<E> view) {
            String cellValue = columnDef.getCellValue(rowValue);
            if (cellValue == null || cellValue.length() < 1) {
                view.setHTML("&nbsp");
            } else {
                view.setHTML(cellValue);
            }
        }

        
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getData(int)
     */
    public Message getData(int rowIndex) {
        return mailTable.getRowValue(rowIndex);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getConfirmDialog()
     */
    public HasDialog getConfirmDeleteDialog() {
        return confirmBox;
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getConfirmDeleteDialogClick()
     */
    public HasClickHandlers getConfirmDeleteDialogClick() {
        return confirmBox;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getConfirmDeleteAllDialog()
     */
    public HasDialog getConfirmDeleteAllDialog() {
        return confirmDeleteAllBox;
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getConfirmDeleteAllDialogClick()
     */
    public HasClickHandlers getConfirmDeleteAllDialogClick() {
        return confirmDeleteAllBox;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#deselectAllMessages()
     */
    public void deselectAllMessages() {
        mailTable.getDataTable().deselectAllRows();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getSelectAllClick()
     */
    public HasClickHandlers getSelectAllClick() {
        return allLink;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getSelectNoneClick()
     */
    public HasClickHandlers getSelectNoneClick() {
        return noneLink;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#selectAllMessages()
     */
    public void selectAllMessages() {
        mailTable.getDataTable().selectAllRows();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getSelectedMessages()
     */
    public ArrayList<Message> getSelectedMessages() {
        return mailTable.getSelectedRows();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#removeMessages(java.util.ArrayList)
     */
    public void removeMessages(ArrayList<Message> messages) {
        mailTable.removeRows(messages);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#addTableListener(com.google.gwt.user.client.ui.TableListener)
     */
    public void addTableListener(TableListener listener) {
        dataTable.addTableListener(listener);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#removeTableListener(com.google.gwt.user.client.ui.TableListener)
     */
    public void removeTableListener(TableListener listener) {
        dataTable.removeTableListener(listener);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#setPostFetchMessageCount(int)
     */
    public void setPostFetchMessageCount(int count) {
        cTableModel.setPostCachedRowCount(count);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#redraw()
     */
    public void redraw() {
        mailTable.reloadPage();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDeleteAllClick()
     */
    public HasClickHandlers getDeleteAllClick() {
        return deleteAllMailButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getMarkSeenClick()
     */
    public HasClickHandlers getMarkSeenClick() {
        return markSeenButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getMarkUnseenClick()
     */
    public HasClickHandlers getMarkUnseenClick() {
        return markUnSeenButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDeleteEnable()
     */
    public HasEnable getDeleteEnable() {
        return deleteMailButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getMarkSeenEnable()
     */
    public HasEnable getMarkSeenEnable() {
        return markSeenButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getMarkUnseenEnable()
     */
    public HasEnable getMarkUnseenEnable() {
        return markUnSeenButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getRefreshClick()
     */
    public HasClickHandlers getRefreshClick() {
        return refreshLink;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#goToPage(int)
     */
    public void goToPage(int page) {
        if (page != mailTable.getCurrentPage()) {
            mailTable.gotoPage(page, false);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getCurrentPage()
     */
    public int getCurrentPage() {
        return mailTable.getCurrentPage();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDataTablePageChange()
     */
    public HasPageChangeHandlers getDataTablePageChange() {
        return mailTable;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getRowsPerPageIndex()
     */
    public int getRowsPerPageIndex() {
        return pageBox.getSelectedIndex();
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getRowsPerPageChange()
     */
    public HasChangeHandlers getRowsPerPageChange() {
        return pageBox;
    }
    

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MainPresenter.Display#getSearchClick()
     */
    public HasClickHandlers getSearchClick() {
        return searchButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MainPresenter.Display#getSearchValue()
     */
    public HasValue<String> getSearchValue() {
        return searchBox;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MainPresenter.Display#fillSearchOracle(java.util.ArrayList)
     */
    public void fillSearchOracle(ArrayList<Message> messages) {
        for (Message m : messages) {
            String subject = m.getSubject();
            String from = m.getFrom();
            if (subject != null && subject.trim().length() > 0) {
                oracle.add(subject.trim());
            }
            if (from != null && from.trim().length() > 0) {
                oracle.add(from.trim());
            }
        }
        //searchBox.setText("");
    }

    public void setExpandLoading(boolean expanding) {
        if (expanding) {
            loading.show();
        } else {
            loading.hide();
        }
    }
}
