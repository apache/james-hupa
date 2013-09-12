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

package org.apache.hupa.client.mapper;

import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.client.ui.LoginLayoutable;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class is responsible for ActivityManager instantiations through GIN, it
 * also associate every ActivityManager with the corresponding display region
 */
public class ActivityManagerInitializer {

	@Inject
	public ActivityManagerInitializer(LoginLayoutable loginLayout,
			HupaLayoutable hupaLayout,
			@Named("LoginPage") ActivityManager loginActivityManager,
			@Named("TopBarRegion") ActivityManager topBarActivityManager,
			@Named("LogoRegion") ActivityManager logoActivityManager,
			@Named("NavigationRegion") ActivityManager navigationActivityManager,
			@Named("ToolBarRegion") ActivityManager toolBarActivityManager,
			@Named("FolderListRegion") ActivityManager folderListActivityManager,
			@Named("MessageListRegion") ActivityManager messageListActivityManager,
			@Named("MessageListFooterRegion") ActivityManager messageListFooterActivityManager,
			@Named("MessageContentRegion") ActivityManager messageContentActivityManager,
			@Named("StatusRegion") ActivityManager statusActivityManager) {
		loginActivityManager.setDisplay(loginLayout.getLoginView());
		topBarActivityManager.setDisplay(hupaLayout.getTopBarView());
		logoActivityManager.setDisplay(hupaLayout.getLogoView());
		navigationActivityManager.setDisplay(hupaLayout.getNavigationView());
		toolBarActivityManager.setDisplay(hupaLayout.getToolBarView());
		folderListActivityManager.setDisplay(hupaLayout.getFolderListView());
		messageListActivityManager.setDisplay(hupaLayout.getMessageListView());
		messageListFooterActivityManager.setDisplay(hupaLayout.getMessageListFooterView());
		messageContentActivityManager.setDisplay(hupaLayout.getMessageContentView());
		statusActivityManager.setDisplay(hupaLayout.getStatusView());
	}

}
