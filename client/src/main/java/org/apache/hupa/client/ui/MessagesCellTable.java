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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.events.MessagesReceivedEvent;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.storage.client.Storage;
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
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessagesCellTable extends DataGrid<Message> {

	@Inject private ToolBarActivity.Displayable toolBar;
	@Inject protected HupaController hc;
	@Inject EventBus eventBus;
	private String folderName;
	private String searchValue;

	public static final int PAGE_SIZE = 100;

	private HupaImageBundle imageBundle;
	CheckboxColumn checkboxCol = new CheckboxColumn();
	Column<Message, ?> fromCol = new FromColumn();
	Column<Message, ?> subjectCol = new SubjectColumn();
	Column<Message, ?> attachedCol = new AttachmentColumn();
	Column<Message, ?> dateCol = new DateColumn();

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

	private MessageListDataProvider dataProvider;
	public static final String CONTACTS_STORE = "hupa-contacts";

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

		Set<String> contacts = new LinkedHashSet<String>();
		private Storage contactsStore = null;

		private void cacheContacts(List<Message> messages) {
			for (Message message : messages) {
				message.getFrom();
				message.getTo();
				message.getCc();
				message.getReplyto();

				contacts.add(message.getFrom());
				contacts.add(message.getReplyto());

				for (String to : message.getTo()) {
					contacts.add(to);
				}
				for (String cc : message.getCc()) {
					contacts.add(cc);
				}
			}
			saveToLocalStorage(contacts);
		}
		private void saveToLocalStorage(Set<String> contacts) {
			contactsStore = Storage.getLocalStorageIfSupported();
			if (contactsStore != null) {
				String contactsString = contactsStore.getItem(CONTACTS_STORE);
				if (null != contactsString) {
					for (String contact : contactsString.split(",")) {
						contacts.add(contact.replace("[", "").replace("]", "").trim());
					}
				}
				contactsStore.setItem(CONTACTS_STORE, contacts.toString());
			}
		}

		@Override
		protected void onRangeChanged(HasData<Message> display) {
			FetchMessagesRequest req = rf.messagesRequest();
			FetchMessagesAction action = req.create(FetchMessagesAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			final int start = display.getVisibleRange().getStart();
			f.setFullName(parseFolderName(pc));
			action.setFolder(f);
			action.setOffset(display.getVisibleRange().getLength());
			action.setSearchString(searchValue);
			action.setStart(start);
			req.fetch(action).fire(new Receiver<FetchMessagesResult>() {
				@Override
				public void onSuccess(final FetchMessagesResult response) {
					if (response == null || response.getRealCount() == 0) {
						updateRowCount(-1, true);
					} else {
						updateRowCount(response.getRealCount(), true);
						updateRowData(start, response.getMessages());
					    getColumnSortList().push(dateCol);
					}
					hc.hideTopLoading();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							cacheContacts(response.getMessages());
							eventBus.fireEvent(new MessagesReceivedEvent(f, response.getMessages()));
						}
					});
				}

				@Override
				public void onFailure(ServerFailure error) {
					if (error.isFatal()) {
						throw new RuntimeException(error.getMessage());
					}
					hc.hideTopLoading();
				}
			});

		}

	}
	
	public void setSearchValue(String searchValue){
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

		@Override
		public void onBrowserEvent(Context context, Element elem, NativeEvent event) {
			InputElement input = elem.getFirstChild().cast();
			Boolean isChecked = input.isChecked();
			List<Message> displayedItems = MessagesCellTable.this.getVisibleItems();
			for (Message element : displayedItems) {
				selectionModel.setSelected(element, isChecked);
				checkboxCol.getFieldUpdater().update(0, element, isChecked);
			}
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

		if (dataProvider == null) {
			dataProvider = new MessageListDataProvider();
			dataProvider.addDataDisplay(this);
		}
		
		// make table sortable
	    AsyncHandler columnSortHandler = new AsyncHandler(this);
	    addColumnSortHandler(columnSortHandler);
        fromCol.setSortable(true);
        subjectCol.setSortable(true);
        attachedCol.setSortable(true);
        dateCol.setSortable(true);
        
		refresh();
	}

	// TODO: this should be perform in the server side, but in the meanwhile it is useful
	// some kind of sorting in client side.
	@Override
	public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
	    final ColumnSortInfo sortInfo = getColumnSortList().get(0);

	    List<Message> sortedList = new ArrayList<Message>(getVisibleItems());
        Collections.sort(sortedList, new Comparator<Message>() {
            public int compare(Message o1, Message o2) {
                Column<?,?> column = sortInfo.getColumn();
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
                    // Remove Re & Fwd, using ugly regex since replaceAll is not case-insensitive in client side.
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

	private class FromColumn extends Column<Message, String> {
		public FromColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Message object) {
			return object.getFrom();
		}
	}

	private class SubjectColumn extends Column<Message, String> {
		public SubjectColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Message object) {
			return object.getSubject();
		}
	}

	private class AttachmentColumn extends Column<Message, ImageResource> {
		public AttachmentColumn() {
			super(new ImageResourceCell());
		}

		@Override
		public ImageResource getValue(Message object) {
			return object.hasAttachment() ? imageBundle.attachmentIcon() : null;
		}
	}

	private class DateColumn extends Column<Message, Date> {
		private static final String DATE_FORMAT = "dd.MMM.yyyy";

		public DateColumn() {
			super(new DateCell(DateTimeFormat.getFormat(DATE_FORMAT)));
		}

		@Override
		public Date getValue(Message object) {
			return object.getReceivedDate();
		}
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
