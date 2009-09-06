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

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.DisplayCallback;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.SessionAsyncCallback;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.DecreaseUnseenEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.DeleteFolder;
import org.apache.hupa.shared.rpc.EmptyResult;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;

public class IMAPFolderPresenter extends WidgetPresenter<IMAPFolderPresenter.Display>{

	private DispatchAsync dispatcher;
	protected User user;
	protected IMAPTreeItem tItem;
	protected IMAPFolder folder;
	private HasEditable editableTreeItem;

	@Inject
	public IMAPFolderPresenter(Display display, EventBus eventBus, DispatchAsync dispatcher) {
		super(display, eventBus);
		this.dispatcher = dispatcher;
	}

	public interface Display extends WidgetDisplay {
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
		public void deleteSelectedFolder();
		public HasEditable createFolder(EditHandler handler);
		public void increaseUnseenMessageCount(IMAPFolder folder, int amount);
		public void decreaseUnseenMessageCount(IMAPFolder folder, int amount);
	}

	protected void loadTreeItems() {
		dispatcher.execute(new FetchFolders(user.getSessionId()), new SessionAsyncCallback<FetchFoldersResult>(new DisplayCallback<FetchFoldersResult>(display) {

			@Override
			protected void handleFailure(Throwable e) {
				GWT.log("ERROR=", e);
			}

			@Override
			protected void handleSuccess(FetchFoldersResult result) {
				display.bindTreeItems(createTreeNodes(result.getFolders()));
				
				// disable
				display.getDeleteEnable().setEnabled(false);
				display.getRenameEnable().setEnabled(false);
			}

			
		},eventBus,user));
	}
	
	
	
	/**
	 * Create recursive the TreeNodes with all childs
	 * 
	 * @param list
	 * @return
	 */
	private List<IMAPTreeItem> createTreeNodes(List<IMAPFolder> list) {
		List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

		for (int i = 0; i < list.size(); i++) {
			IMAPFolder iFolder = list.get(i);
			
			final IMAPTreeItem record = new IMAPTreeItem(iFolder);
			record.addEditHandler(new EditHandler() {

				public void onEditEvent(EditEvent event) {
					if(event.getEventType().equals(EditEvent.EventType.Stop)) {
						IMAPFolder iFolder = new IMAPFolder((String)event.getOldValue());
						final String newName = (String)event.getNewValue();
						if (iFolder.getFullName().equalsIgnoreCase(newName) == false) {
							dispatcher.execute(new RenameFolder(user.getSessionId(), iFolder, newName), new SessionAsyncCallback<EmptyResult>(new AsyncCallback<EmptyResult>() {

								public void onFailure(Throwable caught) {
									record.cancelEdit();
								}	

								public void onSuccess(EmptyResult result) {
									folder.setFullName(newName);
								}
								
							},eventBus,user));
						}

					}
				}
				
			});
			record.setUserObject(iFolder);

			List<IMAPFolder> childFolders = iFolder.getChildIMAPFolders();
			if (childFolders != null && childFolders.isEmpty() == false) {
				List<IMAPTreeItem> items = createTreeNodes(childFolders);
				for (int a = 0; a < items.size(); a++) {
					record.addItem(items.get(a));
				}
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

	
	@Override
	public Place getPlace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onBind() {
		
		registerHandler(eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

			public void onExpandMessage(ExpandMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}
			
		}));
		registerHandler(eventBus.addHandler(DecreaseUnseenEvent.TYPE, new DecreaseUnseenEventHandler() {

			public void onDecreaseUnseenEvent(DecreaseUnseenEvent event) {
				// Check if the folder was the trash folder. If not increase the message count of the trash folder
				if (user.getSettings().getTrashFolderName().equalsIgnoreCase(event.getFolder().getFullName()) == false) {
					display.increaseUnseenMessageCount(new IMAPFolder(user.getSettings().getTrashFolderName()),event.getAmount());
				}
				display.decreaseUnseenMessageCount(event.getFolder(),event.getAmount());
			}
			
		}));
		registerHandler(display.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

			public void onSelection(SelectionEvent<TreeItem> event) {
				tItem = (IMAPTreeItem)event.getSelectedItem();
				folder = (IMAPFolder) tItem.getUserObject();
				eventBus.fireEvent(new LoadMessagesEvent(user,folder));
			}
			
		}));
		
		registerHandler(display.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

			public void onSelection(SelectionEvent<TreeItem> event) {
				tItem = (IMAPTreeItem)event.getSelectedItem();
				folder = (IMAPFolder) tItem.getUserObject();
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
				dispatcher.execute(new DeleteFolder(user.getSessionId(),folder), new AsyncCallback<EmptyResult>() {

					public void onFailure(Throwable caught) {
						GWT.log("ERROR while deleting", caught);
					}

					public void onSuccess(EmptyResult result) {
						display.deleteSelectedFolder();
					}
					
				});
			}
			
		}));
		
		registerHandler(display.getNewClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				editableTreeItem = display.createFolder(new EditHandler() {

					public void onEditEvent(EditEvent event) {
						final IMAPTreeItem item = (IMAPTreeItem)event.getSource();
						final String newValue = (String) event.getNewValue();
						if (event.getEventType().equals(EditEvent.EventType.Stop)) {
							dispatcher.execute(new CreateFolder(user.getSessionId(),new IMAPFolder(newValue.trim())),  new AsyncCallback<EmptyResult>() {

								public void onFailure(Throwable caught) {
									GWT.log("Error while create folder",caught);
									item.cancelEdit();
								}

								public void onSuccess(EmptyResult result) {
									// Nothing todo
								}
								
							});
						}
					}
					
				});
			}
			
		}));
	}

	public void bind(User user) {
		this.user = user;
		bind();

		refreshDisplay();
	}
	
	@Override
	protected void onPlaceRequest(PlaceRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onUnbind() {
		// TODO Auto-generated method stub
		
	}

	public void refreshDisplay() {
		loadTreeItems();		
	}

	public void revealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
