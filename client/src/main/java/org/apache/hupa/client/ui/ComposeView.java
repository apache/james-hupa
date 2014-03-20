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

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaMessages;
import org.apache.hupa.client.activity.ComposeActivity;
import org.apache.hupa.client.validation.AddStyleAction;
import org.apache.hupa.client.validation.EmailListValidator;
import org.apache.hupa.client.validation.NotEmptyValidator;
import org.apache.hupa.client.validation.SetFocusAction;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.widgets.editor.Editor;
import org.apache.hupa.widgets.editor.Toolbar;
import org.apache.hupa.widgets.ui.MultiValueSuggestArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationProcessor;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;
import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

public class ComposeView extends Composite implements ComposeActivity.Displayable {

    @UiField protected DockLayoutPanel thisPanel;
    @UiField protected SimplePanel header;
    @UiField protected FlexTable headerTable;
    @UiField protected SimplePanel editorToolBar;
    @UiField protected SimplePanel composeEditor;
    @UiField protected FlowPanel attach;
    @UiField protected FocusPanel attachButton;
    @UiField protected Style style;
    private ListBox selectFrom;

    /* we only need one instance for all suggestion-boxes */
    private MultiValueSuggestArea toSuggest = new MultiValueSuggestArea(" ,@<>");
    private MultiValueSuggestArea ccSuggest = new MultiValueSuggestArea(toSuggest.getOracle());
    private MultiValueSuggestArea bccSuggest = new MultiValueSuggestArea(toSuggest.getOracle());
    private TextBox subject = new TextBox();

    private Button sendButton;
    private Button saveButton;
    private Button cancelButton;

    private Anchor addCcButton;
    private Anchor addBccButton;
    private Anchor addReplyButton;
    private Anchor addFollowupButton;

    private Anchor _CcButton = new Anchor("x");
    private Anchor _BccButton = new Anchor("x");
    private Anchor _ReplyButton = new Anchor("x");
    private Anchor _FollowupButton = new Anchor("x");

    private FlexCellFormatter cellFormatter;
    private RowFormatter rowFormatter;

    private Editor editor;

    private ValidationProcessor validator;
    private MultiUploader uploader = null;

    private static final int ROW_FROM = 0;
    private static final int ROW_TO = 1;
    private static final int ROW_CC = 2;
    private static final int ROW_BCC = 3;
    private static final int ROW_REPLY = 4;
    private static final int ROW_FOLLOWUP = 5;
    private static final int ROW_SWITCH = 6;
    private static final int ROW_SUBJECT = 7;

    private static final int ROW_HEIGHT = 33;

    interface Style extends CssResource {
        String show();
        String hide();
        String iconlink();
        String add();
        String cancel();
        String formlinks();
        String left();
        String right();
        String operation();
    }

    @Inject
    public ComposeView(HupaConstants constants, HupaMessages messages) {
        initWidget(binder.createAndBindUi(this));
        initFormatters();
        createFirstColumn();
        createSecondColumn();
        bindValidators(messages);
        createEditor(constants);
    }

    private void createEditor(HupaConstants constants) {
        editor = new Editor();
        BaseUploadStatus uploadStatus = new BaseUploadStatus();
        uploadStatus.setCancelConfiguration(IUploadStatus.DEFAULT_CANCEL_CFG);

        uploader = new MultiUploader(FileInputType.CUSTOM.with(attachButton), uploadStatus);
        uploader.setServletPath(GWT.getModuleBaseURL() + SConsts.SERVLET_UPLOAD);
        uploader.avoidRepeatFiles(true);
        uploader.setI18Constants(constants);
        attach.add(uploader);

        Toolbar toolbar = new Toolbar(editor.getArea(), constants);
        toolbar.ensureDebugId("hupa-editor-toolbar");
        editorToolBar.add(toolbar);
        composeEditor.add(editor);
    }

    private void bindValidators(HupaMessages messages) {
        SetFocusAction fAction = new SetFocusAction();
        AddStyleAction sAction = new AddStyleAction(HupaCSS.C_validate, 3000);
        validator = new DefaultValidationProcessor(new ValidationMessages(messages));
        validator.addValidators("cc",
                new EmailListValidator(getCc()).addActionForFailure(sAction).addActionForFailure(fAction));
        validator.addValidators("bcc", new EmailListValidator(getBcc()).addActionForFailure(sAction)
                .addActionForFailure(fAction));
        validator.addValidators("to",
                new EmailListValidator(getTo()).addActionForFailure(sAction).addActionForFailure(fAction),
                new NotEmptyValidator(getTo()).addActionForFailure(sAction).addActionForFailure(fAction));
    }

