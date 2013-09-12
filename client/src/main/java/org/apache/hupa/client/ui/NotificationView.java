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

import org.apache.hupa.client.activity.NotificationActivity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.google.gwt.safehtml.shared.SafeHtml;
>>>>>>> add loading and notification bar(finishing the folder list click event), related to the issue#18
=======
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
>>>>>>> make the notification be able to cope with link
=======
>>>>>>> upgrade maven-processor-plugin to the latest 2.2.4; change the middle-man in MessageListActivityMapper to string instead of the whole MessageFolderPlace
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class NotificationView extends Composite implements NotificationActivity.Displayable {

	@UiField FlowPanel notificationContainer;
	@UiField HTML notification;
	
	@UiField Style style;
	

	interface Style extends CssResource {
		String hideNotification();
	}
	
	
	@Override
<<<<<<< HEAD
<<<<<<< HEAD
	public void notice(String html){
=======
	public void notice(SafeHtml html){
>>>>>>> add loading and notification bar(finishing the folder list click event), related to the issue#18
=======
	public void notice(String html){
>>>>>>> make the notification be able to cope with link
		this.notificationContainer.removeStyleName(style.hideNotification());
		this.notification.setHTML(html);
	}
	
	@Override
	public void hideNotification(){
		this.notification.setHTML("");
		this.notificationContainer.addStyleName(style.hideNotification());
	}
	
	
	public NotificationView() {
		initWidget(binder.createAndBindUi(this));
	}

	interface NotificationUiBinder extends UiBinder<FlowPanel, NotificationView> {
	}

	private static NotificationUiBinder binder = GWT.create(NotificationUiBinder.class);

}
