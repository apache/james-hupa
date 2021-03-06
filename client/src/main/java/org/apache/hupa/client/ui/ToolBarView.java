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

import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.DeleteClickEvent;
import org.apache.hupa.shared.events.ShowRawEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ToolBarView extends Composite implements ToolBarActivity.Displayable {
	
    @Inject private PlaceController placeController;
    @Inject private EventBus eventBus;

    @UiField public Anchor refresh;
    @UiField public Anchor compose;
    @UiField public Anchor reply;
    @UiField public HTMLPanel replyAllGroup;
    @UiField public Anchor replyAll;
    @UiField public HTMLPanel forwardGroup;
    @UiField public Anchor forward;
    @UiField public Anchor delete;
    @UiField public Anchor mark;
    @UiField public Anchor raw;
    @UiField public Anchor more;

    @UiField public HTMLPanel replyAllTip;
    @UiField public HTMLPanel forwardTip;

    @UiField public Style style;

    public interface Style extends CssResource {
        String disabledButton();
        String popupMenu();
        String activeIcon();
        String toolBarMenu();
        String listicon();
        String read();
        String unread();
    }

    private VerticalPanel popup;
    final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
    private Anchor markRead;
    private Anchor markUnread;

    private Parameters parameters;

    public Parameters getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public static class Parameters {
        private User user;
        private String folderName;
        private Message oldmessage;
        private MessageDetails oldDetails;

        public Parameters(User user, String folderName, Message oldmessage, MessageDetails oldDetails) {
            this.user = user;
            this.folderName = folderName;
            this.oldmessage = oldmessage;
            this.oldDetails = oldDetails;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public Message getOldmessage() {
            return oldmessage;
        }

        public void setOldmessage(Message oldmessage) {
            this.oldmessage = oldmessage;
        }

        public MessageDetails getOldDetails() {
            return oldDetails;
        }

        public void setOldDetails(MessageDetails oldDetails) {
            this.oldDetails = oldDetails;
        }
    }

    interface ToolBarUiBinder extends UiBinder<FlowPanel, ToolBarView> {
    }

    @SuppressWarnings("rawtypes")
    protected UiBinder getBinder() {
        return GWT.create(ToolBarUiBinder.class);
    }

    @SuppressWarnings("unchecked")
    public ToolBarView() {
        initWidget((Widget)getBinder().createAndBindUi(this));
        simplePopup.addStyleName(style.popupMenu());
        popup = new VerticalPanel();
        markRead = new Anchor("As Read");
        markUnread = new Anchor("As Unread");
        popup.addStyleName(style.toolBarMenu());
        markRead.addStyleName(style.activeIcon());
        markRead.addStyleName(style.listicon());
        markRead.addStyleName(style.read());
        markUnread.addStyleName(style.activeIcon());
        markUnread.addStyleName(style.listicon());
        markUnread.addStyleName(style.unread());
        popup.add(markRead);
        popup.add(markUnread);
        simplePopup.setWidget(popup);
    }

    @UiHandler("compose")
    public void handleCompose(ClickEvent e) {
        placeController.goTo(new ComposePlace("new").with(parameters));
    }
    
    @UiHandler("forward")
    public void handleForward(ClickEvent e) {
    	if(isEnabled(forward)){
            placeController.goTo(new ComposePlace("forward").with(parameters));
    	}
    }
    
    @UiHandler("replyAll")
    public void handleReplyAll(ClickEvent e) {
    	if(isEnabled(replyAll)){
    		placeController.goTo(new ComposePlace("replyAll").with(parameters));
    	}
    }
    
    @UiHandler("reply")
    public void handleReply(ClickEvent e) {
    	if(isEnabled(reply)){
    		placeController.goTo(new ComposePlace("reply").with(parameters));
    	}
    }
    
    @UiHandler("delete")
    public void handleDelete(ClickEvent e) {
    	if(isEnabled(delete)){
    		eventBus.fireEvent(new DeleteClickEvent());
    	}
    }
    
    @UiHandler("mark")
    public void handleMark(ClickEvent e) {
    	if(isEnabled(mark)){
	        Widget source = (Widget) e.getSource();
	        int left = source.getAbsoluteLeft();
	        int top = source.getAbsoluteTop() + source.getOffsetHeight();
	        simplePopup.setPopupPosition(left, top);
	        simplePopup.show();
    	}
    }
    
    @UiHandler("raw")
    public void handleRaw(ClickEvent e) {
    	if(isEnabled(raw)){
    		eventBus.fireEvent(new ShowRawEvent());
    	}
    }
    
    private boolean isEnabled(UIObject whichButton){
    	return !whichButton.getStyleName().contains(style.disabledButton());
    }

    @Override
    public HasClickHandlers getMark() {
        return mark;
    }

    @Override
    public HasClickHandlers getMarkRead() {
        return markRead;
    }

    @Override
    public HasClickHandlers getMarkUnread() {
        return markUnread;
    }

    @Override
    public HasClickHandlers getCompose() {
        return compose;
    }
    @Override
    public HasClickHandlers getReply() {
        return reply;
    }

    @Override
    public HasClickHandlers getReplyAll() {
        return replyAll;
    }

    @Override
    public HasClickHandlers getForward() {
        return forward;
    }
    @Override
    public PopupPanel getPopup() {
        return simplePopup;
    }

    @Override
    public HasClickHandlers getDelete() {
        return delete;
    }
    @Override
    public HasClickHandlers getRefresh() {
        return refresh;
    }
    @Override
    public void enableAllTools(boolean is) {
        this.enableSendingTools(is);
        this.enableDealingTools(is);
        this.enableUpdatingTools(is);
    }
    @Override
    public void enableSendingTools(boolean is) {
        if (is) {
            removeSendingDisableds();
        } else {
            addSendingDisableds();
        }
    }

    @Override
    public void enableDealingTools(boolean is) {
        if (is) {
            removeDealingDisableds();
        } else {
            addDealingDisableds();
        }
    }

    public void enableUpdatingTools(boolean is) {
    }

    private void addSendingDisableds() {
        reply.addStyleName(style.disabledButton());
        replyAllGroup.addStyleName(style.disabledButton());
        replyAll.addStyleName(style.disabledButton());
        forwardGroup.addStyleName(style.disabledButton());
        forward.addStyleName(style.disabledButton());
        replyAllTip.addStyleName(style.disabledButton());
        forwardTip.addStyleName(style.disabledButton());
        raw.addStyleName(style.disabledButton());
    }

    private void removeSendingDisableds() {
        reply.removeStyleName(style.disabledButton());
        replyAllGroup.removeStyleName(style.disabledButton());
        replyAll.removeStyleName(style.disabledButton());
        forwardGroup.removeStyleName(style.disabledButton());
        forward.removeStyleName(style.disabledButton());
        replyAllTip.removeStyleName(style.disabledButton());
        forwardTip.removeStyleName(style.disabledButton());
        raw.removeStyleName(style.disabledButton());
    }


    private void addDealingDisableds() {
        delete.addStyleName(style.disabledButton());
        mark.addStyleName(style.disabledButton());
    }

    private void removeDealingDisableds() {
        delete.removeStyleName(style.disabledButton());
        mark.removeStyleName(style.disabledButton());
    }

}