    private void initFormatters() {
        cellFormatter = headerTable.getFlexCellFormatter();
        rowFormatter = headerTable.getRowFormatter();
    }

    private void createSecondColumn() {
        selectFrom = new ListBox();
        sendButton = new Button("Send message");
        saveButton = new Button("Save as draft");
        cancelButton = new Button("Cancel");
        sendButton.addStyleName(style.hide());
        saveButton.addStyleName(style.hide());
        cancelButton.addStyleName(style.hide());
        HorizontalPanel operationPanel = new HorizontalPanel();
        FlowPanel contactPanel = new FlowPanel();
        FlowPanel buttonPanel = new FlowPanel();
        contactPanel.add(selectFrom);
        contactPanel.addStyleName(style.left());
        // buttonPanel.add(new Anchor("Edit identities"));
        buttonPanel.add(sendButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.addStyleName(style.right());
        operationPanel.add(contactPanel);
        operationPanel.add(buttonPanel);
        operationPanel.addStyleName(style.operation());
        headerTable.setWidget(ROW_FROM, 1, operationPanel);
        headerTable.setWidget(ROW_TO, 1, toSuggest);
        headerTable.setWidget(ROW_CC, 1, ccSuggest);
        headerTable.setWidget(ROW_BCC, 1, bccSuggest);
        headerTable.setWidget(ROW_REPLY, 1, create());
        headerTable.setWidget(ROW_FOLLOWUP, 1, create());
        headerTable.setWidget(ROW_SWITCH, 1, createOpsPanel());
        cellFormatter.addStyleName(ROW_SWITCH, 1, style.formlinks());
        headerTable.setWidget(ROW_SUBJECT, 1, subject);
        rowFormatter.addStyleName(ROW_CC, style.hide());
        rowFormatter.addStyleName(ROW_BCC, style.hide());
        rowFormatter.addStyleName(ROW_REPLY, style.hide());
        rowFormatter.addStyleName(ROW_FOLLOWUP, style.hide());
    }

    private void createFirstColumn() {
        headerTable.setWidget(ROW_FROM, 0, new Label("From"));
        headerTable.setWidget(ROW_TO, 0, new Label("To"));
        headerTable.setWidget(ROW_CC, 0, createCell("Cc", _CcButton));
        headerTable.setWidget(ROW_BCC, 0, createCell("Bcc", _BccButton));
        headerTable.setWidget(ROW_REPLY, 0, createCell("Reply", _ReplyButton));
        headerTable.setWidget(ROW_FOLLOWUP, 0, createCell("Follow", _FollowupButton));
        headerTable.setWidget(ROW_SWITCH, 0, new Label(""));
        headerTable.setWidget(ROW_SUBJECT, 0, new Label("Subject"));
    }

    private FlowPanel createCell(String labelText, Anchor w) {
        FlowPanel ccCell = new FlowPanel();
        Label cc = new Label(labelText);
        cc.addStyleName(style.left());
        ccCell.add(cc);
        w.addStyleName(style.iconlink());
        w.addStyleName(style.cancel());
        ccCell.add(w);
        return ccCell;
    }

    private FlowPanel createOpsPanel() {
        FlowPanel addOpsPanel = new FlowPanel();
        addCcButton = new Anchor("Add Cc");
        addCcButton.addStyleName(style.iconlink());
        addCcButton.addStyleName(style.add());
        addBccButton = new Anchor("Add Bcc");
        addBccButton.addStyleName(style.iconlink());
        addBccButton.addStyleName(style.add());
        addReplyButton = new Anchor("Add Reply-To");
        addReplyButton.addStyleName(style.iconlink());
        addReplyButton.addStyleName(style.add());
        addFollowupButton = new Anchor("Add Followup-To");
        addFollowupButton.addStyleName(style.iconlink());
        addFollowupButton.addStyleName(style.add());
        addOpsPanel.add(addCcButton);
        addOpsPanel.add(addBccButton);
        // TODO the other operations
        // addOpsPanel.add(addReplyButton);
        // addOpsPanel.add(addFollowupButton);
        return addOpsPanel;
    }

    HasText emptyText = new HasText() {
        @Override
        public String getText() {
            return "";
        }
        @Override
        public void setText(String text) {
        }
    };

    @Override
    public HasText getTo() {
        return toSuggest;
    }

    @Override
    public HasText getCc() {
        if (isShowing(ROW_CC))
            return ccSuggest;
        else
            return emptyText;
    }

    @Override
    public HasText getBcc() {
        if (isShowing(ROW_BCC))
            return bccSuggest;
        else
            return emptyText;
    }

    @Override
    public HasClickHandlers getSendClick() {
        return sendButton;
    }

    @Override
    public HasClickHandlers getCancelClick() {
        return cancelButton;
    }

    @Override
    public HasClickHandlers getCcClick() {
        return addCcButton;
    }

    @Override
    public HasClickHandlers get_CcClick() {
        return _CcButton;
    }

    @Override
    public HasClickHandlers getBccClick() {
        return addBccButton;
    }

    @Override
    public HasClickHandlers get_BccClick() {
        return _BccButton;
    }

    @Override
    public HasClickHandlers getReplyClick() {
        return addReplyButton;
    }

    @Override
    public HasClickHandlers get_ReplyClick() {
        return _ReplyButton;
    }

    @Override
    public HasClickHandlers getFollowupClick() {
        return addFollowupButton;
    }

    @Override
    public HasClickHandlers get_FollowupClick() {
        return _FollowupButton;
    }

    @Override
    public HasFocusHandlers getAttachButton() {
        return this.attachButton;
    }

    @Override
    public HasText getSubject() {
        return subject;
    }

    @Override
    public boolean validate() {
        return this.validator.validate();
    }

    @Override
    public String getFromText() {
        // TODO hardcode to the first identifier
        return selectFrom.getItemText(0);
    }

    @Override
    public ListBox getFromList() {
        return selectFrom;
    }

    @Override
    public HasText getMessage() {
        return editor;
    }

    @Override
    public HasHTML getMessageHTML() {
        return editor;
    }

    @Override
    public IUploader getUploader() {
        return uploader;
    }

    @Override
    public void showCc() {
        showRow(ROW_CC);
        addCcButton.setVisible(false);
    }

    @Override
    public void hideCc() {
        hideRow(ROW_CC);
        ccSuggest.setText("");
        addCcButton.setVisible(true);
    }

    @Override
    public void showBcc() {
        showRow(ROW_BCC);
        addBccButton.setVisible(false);
    }

    @Override
    public void hideBcc() {
        hideRow(ROW_BCC);
        bccSuggest.setText("");
        addBccButton.setVisible(true);
    }

    @Override
    public void showReply() {
        showRow(ROW_REPLY);
    }

    @Override
    public void hideReply() {
        hideRow(ROW_REPLY);
    }

    @Override
    public void showFollowup() {
        showRow(ROW_FOLLOWUP);
    }

    @Override
    public void hideFollowup() {
        hideRow(ROW_FOLLOWUP);
    }

    @Override
    public void fillContactList(String[] contacts) {
        toSuggest.fillOracle(contacts);
    }

    private void showRow(int row) {
        if (isShowing(row)) {
            return;
        }
        rowFormatter.removeStyleName(row, style.hide());
        rowFormatter.addStyleName(row, style.show());
        thisPanel.setWidgetSize(header, thisPanel.getWidgetSize(header) + ROW_HEIGHT);
    }

    private void hideRow(int row) {
        if (isHiding(row)) {
            return;
        }
        rowFormatter.removeStyleName(row, style.show());
        rowFormatter.addStyleName(row, style.hide());
        thisPanel.setWidgetSize(header, thisPanel.getWidgetSize(header) - ROW_HEIGHT);
    }

    private boolean isShowing(int row) {
        return rowFormatter.getStyleName(row).contains(style.show());
    }

    private boolean isHiding(int row) {
        return rowFormatter.getStyleName(row).contains(style.hide());
    }

    // TODO
    private TextArea create() {
        TextArea t = new TextArea();
        return t;
    }

    interface ComposeUiBinder extends UiBinder<DockLayoutPanel, ComposeView> {
    }

    private static ComposeUiBinder binder = GWT.create(ComposeUiBinder.class);

}
