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

import java.util.List;
import java.util.Set;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaMessages;
import org.apache.hupa.client.activity.IMAPMessageListActivity;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.widgets.ConfirmDialogBox;
import org.apache.hupa.client.widgets.EnableButton;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.widgets.ui.HasEnable;
import org.apache.hupa.widgets.ui.Loading;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.ButtonBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@SuppressWarnings("deprecation")
public class IMAPMessageListView extends Composite implements IMAPMessageListActivity.Displayable {

	@SuppressWarnings("unused") private HupaMessages messages;

	private EnableButton deleteMailButton;
	private Button newMailButton;
	private Button deleteAllMailButton;
	private ConfirmDialogBox confirmBox = new ConfirmDialogBox();
	private ConfirmDialogBox confirmDeleteAllBox = new ConfirmDialogBox();
	private EnableButton markSeenButton;
	private EnableButton markUnSeenButton;

	private ListBox pageBox = new ListBox();
	private Anchor allLink;
	private Anchor noneLink;
	private Anchor refreshLink;
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(" ,@");
	private SuggestBox searchBox = new SuggestBox(oracle);
	private Button searchButton;
	private Loading loading;

	private SimplePager pager;
	private EventBus eventBus;

	private User user;
	private ImapFolder folder;
	private String searchValue;
	private HupaRequestFactory requestFactory;
	public final ProvidesKey<Message> KEY_PROVIDER = new ProvidesKey<Message>() {
		@Override
		public Object getKey(Message item) {
			return item == null ? null : item.getUid();
		}
	};
	private SelectionModel<? super Message> selectionModel;

	public void fetch(final int start) {
		FetchMessagesRequest messagesRequest = requestFactory.messagesRequest();
		FetchMessagesAction action = messagesRequest.create(FetchMessagesAction.class);
		final ImapFolder folder1 = messagesRequest.create(ImapFolder.class);
		folder1.setChildren(folder.getChildren());
		folder1.setDelimiter(folder.getDelimiter());
		folder1.setFullName(folder.getFullName());
		folder1.setMessageCount(folder.getMessageCount());
		folder1.setName(folder.getName());
		folder1.setSubscribed(folder.getSubscribed());
		folder1.setUnseenMessageCount(folder.getUnseenMessageCount());
		action.setFolder(folder1);
		action.setOffset(table.getPageSize());
		action.setSearchString(searchValue);
		action.setStart(start);
		messagesRequest.fetch(action).fire(new Receiver<FetchMessagesResult>() {

			@Override
			public void onFailure(ServerFailure error) {
				if (error.isFatal()) {
					throw new RuntimeException(error.getMessage());
				}
			}

			@Override
			public void onSuccess(final FetchMessagesResult result) {
				assert result != null;
				// folder.setMessageCount(result.getRealCount());// TODO if do
				// this, there will be auto bean has been frozen.
				// folder.setUnseenMessageCount(result.getRealUnreadCount());
				table.setRowCount(result.getRealCount());
				table.setRowData(start, result.getMessages());

				pager.setPageStart(start);
				eventBus.fireEvent(new MessagesReceivedEvent(folder1, result.getMessages()));
			}
		});
	}

	private MessagesCellTable table;
	private boolean pending;

	@Inject
	public IMAPMessageListView(final HupaConstants constants, final HupaMessages messages, final EventBus eventBus,
	        final HupaRequestFactory requestFactory, final MessagesCellTable table) {
		this.table = table;
		this.eventBus = eventBus;
		this.requestFactory = requestFactory;
		selectionModel = table.getSelectionModel();
		table.addCellPreviewHandler(new Handler<Message>() {
			@Override
			public void onCellPreview(CellPreviewEvent<Message> event) {
				if (hasClickedButFirstCol(event)) {
					setExpandLoading(true);
					eventBus.fireEvent(new ExpandMessageEvent(user, folder, event.getValue()));
				}
			}
			private boolean hasClickedButFirstCol(CellPreviewEvent<Message> event) {
				return "click".equals(event.getNativeEvent().getType()) && 0 != event.getColumn();
			}

		});
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (selectedCount() == 0) {
					toggleButtons(false);
				} else {
					toggleButtons(true);
				}
			}
		});
