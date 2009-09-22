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

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.SessionAsyncCallback;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.MoveMessageEvent;
import org.apache.hupa.shared.events.MoveMessageEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.rpc.DeleteAllMessages;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.DeleteMessageResult;
import org.apache.hupa.shared.rpc.EmptyResult;
import org.apache.hupa.shared.rpc.MoveMessage;
import org.apache.hupa.shared.rpc.MoveMessageResult;
import org.apache.hupa.shared.rpc.SetFlag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.gen2.table.event.client.HasPageLoadHandlers;
import com.google.gwt.gen2.table.event.client.HasRowSelectionHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.inject.Inject;

@SuppressWarnings("deprecation")
public class IMAPMessageListPresenter extends WidgetPresenter<IMAPMessageListPresenter.Display>{
	
	public interface Display extends WidgetDisplay{
		public HasRowSelectionHandlers getDataTableSelection();
		public HasPageLoadHandlers getDataTableLoad();
		public void addTableListener(TableListener listener) ;
		public void removeTableListener(TableListener listener) ;
		public void setPostFetchMessageCount(int count);
		public HasClickHandlers getNewClick();
		public Message getData(int rowIndex);
		public HasClickHandlers getDeleteClick();
		public HasClickHandlers getDeleteAllClick();
		public void reloadData(User user, IMAPFolder folder,String searchValue);
		public void removeMessages(ArrayList<Message> messages);
		public ArrayList<Message> getSelectedMessages();
		public void reset();
		public HasDialog getConfirmDeleteDialog();
		public HasDialog getConfirmDeleteAllDialog();

		public HasClickHandlers getConfirmDeleteDialogClick();
		public HasClickHandlers getConfirmDeleteAllDialogClick();

		public void selectAllMessages();
		public void deselectAllMessages();
		public HasClickHandlers getSelectAllClick();
		public HasClickHandlers getSelectNoneClick();
		public HasClickHandlers getMarkSeenClick();
		public HasClickHandlers getMarkUnseenClick();
		public void redraw();
	}

	private User user;
	private IMAPFolder folder;
	private String searchValue;
	private DispatchAsync dispatcher;
	private ShowMessageTableListener tableListener = new ShowMessageTableListener();
	private boolean isBound = false;
	public final static Place PLACE = new Place("IMAPMessageList");
	
	
	@Inject
	public IMAPMessageListPresenter(IMAPMessageListPresenter.Display display,EventBus bus,DispatchAsync dispatcher) {
		super(display,bus);
		this.dispatcher = dispatcher;
	}
	
	@Override
	public Place getPlace() {
		return PLACE;
	}

