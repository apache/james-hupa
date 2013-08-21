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

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import org.apache.hupa.client.HupaController;
=======
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.rf.SetFlagRequest;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.ToolBarView.Parameters;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.SetFlagAction;
<<<<<<< HEAD
=======
import org.apache.hupa.shared.events.RefreshMessagesEvent;
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import org.apache.hupa.shared.events.RefreshUnreadEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
<<<<<<< HEAD
import com.google.gwt.event.shared.HandlerRegistration;
=======
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class ToolBarActivity extends AppBaseActivity {

	@Inject private Displayable display;
	@Inject private MessagesCellTable table;
	@Inject private MessageListActivity.Displayable messagesDisplay;
<<<<<<< HEAD
	@Inject private HupaController hupaController;
=======
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
	private String folderName;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
		if(pc.getWhere() instanceof FolderPlace){
			display.enableAllTools(false);
		}
	}

	public ToolBarActivity with(String folder) {
		this.folderName = folder;
		return this;
	}

	public Displayable getDisplay() {
		return display;
	}

<<<<<<< HEAD
	private void bindTo(EventBus eventBus) {
		registerHandler(display.getMarkRead().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hupaController.showTopLoading("Loading");
=======
	private void bindTo(final EventBus eventBus) {
		registerHandler(display.getMarkRead().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hc.showTopLoading("Loading");
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
				toMarkRead(true);
				display.getPopup().hide();
			}
		}));
		registerHandler(display.getMarkUnread().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
<<<<<<< HEAD
				hupaController.showTopLoading("Loading");
=======
				hc.showTopLoading("Loading");
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
				toMarkRead(false);
				display.getPopup().hide();
			}
		}));
<<<<<<< HEAD
=======

		registerHandler(display.getRefresh().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hc.showTopLoading("Loading");
				eventBus.fireEvent(new RefreshMessagesEvent());
				eventBus.fireEvent(new RefreshUnreadEvent());
			}
		}));
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
	}

	protected void toMarkRead(boolean read) {
		List<Long> uids = new ArrayList<Long>();
		for (Message msg : table.getVisibleItems()) {
			if (table.getSelectionModel().isSelected(msg)) {
				uids.add(msg.getUid());
				table.markRead(msg, read);
			}
		}
		SetFlagRequest req = this.rf.setFlagRequest();
		SetFlagAction action = req.create(SetFlagAction.class);
		ImapFolder f = req.create(ImapFolder.class);
		f.setFullName(folderName);
		action.setFolder(f);
		action.setFlag(IMAPFlag.SEEN);
		action.setValue(read);
		action.setUids(uids);
		req.set(action).fire(new Receiver<GenericResult>() {
			@Override
			public void onSuccess(GenericResult response) {
				eventBus.fireEvent(new RefreshUnreadEvent());
				messagesDisplay.refresh();
<<<<<<< HEAD
				hupaController.hideTopLoading();
=======
				hc.hideTopLoading();
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
			}
		});
	}


	// FIXME: for some reason if we extend WidgetDisplayable we get the error
	// ToolBarActivity.java:[124,38] error: cannot find symbol
	public interface Displayable /*extends WidgetDisplayable*/ {
	    // FIXME: added this method until we fix the problem with inheritance
	    com.google.gwt.user.client.ui.Widget asWidget();

		void enableSendingTools(boolean is);
<<<<<<< HEAD
		HandlerRegistration getForwardReg();
		HandlerRegistration getReplyAllReg();
		HandlerRegistration getReplyReg();
		HandlerRegistration getMarkReg();
		HandlerRegistration getDeleteReg();
		void enableDealingTools(boolean is);
		void enableAllTools(boolean is);
=======
		void enableDealingTools(boolean is);
		void enableAllTools(boolean is);

		HasClickHandlers getRefresh();
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
		HasClickHandlers getReply();
		HasClickHandlers getReplyAll();
		HasClickHandlers getForward();
		void setParameters(Parameters parameters);
		Parameters getParameters();
		HasClickHandlers getMarkUnread();
		HasClickHandlers getMarkRead();
		HasClickHandlers getMark();
		HasClickHandlers getDelete();
		PopupPanel getPopup();
		HasClickHandlers getCompose();
	}
}
