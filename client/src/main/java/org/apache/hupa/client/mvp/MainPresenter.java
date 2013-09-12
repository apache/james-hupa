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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

<<<<<<< HEAD
<<<<<<< HEAD
import net.customware.gwt.dispatch.client.DispatchAsync;
=======
>>>>>>> first commit
=======
import net.customware.gwt.dispatch.client.DispatchAsync;
>>>>>>> constantly changed by manolo
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetContainerDisplay;
import net.customware.gwt.presenter.client.widget.WidgetContainerPresenter;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.apache.hupa.client.CachingDispatchAsync;
>>>>>>> first commit
=======
>>>>>>> constantly changed by manolo
import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.client.mvp.MessageSendPresenter.Type;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
=======
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.data.Message.IMAPFlag;
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
>>>>>>> constantly changed by manolo
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.BackEventHandler;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.DecreaseUnseenEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.ForwardMessageEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.IncreaseUnseenEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.events.NewMessageEventHandler;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.events.ReplyMessageEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> first commit
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.DeleteFolder;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.GetMessageDetails;
import org.apache.hupa.shared.rpc.GetMessageDetailsResult;
import org.apache.hupa.shared.rpc.RenameFolder;
import org.apache.hupa.widgets.event.EditEvent;
import org.apache.hupa.widgets.event.EditHandler;
import org.apache.hupa.widgets.ui.HasEditable;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;

/**
 *
 */
public class MainPresenter extends WidgetContainerPresenter<MainPresenter.Display> {

    public interface Display extends NameAwareWidgetDisplay, WidgetContainerDisplay {
      
        public HasSelectionHandlers<TreeItem> getTree();

        public void bindTreeItems(List<IMAPTreeItem> treeList);

        public HasClickHandlers getRenameClick();

        public HasClickHandlers getDeleteClick();

        public HasClickHandlers getNewClick();

        public HasDialog getDeleteConfirmDialog();

        public HasClickHandlers getDeleteConfirmClick();

        public HasEnable getRenameEnable();

        public HasEnable getDeleteEnable();

        public HasEnable getNewEnable();

<<<<<<< HEAD
<<<<<<< HEAD
        public void updateTreeItem(IMAPFolderProxy folder);
=======
        public void updateTreeItem(IMAPFolder folder);
>>>>>>> first commit
=======
        public void updateTreeItem(IMAPFolderProxy folder);
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.

        public void deleteSelectedFolder();

        public HasEditable createFolder(EditHandler handler);

<<<<<<< HEAD
<<<<<<< HEAD
        public void increaseUnseenMessageCount(IMAPFolderProxy folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolderProxy folder, int amount);
=======
        public void increaseUnseenMessageCount(IMAPFolder folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolder folder, int amount);
>>>>>>> first commit
=======
        public void increaseUnseenMessageCount(IMAPFolderProxy folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolderProxy folder, int amount);
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
        
        public void setLoadingFolders(boolean loading);
        public void setLoadingMessage(boolean loading);

    }

<<<<<<< HEAD
<<<<<<< HEAD
    private DispatchAsync dispatcher;
    private User user;
    private IMAPFolderProxy folder;
=======
    private CachingDispatchAsync cachingDispatcher;
=======
    private DispatchAsync dispatcher;
>>>>>>> constantly changed by manolo
    private User user;
<<<<<<< HEAD
    private IMAPFolder folder;
>>>>>>> first commit
=======
    private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
    private String searchValue;
    private IMAPMessageListPresenter messageListPresenter;
    private IMAPMessagePresenter messagePresenter;
    private MessageSendPresenter sendPresenter;
    private IMAPTreeItem tItem;
    private HasEditable editableTreeItem;
    
