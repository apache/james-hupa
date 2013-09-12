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
import java.util.Iterator;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> first commit
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
>>>>>>> Allow client can use the domain entity interface.
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.TableModelHelper;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.inject.Inject;

/**
 * TableModel which retrieve the messages for the user
 *
 */
public class MessageTableModel extends MutableTableModel<Message> {

    private EventBus eventBus;
    private DispatchAsync dispatcher;
    private User user;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private IMAPFolderProxy folder;
=======
    private IMAPFolder folder;
>>>>>>> first commit
=======
    private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private ImapFolder folder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
    private String searchValue;

    @Inject
    public MessageTableModel(EventBus eventBus, DispatchAsync dispatcher){
        
        this.eventBus = eventBus;
        this.dispatcher = dispatcher;

        // bind some Events 
        eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {
            
            public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
                user = loadMessagesEvent.getUser();
                folder = loadMessagesEvent.getFolder();
                searchValue = loadMessagesEvent.getSearchValue();
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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());
=======
                folder = new IMAPFolder(user.getSettings().getInboxFolderName());
>>>>>>> first commit
=======
                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
                folder = (ImapFolder)new ImapFolderImpl(user.getSettings().getInboxFolderName());
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
                searchValue = null;
            }
        });
        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
            
            public void onLogout(LogoutEvent logoutEvent) {
                user = null;
                folder = null;
                searchValue = null;
            }
        });
        
        
    }

    @Override 
    public void requestRows(
            final Request request,
            final com.google.gwt.gen2.table.client.TableModel.Callback<Message> callback) {
        
        // if the given user or folder is null, its safe to return an empty list
        if (user == null || folder == null) {
            callback.onRowsReady(request, new TableModelHelper.Response<Message>() {

                @Override
                public Iterator<Message> getRowValues() {
                    return new ArrayList<Message>().iterator();
                }
                
            });
        }
        
        dispatcher.execute(new FetchMessages(folder, request.getStartRow(), request.getNumRows(), searchValue),new HupaCallback<FetchMessagesResult>(dispatcher, eventBus) {
            public void callback(final FetchMessagesResult result) {
                // Update folder information before notifying presenter
                folder.setMessageCount(result.getRealCount());
                folder.setUnseenMessageCount(result.getRealUnreadCount());
<<<<<<< HEAD
<<<<<<< HEAD
                
                setRowCount(result.getRealCount());
                callback.onRowsReady(request, new TableModelHelper.Response<Message>() {
=======
                // Notify presenter to update folder tree view
                eventBus.fireEvent(new MessagesReceivedEvent(folder, result.getMessages()));
                TableModelHelper.Response<Message> response = new TableModelHelper.Response<Message>() {
>>>>>>> first commit
=======
                
                setRowCount(result.getRealCount());
                callback.onRowsReady(request, new TableModelHelper.Response<Message>() {
>>>>>>> constantly changed by manolo
                    @Override
                    public Iterator<Message> getRowValues() {
                        if (result != null && result.getMessages() != null) {
                            return result.getMessages().iterator();
                        } else {
                            return new ArrayList<Message>().iterator();
                        }
                    }
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> constantly changed by manolo
                });
                
                // Notify presenter to update folder tree view
                eventBus.fireEvent(new MessagesReceivedEvent(folder, result.getMessages()));
<<<<<<< HEAD
=======
                };
                setRowCount(result.getRealCount());
                callback.onRowsReady(request,response);
>>>>>>> first commit
=======
>>>>>>> constantly changed by manolo
            }
        }); 
    }

    @Override
    protected boolean onRowInserted(int beforeRow) {
        return true;
    }

    @Override
    protected boolean onRowRemoved(int row) {    
        return true;
    }

    @Override
    protected boolean onSetRowValue(int row, Message rowValue) {
        return true;
    }
}
