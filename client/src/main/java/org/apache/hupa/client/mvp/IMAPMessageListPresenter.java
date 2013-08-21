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
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.HandlerRegistrationAdapter;
import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.MoveMessageEvent;
import org.apache.hupa.shared.events.MoveMessageEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.rpc.DeleteAllMessages;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.DeleteMessageResult;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.MoveMessage;
import org.apache.hupa.shared.rpc.MoveMessageResult;
import org.apache.hupa.shared.rpc.SetFlag;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.gen2.table.event.client.HasPageChangeHandlers;
import com.google.gwt.gen2.table.event.client.HasPageLoadHandlers;
import com.google.gwt.gen2.table.event.client.HasRowSelectionHandlers;
import com.google.gwt.gen2.table.event.client.PageChangeEvent;
import com.google.gwt.gen2.table.event.client.PageChangeHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.inject.Inject;

@SuppressWarnings("deprecation")
public class IMAPMessageListPresenter extends WidgetPresenter<IMAPMessageListPresenter.Display>{
    
    public interface Display extends WidgetDisplay {
        public HasRowSelectionHandlers getDataTableSelection();
        public HasPageLoadHandlers getDataTableLoad();
        public void addTableListener(TableListener listener) ;
        public void removeTableListener(TableListener listener) ;
        public void setPostFetchMessageCount(int count);
        public HasClickHandlers getNewClick();
        public Message getData(int rowIndex);
        public HasClickHandlers getDeleteClick();
        public HasClickHandlers getDeleteAllClick();
        public HasEnable getDeleteEnable();
        public void reloadData();
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
        public HasEnable getMarkSeenEnable();
        public HasEnable getMarkUnseenEnable();
        public HasClickHandlers getRefreshClick();
        public void redraw();
        public HasPageChangeHandlers getDataTablePageChange();
        public void goToPage(int page);
        public int getCurrentPage();
        public int getRowsPerPageIndex();
        public HasChangeHandlers getRowsPerPageChange();     
        public HasClickHandlers getSearchClick();
        public HasValue<String> getSearchValue();
        public void fillSearchOracle(ArrayList<Message> messages);
        public void setExpandLoading(boolean expanding);

    }

    private String searchValue;
    private User user;
    private IMAPFolder folder;
    private DispatchAsync dispatcher;
    private ShowMessageTableListener tableListener = new ShowMessageTableListener();
    
