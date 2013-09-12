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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class __ComposePanel extends Composite {

	@UiField FlexTable headerTable;

	public __ComposePanel() {
		initWidget(binder.createAndBindUi(this));
		FlexCellFormatter cellFormatter = headerTable.getFlexCellFormatter();
		headerTable.addStyleName("cw-FlexTable");
		headerTable.setWidth("32em");
		headerTable.setCellSpacing(5);
		headerTable.setCellPadding(3);

		// Add some text
		cellFormatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.setHTML(0, 0, "cwFlexTableDetails");
		cellFormatter.setColSpan(0, 0, 2);

		// Add a button that will add more rows to the table
		Button addRowButton = new Button("cwFlexTableAddRow");
		addRowButton.addStyleName("sc-FixedWidthButton");

		Button removeRowButton = new Button("cwFlexTableRemoveRow");
		removeRowButton.addStyleName("sc-FixedWidthButton");
		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.setStyleName("cw-FlexTable-buttonPanel");
		buttonPanel.add(addRowButton);
		buttonPanel.add(removeRowButton);
		headerTable.setWidget(0, 1, buttonPanel);
		cellFormatter
				.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		// Add two rows to start
		addRow(headerTable);
		addRow(headerTable);

		// Return the panel
		headerTable.ensureDebugId("cwFlexTable");
	}

	/**
	 * Add a row to the flex table.
	 */
	private void addRow(FlexTable flexTable) {
		int numRows = flexTable.getRowCount();
		flexTable.setWidget(numRows, 0, new HTML("logo"));
		flexTable.setWidget(numRows, 1, new HTML("logo"));
		flexTable.getFlexCellFormatter().setRowSpan(0, 1, numRows + 1);
	}

	/**
	 * Remove a row from the flex table.
	 */
	private void removeRow(FlexTable flexTable) {
		int numRows = flexTable.getRowCount();
		if (numRows > 1) {
			flexTable.removeRow(numRows - 1);
			flexTable.getFlexCellFormatter().setRowSpan(0, 1, numRows - 1);
		}
	}

	interface __ComposePanelUiBinder extends
			UiBinder<DockLayoutPanel, __ComposePanel> {
	}

	private static __ComposePanelUiBinder binder = GWT
			.create(__ComposePanelUiBinder.class);

>>>>>>> preparing for composing panel
}
