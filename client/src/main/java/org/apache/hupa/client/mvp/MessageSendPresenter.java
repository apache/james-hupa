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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationProcessor;
import eu.maydu.gwt.validation.client.actions.FocusAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;
import gwtupload.client.IUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStatusChangedHandler;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.apache.hupa.client.HupaCallback;
import org.apache.hupa.client.validation.EmailListValidator;
import org.apache.hupa.client.validation.NotEmptyValidator;
import org.apache.hupa.shared.Util;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.rpc.ForwardMessage;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.ReplyMessage;
import org.apache.hupa.shared.rpc.SendMessage;
import org.apache.hupa.widgets.ui.HasEnable;

import java.util.ArrayList;

/**
 * Presenter which handles the sending, reply, replay-all, forward of mails
 * 
 */
public class MessageSendPresenter extends WidgetPresenter<MessageSendPresenter.Display> {

    private DispatchAsync dispatcher;
    private ArrayList<MessageAttachment> attachments = new ArrayList<MessageAttachment>();
    private Type type = Type.NEW;
    private IMAPFolder folder;
    private Message oldmessage;
    private ValidationMessages vMessages = new ValidationMessages();
    private ValidationProcessor validator = new DefaultValidationProcessor(vMessages);
    @SuppressWarnings("unused")
    private MessageDetails oldDetails;