//		table.getCheckboxCol().setFieldUpdater(new FieldUpdater<Message, Boolean>() {
//			@Override
//			public void update(int index, Message object, Boolean value) {
//				selectionModel.setSelected(object, value);
//				if (selectedCount() == 0) {
//					toggleButtons(false);
//				} else {
//					toggleButtons(true);
//				}
//			}
//
//			private int selectedCount() {
//				return getSelectedMessages().size();
//			}
//
//			private void toggleButtons(boolean b) {
//				getDeleteEnable().setEnabled(b);
//				getMarkSeenEnable().setEnabled(b);
//				getMarkUnseenEnable().setEnabled(b);
//			}
//		});

		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				fetch(event.getNewRange().getStart());
			}
		});
		// bind some Events
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {
			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				user = loadMessagesEvent.getUser();
				folder = loadMessagesEvent.getFolder();
				searchValue = loadMessagesEvent.getSearchValue();
				fetch(0);

			}
		});
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {
			public void onFolderSelectionEvent(FolderSelectionEvent event) {
				user = event.getUser();
				folder = event.getFolder();
				searchValue = null;
			}
		});
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			public void onLogin(LoginEvent event) {
				user = event.getUser();
				folder = new ImapFolderImpl(user.getSettings().getInboxFolderName());
				searchValue = null;
				if (!pending) {
					pending = true;
					Scheduler.get().scheduleFinally(new ScheduledCommand() {
						@Override
						public void execute() {
							pending = false;
							fetch(0);
						}
					});
				}
			}
		});
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
			public void onLogout(LogoutEvent logoutEvent) {
				user = null;
				folder = null;
				searchValue = null;
			}
		});

		this.messages = messages;
		this.eventBus = eventBus;

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table);

		deleteMailButton = new EnableButton(constants.deleteMailButton());
		newMailButton = new Button(constants.newMailButton());
		deleteAllMailButton = new Button(constants.deleteAll());
		markSeenButton = new EnableButton(constants.markSeen());
		markUnSeenButton = new EnableButton(constants.markUnseen());
		allLink = new Anchor(constants.all());
		noneLink = new Anchor(constants.none());
		refreshLink = new Anchor(constants.refresh());
		searchButton = new Button(constants.searchButton());
		loading = new Loading(constants.loading());

		DockLayoutPanel solidCenterPanel = new DockLayoutPanel(Unit.EM);
		// solidCenterPanel.addStyleName(HupaCSS.C_msg_list_container);

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
		// buttonBar.add(refreshLink); TODO

		pageBox.addItem("" + MessagesCellTable.PAGE_SIZE);
		pageBox.addItem("" + (MessagesCellTable.PAGE_SIZE * 2));
		pageBox.addItem("" + (MessagesCellTable.PAGE_SIZE * 4));
		pageBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				// if (pageBox.getSelectedIndex() > 0)
				table.setVisibleRange(0, Integer.parseInt(pageBox.getItemText(pageBox.getSelectedIndex())));
				// mailTable.setPageSize(Integer.parseInt(pageBox.getItemText(pageBox.getSelectedIndex())));
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

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.addStyleName(HupaCSS.C_msg_top_bar);
		hPanel.add(buttonBar);
		hPanel.add(searchPanel);
		hPanel.setCellHorizontalAlignment(searchPanel, HorizontalPanel.ALIGN_RIGHT);

		HorizontalPanel pagerBar = new HorizontalPanel();

		pagerBar.add(pager);
		pagerBar.add(pageBox);

		solidCenterPanel.addNorth(hPanel, 3);
		solidCenterPanel.addSouth(pagerBar, 2);
		solidCenterPanel.add(table);

		// msgListContainer.add(mailTable);

		confirmBox.setText(messages.confirmDeleteMessages());
		confirmDeleteAllBox.setText(messages.confirmDeleteAllMessages());
		initWidget(solidCenterPanel);
	}

	private int selectedCount() {
		return getSelectedMessages().size();
	}

	private void toggleButtons(boolean b) {
		getDeleteEnable().setEnabled(b);
		getMarkSeenEnable().setEnabled(b);
		getMarkUnseenEnable().setEnabled(b);
	}
	public void reloadData() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#reset()
	 */
	public void reset() {
		pageBox.setSelectedIndex(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
	 */
	public Widget asWidget() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDeleteClick
	 * ()
	 */
	public HasClickHandlers getDeleteClick() {
		return deleteMailButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getNewClick()
	 */
	public HasClickHandlers getNewClick() {
		return newMailButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getData(int)
	 */
	public Message getData(int rowIndex) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getConfirmDialog
	 * ()
	 */
	public HasDialog getConfirmDeleteDialog() {
		return confirmBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getConfirmDeleteDialogClick()
	 */
	public HasClickHandlers getConfirmDeleteDialogClick() {
		return confirmBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getConfirmDeleteAllDialog()
	 */
	public HasDialog getConfirmDeleteAllDialog() {
		return confirmDeleteAllBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getConfirmDeleteAllDialogClick()
	 */
	public HasClickHandlers getConfirmDeleteAllDialogClick() {
		return confirmDeleteAllBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * deselectAllMessages()
	 */
	public void deselectAllMessages() {
		// mailTable.getDataTable().deselectAllRows();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getSelectAllClick
	 * ()
	 */
	public HasClickHandlers getSelectAllClick() {
		return allLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getSelectNoneClick()
	 */
	public HasClickHandlers getSelectNoneClick() {
		return noneLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#selectAllMessages
	 * ()
	 */
	public void selectAllMessages() {
		// mailTable.getDataTable().selectAllRows();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getSelectedMessages()
	 */
	@SuppressWarnings("unchecked")
	public Set<Message> getSelectedMessages() {
		return ((MultiSelectionModel<Message>) (table.getSelectionModel())).getSelectedSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#removeMessages
	 * (java.util.ArrayList)
	 */
	public void removeMessages(List<Message> messages) {
		// mailTable.removeRows(messages);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * setPostFetchMessageCount(int)
	 */
	public void setPostFetchMessageCount(int count) {
		// cTableModel.setPostCachedRowCount(count);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#redraw()
	 */
	public void redraw() {
		// mailTable.reloadPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDeleteAllClick
	 * ()
	 */
	public HasClickHandlers getDeleteAllClick() {
		return deleteAllMailButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getMarkSeenClick
	 * ()
	 */
	public HasClickHandlers getMarkSeenClick() {
		return markSeenButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getMarkUnseenClick()
	 */
	public HasClickHandlers getMarkUnseenClick() {
		return markUnSeenButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getDeleteEnable
	 * ()
	 */
	public HasEnable getDeleteEnable() {
		return deleteMailButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getMarkSeenEnable
	 * ()
	 */
	public HasEnable getMarkSeenEnable() {
		return markSeenButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getMarkUnseenEnable()
	 */
	public HasEnable getMarkUnseenEnable() {
		return markUnSeenButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getRefreshClick
	 * ()
	 */
	public HasClickHandlers getRefreshClick() {
		return refreshLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#goToPage(int)
	 */
	public void goToPage(int page) {
		// if (page != mailTable.getCurrentPage()) {
		// mailTable.gotoPage(page, false);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#getCurrentPage
	 * ()
	 */
	public int getCurrentPage() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getRowsPerPageIndex()
	 */
	public int getRowsPerPageIndex() {
		return pageBox.getSelectedIndex();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.IMAPMessageListPresenter.Display#
	 * getRowsPerPageChange()
	 */
	public HasChangeHandlers getRowsPerPageChange() {
		return pageBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.MainPresenter.Display#getSearchClick()
	 */
	public HasClickHandlers getSearchClick() {
		return searchButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.MainPresenter.Display#getSearchValue()
	 */
	public HasValue<String> getSearchValue() {
		return searchBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.MainPresenter.Display#fillSearchOracle(java
	 * .util.ArrayList)
	 */
	public void fillSearchOracle(List<Message> messages) {
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
		// searchBox.setText("");
	}

	public void setExpandLoading(boolean expanding) {
		if (expanding) {
			loading.show();
		} else {
			loading.hide();
		}
	}

	@Override
	public MessagesCellTable getTable() {
		return table;
	}
}