    @Inject
<<<<<<< HEAD
<<<<<<< HEAD
    public MainPresenter(MainPresenter.Display display, EventBus bus, DispatchAsync cachingDispatcher, IMAPMessageListPresenter messageListPresenter, IMAPMessagePresenter messagePresenter,
            MessageSendPresenter sendPresenter) {
        super(display, bus, messageListPresenter, messagePresenter, sendPresenter);
        this.dispatcher = cachingDispatcher;
=======
    public MainPresenter(MainPresenter.Display display, EventBus bus, CachingDispatchAsync cachingDispatcher, IMAPMessageListPresenter messageListPresenter, IMAPMessagePresenter messagePresenter,
            MessageSendPresenter sendPresenter) {
        super(display, bus, messageListPresenter, messagePresenter, sendPresenter);
        this.cachingDispatcher = cachingDispatcher;
>>>>>>> first commit
=======
    public MainPresenter(MainPresenter.Display display, EventBus bus, DispatchAsync cachingDispatcher, IMAPMessageListPresenter messageListPresenter, IMAPMessagePresenter messagePresenter,
            MessageSendPresenter sendPresenter) {
        super(display, bus, messageListPresenter, messagePresenter, sendPresenter);
        this.dispatcher = cachingDispatcher;
>>>>>>> constantly changed by manolo
        this.messageListPresenter = messageListPresenter;
        this.messagePresenter = messagePresenter;
        this.sendPresenter = sendPresenter;
        
    }

    protected void loadTreeItems() {
        display.setLoadingFolders(true);
<<<<<<< HEAD
<<<<<<< HEAD
        dispatcher.execute(new FetchFolders(), new HupaCallback<FetchFoldersResult>(dispatcher, eventBus, display) {
=======
        cachingDispatcher.execute(new FetchFolders(), new HupaCallback<FetchFoldersResult>(cachingDispatcher, eventBus, display) {
>>>>>>> first commit
=======
        dispatcher.execute(new FetchFolders(), new HupaCallback<FetchFoldersResult>(dispatcher, eventBus, display) {
>>>>>>> constantly changed by manolo
            public void callback(FetchFoldersResult result) {
                display.bindTreeItems(createTreeNodes(result.getFolders()));
                // disable
                display.getDeleteEnable().setEnabled(false);
                display.getRenameEnable().setEnabled(false);
                display.setLoadingFolders(false);

            }
        });
    }

    /**
     * Create recursive the TreeNodes with all childs
     * 
     * @param list
     * @return
     */
<<<<<<< HEAD
<<<<<<< HEAD
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolderProxy> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolderProxy iFolder : list) {
=======
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolder> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolder iFolder : list) {
>>>>>>> first commit
=======
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolderProxy> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolderProxy iFolder : list) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.

            final IMAPTreeItem record = new IMAPTreeItem(iFolder);
            record.addEditHandler(new EditHandler() {

                public void onEditEvent(EditEvent event) {
                    if (event.getEventType().equals(EditEvent.EventType.Stop)) {
                        IMAPFolder iFolder = new IMAPFolder((String) event.getOldValue());
                        final String newName = (String) event.getNewValue();
                        if (iFolder.getFullName().equalsIgnoreCase(newName) == false) {
<<<<<<< HEAD
<<<<<<< HEAD
                            dispatcher.execute(new RenameFolder(iFolder, newName), new HupaCallback<GenericResult>(dispatcher, eventBus) {
=======
                            cachingDispatcher.execute(new RenameFolder(iFolder, newName), new HupaCallback<GenericResult>(cachingDispatcher, eventBus) {
>>>>>>> first commit
=======
                            dispatcher.execute(new RenameFolder(iFolder, newName), new HupaCallback<GenericResult>(dispatcher, eventBus) {
>>>>>>> constantly changed by manolo
                                public void callback(GenericResult result) {
                                    folder.setFullName(newName);
                                }
                                public void callbackError(Throwable caught) {
                                    record.cancelEdit();
                                }
                            }); 
                        }
                    }
                }

            });
            record.setUserObject(iFolder);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            List<IMAPFolderProxy> childFolders = iFolder.getChildIMAPFolders();
=======
            List<IMAPFolder> childFolders = iFolder.getChildIMAPFolders();
>>>>>>> first commit
=======
            List<IMAPFolderProxy> childFolders = iFolder.getChildIMAPFolders();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
            List<IMAPFolderProxy> childFolders = iFolder.getChildren();
>>>>>>> 
            List<IMAPTreeItem> items = createTreeNodes(childFolders);
            for (IMAPTreeItem item : items) {
                record.addItem(item);
            }

            // Store the INBOX as starting point after first loading
            if (iFolder.getFullName().equals(user.getSettings().getInboxFolderName())) {
                folder = iFolder;
                tItem = record;
            }

            tList.add(record);
        }

        // Sort tree
        Collections.sort(tList, new Comparator<TreeItem>() {

            public int compare(TreeItem o1, TreeItem o2) {
                return o1.getText().compareTo(o2.getText());
            }

        });
        return tList;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    private void showMessageTable(User user, IMAPFolderProxy folder, String searchValue) {
=======
    private void showMessageTable(User user, IMAPFolder folder, String searchValue) {
>>>>>>> first commit
=======
    private void showMessageTable(User user, IMAPFolderProxy folder, String searchValue) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
        this.user = user;
        this.folder = folder;
        this.searchValue = searchValue;
        firePresenterChangedEvent();

        messageListPresenter.revealDisplay(user, folder, searchValue);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    private void showMessage(User user, IMAPFolderProxy folder, Message message, MessageDetails details) {
=======
    private void showMessage(User user, IMAPFolder folder, Message message, MessageDetails details) {
>>>>>>> first commit
=======
    private void showMessage(User user, IMAPFolderProxy folder, Message message, MessageDetails details) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
        messagePresenter.revealDisplay(user, folder, message, details);
    }

    private void showNewMessage() {
        sendPresenter.revealDisplay(user);
    }

    private void showForwardMessage(ForwardMessageEvent event) {
        sendPresenter.revealDisplay(event.getUser(), event.getFolder(), event.getMessage(), event.getMessageDetails(), Type.FORWARD);
    }

    private void showReplyMessage(ReplyMessageEvent event) {
        if (event.getReplyAll()) {
            sendPresenter.revealDisplay(event.getUser(), event.getFolder(), event.getMessage(), event.getMessageDetails(), Type.REPLY_ALL);
        } else {
            sendPresenter.revealDisplay(event.getUser(), event.getFolder(), event.getMessage(), event.getMessageDetails(), Type.REPLY);

        }
        sendPresenter.revealDisplay();
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
    /**
     * Reset the presenter and display
     */
    private void reset() {
        // clear the cache
        cachingDispatcher.clear();
    }

>>>>>>> first commit
=======
>>>>>>> constantly changed by manolo

    @Override
    protected void onBind() {
        super.onBind();
        registerHandler(eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {

            public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
                showMessageTable(loadMessagesEvent.getUser(), loadMessagesEvent.getFolder(), loadMessagesEvent.getSearchValue());
            }

        }));


        registerHandler(eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

            public void onExpandMessage(ExpandMessageEvent event) {
                final boolean decreaseUnseen;
                final Message message = event.getMessage();
                // check if the message was already seen in the past
                if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
                    decreaseUnseen = true;
                } else {
                    decreaseUnseen = false;
                }

                display.setLoadingMessage(true);
<<<<<<< HEAD
<<<<<<< HEAD
                dispatcher.execute(new GetMessageDetails(event.getFolder(), message.getUid()), new HupaCallback<GetMessageDetailsResult>(dispatcher, eventBus, display) {
=======
                cachingDispatcher.executeWithCache(new GetMessageDetails(event.getFolder(), message.getUid()), new HupaCallback<GetMessageDetailsResult>(cachingDispatcher, eventBus, display) {
>>>>>>> first commit
=======
                dispatcher.execute(new GetMessageDetails(event.getFolder(), message.getUid()), new HupaCallback<GetMessageDetailsResult>(dispatcher, eventBus, display) {
>>>>>>> constantly changed by manolo
                    public void callback(GetMessageDetailsResult result) {
                        if (decreaseUnseen) {
                            eventBus.fireEvent(new DecreaseUnseenEvent(user, folder));
                        }
                        display.setLoadingMessage(false);
                        showMessage(user, folder, message, result.getMessageDetails());
                    }
                });
            }

        }));
        registerHandler(eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {

            public void onNewMessageEvent(NewMessageEvent event) {
                showNewMessage();
            }

        }));

        registerHandler(eventBus.addHandler(SentMessageEvent.TYPE, new SentMessageEventHandler() {

            public void onSentMessageEvent(SentMessageEvent ev) {
                showMessageTable(user, folder, searchValue);
            }

        }));

        registerHandler(eventBus.addHandler(ForwardMessageEvent.TYPE, new ForwardMessageEventHandler() {

            public void onForwardMessageEvent(ForwardMessageEvent event) {
                showForwardMessage(event);
            }

        }));
        registerHandler(eventBus.addHandler(ReplyMessageEvent.TYPE, new ReplyMessageEventHandler() {

            public void onReplyMessageEvent(ReplyMessageEvent event) {
                showReplyMessage(event);
            }

        }));
        registerHandler(eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {

            public void onFolderSelectionEvent(FolderSelectionEvent event) {
                user = event.getUser();
                folder = event.getFolder();
                showMessageTable(user, event.getFolder(), searchValue);
            }

        }));

        registerHandler(eventBus.addHandler(BackEvent.TYPE, new BackEventHandler() {

            public void onBackEvent(BackEvent event) {
                showMessageTable(user, folder, searchValue);
            }

        }));

        registerHandler(eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

            public void onExpandMessage(ExpandMessageEvent event) {
                if (editableTreeItem != null && editableTreeItem.isEdit()) {
                    editableTreeItem.cancelEdit();
                }
            }

        }));
        registerHandler(eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {

            public void onNewMessageEvent(NewMessageEvent event) {
                if (editableTreeItem != null && editableTreeItem.isEdit()) {
                    editableTreeItem.cancelEdit();
                }
            }

        }));
        registerHandler(eventBus.addHandler(DecreaseUnseenEvent.TYPE, new DecreaseUnseenEventHandler() {

            public void onDecreaseUnseenEvent(DecreaseUnseenEvent event) {
                display.decreaseUnseenMessageCount(event.getFolder(), event.getAmount());
            }

        }));
        registerHandler(eventBus.addHandler(IncreaseUnseenEvent.TYPE, new IncreaseUnseenEventHandler() {

            public void onIncreaseUnseenEvent(IncreaseUnseenEvent event) {
                display.increaseUnseenMessageCount(event.getFolder(), event.getAmount());
            }

        }));
        registerHandler(display.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(SelectionEvent<TreeItem> event) {
                tItem = (IMAPTreeItem) event.getSelectedItem();
                if (tItem.isEdit()) 
                    return;
<<<<<<< HEAD
<<<<<<< HEAD
                folder = (IMAPFolderProxy) tItem.getUserObject();
=======
                folder = (IMAPFolder) tItem.getUserObject();
>>>>>>> first commit
=======
                folder = (IMAPFolderProxy) tItem.getUserObject();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
                eventBus.fireEvent(new LoadMessagesEvent(user, folder));
            }

        }));

        registerHandler(display.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

            public void onSelection(SelectionEvent<TreeItem> event) {
                tItem = (IMAPTreeItem) event.getSelectedItem();
                if (tItem.isEdit()) 
                    return;
<<<<<<< HEAD
<<<<<<< HEAD
                folder = (IMAPFolderProxy) tItem.getUserObject();
=======
                folder = (IMAPFolder) tItem.getUserObject();
>>>>>>> first commit
=======
                folder = (IMAPFolderProxy) tItem.getUserObject();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
                if (folder.getFullName().equalsIgnoreCase(user.getSettings().getInboxFolderName())) {
                    display.getDeleteEnable().setEnabled(false);
                    display.getRenameEnable().setEnabled(false);
                } else {
                    display.getDeleteEnable().setEnabled(true);
                    display.getRenameEnable().setEnabled(true);
                }
            }

        }));

        registerHandler(display.getRenameClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                tItem.startEdit();
            }

        }));

