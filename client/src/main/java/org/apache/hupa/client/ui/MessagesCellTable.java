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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.events.MessageListRangeChangedEvent;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

public class MessagesCellTable extends DataGrid<Message> {

    @Inject private ToolBarActivity.Displayable toolBar;
    @Inject protected HupaController hc;
    @Inject EventBus eventBus;
    private String folderName;
    private String searchValue;

    public static final int PAGE_SIZE = 100;

    private HupaImageBundle imageBundle;
    CheckboxColumn checkboxCol = new CheckboxColumn();
    Column<Message, ?> fromCol;
    Column<Message, ?> subjectCol;
    Column<Message, ?> attachedCol;
    Column<Message, ?> dateCol;

    public interface Resources extends DataGrid.Resources {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("res/CssMessagesCellTable.css")
        CustomStyle dataGridStyle();
    }

    public interface CustomStyle extends Style {
        String fontBold();
        String fontNormal();
    }

    public CheckboxColumn getCheckboxCol() {
        return checkboxCol;
    }

    public final ProvidesKey<Message> KEY_PROVIDER = new ProvidesKey<Message>() {
        @Override
        public Object getKey(Message item) {
            return item == null ? null : item.getUid();
        }
    };
    private final MultiSelectionModel<? super Message> selectionModel = new MultiSelectionModel<Message>(KEY_PROVIDER);

    PlaceController pc;
    HupaRequestFactory rf;

    protected MessageListDataProvider dataProvider;

    public class MessageListDataProvider extends AsyncDataProvider<Message> implements HasRefresh {

        HasData<Message> display;

        @Override
        public void addDataDisplay(HasData<Message> display) {
            super.addDataDisplay(display);
            this.display = display;
        }

        @Override
        public void refresh() {
            this.onRangeChanged(display);
        }

        public void setFechMessagesResult(FetchMessagesResult response) {
            if (response == null || response.getRealCount() == 0) {
                updateRowCount(-1, true);
            } else {
                final List<Message> messages = response.getMessages();
                updateRowCount(response.getRealCount(), true);
                updateRowData(display.getVisibleRange().getStart(), messages);
                getColumnSortList().push(dateCol);
            }
        }

