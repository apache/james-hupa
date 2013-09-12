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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.place.SettingPlace;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

public interface HupaLayoutable extends Layoutable {
	AcceptsOneWidget getTopBarView();

	AcceptsOneWidget getLogoView();

	AcceptsOneWidget getNavigationView();

	AcceptsOneWidget getToolBarView();

	AcceptsOneWidget getFolderListView();

	AcceptsOneWidget getMessageListView();

	AcceptsOneWidget getMessageListFooterView();

	AcceptsOneWidget getMessageContentView();

	AcceptsOneWidget getStatusView();

	AcceptsOneWidget getComposeToolBarView();

	AcceptsOneWidget getComposeView();

	AcceptsOneWidget getNotificationView();

	AcceptsOneWidget getLabelListView();
	AcceptsOneWidget getContactListView();

	AcceptsOneWidget getLabelPropertiesView();

	void switchTo(int layout);

	AcceptsOneWidget getContactPropertiesView();

	AcceptsOneWidget getContactsListView();

	AcceptsOneWidget getSearchBoxView();

	AcceptsOneWidget getSettingNavView();

	void arrangeSettingLayout(SettingPlace sp);
=======
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public interface HupaLayoutable {
	LayoutPanel get();
<<<<<<< HEAD
>>>>>>> refactoring
=======

	SimplePanel getLoginView();
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======

public interface HupaLayoutable extends Layoutable{
>>>>>>> integrate them as a whole one - first: make the default place work
=======
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public interface HupaLayoutable extends Layoutable {
	AcceptsOneWidget getTopBarView();

	AcceptsOneWidget getLogoView();

	AcceptsOneWidget getNavigationView();

	AcceptsOneWidget getToolBarView();

	AcceptsOneWidget getFolderListView();

	AcceptsOneWidget getMessageListView();

	AcceptsOneWidget getMessageListFooterView();

	AcceptsOneWidget getMessageContentView();

	AcceptsOneWidget getStatusView();
<<<<<<< HEAD
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======

<<<<<<< HEAD
	void switchToCompose();

	void switchToMessage();
<<<<<<< HEAD
>>>>>>> could change main panel dynamically currently by clicking the compose button
=======

<<<<<<< HEAD
	AcceptsOneWidget getComposeHeader();

	AcceptsOneWidget getComposeContent();

	AcceptsOneWidget getComposeStatus();
<<<<<<< HEAD
>>>>>>> make compose panel managed by activity manager, there is a problem here that whether the hidden view will be lazy loaded regarding the code split mechnism
=======

	AcceptsOneWidget getComposeToolBarView();
>>>>>>> toggle to display/hide the tool bar view to adjust the compose and message panel
=======
=======
>>>>>>> make layout can be arranged by clicking the navigation buttons; make the layout changing by set their sizes to zero rather than remove/add from their parent widgets; merge to the master branch.
	AcceptsOneWidget getComposeToolBarView();

	AcceptsOneWidget getComposeView();
<<<<<<< HEAD
>>>>>>> make send text mail work excellently
=======

	AcceptsOneWidget getNotificationView();
<<<<<<< HEAD
>>>>>>> add loading and notification bar(finishing the folder list click event), related to the issue#18
=======

<<<<<<< HEAD
	void switchToSetting();
<<<<<<< HEAD
>>>>>>> attempt to add label setting feature
=======

	AcceptsOneWidget getLabelListView();
<<<<<<< HEAD
>>>>>>> make label settings prototype
=======

	AcceptsOneWidget getLabelPropertiesView();
>>>>>>> add rename RF to label setting feature
=======
	AcceptsOneWidget getLabelListView();
	AcceptsOneWidget getContactListView();

	AcceptsOneWidget getLabelPropertiesView();

	void switchTo(int layout);
<<<<<<< HEAD
>>>>>>> make layout can be arranged by clicking the navigation buttons; make the layout changing by set their sizes to zero rather than remove/add from their parent widgets; merge to the master branch.
=======

	AcceptsOneWidget getContactPropertiesView();

	AcceptsOneWidget getContactsListView();
>>>>>>> prepared for issue#73, established the UI layout
}
