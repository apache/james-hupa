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

import org.apache.hupa.client.activity.ComposeHeaderActivity;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;

public class ComposeHeaderView extends Composite implements
		ComposeHeaderActivity.Displayable {

	@UiField protected FlexTable headerTable;

	@UiField protected Style style;

	interface Style extends CssResource {
		String hiddenInput();

		String add();

		String iconlink();

		String formlinks();

		String left();

		String right();

		String operation();
	}

	private static final int ROW_FROM = 0;
	private static final int ROW_TO = 1;
	private static final int ROW_CC = 2;
	private static final int ROW_BCC = 3;
	private static final int ROW_REPLY = 4;
	private static final int ROW_FOLLOWUP = 5;
	private static final int ROW_SWITCH = 6;
	private static final int ROW_SUBJECT = 7;

	public ComposeHeaderView() {
		initWidget(binder.createAndBindUi(this));
		FlexCellFormatter cellFormatter = headerTable.getFlexCellFormatter();
		RowFormatter rowFormatter = headerTable.getRowFormatter();

		// Add some text
		// cellFormatter.setHorizontalAlignment(0, 1,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		headerTable.setWidget(ROW_FROM, 0, new Label("From"));

		headerTable.setWidget(ROW_TO, 0, new Label("To"));
		headerTable.setWidget(ROW_CC, 0, new Label("Cc"));
		headerTable.setWidget(ROW_BCC, 0, new Label("Bcc"));
		headerTable.setWidget(ROW_REPLY, 0, new Label("Reply-To"));
		headerTable.setWidget(ROW_FOLLOWUP, 0, new Label("Followup-To"));
		headerTable.setWidget(ROW_SWITCH, 0, new Label(""));
		headerTable.setWidget(ROW_SUBJECT, 0, new Label("Subject"));
		// cellFormatter.setColSpan(0, 0, 2);

		// Add a button that will add more rows to the table
		ListBox lb = new ListBox();
		lb.addItem("echowdx#googlemail.com");
		lb.addItem("bar");
		Button addRowButton = new Button("Send message");
		Button removeRowButton = new Button("Save as draft");
		Button cancel = new Button("Cancel");
		FlowPanel operationPanel = new FlowPanel();
		FlowPanel contactPanel = new FlowPanel();
		FlowPanel buttonPanel = new FlowPanel();

		contactPanel.add(lb);
		contactPanel.addStyleName(style.left());
		// buttonPanel.add(new Anchor("Edit identities"));
		buttonPanel.add(addRowButton);
		buttonPanel.add(removeRowButton);
		buttonPanel.add(cancel);
		buttonPanel.addStyleName(style.right());
		operationPanel.add(contactPanel);
		operationPanel.add(buttonPanel);
		operationPanel.addStyleName(style.operation());
		headerTable.setWidget(ROW_FROM, 1, operationPanel);

		TextArea to = new TextArea();
		to.setFocus(true);
		headerTable.setWidget(ROW_TO, 1, to);

		headerTable.setWidget(ROW_CC, 1, create());
		headerTable.setWidget(ROW_BCC, 1, create());
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
		headerTable.setWidget(ROW_SUBJECT, 1, create());

		rowFormatter.addStyleName(ROW_CC, style.hiddenInput());
		rowFormatter.addStyleName(ROW_BCC, style.hiddenInput());
		rowFormatter.addStyleName(ROW_REPLY, style.hiddenInput());
		rowFormatter.addStyleName(ROW_FOLLOWUP, style.hiddenInput());

		// cellFormatter
		// .setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		// Add two rows to start
		// addRow(headerTable);
		// addRow(headerTable);

		// Return the panel
		// headerTable.ensureDebugId("cwFlexTable");
	}

	private TextArea create() {
		TextArea t = new TextArea();
		return t;
	}

	interface ComposeHeaderUiBinder extends
			UiBinder<FlexTable, ComposeHeaderView> {
	}

	private static ComposeHeaderUiBinder binder = GWT
			.create(ComposeHeaderUiBinder.class);

>>>>>>> preparing for composing panel
}
