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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.LabelListActivity;
=======
>>>>>>> make layout can be arranged by clicking the navigation buttons; make the layout changing by set their sizes to zero rather than remove/add from their parent widgets; merge to the master branch.
=======
import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.LabelListActivity;
>>>>>>> make add of label setting work in backend
import org.apache.hupa.client.activity.LabelPropertiesActivity;
import org.apache.hupa.client.rf.CreateFolderRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.RenameFolderRequest;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.events.RefreshLabelListEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
<<<<<<< HEAD
import com.google.gwt.event.shared.EventBus;
=======
import org.apache.hupa.client.activity.LabelListActivity;
import org.apache.hupa.client.activity.LabelPropertiesActivity;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.RenameFolderRequest;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.RenameFolderAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
>>>>>>> add rename RF to label setting feature
=======
>>>>>>> make add of label setting work in backend
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.user.client.ui.CaptionPanel;
=======
>>>>>>> add rename RF to label setting feature
=======
import com.google.gwt.user.client.ui.CaptionPanel;
>>>>>>> make add of label setting work in backend
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
=======
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
>>>>>>> add rename RF to label setting feature
=======
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
>>>>>>> make add of label setting work in backend

public class LabelPropertiesView extends Composite implements LabelPropertiesActivity.Displayable {

	@Inject HupaRequestFactory rf;
<<<<<<< HEAD
<<<<<<< HEAD
	@Inject HupaController hc;
	@Inject EventBus eventBus;

	@UiField TextBox name;
	private String path;

	@UiField ListBox parent;
	@UiField Button save;

	@UiField VerticalPanel propContainer;
	@UiField CaptionPanel information;
	private static final String ROOT_PATH = "imap_root";

	private int state;

	ImapFolder folder;