	@Override
	protected void onBind() {
		
		registerHandler(eventBus.addHandler(MoveMessageEvent.TYPE, new MoveMessageEventHandler() {

			public void onMoveMessageHandler(MoveMessageEvent event) {
				final Message message = event.getMessage();
				dispatcher.execute(new MoveMessage(event.getUser().getSessionId(),event.getOldFolder(),event.getNewFolder(),message.getUid()), new SessionAsyncCallback<MoveMessageResult>(new AsyncCallback<MoveMessageResult>() {

					public void onFailure(Throwable caught) {
						GWT.log("ERROR while moving",caught);
					}

					public void onSuccess(MoveMessageResult result) {
						ArrayList<Message> messageArray = new ArrayList<Message>();
						messageArray.add(message);
						display.removeMessages(messageArray);
					}
					
				},eventBus,user));
			}
			
		}));
		registerHandler(display.getSelectAllClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.deselectAllMessages();
				display.selectAllMessages();
			}
			
		}));
		
		registerHandler(display.getSelectNoneClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.deselectAllMessages();
			}
			
		}));


		registerHandler(display.getDeleteClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (folder.getFullName().equals(user.getSettings().getTrashFolderName())) {
					display.getConfirmDeleteDialog().show();
				} else {
					deleteMessages();
				}
				
			}
			
		}));
		registerHandler(display.getConfirmDeleteDialogClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				deleteMessages();
			}
			
		}));
		
		registerHandler(display.getNewClick().addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				eventBus.fireEvent(new NewMessageEvent());
			}
			
		}));
		
		registerHandler(display.getDeleteAllClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.getConfirmDeleteAllDialog().center();
			}
			
		}));
		
		registerHandler(display.getConfirmDeleteAllDialogClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				dispatcher.execute(new DeleteAllMessages(user.getSessionId(),folder), new SessionAsyncCallback<DeleteMessageResult>(new AsyncCallback<DeleteMessageResult>() {

					public void onFailure(Throwable caught) {
						GWT.log("E=", caught);
					}

					public void onSuccess(DeleteMessageResult result) {
						display.reset();
						eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,result.getCount()));
					}
				}, eventBus,user));
			}
			
		}));
		registerHandler(display.getMarkSeenClick().addClickHandler( new ClickHandler() {
			public void onClick(ClickEvent event) {
				final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
				ArrayList<Long> uids = new ArrayList<Long>();
				for (int i = 0; i < selectedMessages.size(); i++) {
					Message m = selectedMessages.get(i);
					if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
						uids.add(m.getUid());
					} else {
						selectedMessages.remove(m);
					}
				}
				dispatcher.execute(new SetFlag(user.getSessionId(), folder, IMAPFlag.SEEN, true, uids), new SessionAsyncCallback<EmptyResult>(new AsyncCallback<EmptyResult>() {
					public void onFailure(Throwable caught) {
						GWT.log("ERR=", caught);
					}

					public void onSuccess(EmptyResult result) {
						GWT.log("S="+selectedMessages.size(), null);
						for (int i = 0; i < selectedMessages.size(); i++) {
							Message m = selectedMessages.get(i);
							if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
								m.getFlags().add(IMAPFlag.SEEN);
							}
						}
						display.redraw();
						eventBus.fireEvent(new DecreaseUnseenEvent(user, folder,selectedMessages.size()));
												
					}

				}, eventBus, user));
			}

		}));
		
		registerHandler(display.getMarkUnseenClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
				ArrayList<Long> uids = new ArrayList<Long>();
				for (int i = 0; i < selectedMessages.size(); i++) {
					Message m = selectedMessages.get(i);
					if (m.getFlags().contains(IMAPFlag.SEEN)) {
						uids.add(m.getUid());
					} else {
						selectedMessages.remove(m);
					}
				}
				
				dispatcher.execute(new SetFlag(user.getSessionId(), folder, IMAPFlag.SEEN, false, uids), new SessionAsyncCallback<EmptyResult>(new AsyncCallback<EmptyResult>() {
					public void onFailure(Throwable caught) {
						GWT.log("ERR=", caught);
					}

					public void onSuccess(EmptyResult result) {
						for (int i = 0; i < selectedMessages.size(); i++) {
							Message m = selectedMessages.get(i);
							if (m.getFlags().contains(IMAPFlag.SEEN)) {
								m.getFlags().remove(IMAPFlag.SEEN);
							}
						}
						display.redraw();
						eventBus.fireEvent(new IncreaseUnseenEvent(user, folder,selectedMessages.size()));
												
					}

				}, eventBus, user));
			}
			
			
		}));
		
		display.addTableListener(tableListener);
		isBound = true;
	}

	private void deleteMessages() {
		final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
		ArrayList<Long> uids = new ArrayList<Long>();
		for (int i = 0; i < selectedMessages.size(); i++) {
			uids.add(selectedMessages.get(i).getUid());
		}
		dispatcher.execute(new DeleteMessageByUid(user.getSessionId(),folder,uids), new SessionAsyncCallback<DeleteMessageResult>(new AsyncCallback<DeleteMessageResult>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(DeleteMessageResult result) {
				display.removeMessages(selectedMessages);
				eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,result.getCount()));
			}
		}, eventBus,user));
	}
	
	public void bind(User user, IMAPFolder folder, String searchValue) {
		this.user = user;
		this.folder = folder;
		this.searchValue  = searchValue;
		display.setPostFetchMessageCount(user.getSettings().getPostFetchMessageCount());
		// workaround
		if (isBound == false) {
			bind();
		}
		
		//refreshDisplay();
	}

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.presenter.client.BasicPresenter#onPlaceRequest(net.customware.gwt.presenter.client.place.PlaceRequest)
	 */
	protected void onPlaceRequest(PlaceRequest request) {
		// TODO Auto-generated method stub
		
	}


	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
	 */
	protected void onUnbind() {
		display.removeTableListener(tableListener);
		isBound = false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.presenter.client.Presenter#refreshDisplay()
	 */
	public void refreshDisplay() {
		display.reset();
		display.deselectAllMessages();

		display.reloadData(user, folder, searchValue);
	}

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.presenter.client.Presenter#revealDisplay()
	 */
	public void revealDisplay() {
		display.redraw();
	}
	
	private final class ShowMessageTableListener implements TableListener {

		public void onCellClicked(SourcesTableEvents sender, int row,
				int cell) {
			
			Message message = display.getData(row);
			
			// mark the message as seen and redraw the table to reflect this
			message.getFlags().add(Message.IMAPFlag.SEEN);
			display.redraw();
			
			eventBus.fireEvent(new ExpandMessageEvent(user,folder,message));
		}

	};

}