    @Inject
    public IMAPMessageListPresenter(IMAPMessageListPresenter.Display display,EventBus bus,DispatchAsync dispatcher) {
        super(display,bus);
        this.dispatcher = dispatcher;
        
        // add this event on constructor because we don't want to remove it on unbind
        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

            public void onLogout(LogoutEvent logoutEvent) {
                getDisplay().reset();
                getDisplay().getSearchValue().setValue("");
                
                // we need to fire a event to notify the history about the reset
                firePresenterChangedEvent();
            }
            
        });
    }

    @Override
    protected void onBind() {
        registerHandler(eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {

            public void onMessagesReceived(MessagesReceivedEvent event) {

                // fill the oracle
                display.fillSearchOracle(event.getMessages());
            }

        }));
        registerHandler(display.getSearchClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                String searchValue = null;
                if (display.getSearchValue().getValue().trim().length() > 0) {
                    searchValue = display.getSearchValue().getValue().trim();
                }
                eventBus.fireEvent(new LoadMessagesEvent(user, folder, searchValue));
            }

        }));
        registerHandler(eventBus.addHandler(MoveMessageEvent.TYPE, new MoveMessageEventHandler() {

            public void onMoveMessageHandler(MoveMessageEvent event) {
                final Message message = event.getMessage();
                dispatcher.execute(new MoveMessage(event.getOldFolder(), event.getNewFolder(), message.getUid()), new HupaCallback<MoveMessageResult>(dispatcher, eventBus) {
                    public void callback(MoveMessageResult result) {
                        ArrayList<Message> messageArray = new ArrayList<Message>();
                        messageArray.add(message);
                        display.removeMessages(messageArray);
                    }
                }); 
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
                dispatcher.execute(new DeleteAllMessages(folder), new HupaCallback<DeleteMessageResult>(dispatcher, eventBus) {
                    public void callback(DeleteMessageResult result) {
                        display.reset();
                        display.reloadData();
//                        eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,result.getCount()));
                    }
                });
            }
            
        }));
        registerHandler(display.getMarkSeenClick().addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
                ArrayList<Long> uids = new ArrayList<Long>();
                for (Message m : selectedMessages) {
                    if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
                        uids.add(m.getUid());
                    } else {
                        selectedMessages.remove(m);
                    }
                }
                dispatcher.execute(new SetFlag(folder, IMAPFlag.SEEN, true, uids), new HupaCallback<GenericResult>(dispatcher, eventBus) {
                    public void callback(GenericResult result) {
                        for (Message m : selectedMessages) {
                            if (m.getFlags().contains(IMAPFlag.SEEN) == false) {
                                m.getFlags().add(IMAPFlag.SEEN);
                            }
                        }
                        display.redraw();
                        eventBus.fireEvent(new DecreaseUnseenEvent(user, folder,selectedMessages.size()));
                    }
                });
            }

        }));
        
        registerHandler(display.getMarkUnseenClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
                ArrayList<Long> uids = new ArrayList<Long>();
                for (Message m : selectedMessages) {
                    if (m.getFlags().contains(IMAPFlag.SEEN)) {
                        uids.add(m.getUid());
                    } else {
                        selectedMessages.remove(m);
                    }
                }
                
                dispatcher.execute(new SetFlag(folder, IMAPFlag.SEEN, false, uids), new HupaCallback<GenericResult>(dispatcher, eventBus) {
                    public void callback(GenericResult result) {
                        for (Message m : selectedMessages) {
                            if (m.getFlags().contains(IMAPFlag.SEEN)) {
                                m.getFlags().remove(IMAPFlag.SEEN);
                            }
                        }
                        display.redraw();
                        eventBus.fireEvent(new IncreaseUnseenEvent(user, folder,selectedMessages.size()));
                    }
                });
            }
            
            
        }));
        registerHandler(eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {

            public void onFolderSelectionEvent(FolderSelectionEvent event) {
                folder = event.getFolder();
                user = event.getUser();
                firePresenterChangedEvent();
            }
            
        }));
        registerHandler(new HandlerRegistrationAdapter(display.getDataTableSelection().addRowSelectionHandler(new RowSelectionHandler() {
                public void onRowSelection(RowSelectionEvent event) {
                    if (event.getSelectedRows().size() == 0) {
                        display.getDeleteEnable().setEnabled(false);
                        display.getMarkSeenEnable().setEnabled(false);
                        display.getMarkUnseenEnable().setEnabled(false);
                    } else {
                        display.getDeleteEnable().setEnabled(true);
                        display.getMarkSeenEnable().setEnabled(true);
                        display.getMarkUnseenEnable().setEnabled(true);
                    }
                }
                
            
            
        })));
        
        registerHandler(display.getRefreshClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.reset();
                display.reloadData();
            }
            
        }));
        registerHandler(new HandlerRegistrationAdapter(display.getDataTablePageChange().addPageChangeHandler(new PageChangeHandler() {

            public void onPageChange(PageChangeEvent event) {
                //firePresenterRevealedEvent(true);
                firePresenterChangedEvent();
            }
            
        })));
        registerHandler(display.getRowsPerPageChange().addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                //firePresenterRevealedEvent(true);
                firePresenterChangedEvent();
            }
            
        }));
        display.addTableListener(tableListener);
    }

    
    private void deleteMessages() {
        final ArrayList<Message> selectedMessages = new ArrayList<Message>(display.getSelectedMessages());
        ArrayList<Long> uids = new ArrayList<Long>();
        for (Message m : selectedMessages) {
            uids.add(m.getUid());
        }
        // maybe its better to just remove the messages from the table and expect the removal will work
        display.removeMessages(selectedMessages);

        dispatcher.execute(new DeleteMessageByUid(folder,uids), new HupaCallback<DeleteMessageResult>(dispatcher, eventBus) {
            public void callback(DeleteMessageResult result) {
                eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,result.getCount()));
            }
        }); 
    }
 

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    protected void onUnbind() {
        display.removeTableListener(tableListener);
    }

    
    private final class ShowMessageTableListener implements TableListener {

        public void onCellClicked(SourcesTableEvents sender, int row,
                int cell) {
            
            display.setExpandLoading(true);
            Message message = display.getData(row);
            
            // mark the message as seen and redraw the table to reflect this
            if (message.getFlags().contains(Message.IMAPFlag.SEEN) == false) {
                // add flag, fire event and redraw
                message.getFlags().add(Message.IMAPFlag.SEEN);
                eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,1));
                
                display.redraw();

            }
            
            eventBus.fireEvent(new ExpandMessageEvent(user,folder,message));
        }

    }

    @Override
    protected void onRevealDisplay() {
        display.reloadData();  
    }
    
    public void revealDisplay(User user, IMAPFolder folder, String searchValue) {
        this.user = user;
       
        if (this.folder == null || this.folder.getFullName().equals(folder.getFullName()) == false 
                || (searchValue == null && this.searchValue != null) || (searchValue != null && searchValue.equals(this.searchValue) == false)) {
            display.reset();
            display.deselectAllMessages();
        }
        display.setExpandLoading(false);
        this.searchValue = searchValue;
        this.folder = folder;
        revealDisplay();
    }

}