	@UiHandler("save")
	void handleSave(ClickEvent e) {
		hc.showTopLoading("Saving...");
		if (state == LabelListActivity.Displayable.CASCADE_TYPE_RENAME) {
			RenameFolderRequest req = rf.renameFolderRequest();
			RenameFolderAction action = req.create(RenameFolderAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(folder.getFullName());
			action.setFolder(f);

			if (ROOT_PATH.equals(parent.getValue(parent.getSelectedIndex()))) {
				action.setNewName(name.getText());
			} else {
				action.setNewName(parent.getValue(parent.getSelectedIndex()) + "/" + name.getText());
			}
			req.rename(action).fire(new Receiver<GenericResult>() {
				@Override
				public void onSuccess(GenericResult response) {
					hc.hideTopLoading();
					eventBus.fireEvent(new RefreshLabelListEvent());
					hc.showNotice("The label \"" + f.getFullName() + "\" has been renamed to " + name.getText(), 10000);
				}
				@Override
				public void onFailure(ServerFailure error) {
					hc.hideTopLoading();
					hc.showNotice(error.getMessage(), 10000);
				}
			});
		} else if (state == LabelListActivity.Displayable.CASCADE_TYPE_ADD) {
			CreateFolderRequest req = rf.createFolderRequest();
			CreateFolderAction action = req.create(CreateFolderAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(path + "/" + name.getText());
			action.setFolder(f);
			req.create(action).fire(new Receiver<GenericResult>() {
				@Override
				public void onSuccess(GenericResult response) {
					hc.hideTopLoading();
					eventBus.fireEvent(new RefreshLabelListEvent());
					hc.showNotice("The label \"" + f.getName() + "\" was created.", 10000);
				}
				@Override
				public void onFailure(ServerFailure error) {
					hc.hideTopLoading();
					hc.showNotice(error.getMessage(), 10000);
				}
			});

		}
=======
	
	@UiField TextBox _name;
	@UiField ListBox _parent;
=======
	@Inject HupaController hc;

	@UiField TextBox name;
	@UiField ListBox parent;
>>>>>>> make add of label setting work in backend
	@UiField Button save;
	
	@UiField VerticalPanel propContainer;
	@UiField CaptionPanel information;
	
	private int state;

	ImapFolder folder;

	@UiHandler("save")
<<<<<<< HEAD
	void handleCompose(ClickEvent e){
		RenameFolderRequest req = rf.renameFolderRequest();
		RenameFolderAction action = req.create(RenameFolderAction.class);
		action.setFolder(folder);
		action.setNewName(_name.getText());
		req.rename(action).fire(new Receiver<GenericResult>() {
			@Override
			public void onSuccess(GenericResult response) {
//				afterSend(response);
				System.out.println("success to rename");
			}
		});
>>>>>>> add rename RF to label setting feature
=======
	void handleSave(ClickEvent e) {
		hc.showTopLoading("Saving...");
		if(state == LabelListActivity.Displayable.CASCADE_TYPE_RENAME){
			RenameFolderRequest req = rf.renameFolderRequest();
			RenameFolderAction action = req.create(RenameFolderAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(folder.getFullName());
			action.setFolder(f);
			action.setNewName(name.getText());
			req.rename(action).fire(new Receiver<GenericResult>() {
				@Override
				public void onSuccess(GenericResult response) {
					hc.hideTopLoading();
					hc.showNotice("The label \"" + f.getFullName() + "\" has been renamed to "+name.getText(), 10000);
				}
				@Override
				public void onFailure(ServerFailure error) {
					hc.hideTopLoading();
					hc.showNotice(error.getMessage(), 10000);
				}
			});	
		} else if (state == LabelListActivity.Displayable.CASCADE_TYPE_ADD){
			CreateFolderRequest req = rf.createFolderRequest();
			CreateFolderAction action = req.create(CreateFolderAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(folder.getFullName()+"/"+name.getText());
			action.setFolder(f);
			req.create(action).fire(new Receiver<GenericResult>(){
				@Override
				public void onSuccess(GenericResult response) {
					hc.hideTopLoading();
					hc.showNotice("The label \"" + f.getFullName() + "\" was created.", 10000);
				}
				@Override
				public void onFailure(ServerFailure error) {
					hc.hideTopLoading();
					hc.showNotice(error.getMessage(), 10000);
				}
			});
			
		}
>>>>>>> make add of label setting work in backend
	}
	public LabelPropertiesView() {
		initWidget(binder.createAndBindUi(this));
	}

	interface Binder extends UiBinder<DecoratorPanel, LabelPropertiesView> {
	}

	private static Binder binder = GWT.create(Binder.class);

	@Override
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> make add of label setting work in backend
	public void cascade(LabelNode labelNode, List<LabelNode> wholeList, int type) {
		state = type;
		switch (type) {
		case LabelListActivity.Displayable.CASCADE_TYPE_ADD:
			makeParentList(labelNode, true, wholeList);
			name.setText("");
<<<<<<< HEAD
			path = labelNode.getPath();
=======
>>>>>>> make add of label setting work in backend
			information.setVisible(false);
			break;
		case LabelListActivity.Displayable.CASCADE_TYPE_RENAME:
			name.setText(labelNode.getName());
<<<<<<< HEAD
			path = labelNode.getPath();
=======
>>>>>>> make add of label setting work in backend
			makeParentList(labelNode, false, wholeList);
			information.setVisible(true);
			break;
		default:
			name.setText("");
		}
<<<<<<< HEAD
		folder = labelNode.getFolder();
		if (!(labelNode.getFolder().getSubscribed())) {
			name.setEnabled(false);
		} else {
			name.setEnabled(true);
		}
	}
	private void makeParentList(LabelNode labelNode, boolean isParent, List<LabelNode> wholeList) {
		parent.clear();
		parent.addItem("---", ROOT_PATH);
		for (LabelNode folderNode : wholeList) {
			if (isItself(labelNode, isParent, folderNode) || isItsDecendant(labelNode, isParent, folderNode)) {
				continue;
			}
			parent.addItem(folderNode.getNameForDisplay().replace("&nbsp;&nbsp;", ". "), folderNode.getPath());
		}

		int parentIndex = wholeList.indexOf(isParent ? labelNode : labelNode.getParent());
		parent.setSelectedIndex(parentIndex < 0 ? 0 : parentIndex + 1);
	}
	private boolean isItself(LabelNode labelNode, boolean isParent, LabelNode folderNode) {
		return !isParent && labelNode.compareTo(folderNode) == 0;
	}
	private boolean isItsDecendant(LabelNode labelNode, boolean isParent, LabelNode folderNode) {
		return !isParent && isKinship(labelNode, folderNode);
	}

	private boolean isKinship(LabelNode labelNode, LabelNode folderNode) {
		if (folderNode == null) {
			return false;
		}
		if (labelNode.compareTo(folderNode.getParent()) == 0)
			return true;
		return isKinship(labelNode, folderNode.getParent());
	}
	@Override
	public HasClickHandlers getSave() {
		return save;
=======
	public void cascade(LabelNode labelNode, List<LabelNode> list) {
		_name.setText(labelNode.getName());
=======
>>>>>>> make add of label setting work in backend
		folder = labelNode.getFolder();
		if (!(labelNode.getFolder().getSubscribed())) {
			name.setEnabled(false);
		} else {
			name.setEnabled(true);
		}
	}
	private void makeParentList(LabelNode labelNode, boolean isParent, List<LabelNode> wholeList) {
		parent.clear();
		parent.addItem("---", "root");
		for (LabelNode folderNode : wholeList) {
			parent.addItem(folderNode.getName(), folderNode.getPath());
		}
<<<<<<< HEAD
		_parent.setSelectedIndex(list.indexOf(labelNode.getParent()));
>>>>>>> add rename RF to label setting feature
=======
		int parentIndex = wholeList.indexOf(isParent ? labelNode : labelNode.getParent());
		parent.setSelectedIndex(parentIndex < 0 ? 0 : parentIndex + 1);
	}
	@Override
	public HasClickHandlers getSave() {
		return save;
>>>>>>> make add of label setting work in backend
	}

}
