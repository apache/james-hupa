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
import org.apache.hupa.widgets.ui.MultiValueSuggestArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class __ComposePanel extends Composite {

	@UiField protected SimplePanel composeHeaderContainer;
	@UiField protected SimpleLayoutPanel composeContentContainer;
	@UiField protected SimplePanel composeStatusContainer;

	public __ComposePanel() {
		initWidget(binder.createAndBindUi(this));
	}

	interface __ComposePanelUiBinder extends
			UiBinder<DockLayoutPanel, __ComposePanel> {
	}

	private static __ComposePanelUiBinder binder = GWT
			.create(__ComposePanelUiBinder.class);

	public AcceptsOneWidget getComposeHeaderContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				composeHeaderContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	public AcceptsOneWidget getComposeContentContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				composeContentContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	public AcceptsOneWidget getComposeStatusContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				composeStatusContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

=======
=======
import com.google.gwt.user.client.ui.Anchor;
>>>>>>> composing composing panel
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
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

	@UiField protected FlexTable headerTable;
	@UiField protected SimplePanel composeEditor;
	@UiField protected FlowPanel attach;
	@UiField protected Style style;
	private ListBox selectFrom;
	// we only need one instance for all suggestion-boxes
	private MultiValueSuggestArea to = new MultiValueSuggestArea(" ,@<>");
	private MultiValueSuggestArea cc = new MultiValueSuggestArea(to.getOracle());
	private MultiValueSuggestArea bcc = new MultiValueSuggestArea(to.getOracle());
	private TextBox subject = new TextBox();

	private Button sendButton;
	private Button saveButton;
	private Button cancelButton;

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

	interface Style extends CssResource {
		String hiddenInput();

		String add();

		String iconlink();

		String formlinks();

		String left();

		String right();

		String operation();
	}

	@Inject
	public ComposeView(HupaConstants constants, HupaMessages messages) {
		initWidget(binder.createAndBindUi(this));
		FlexCellFormatter cellFormatter = headerTable.getFlexCellFormatter();
		RowFormatter rowFormatter = headerTable.getRowFormatter();

		headerTable.setWidget(ROW_FROM, 0, new Label("From"));

		headerTable.setWidget(ROW_TO, 0, new Label("To"));
		headerTable.setWidget(ROW_CC, 0, new Label("Cc"));
		headerTable.setWidget(ROW_BCC, 0, new Label("Bcc"));
		headerTable.setWidget(ROW_REPLY, 0, new Label("Reply-To"));
		headerTable.setWidget(ROW_FOLLOWUP, 0, new Label("Followup-To"));
		headerTable.setWidget(ROW_SWITCH, 0, new Label(""));
		headerTable.setWidget(ROW_SUBJECT, 0, new Label("Subject"));

		selectFrom = new ListBox();
		sendButton = new Button("Send message");
		saveButton = new Button("Save as draft");
		cancelButton = new Button("Cancel");
		FlowPanel operationPanel = new FlowPanel();
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

		headerTable.setWidget(ROW_TO, 1, to);

		headerTable.setWidget(ROW_CC, 1, cc);
		headerTable.setWidget(ROW_BCC, 1, bcc);
		headerTable.setWidget(ROW_REPLY, 1, create());
		headerTable.setWidget(ROW_FOLLOWUP, 1, create());

		FlowPanel linkPanel = new FlowPanel();
		Anchor cc = new Anchor("Add Cc");
		cc.addStyleName(style.iconlink());
		cc.addStyleName(style.add());
		Anchor bcc = new Anchor("Add Bcc");
		bcc.addStyleName(style.iconlink());
		bcc.addStyleName(style.add());
		Anchor replyTo = new Anchor("Add Reply-To");
		replyTo.addStyleName(style.iconlink());
		replyTo.addStyleName(style.add());
		Anchor followupTo = new Anchor("Add Followup-To");
		followupTo.addStyleName(style.iconlink());
		followupTo.addStyleName(style.add());
		linkPanel.add(cc);
		linkPanel.add(bcc);
		linkPanel.add(replyTo);
		linkPanel.add(followupTo);
		headerTable.setWidget(ROW_SWITCH, 1, linkPanel);
		cellFormatter.addStyleName(ROW_SWITCH, 1, style.formlinks());
		headerTable.setWidget(ROW_SUBJECT, 1, subject);

		rowFormatter.addStyleName(ROW_CC, style.hiddenInput());
		rowFormatter.addStyleName(ROW_BCC, style.hiddenInput());
		rowFormatter.addStyleName(ROW_REPLY, style.hiddenInput());
		rowFormatter.addStyleName(ROW_FOLLOWUP, style.hiddenInput());

		SetFocusAction fAction = new SetFocusAction();
		AddStyleAction sAction = new AddStyleAction(HupaCSS.C_validate, 3000);
		validator = new DefaultValidationProcessor(new ValidationMessages(messages));
		validator.addValidators("cc", new EmailListValidator(getCcText()).addActionForFailure(sAction)
				.addActionForFailure(fAction));
		validator.addValidators("bcc", new EmailListValidator(getBccText()).addActionForFailure(sAction)
				.addActionForFailure(fAction));
		validator.addValidators("to", new EmailListValidator(getToText()).addActionForFailure(sAction)
				.addActionForFailure(fAction), new NotEmptyValidator(getToText()).addActionForFailure(sAction)
				.addActionForFailure(fAction));
		editor = new Editor();

		BaseUploadStatus uploadStatus = new BaseUploadStatus();
		uploadStatus.setCancelConfiguration(IUploadStatus.GMAIL_CANCEL_CFG);
		uploader = new MultiUploader(FileInputType.ANCHOR, uploadStatus);
		uploader.setServletPath(GWT.getModuleBaseURL() + SConsts.SERVLET_UPLOAD);
		uploader.avoidRepeatFiles(true);
		uploader.setI18Constants(constants);
		attach.add(uploader);
		composeEditor.add(editor);
	}

	@Override
	public HasText getToText() {
		return to;
	}

	@Override
	public HasText getCcText() {
		return cc;
	}

	@Override
	public HasText getBccText() {
		return bcc;
	}

	@Override
	public HasClickHandlers getSendClick() {
		return sendButton;
	}

	@Override
	public HasText getSubjectText() {
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
	public HasText getMessageText() {
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

	// TODO
	private TextArea create() {
		TextArea t = new TextArea();
		return t;
	}

	interface ComposeUiBinder extends UiBinder<DockLayoutPanel, ComposeView> {
	}

	private static ComposeUiBinder binder = GWT.create(ComposeUiBinder.class);

>>>>>>> preparing for composing panel
}
