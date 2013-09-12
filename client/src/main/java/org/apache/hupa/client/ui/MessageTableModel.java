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
import java.util.Iterator;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchMessagesResult;

import com.google.gwt.event.shared.EventBus;
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
    private IMAPFolder folder;
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
                folder = new IMAPFolder(user.getSettings().getInboxFolderName());
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
        
        dispatcher.execute(new FetchMessages(folder, request.getStartRow(), request.getNumRows(), searchValue),new HupaEvoCallback<FetchMessagesResult>(dispatcher, eventBus) {
            public void callback(final FetchMessagesResult result) {
                // Update folder information before notifying presenter
                folder.setMessageCount(result.getRealCount());
                folder.setUnseenMessageCount(result.getRealUnreadCount());
                
                setRowCount(result.getRealCount());
                callback.onRowsReady(request, new TableModelHelper.Response<Message>() {
                    @Override
                    public Iterator<Message> getRowValues() {
                        if (result != null && result.getMessages() != null) {
                            return result.getMessages().iterator();
                        } else {
                            return new ArrayList<Message>().iterator();
                        }
                    }
                });
                
                // Notify presenter to update folder tree view
                eventBus.fireEvent(new MessagesReceivedEvent(folder, result.getMessages()));
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
