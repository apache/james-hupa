package org.apache.hupa.client.mock;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import gwtupload.client.IUploader;

import org.apache.hupa.client.mvp.MessageSendPresenter.Display;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;
import org.apache.hupa.widgets.ui.HasEnable;

public class MockMessageSendDisplay implements Display {

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

    public HasText getFromText() {
        return fromText;
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

}
