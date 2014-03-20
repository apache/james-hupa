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

package org.apache.hupa.client.activity;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.hupa.client.place.AbstractPlace;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.rf.MoveMessageRequest;
import org.apache.hupa.client.storage.HupaStorage;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.MessagesCellTable.MessageListDataProvider;
import org.apache.hupa.client.ui.ToolBarView;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MoveMessageAction;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.DeleteClickEvent;
import org.apache.hupa.shared.events.DeleteClickEventHandler;
import org.apache.hupa.shared.events.MessageListRangeChangedEvent;
import org.apache.hupa.shared.events.MessageListRangeChangedEventHandler;
import org.apache.hupa.shared.events.MessageViewEvent;
import org.apache.hupa.shared.events.MessageViewEventHandler;
import org.apache.hupa.shared.events.MoveMessageEvent;
import org.apache.hupa.shared.events.MoveMessageEventHandler;
import org.apache.hupa.shared.events.RefreshFoldersEvent;
import org.apache.hupa.shared.events.RefreshMessagesEvent;
import org.apache.hupa.shared.events.RefreshMessagesEventHandler;
import org.apache.hupa.widgets.dialog.Dialog;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Promise;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessageListActivity extends AppBaseActivity {

    @Inject protected Displayable display;
    @Inject protected ToolBarActivity.Displayable toolBar;
    @Inject protected HupaStorage hupaStorage;

    protected String folderName;
    protected User user;
    FetchMessagesResult currentFechResult;
    Promise gettingMessages;

    boolean first = true;

    @Inject
    public MessageListActivity(EventBus eventBus) {
        bindTo(eventBus);
    }

    private Timer refreshMessagesTimer = new Timer() {
        public void run() {
            display.refresh();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        refreshMessagesTimer.cancel();
    }

    @Override
    public void start(AcceptsOneWidget container, final EventBus eventBus) {
        refreshMessagesTimer.scheduleRepeating(5*60*1000);

        container.setWidget(display.asWidget());
        if (!first) {
            display.refresh();
            first = false;
        }
        rf.sessionRequest().getUser().fire(new Receiver<User>() {
            @Override
            public void onSuccess(User u) {
                if (u == null) {
                    onFailure(null);
                } else {
                    user = u;
                }
            }
            @Override
            public void onFailure(ServerFailure error) {
            }
        });
        this.registerHandler(display.getGrid().addCellPreviewHandler(new Handler<Message>() {
            @Override
            public void onCellPreview(final CellPreviewEvent<Message> event) {
                if (hasClickedButFirstCol(event)) {
                    onMessageSelected(event.getValue());
                }
            }
        }));
    }

    protected void onMessageSelected(Message message) {
        antiSelectMessages(display.getGrid().getVisibleItems());
        GetMessageDetailsRequest req = rf.messageDetailsRequest();
        GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
        final ImapFolder f = req.create(ImapFolder.class);
        f.setFullName(folderName);
        action.setFolder(f);
        action.setUid(message.getUid());
        String token = getToken(message);
        MessagePlace place = new MessagePlace(token);
        pc.goTo(place);
        display.getGrid().getSelectionModel().setSelected(message, true);
        toolBar.enableSendingTools(true);
        toolBar.enableDealingTools(true);
        ToolBarView.Parameters p = new ToolBarView.Parameters(user, folderName, message, null);
        toolBar.setParameters(p);

        // display.refresh();
//        eventBus.fireEvent(new RefreshFoldersEvent(event.getValue()));

    }

    private String getToken(Message message) {
        String token = folderName + AbstractPlace.SPLITTER + message.getUid();
        return token;
    }

    private boolean hasClickedButFirstCol(CellPreviewEvent<Message> event) {
        return "click".equals(event.getNativeEvent().getType()) && 0 != event.getColumn();
    }

    private void bindTo(final EventBus eventBus) {

        eventBus.addHandler(DeleteClickEvent.TYPE, new DeleteClickEventHandler() {
            @Override
            public void onDeleteClickEvent(DeleteClickEvent event) {
                deleteSelectedMessages();
            }
        });

        eventBus.addHandler(RefreshMessagesEvent.TYPE, new RefreshMessagesEventHandler() {
            @Override
            public void onRefresh(RefreshMessagesEvent event) {
                display.setSearchValue(event.getSearchValue());
                display.refresh();
                System.out.println("REFRESH DISPLAY");
            }
        });

        eventBus.addHandler(MoveMessageEvent.TYPE, new MoveMessageEventHandler() {

            @Override
            public void onMoveMessageHandler(final MoveMessageEvent event) {
                hc.showTopLoading("Moving...");
                MoveMessageRequest req = rf.moveMessageRequest();
                ImapFolder f = req.create(ImapFolder.class);
                ImapFolder newF = req.create(ImapFolder.class);

                String fullName = null;
                if (pc.getWhere() instanceof FolderPlace) {
                    fullName = ((FolderPlace) pc.getWhere()).getToken();
                } else {
                    fullName = ((MessagePlace) pc.getWhere()).getTokenWrapper().getFolder();
                }
                f.setFullName(fullName);
                newF.setFullName(event.getNewFolder().getFullName());
                MoveMessageAction action = req.create(MoveMessageAction.class);

                final List<Long> uids = display.getSelectedMessagesIds();
                // TODO can move more than one message once.
                if (uids.isEmpty() || uids.size() > 1) {
                    hc.hideTopLoading();
                    hc.showNotice("Please select one and only one message", 10000);
                    return;
                }
                action.setMessageUid(uids.get(0));
                action.setNewFolder(newF);
                action.setOldFolder(f);
                req.move(action).fire(new Receiver<GenericResult>() {

                    @Override
                    public void onSuccess(GenericResult response) {
                        display.refresh();
                        eventBus.fireEvent(new RefreshFoldersEvent());
                        hc.hideTopLoading();
                        hc.showNotice("The conversation has been moved to \"" + event.getNewFolder() + "\"", 10000);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        super.onFailure(error);
                        hc.hideTopLoading();
                        hc.showNotice(error.getMessage(), 10000);
                    }

                });
            }

        });

        eventBus.addHandler(MessageViewEvent.TYPE, new MessageViewEventHandler() {
            public void onMessageViewEvent(final MessageViewEvent event) {
                if (event.messageDetails != null && gettingMessages != null) {
                    gettingMessages.done(new Function() {
                        public void f() {
                            List<Message> messages = currentFechResult.getMessages();
                            int l = messages.size();
                            for (int i = 0; i < l; i++){
                                Message m = messages.get(i);
                                if (m.getUid() == event.messageDetails.getUid()) {
                                    List<IMAPFlag> flags = m.getFlags();
                                    if (!flags.contains(IMAPFlag.SEEN)) {
                                        flags.add(IMAPFlag.SEEN);
                                        m = rf.deleteMessageByUidRequest().edit(m);
                                        m.setFlags(flags);
                                        messages.set(i, m);
                                        display.getDataProvider().setFechMessagesResult(currentFechResult);
                                    }
                                    display.getGrid().getSelectionModel().setSelected(m, true);
                                    display.getGrid().getRowElement(i).scrollIntoView();
                                    ToolBarView.Parameters p = new ToolBarView.Parameters(user, folderName, m, event.messageDetails);
                                    toolBar.setParameters(p);
                                    break;
                                }
                            }
                        }
                    });
                }
            }
        });

        eventBus.addHandler(MessageListRangeChangedEvent.TYPE, new MessageListRangeChangedEventHandler() {
            public void onRangeChangedEvent(MessageListRangeChangedEvent event) {
                gettingMessages = hupaStorage
                .gettingMessages(true, folderName, event.start, event.size, event.search)
                .done(new Function() {
                    public void f() {
                        FetchMessagesResult response = arguments(0);
                        display.getDataProvider().setFechMessagesResult(response);
                        currentFechResult = response;
                        cacheContacts();
                    }
                })
                .fail(new Function() {
                    public void f() {
                        hc.showNotice("" + arguments(0), 3000);
                    }
                })
                .always(new Function() {
                    public void f() {
                        hc.hideTopLoading();
                    }
                });

            }
        });

    }

    public MessageListActivity with(String folderName) {
        this.folderName = folderName;
        return this;
    }

    public interface Displayable extends IsWidget {
        MessagesCellTable getGrid();

        void refresh();

        List<Long> getSelectedMessagesIds();

        Set<Message> getSelectedMessages();

        void setSearchValue(String searchValue);

        MessageListDataProvider getDataProvider();
    }

    private void antiSelectMessages(Collection<Message> c) {
        toolBar.enableAllTools(false);
        for (Message msg : c) {
            if (!display.getGrid().getSelectionModel().isSelected(msg))
                continue;
            display.getGrid().getSelectionModel().setSelected(msg, false);
        }
    }
    private void deleteSelectedMessages() {
        final List<Long> uids = display.getSelectedMessagesIds();

        if (uids.size() > 1) {
            Dialog.confirm("Do you want to delete selected messages?", new Command() {
                public void execute() {
                    doDelete(uids);
                }
            });
        }

    }

    protected void doDelete(final List<Long> uids) {
        hc.showTopLoading("Deleting...");

        String fullName = null;
        if (pc.getWhere() instanceof FolderPlace) {
            fullName = ((FolderPlace) pc.getWhere()).getToken();
        } else {
            fullName = ((MessagePlace) pc.getWhere()).getTokenWrapper().getFolder();
        }

        DeleteMessageByUidRequest req = rf.deleteMessageByUidRequest();
        DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
        ImapFolder f = req.create(ImapFolder.class);
        f.setFullName(fullName);
        action.setMessageUids(uids);
        action.setFolder(f);
        req.delete(action).fire(new Receiver<DeleteMessageResult>() {
            @Override
            public void onSuccess(DeleteMessageResult response) {
                antiSelectMessages(display.getSelectedMessages());
                display.refresh();
                hc.hideTopLoading();
                pc.goTo(new FolderPlace(folderName));
                eventBus.fireEvent(new RefreshFoldersEvent());
                for (Long uid : uids) {
                    removeMessage(uid);
                }
                display.getDataProvider().setFechMessagesResult(currentFechResult);
//                display.getGrid().getRowElement(0).scrollIntoView();
            }

            @Override
            public void onFailure(ServerFailure error) {
                hc.hideTopLoading();
                hc.showNotice("Error removing messages", 5000);
                super.onFailure(error);
            }

            private void removeMessage(Long uid) {
                List<Message> messages = currentFechResult.getMessages();
                int l = messages.size();
                for (int i = 0; i < l; i++){
                    Message m = messages.get(i);
                    if (m.getUid() == uid) {
                        messages.remove(i);
                        return;
                    }
                }
            }
        });
    }


    // TODO move this stuff to hupaStorage
    private void cacheContacts() {
        for (Message message : currentFechResult.getMessages()) {
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

    public static final String CONTACTS_STORE = "hupa-contacts";
    Set<String> contacts = new LinkedHashSet<String>();
    private Storage contactsStore = null;
}