        registerHandler(display.getDeleteClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                display.getDeleteConfirmDialog().show();
            }

        }));

        registerHandler(display.getDeleteConfirmClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
<<<<<<< HEAD
<<<<<<< HEAD
                dispatcher.execute(new DeleteFolder(folder), new AsyncCallback<GenericResult>() {
=======
                cachingDispatcher.execute(new DeleteFolder(folder), new AsyncCallback<GenericResult>() {
>>>>>>> first commit
=======
                dispatcher.execute(new DeleteFolder(folder), new AsyncCallback<GenericResult>() {
>>>>>>> constantly changed by manolo

                    public void onFailure(Throwable caught) {
                        GWT.log("ERROR while deleting", caught);
                    }

                    public void onSuccess(GenericResult result) {
                        display.deleteSelectedFolder();
                    }

                });
            }

        }));

        registerHandler(display.getNewClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                editableTreeItem = display.createFolder(new EditHandler() {

                    public void onEditEvent(EditEvent event) {
                        final IMAPTreeItem item = (IMAPTreeItem) event.getSource();
                        final String newValue = (String) event.getNewValue();
                        if (event.getEventType().equals(EditEvent.EventType.Stop)) {
<<<<<<< HEAD
<<<<<<< HEAD
                            dispatcher.execute(new CreateFolder(new IMAPFolder(newValue.trim())), new AsyncCallback<GenericResult>() {
=======
                            cachingDispatcher.execute(new CreateFolder(new IMAPFolder(newValue.trim())), new AsyncCallback<GenericResult>() {
>>>>>>> first commit
=======
                            dispatcher.execute(new CreateFolder(new IMAPFolder(newValue.trim())), new AsyncCallback<GenericResult>() {
>>>>>>> constantly changed by manolo

                                public void onFailure(Throwable caught) {
                                    GWT.log("Error while create folder", caught);
                                    item.cancelEdit();
                                }

                                public void onSuccess(GenericResult result) {
                                    // Nothing todo
                                }

                            });
                        }
                    }

                });
            }

        }));

        registerHandler(eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {

            public void onMessagesReceived(MessagesReceivedEvent event) {
<<<<<<< HEAD
<<<<<<< HEAD
            	IMAPFolderProxy f = event.getFolder();
=======
                IMAPFolder f = event.getFolder();
>>>>>>> first commit
=======
            	IMAPFolderProxy f = event.getFolder();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
                display.updateTreeItem(f);
            }

        }));
        
        registerHandler(eventBus.addHandler(LoginEvent.TYPE,  new LoginEventHandler() {

            public void onLogin(LoginEvent event) {
                user = event.getUser();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());;
                searchValue = null;
                showMessageTable(user, folder, searchValue);
=======
                folder = new IMAPFolder(user.getSettings().getInboxFolderName());
                messageListPresenter.revealDisplay(user, folder, null);
>>>>>>> first commit
=======
                folder = new IMAPFolder(user.getSettings().getInboxFolderName());;
=======
                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
                searchValue = null;
                showMessageTable(user, folder, searchValue);
>>>>>>> constantly changed by manolo
            }
            
        }));
        
        // Export native javascript methods
        exportJSMethods(this);

    }

