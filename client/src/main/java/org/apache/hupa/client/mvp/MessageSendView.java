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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationProcessor;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaMessages;
import org.apache.hupa.client.validation.AddStyleAction;
import org.apache.hupa.client.validation.EmailListValidator;
import org.apache.hupa.client.validation.NotEmptyValidator;
import org.apache.hupa.client.validation.SetFocusAction;
import org.apache.hupa.client.widgets.CommandsBar;
import org.apache.hupa.client.widgets.EnableButton;
import org.apache.hupa.client.widgets.MessageHeaders;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;
import org.apache.hupa.widgets.editor.Editor;
import org.apache.hupa.widgets.ui.EnableHyperlink;
import org.apache.hupa.widgets.ui.HasEnable;
import org.apache.hupa.widgets.ui.Loading;
import org.apache.hupa.widgets.ui.MultiValueSuggestArea;

/**
 * View which displays a form which handle sending of mails
 * 
 *
 */
public class MessageSendView extends Composite implements MessageSendPresenter.Display {

    final VerticalPanel sendContainer = new VerticalPanel();
    
    private MessageHeaders headers;
    
    private Editor editor;
    private CommandsBar buttonsBar = new CommandsBar();

    private Label from = new Label();
    
    private MultiValueSuggestArea to = new MultiValueSuggestArea(" ,@<>");
    // we only need one instance for all suggestion-boxes
    private MultiValueSuggestArea cc = new MultiValueSuggestArea(to.getOracle());
    private MultiValueSuggestArea bcc = new MultiValueSuggestArea(to.getOracle());
    
    private TextBox subject = new TextBox();
    private MultiUploader uploader = null;
    
    private EnableButton sendButton;
    private EnableHyperlink backButton;
    private Loading loading;
    
    private ValidationProcessor validator;

    @Inject
    public MessageSendView(HupaConstants constants, HupaMessages messages) {
        
        sendButton = new EnableButton(constants.sendButton());
        backButton = new EnableHyperlink(constants.backButton(),"");
        headers = new MessageHeaders(constants);
        loading = new Loading(constants.loading());
        editor = new Editor(constants);
        
        BaseUploadStatus uploadStatus = new BaseUploadStatus();
        uploadStatus.setCancelConfiguration(IUploadStatus.GMAIL_CANCEL_CFG);
        uploader = new MultiUploader(FileInputType.ANCHOR, uploadStatus);
        uploader.setServletPath(GWT.getModuleBaseURL() + SConsts.SERVLET_UPLOAD);
        uploader.avoidRepeatFiles(true);
        uploader.setI18Constants(constants);        
        
        sendContainer.addStyleName(HupaCSS.C_msgsend_container);
        
        buttonsBar.add(sendButton);
        buttonsBar.add(loading);
        buttonsBar.add(backButton);
        
        sendContainer.add(headers);
        sendContainer.add(buttonsBar);

        sendContainer.add(editor);

        loading.hide();

        initWidget(sendContainer);
        
        SetFocusAction fAction = new SetFocusAction();
        AddStyleAction sAction = new AddStyleAction(HupaCSS.C_validate, 3000);
        validator = new DefaultValidationProcessor(new ValidationMessages(messages));
        validator.addValidators("cc", 
                new EmailListValidator(getCcText()).addActionForFailure(sAction).addActionForFailure(fAction));
        validator.addValidators("bcc", 
                new EmailListValidator(getBccText()).addActionForFailure(sAction).addActionForFailure(fAction));
        validator.addValidators("to", 
                new EmailListValidator(getToText()).addActionForFailure(sAction).addActionForFailure(fAction),
                new NotEmptyValidator(getToText()).addActionForFailure(sAction).addActionForFailure(fAction));

    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    public Widget asWidget() {
        return this;
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#setLoading(boolean)
     */ 
    public void setLoading(boolean load) {
        if (load) {
            loading.show();
            sendButton.setEnabled(false);
            backButton.setEnabled(false);
        } else {
            loading.hide();
            sendButton.setEnabled(true);
            backButton.setEnabled(true);
        }

    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getBccText()
     */
    public HasText getBccText() {
        return bcc;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getCcText()
     */
    public HasText getCcText() {
        return cc;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getFromText()
     */
    public HasText getFromText() {
        return from;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getMessageText()
     */
    public HasText getMessageText() {
        return editor;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getSubjectText()
     */
    public HasText getSubjectText() {
        return subject;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getToText()
     */
    public HasText getToText() {
        return to;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getSendClick()
     */
    public HasClickHandlers getSendClick() {
        return sendButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getUploader()
     */
    public IUploader getUploader() {
        return uploader;
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getSendEnable()
     */
    public HasEnable getSendEnable() {
        return sendButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getBackButtonClick()
     */
    public HasClickHandlers getBackButtonClick() {
        return backButton;
    }

    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getMessageHTML()
     */
    public HasHTML getMessageHTML() {
        return editor;
    }
    
    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#getEditorFocus()
     */
    public Focusable getEditorFocus() {
        return editor;
    }

    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.MessageSendPresenter.Display#refresh()
     */
    public void refresh() {
        if (to.getText().length() == 0)
            to.setText(" ");
        headers.setValues(from, to, cc, bcc, subject, uploader);
    }
    
    public void fillContactList(Contact[] contacts){
        to.fillOracle(contacts);
    }

    public boolean validate() {
        return validator.validate();
    }

}