    private OnFinishUploaderHandler onFinishUploadHandler = new OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                String name = uploader.getInputName();
                MessageAttachment attachment = new MessageAttachment();
                attachment.setName(name);
                attachments.add(attachment);
                display.getSendEnable().setEnabled(true);
            }
        }
    };

    private OnStatusChangedHandler onStatusChangedHandler = new OnStatusChangedHandler() {
        public void onStatusChanged(IUploader uploader) {
            Status stat = display.getUploader().getStatus();
            if (stat == Status.INPROGRESS)
                display.getSendEnable().setEnabled(false);
            else
                display.getSendEnable().setEnabled(true);
        }
    };

    private OnCancelUploaderHandler onCancelUploadHandler = new OnCancelUploaderHandler() {
        public void onCancel(IUploader uploader) {
            for (MessageAttachment attachment : attachments) {
                if (attachment.getName().equals(uploader.getInputName()))
                    attachments.remove(attachment);
            }
        }
    };

    @Inject
    public MessageSendPresenter(Display display, EventBus eventBus, DispatchAsync dispatcher) {
        super(display, eventBus);
        this.dispatcher = dispatcher;

        FocusAction fAction = new FocusAction();
        validator.addValidators("cc", new EmailListValidator(display.getCcText()).addActionForFailure(new StyleAction("hupa-validationErrorBorder")).addActionForFailure(fAction));
        validator.addValidators("bcc", new EmailListValidator(display.getBccText()).addActionForFailure(new StyleAction("hupa-validationErrorBorder")).addActionForFailure(fAction));
        validator.addValidators("to", new EmailListValidator(display.getToText()).addActionForFailure(new StyleAction("hupa-validationErrorBorder")).addActionForFailure(fAction),
                new NotEmptyValidator(display.getToText()).addActionForFailure(new StyleAction("hupa-validationErrorBorder")).addActionForFailure(fAction));
    }

    /**
     * The Type for which the SendPresenter will get used
     * 
     */
    public enum Type {
        /**
         * Compose new Mail
         */
        NEW,
        /**
         * Reply to Mail
         */
        REPLY,
        /**
         * Reply-all to mail
         */
        REPLY_ALL,
        /**
         * Forward mail
         */
        FORWARD
    }

    public interface Display extends WidgetDisplay {
        public HasText getFromText();

        public HasText getToText();

        public HasText getCcText();

        public HasText getBccText();

        public HasText getSubjectText();

        public HasHTML getMessageHTML();
        
        public Focusable getEditorFocus();

        public HasClickHandlers getSendClick();

        public HasEnable getSendEnable();

        public IUploader getUploader();

        public HasClickHandlers getBackButtonClick();
        
        public void refresh();

        public void setLoading(boolean loading);
    }

    @Override
    protected void onBind() {
        registerHandler(eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {

            public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
                reset();
            }

        }));
        registerHandler(eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {

            public void onFolderSelectionEvent(FolderSelectionEvent event) {
                reset();
            }

        }));

        registerHandler(display.getSendClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (validator.validate() == false) {
                    return;
                }
                SMTPMessage message = new SMTPMessage();

                message.setFrom(display.getFromText().getText());

                ArrayList<String> to = new ArrayList<String>();
                String[] toRaw = display.getToText().getText().split("[,;]+");
                if (toRaw != null) {
                    for (int i = 0; i < toRaw.length; i++) {
                        String toRecip = toRaw[i].trim();
                        if (toRecip.length() > 0) {
                            to.add(toRaw[i].trim());
                        }
                    }
                }
                message.setTo(to);

                ArrayList<String> cc = new ArrayList<String>();
                String[] ccRaw = display.getCcText().getText().split("[,;]+");
                if (ccRaw != null) {
                    for (int i = 0; i < ccRaw.length; i++) {
                        String ccRecip = ccRaw[i].trim();
                        if (ccRecip.length() > 0) {
                            cc.add(ccRaw[i].trim());
                        }
                    }
                }
                message.setCc(cc);

                message.setSubject(display.getSubjectText().getText());
                message.setText(display.getMessageHTML().getHTML());

                message.setMessageAttachments(attachments);

                // TODO: good handling of error messages, and use an error
                // widget instead of Window.alert

                if (type.equals(Type.NEW)) {
                    display.setLoading(true);

                    dispatcher.execute(new SendMessage(message), new HupaCallback<GenericResult>(dispatcher, eventBus) {
                        public void callback(GenericResult result) {
                            if (result.isSuccess()) {
                                eventBus.fireEvent(new SentMessageEvent());
                                reset();
                            } else {
                                Window.alert(result.getMessage());
                            }
                            display.setLoading(false);

                        }
                    });
                } else if (type.equals(Type.FORWARD)) {
                    display.setLoading(true);

                    dispatcher.execute(new ForwardMessage(message, folder, oldmessage.getUid()), new HupaCallback<GenericResult>(dispatcher, eventBus) {
                        public void callback(GenericResult result) {
                            if (result.isSuccess()) {
                                eventBus.fireEvent(new SentMessageEvent());
                                reset();
                            } else {
                                Window.alert(result.getMessage());
                            }
                            display.setLoading(false);

                        }
                    });
                } else if (type.equals(Type.REPLY) || type.equals(Type.REPLY_ALL)) {
                    display.setLoading(true);

                    dispatcher.execute(new ReplyMessage(message, folder, oldmessage.getUid()), new HupaCallback<GenericResult>(dispatcher, eventBus) {
                        public void callback(GenericResult result) {
                            if (result.isSuccess()) {
                                eventBus.fireEvent(new SentMessageEvent());
                                reset();
                            } else {
                                Window.alert(result.getMessage());
                            }
                            display.setLoading(false);
                        }
                    });
                }
            }
        }));

        registerHandler(display.getBackButtonClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.fireEvent(new BackEvent());
            }

        }));

        display.getUploader().addOnStatusChangedHandler(onStatusChangedHandler);
        display.getUploader().addOnFinishUploadHandler(onFinishUploadHandler);
        display.getUploader().addOnCancelUploadHandler(onCancelUploadHandler);

        reset();
    }

    /**
     * Reset everything
     */
    private void reset() {
        display.getUploader().reset();
        display.getBccText().setText("");
        display.getCcText().setText("");
        display.getToText().setText("");
        display.getSubjectText().setText("");
        attachments.clear();
        folder = null;
        oldmessage = null;
        type = Type.NEW;
    }

    @Override
    protected void onUnbind() {
        // cancel the current upload when unbinding
        display.getUploader().cancel();
    }

    /**
     * Bind the given values to this presenter
     * 
     * @param user
     *            the user
     * @param folder
     *            the currently selected folder
     * @param oldmessage
     *            the oldmessage ( if there is any)
     * @param oldDetails
     *            the olddetails ( if there are any)
     * @param type
     *            the type
     */
    public void revealDisplay(User user, IMAPFolder folder, Message oldmessage, MessageDetails oldDetails, String mailto, Type type) {

        this.reset();
        this.oldmessage = oldmessage;
        this.oldDetails = oldDetails;
        this.folder = folder;
        this.type = type;
        
        display.getFromText().setText(user.getName());

        if (type.equals(Type.FORWARD)) {
            if (!oldmessage.getSubject().toLowerCase().startsWith("fwd:"))
                display.getSubjectText().setText("Fwd: " + oldmessage.getSubject());
        } else if (type.equals(Type.REPLY) || type.equals(Type.REPLY_ALL)) {
            if (!oldmessage.getSubject().toLowerCase().startsWith("re:"))
                display.getSubjectText().setText("Re: " + oldmessage.getSubject());

            if (oldmessage.getReplyto() != null) {
                display.getToText().setText(oldmessage.getReplyto());
            } else if (type.equals(Type.REPLY)) {
                display.getToText().setText(oldmessage.getFrom());
            } else {
                oldmessage.getCc().remove(user.getName());
                display.getCcText().setText(Util.listToString(oldmessage.getCc()));
                oldmessage.getTo().remove(user.getName());
                display.getToText().setText(Util.listToString(oldmessage.getTo()));
            }
        }

        display.getMessageHTML().setHTML(wrapMessage(oldmessage, oldDetails, type));

        if (mailto != null)
            display.getToText().setText(mailto);
        
        display.refresh();
        
        firePresenterChangedEvent();
        
        revealDisplay();
        
        display.getEditorFocus().setFocus(true);
    }

    public void revealDisplay(User user, IMAPFolder folder, Message oldmessage, MessageDetails oldDetails, Type type) {
        this.revealDisplay(user, folder, oldmessage, oldDetails, null, type);
    }

    public void revealDisplay(User user, String mailto) {
        revealDisplay(user, null, null, null, mailto, Type.NEW);
    }

    /**
     * Bind the given user to the presenter
     * 
     * @param user
     */
    public void revealDisplay(User user) {
        revealDisplay(user, null, null, null, null, Type.NEW);
    }

    @Override
    protected void onRevealDisplay() {
    }
    
    private static String generateHeader(Message message, Type type) {
        String ret = "<br>";
        if (message == null)
            return ret;
        if (type.equals(Type.FORWARD)) {
            ret += "--------- Forwarded message --------- <br>";
            ret += "From: " + message.getFrom().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "<br>";
            ret += "Date: " + message.getReceivedDate() + "<br>";
            ret += "Subject: " + message.getSubject() + "<br>";
            ArrayList<String> to = new ArrayList<String>();
            to.addAll(message.getTo());
            to.addAll(message.getCc());
            ret += "To: " + Util.listToString(to).replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "<br>";
        } else if (type.equals(Type.REPLY) || type.equals(Type.REPLY_ALL)) {
            ret += "On " + message.getReceivedDate();
            ret += ", " + message.getFrom().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            ret += ". wrote:<br>";
        }
        return ret + "<br>";
    }

    public static String wrapMessage(Message message, MessageDetails details, Type type) {
        String ret = "";
        if (message != null) {
            ret += generateHeader(message, type);
        }
        if (details != null && details.getText() != null && !details.getText().isEmpty()) {
            ret += "<blockquote style='border-left: 1px solid rgb(204, 204, 204); margin: 0pt 0pt 0pt 0.8ex; padding-left: 1ex;'>";
            ret += details.getText();
            ret += "</blockquote>";
        }
        return ret;
    }

}