        @Override
        protected void onRangeChanged(final HasData<Message> display) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                public void execute() {
                    eventBus.fireEvent(new MessageListRangeChangedEvent(
                            display.getVisibleRange().getStart(),
                            display.getVisibleRange().getLength(),
                            searchValue));
                }
            });
        }

    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public final class CheckboxHeader extends Header<Boolean> {

        private final MultiSelectionModel<? super Message> selectionModel;
        private final AsyncDataProvider<Message> provider;

        public CheckboxHeader(MultiSelectionModel<? super Message> selectionModel, AsyncDataProvider<Message> provider) {
            super(new CheckboxCell());
            this.selectionModel = selectionModel;
            this.provider = provider;
        }

        @Override
        public Boolean getValue() {
            if (selectionModel == null || provider == null) {
                return false;
            }
            if (selectionModel.getSelectedSet().size() == 0 || provider.getDataDisplays().size() == 0) {
                return false;
            }
            boolean allItemsSelected = selectionModel.getSelectedSet().size() == provider.getDataDisplays().size();
            return allItemsSelected;
        }

//        @Override
//        public void onBrowserEvent(Context context, Element elem, NativeEvent event) {
//            InputElement input = elem.getFirstChild().cast();
//            Boolean isChecked = input.isChecked();
//            List<Message> displayedItems = MessagesCellTable.this.getVisibleItems();
//            for (Message element : displayedItems) {
//                selectionModel.setSelected(element, isChecked);
//                checkboxCol.getFieldUpdater().update(0, element, isChecked);
//            }
//        }

    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if (dataProvider == null) {
            dataProvider = new MessageListDataProvider();
            dataProvider.addDataDisplay(this);
        }
    }

    @Inject
    public MessagesCellTable(final HupaImageBundle imageBundle, final HupaConstants constants,
            final PlaceController pc, final HupaRequestFactory rf) {
        super(PAGE_SIZE, Resources.INSTANCE);
        this.pc = pc;
        this.rf = rf;
        this.imageBundle = imageBundle;

        CheckboxCell headerCheckbox = new CheckboxCell();
        ImageResourceCell headerAttached = new ImageResourceCell();
        Header<Boolean> header = new Header<Boolean>(headerCheckbox) {
            @Override
            public Boolean getValue() {
                return false;
            }
        };
        Header<ImageResource> attachedPin = new Header<ImageResource>(headerAttached) {
            @Override
            public ImageResource getValue() {
                return imageBundle.attachmentIcon();
            }
        };
        header.setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                List<Message> displayedItems = MessagesCellTable.this.getVisibleItems();
                for (Message msg : displayedItems) {
                    checkboxCol.getFieldUpdater().update(0, msg, value);
                }
            }
        });

        fromCol = getFromColumn();
        subjectCol = getSubjectColumn();
        attachedCol = getAttachmentColumn();
        dateCol = getDateColumn();

        addColumn(checkboxCol, new CheckboxHeader(selectionModel, dataProvider));
        setColumnWidth(checkboxCol, 3, Unit.EM);
        addColumn(fromCol, constants.mailTableFrom());
        setColumnWidth(fromCol, 40, Unit.PCT);
        addColumn(subjectCol, constants.mailTableSubject());
        setColumnWidth(subjectCol, 60, Unit.PCT);
        addColumn(attachedCol, attachedPin);
        setColumnWidth(attachedCol, 33, Unit.PX);
        addColumn(dateCol, constants.mailTableDate());
        setColumnWidth(dateCol, 10, Unit.EM);
        setRowCount(PAGE_SIZE, false);
        this.setStyleBaseOnTag();
        // redraw();
        setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
        setAutoHeaderRefreshDisabled(true);

        setSelectionModel(selectionModel, DefaultSelectionEventManager.<Message> createBlacklistManager(0));

        // make table sortable
        AsyncHandler columnSortHandler = new AsyncHandler(this);
        addColumnSortHandler(columnSortHandler);
        fromCol.setSortable(true);
        subjectCol.setSortable(true);
        attachedCol.setSortable(true);
        dateCol.setSortable(true);
    }

    // TODO: this should be perform in the server side, but in the meanwhile it
    // is useful
    // some kind of sorting in client side.
    @Override
    public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
        final ColumnSortInfo sortInfo = getColumnSortList().get(0);

        List<Message> sortedList = new ArrayList<Message>(getVisibleItems());
        Collections.sort(sortedList, new Comparator<Message>() {
            public int compare(Message o1, Message o2) {
                Column<?, ?> column = sortInfo.getColumn();
                Message a = sortInfo.isAscending() ? o1 : o2;
                Message b = sortInfo.isAscending() ? o2 : o1;
                if (fromCol.equals(column)) {
                    return a.getFrom().compareToIgnoreCase(b.getFrom());
                }
                if (attachedCol.equals(column)) {
                    return Boolean.valueOf(a.hasAttachment()).compareTo(Boolean.valueOf(b.hasAttachment()));
                }
                if (dateCol.equals(column)) {
                    return a.getReceivedDate().compareTo(b.getReceivedDate());
                }
                if (subjectCol.equals(column)) {
                    // Remove Re & Fwd, using ugly regex since replaceAll is not
                    // case-insensitive in client side.
                    String s1 = a.getSubject().replaceAll("^([Rr][Ee]|[Ff][Ww][Dd]): (.+)$", "$2 ");
                    String s2 = b.getSubject().replaceAll("^([Rr][Ee]|[Ff][Ww][Dd]): (.+)$", "$2 ");
                    return s1.compareTo(s2);
                }
                return 0;
            }
        });
        dataProvider.updateRowData(range.getStart(), sortedList);
    }

    public String parseFolderName(final PlaceController pc) {
        Place place = pc.getWhere();
        if (place instanceof FolderPlace) {
            folderName = ((FolderPlace) place).getToken();
        } else if (place instanceof MessagePlace) {
            folderName = ((MessagePlace) place).getTokenWrapper().getFolder();
        }
        return folderName;
    }

    Message message; // the object selected by selectionModel

    public String getMessageStyle(Message row) {
        return haveRead(row) ? getReadStyle() : getUnreadStyle();
    }
    private String getUnreadStyle() {
        return Resources.INSTANCE.dataGridStyle().fontBold();
    }

    private String getReadStyle() {
        return Resources.INSTANCE.dataGridStyle().fontNormal();
    }

    private boolean haveRead(Message row) {
        return row.getFlags().contains(IMAPFlag.SEEN);
    }
    public void markRead(final Message message, final boolean read) {
        flush();
    }

    public class CheckboxColumn extends Column<Message, Boolean> {

        public CheckboxColumn() {
            super(new CheckboxCell(false, false));
            setFieldUpdater(new FieldUpdater<Message, Boolean>() {
                @Override
                public void update(int index, Message object, Boolean value) {
                    selectionModel.setSelected(object, value);
                    int size = selectionModel.getSelectedSet().size();
                    if (size >= 1) {
                        toolBar.enableDealingTools(true);
                        toolBar.enableSendingTools(false);
                        toolBar.enableUpdatingTools(false);
                    } else {
                        toolBar.enableAllTools(false);
                    }
                }
            });
        }

        @Override
        public Boolean getValue(Message object) {
            return selectionModel.isSelected(object);
        }
    }

    protected Column<Message, SafeHtml> getFromColumn () {
        return new Column<Message, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(Message object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.append(SafeHtmlUtils.fromString(object.getFrom() == null ? "" : object.getFrom()));
                return sb.toSafeHtml();
            }
        };
    }

    protected Column<Message, SafeHtml> getSubjectColumn () {
        return new Column<Message, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(Message object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.append(SafeHtmlUtils.fromString(object.getSubject() == null ? "" : object.getSubject()));
                return sb.toSafeHtml();
            }
        };
    }

    protected Column<Message, ImageResource> getAttachmentColumn () {
        return new Column<Message, ImageResource>(new ImageResourceCell()) {
            public ImageResource getValue(Message object) {
                return object.hasAttachment() ? imageBundle.attachmentIcon() : null;
            }
        };
    }

    protected Column<Message, Date> getDateColumn() {
        return new Column<Message, Date>(new DateCell(DateTimeFormat.getFormat("dd.MMM.yyyy"))) {
            public Date getValue(Message object) {
                return object.getReceivedDate();
            }
        };
    }

    public void setStyleBaseOnTag() {
        setRowStyles(new RowStyles<Message>() {
            @Override
            public String getStyleNames(Message row, int rowIndex) {
                return getMessageStyle(row);
            }
        });
    }
    public void refresh() {
        dataProvider.refresh();
        redrawHeaders();
    }

}
