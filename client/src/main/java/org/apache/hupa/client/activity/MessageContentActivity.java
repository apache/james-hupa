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

import java.util.List;
import java.util.logging.Logger;

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.place.MessagePlace.TokenWrapper;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.ui.ToolBarView.Parameters;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.MailHeader;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.events.MailToEvent;
import org.apache.hupa.shared.events.MessageViewEvent;
import org.apache.hupa.shared.events.RefreshFoldersEvent;
import org.apache.hupa.shared.events.RefreshFoldersEventHandler;
import org.apache.hupa.shared.events.ShowRawEvent;
import org.apache.hupa.shared.events.ShowRawEventHandler;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessageContentActivity extends AppBaseActivity {

    private static final Logger log = Logger.getLogger(MessageContentActivity.class.getName());

    @Inject private Displayable display;
    @Inject private ToolBarActivity.Displayable toolBar;

    private String folder;
    private String uid;
    private MessageDetails details;

    public MessageContentActivity() {
       exportJSMethods(this);
    }

    @Override
    public void start(AcceptsOneWidget container, final EventBus eventBus) {
        bindTo(eventBus);
        if (isUidSet()) {
            hc.showTopLoading("Loading... ");
            display.clearContent();
            GetMessageDetailsRequest req = rf.messageDetailsRequest();
            GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
            final ImapFolder f = req.create(ImapFolder.class);
            f.setFullName(folder);
            action.setFolder(f);
            action.setUid(Long.parseLong(uid));

            final String id = uid;
            req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
                @Override
                public void onSuccess(GetMessageDetailsResult response) {
                    if (!id.equals(uid)) {
                        return;
                    }
                    hc.hideTopLoading();
                    eventBus.fireEvent(new MessageViewEvent(response.getMessageDetails()));

                    details = response.getMessageDetails();
                    display.fillMessageContent(details.getText(), false);

                    List<MessageAttachment> attaches = details.getMessageAttachments();
                    if (attaches != null && !attaches.isEmpty()) {
                        display.setAttachments(attaches, folder, Long.parseLong(uid));
                    }
                }

                @Override
                public void onFailure(ServerFailure error) {
                    hc.hideTopLoading();
                    toolBar.enableAllTools(false);
                    if (error.isFatal()) {
                        hc.showNotice(error.getMessage(), 10000);
                    }
                }
            });
        }
        container.setWidget(display.asWidget());
    }

    private void bindTo(EventBus eventBus) {
        eventBus.addHandler(ShowRawEvent.TYPE, new ShowRawEventHandler() {
            @Override
            public void onShowRaw(ShowRawEvent event) {
                String message_url = GWT.getModuleBaseURL() + SConsts.SERVLET_SOURCE + "?" + SConsts.PARAM_UID + "="
                        + uid + "&" + SConsts.PARAM_FOLDER + "=" + folder;
                Window.open(message_url, "_blank", "");
            }
        });
        eventBus.addHandler(RefreshFoldersEvent.TYPE, new RefreshFoldersEventHandler() {
            public void onRefreshEvent(RefreshFoldersEvent event) {
                display.clearContent();
            }
        });
    }

    private boolean isUidSet() {
        return uid != null && uid.matches("\\d+");
    }

    public interface Displayable extends IsWidget {
        void clearContent();
        void setAttachments(List<MessageAttachment> attachements, String folder, long uid);
        HasHTML getMessageHTML();
        void fillMessageContent(String messageDetail, boolean isEditable);
    }

    public Activity with(TokenWrapper tokenWrapper) {
        folder = tokenWrapper.getFolder();
        uid = tokenWrapper.getUid();
        return this;
    }

    public void openLink(String url) {
        Window.open(url, "_blank", "");
    }

    public void mailTo(String mailto) {
        pc.goTo(new ComposePlace("new").with(new Parameters(null, null, null, null)));
        eventBus.fireEvent(new MailToEvent(mailto));
    }

    private String getHeader(String key) {
        for (MailHeader h : details.getMailHeaders()) {
            if (h.getName().equals(key)) {
                return h.getValue();
            }
        }
        return null;
    }

    private boolean isSenderMessage() {
        String from = getHeader("From");
        return from != null && from.contains(HupaController.user.getName())
              || folder.equals(HupaController.user.getSettings().getSentFolderName());
    }

    protected native void exportJSMethods(MessageContentActivity activity)
    /*-{
       $wnd.openLink = function(url) {
           try {
               activity.@org.apache.hupa.client.activity.MessageContentActivity::openLink(Ljava/lang/String;) (url);
           } catch(e) {}
           return false;
       };
       $wnd.mailTo = function(mail) {
           try {
               activity.@org.apache.hupa.client.activity.MessageContentActivity::mailTo(Ljava/lang/String;) (mail);
           } catch(e) {}
           return false;
       };
   }-*/;
}
