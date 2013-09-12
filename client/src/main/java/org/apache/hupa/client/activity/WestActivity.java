<<<<<<< HEAD
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

import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.BackEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.ForwardMessageEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.events.NewMessageEventHandler;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.events.ReplyMessageEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
import org.apache.hupa.widgets.ui.HasEditable;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class WestActivity extends AppBaseActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		container.setWidget(display.asWidget());
	}

	public void setCurrentPlace(Place place) {
		this.currentPlace = place;
	}

	public WestActivity with(FolderPlace place) {
		this.currentPlace = place;
//		this.user = place.getUser();
		this.folder = new ImapFolderImpl(user.getSettings().getInboxFolderName());
		return this;
	}

	private void cloneFolder(ImapFolder desc, ImapFolder src) {
	    desc.setChildren(src.getChildren());
		desc.setDelimiter(src.getDelimiter());
		desc.setFullName(src.getFullName());
		desc.setMessageCount(src.getMessageCount());
		desc.setName(src.getName());
		desc.setSubscribed(src.getSubscribed());
		desc.setUnseenMessageCount(src.getUnseenMessageCount());
    }

	private void bind() {
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {
			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				showMessageTable(loadMessagesEvent.getUser(), loadMessagesEvent.getFolder(),
				        loadMessagesEvent.getSearchValue());
			}
		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {
			public void onExpandMessage(ExpandMessageEvent event) {
//				final boolean decreaseUnseen;
				final Message message = event.getMessage();
				// check if the message was already seen in the past
				if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
//					decreaseUnseen = true;//TODO 1209
				} else {
//					decreaseUnseen = false;
				}

				GetMessageDetailsRequest req = rf.messageDetailsRequest();
				GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
				final ImapFolder f = req.create(ImapFolder.class);
//				event.getFolder().setFolderTo(f);
				cloneFolder(f, event.getFolder());
				action.setFolder(f);
				action.setUid(message.getUid());
				req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
					@Override
					public void onSuccess(GetMessageDetailsResult response) {
						/*
						 * TODO if (decreaseUnseen) { eventBus.fireEvent(new
						 * DecreaseUnseenEvent(user, folder)); }
						 */
//						placeController.goTo(messagePlaceProvider.get().with(user, f, message,
//						        response.getMessageDetails()));
					}
				});
			}
		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {
			public void onNewMessageEvent(NewMessageEvent event) {
				showNewMessage();
			}
		});
		eventBus.addHandler(SentMessageEvent.TYPE, new SentMessageEventHandler() {
			public void onSentMessageEvent(SentMessageEvent ev) {
				showMessageTable(user, folder, searchValue);
			}
		});
		eventBus.addHandler(ForwardMessageEvent.TYPE, new ForwardMessageEventHandler() {
			public void onForwardMessageEvent(ForwardMessageEvent event) {
				showForwardMessage(event);
			}
		});
		eventBus.addHandler(ReplyMessageEvent.TYPE, new ReplyMessageEventHandler() {
			public void onReplyMessageEvent(ReplyMessageEvent event) {
				showReplyMessage(event);
			}
		});
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {
			public void onFolderSelectionEvent(FolderSelectionEvent event) {
				user = event.getUser();
				folder = event.getFolder();
				showMessageTable(user, event.getFolder(), searchValue);
			}
		});
		eventBus.addHandler(BackEvent.TYPE, new BackEventHandler() {
			public void onBackEvent(BackEvent event) {
				showMessageTable(user, folder, searchValue);
			}
		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {
			public void onExpandMessage(ExpandMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}
		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {
			public void onNewMessageEvent(NewMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}
		});
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			public void onLogin(LoginEvent event) {
				user = event.getUser();
				searchValue = null;
			}
		});
		exportJSMethods(this);
	}

	public void openLink(String url) {
		Window.open(url, "_blank", "");
	}

	public void mailTo(String mailto) {
		// sendPresenter.revealDisplay(user, mailto);
	}

	private native void exportJSMethods(WestActivity westactivity) /*-{
	                                                               $wnd.openLink = function(url) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::openLink(Ljava/lang/String;) (url);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               $wnd.mailTo = function(mail) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::mailTo(Ljava/lang/String;) (mail);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               }-*/;

	private void showMessageTable(User user, ImapFolder folder, String searchValue) {
		this.user = user;
		this.folder = folder;
		this.searchValue = searchValue;

		// FIXME goto?
//		placeController.goTo(new MailFolderPlace().with(user, folder, searchValue));
	}

	private void showNewMessage() {
		pc.goTo(this.messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
	}

	private void showForwardMessage(ForwardMessageEvent event) {
		pc.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), Type.FORWARD));
	}

	private void showReplyMessage(ReplyMessageEvent event) {
		pc.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), event.getReplyAll() ? Type.REPLY_ALL : Type.REPLY));
	}

	@Inject private Displayable display;
	@Inject private Provider<MessageSendPlace> messageSendPlaceProvider;
//	@Inject private Provider<IMAPMessagePlace> messagePlaceProvider;
	private User user;
	private ImapFolder folder;
	private IMAPTreeItem tItem;
	private HasEditable editableTreeItem;
	private String searchValue;
	private Place currentPlace;

	public interface Displayable extends WidgetDisplayable {
	}
=======
package org.apache.hupa.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.mvp.WidgetContainerDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.RenameFolder;
import org.apache.hupa.widgets.event.EditEvent;
import org.apache.hupa.widgets.event.EditHandler;
import org.apache.hupa.widgets.ui.HasEditable;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;

public class WestActivity extends AbstractActivity {

	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	
    private DispatchAsync dispatcher;
    private User user;
    private IMAPFolder folder;
    private IMAPTreeItem tItem;
    private HasEditable editableTreeItem;
    
    @Inject
    public WestActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			DispatchAsync dispatcher){
    	this.dispatcher = dispatcher;
    	this.display = display;
    	this.eventBus = eventBus;
    	this.placeController = placeController;
    	
    }

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		display.setUser(user);
		loadTreeItems();
		bind();
		container.setWidget(display.asWidget());
	}
	
    public WestActivity with(User user){
    	this.user = user;
    	return this;
    }

    protected void loadTreeItems() {
        display.setLoadingFolders(true);
        dispatcher.execute(new FetchFolders(), new HupaEvoCallback<FetchFoldersResult>(dispatcher, eventBus, display) {
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
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolder> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolder iFolder : list) {

            final IMAPTreeItem record = new IMAPTreeItem(iFolder);
            record.addEditHandler(new EditHandler() {

                public void onEditEvent(EditEvent event) {
                    if (event.getEventType().equals(EditEvent.EventType.Stop)) {
                        IMAPFolder iFolder = new IMAPFolder((String) event.getOldValue());
                        final String newName = (String) event.getNewValue();
                        if (iFolder.getFullName().equalsIgnoreCase(newName) == false) {
                            dispatcher.execute(new RenameFolder(iFolder, newName), new HupaEvoCallback<GenericResult>(dispatcher, eventBus) {
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

            List<IMAPFolder> childFolders = iFolder.getChildIMAPFolders();
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
	private void bind(){
		
	}

    public interface Displayable extends WidgetContainerDisplayable {
        
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

        public void updateTreeItem(IMAPFolder folder);

        public void deleteSelectedFolder();

        public HasEditable createFolder(EditHandler handler);

        public void increaseUnseenMessageCount(IMAPFolder folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolder folder, int amount);
        
        public void setLoadingFolders(boolean loading);
        public void setLoadingMessage(boolean loading);
        
        public void setUser(User user);

    }		

>>>>>>> Change to new mvp framework - first step

}
