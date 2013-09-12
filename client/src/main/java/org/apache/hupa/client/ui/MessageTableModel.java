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

<<<<<<< HEAD
import net.customware.gwt.dispatch.client.DispatchAsync;

<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.evo.HupaEvoCallback;
<<<<<<< HEAD
=======
import org.apache.hupa.client.HupaEvoCallback;
>>>>>>> Change to new mvp framework - first step
=======
import org.apache.hupa.client.evo.HupaEvoCallback;
>>>>>>> Make the evo more clear.
import org.apache.hupa.shared.data.IMAPFolder;
=======
import org.apache.hupa.shared.data.ImapFolderImpl;
<<<<<<< HEAD
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
import org.apache.hupa.shared.data.Message;
=======
>>>>>>> try to change fetch messages to use RF
=======
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
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
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> Change to new mvp framework - first step
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
=======
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.TableModelHelper;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * TableModel which retrieve the messages for the user
 * 
 */
public class MessageTableModel extends MutableTableModel<Message> {

<<<<<<< HEAD
    private EventBus eventBus;
    private User user;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private IMAPFolderProxy folder;
=======
    private IMAPFolder folder;
>>>>>>> Change to new mvp framework - first step
=======
    private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private ImapFolder folder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
    private String searchValue;

    @Inject
    public MessageTableModel(EventBus eventBus){
        
        this.eventBus = eventBus;
//        this.dispatcher = dispatcher;

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
//                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());
=======
                folder = new IMAPFolder(user.getSettings().getInboxFolderName());
>>>>>>> Change to new mvp framework - first step
=======
//                folder = (IMAPFolderProxy)new IMAPFolder(user.getSettings().getInboxFolderName());
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
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
        
//        dispatcher.execute(new FetchMessages(folder, request.getStartRow(), request.getNumRows(), searchValue),new HupaEvoCallback<FetchMessagesResult>(dispatcher, eventBus) {
//            public void callback(final FetchMessagesResult result) {
//                // Update folder information before notifying presenter
//                folder.setMessageCount(result.getRealCount());
//                folder.setUnseenMessageCount(result.getRealUnreadCount());
//                
//                setRowCount(result.getRealCount());
//                callback.onRowsReady(request, new TableModelHelper.Response<Message>() {
//                    @Override
//                    public Iterator<Message> getRowValues() {
//                        if (result != null && result.getMessages() != null) {
//                            return result.getMessages().iterator();
//                        } else {
//                            return new ArrayList<Message>().iterator();
//                        }
//                    }
//                });
//                
//                // Notify presenter to update folder tree view
//                eventBus.fireEvent(new MessagesReceivedEvent(folder, result.getMessages()));
//            }
//        }); 
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
=======
	private EventBus eventBus;
	private User user;
	private ImapFolder folder;
	private String searchValue;
	private HupaRequestFactory requestFactory;

	@Inject
	public MessageTableModel(EventBus eventBus, HupaRequestFactory requestFactory) {

		this.eventBus = eventBus;
		this.requestFactory = requestFactory;

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
				folder = new ImapFolderImpl(user.getSettings().getInboxFolderName());
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
	public void requestRows(final Request request,
	        final com.google.gwt.gen2.table.client.TableModel.Callback<Message> callback) {

		// if the given user or folder is null, its safe to return an empty list
		if (user == null || folder == null) {
			callback.onRowsReady(request, new TableModelHelper.Response<Message>() {
				@Override
				public Iterator<Message> getRowValues() {
					return new ArrayList<Message>().iterator();
				}

			});
			return;
		}
		FetchMessagesRequest messagesRequest = requestFactory.messagesRequest();
		final FetchMessagesAction action = messagesRequest.create(FetchMessagesAction.class);
		// FIXME cannot put setFolder to the first place
		action.setOffset(request.getNumRows());
		action.setFolder(folder);
		action.setSearchString(searchValue);
		action.setStart(request.getStartRow());
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
				System.out.println(result.getOffset());
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

		// dispatcher.execute(new FetchMessages(folder, request.getStartRow(),
		// request.getNumRows(), searchValue),new
		// HupaEvoCallback<FetchMessagesResult>(dispatcher, eventBus) {
		// public void callback(final FetchMessagesResult result) {
		// // Update folder information before notifying presenter
		// folder.setMessageCount(result.getRealCount());
		// folder.setUnseenMessageCount(result.getRealUnreadCount());
		//
		// setRowCount(result.getRealCount());
		// callback.onRowsReady(request, new
		// TableModelHelper.Response<Message>() {
		// @Override
		// public Iterator<Message> getRowValues() {
		// if (result != null && result.getMessages() != null) {
		// return result.getMessages().iterator();
		// } else {
		// return new ArrayList<Message>().iterator();
		// }
		// }
		// });
		//
		// // Notify presenter to update folder tree view
		// eventBus.fireEvent(new MessagesReceivedEvent(folder,
		// result.getMessages()));
		// }
		// });
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
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
}
