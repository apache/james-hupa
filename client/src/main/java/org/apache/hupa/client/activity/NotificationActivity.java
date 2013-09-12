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

package org.apache.hupa.client.activity;

<<<<<<< HEAD
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
=======
import org.apache.hupa.client.ui.WidgetDisplayable;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
>>>>>>> add loading and notification bar(finishing the folder list click event), related to the issue#18
import com.google.inject.Inject;

public class NotificationActivity extends AppBaseActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
	}

	@Inject private Displayable display;

<<<<<<< HEAD
	public interface Displayable extends IsWidget {
		void hideNotification();
		void notice(String html);
=======
	public interface Displayable extends WidgetDisplayable {
		void hideNotification();
<<<<<<< HEAD
		void notice(SafeHtml html);
>>>>>>> add loading and notification bar(finishing the folder list click event), related to the issue#18
=======
		void notice(String html);
>>>>>>> make the notification be able to cope with link
	}
}
