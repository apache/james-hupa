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
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class _CenterPanel extends Composite {

<<<<<<< HEAD
<<<<<<< HEAD
	@UiField protected SplitLayoutPanel thisPanel;
	@UiField protected __OutlinePanel outlinePanel;
	@UiField protected __ContentPanel contentPanel;
=======
	@UiField SplitLayoutPanel thisPanel;
	@UiField __OutlinePanel outlinePanel;
	@UiField __ContentPanel contentPanel;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> split the layout panel to more small ones
=======
	@UiField __ComposePanel composePanel;
>>>>>>> preparing for composing panel
=======
	@UiField ComposeView composeView;
>>>>>>> scrub code
=======
	@UiField protected SplitLayoutPanel thisPanel;
	@UiField protected __OutlinePanel outlinePanel;
	@UiField protected __ContentPanel contentPanel;
	@UiField protected __ComposePanel composePanel;
>>>>>>> make compose panel managed by activity manager, there is a problem here that whether the hidden view will be lazy loaded regarding the code split mechnism

	public _CenterPanel() {
		initWidget(binder.createAndBindUi(this));
		thisPanel.setWidgetMinSize(outlinePanel, 144);
		thisPanel.setWidgetHidden(composePanel, true);
	}

	// TODO make it display to remove this method and the corresponding code
	public void temporarilyHiddenTheUnimplementedContactPanel(boolean hidden) {
		thisPanel.setWidgetHidden(outlinePanel, hidden);
	}

	public AcceptsOneWidget getFolderListView() {
		return outlinePanel.getFolderListView();
	}

	public AcceptsOneWidget getMessageListView() {
		return contentPanel.getMessageListView();
	}

	public AcceptsOneWidget getMessageListFooterView() {
		return contentPanel.getMessageListFooterView();
	}

	public AcceptsOneWidget getMessageContentView() {
		return contentPanel.getMessageContentView();
	}

	public AcceptsOneWidget getStatusView() {
		return contentPanel.getStatusView();
	}

	interface _CenterPanelUiBinder extends
			UiBinder<SplitLayoutPanel, _CenterPanel> {
	}

	private static _CenterPanelUiBinder binder = GWT
			.create(_CenterPanelUiBinder.class);

	public AcceptsOneWidget getComposeHeader() {
		return composePanel.getComposeHeaderContainer();
	}

	public AcceptsOneWidget getComposeContent() {
		return composePanel.getComposeContentContainer();
	}

	public AcceptsOneWidget getComposeStatus() {
		return composePanel.getComposeStatusContainer();
	}

}
