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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AppLayoutImpl implements AppLayout {

	private final DockLayoutPanel appLayoutPanel;

	interface AppLayoutUiBinder extends UiBinder<DockLayoutPanel, AppLayoutImpl> {
	}

	private static AppLayoutUiBinder binder = GWT.create(AppLayoutUiBinder.class);

	@UiField LayoutPanel northPanel;
	@UiField HTMLPanel southPanel;
	@UiField SimplePanel westPanel;
	@UiField SimplePanel eastPanel;
	@UiField LayoutPanel centerPanel;
	@UiField SimplePanel logoContainer;
	@UiField SimplePanel topContainer;

	@Inject
	public AppLayoutImpl() {
		appLayoutPanel = binder.createAndBindUi(this);
		logoContainer.addStyleName(HupaCSS.C_logo_container);
		setLoginLayout();
	}

	@Override
	public DockLayoutPanel getAppLayoutPanel() {
		return appLayoutPanel;
	}

	@Override
	public AcceptsOneWidget getNorthContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				Widget widget = Widget.asWidgetOrNull(w);
				if(widget != null){
					topContainer.add(widget);
				}
			}
		};
	}
	@Override
	public AcceptsOneWidget getWestContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				Widget widget = Widget.asWidgetOrNull(w);
				westPanel.setWidget(widget);
			}
		};
	}

	@Override
	public AcceptsOneWidget getCenterContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				Widget widget = Widget.asWidgetOrNull(w);
				if(centerPanel.getWidgetCount() > 0){
					centerPanel.remove(0);
				}
				if(widget != null){
					centerPanel.add(widget);
				}
			}
		};
	}

	public void setDefaultLayout() {
		arrangeLayoutSize(8, 2, 15, 0);
	}

	public void setLoginLayout() {
		arrangeLayoutSize(0, 2, 0, 0);
	}
	
	private void arrangeLayoutSize(double n, double s, double w, double e){
		appLayoutPanel.setWidgetSize(northPanel, n);
		appLayoutPanel.setWidgetSize(southPanel, s);
		appLayoutPanel.setWidgetSize(westPanel, w);
		appLayoutPanel.setWidgetSize(eastPanel, e);
	}
}