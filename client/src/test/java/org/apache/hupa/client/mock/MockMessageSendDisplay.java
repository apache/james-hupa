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

package org.apache.hupa.client.mock;

import gwtupload.client.IUploader;

import org.apache.hupa.client.activity.ComposeActivity.Displayable;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class MockMessageSendDisplay implements Displayable {

    HasClickHandlers backClick = new MockWidget();
    HasText bccText = new MockWidget();
    HasText ccText = new MockWidget();
    HasText fromText = new MockWidget();
    HasHTML messageHtml = new MockWidget();

    HasClickHandlers sendClick = new MockWidget();
    HasText subjectText = new MockWidget();
    HasText toText = new MockWidget();
    Focusable editorFocus = new MockWidget();

    IUploader uploader = new MockUploader();

    public Widget asWidget() {
        return null;
    }

    public void fillContactList(Contact[] contacts) {
    }

    public HasClickHandlers getBackButtonClick() {
        return backClick;
    }

    public HasText getBccText() {
        return bccText;
    }

    public HasText getCcText() {
        return ccText;
    }

    public Focusable getEditorFocus() {
        return editorFocus;
    }

    public HasHTML getMessageHTML() {
        return messageHtml;
    }

    public HasClickHandlers getSendClick() {
        return sendClick;
    }

    public HasEnable getSendEnable() {
        return null;
    }

    public HasText getSubjectText() {
        return subjectText;
    }

    public HasText getToText() {
        return toText;
    }

    public IUploader getUploader() {
        return uploader;
    }

    public void refresh() {
    }

    public void setBccText(HasText bccText) {
        this.bccText = bccText;
    }

    public void setCcText(HasText ccText) {
        this.ccText = ccText;
    }

    public void setFromText(HasText fromText) {
        this.fromText = fromText;
    }

    public void setLoading(boolean loading) {
    }

    public void setMessageHTML(HasHTML messageHtml) {
        this.messageHtml = messageHtml;
    }

    public void setSubjectText(HasText subjectText) {
        this.subjectText = subjectText;
    }

    public void setToText(HasText toText) {
        this.toText = toText;
    }

    public boolean validate() {
        return true;
    }

    @Override
    public void showCc() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hideCc() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showBcc() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hideBcc() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showReply() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hideReply() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showFollowup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hideFollowup() {
        // TODO Auto-generated method stub

    }

    @Override
    public HasText getTo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasText getCc() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasText getBcc() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasText getSubject() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers getCancelClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers getCcClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers get_CcClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers getBccClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers get_BccClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers getReplyClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers get_ReplyClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers getFollowupClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasClickHandlers get_FollowupClick() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasText getMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListBox getFromList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void fillContactList(String[] contacts) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getFromText() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasFocusHandlers getAttachButton() {
        // TODO Auto-generated method stub
        return null;
    }

}