<<<<<<< HEAD
<<<<<<< HEAD
=======

    @Override
    protected void onUnbind() {
        reset();

        super.onUnbind();
    }

    
>>>>>>> first commit
=======
>>>>>>> constantly changed by manolo
    public void revealDisplay(User user) {
        this.user = user;
        loadTreeItems();  
        revealDisplay();
    }
    
    @Override
    protected void onRevealDisplay() {
<<<<<<< HEAD
<<<<<<< HEAD
//        showMessageTable(user, folder, searchValue);
//        super.onRevealDisplay();
=======
        showMessageTable(user, folder, searchValue);
        super.onRevealDisplay();
>>>>>>> first commit
=======
//        showMessageTable(user, folder, searchValue);
//        super.onRevealDisplay();
>>>>>>> constantly changed by manolo
    }
    
    public void openLink(String url) {
        Window.open(url, "_blank", "");
    }

    public void mailTo(String mailto) {
        sendPresenter.revealDisplay(user, mailto);
    }
    
    private native void exportJSMethods(MainPresenter presenter) /*-{
      $wnd.openLink = function(url) {
        try {
           presenter.@org.apache.hupa.client.mvp.MainPresenter::openLink(Ljava/lang/String;) (url);
        } catch(e) {}
        return false;
      };
      $wnd.mailTo = function(mail) {
        try {
           presenter.@org.apache.hupa.client.mvp.MainPresenter::mailTo(Ljava/lang/String;) (mail);
        } catch(e) {}
        return false;
      };
    }-*/;
}
