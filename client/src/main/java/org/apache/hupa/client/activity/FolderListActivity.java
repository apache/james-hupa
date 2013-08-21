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
import org.apache.hupa.client.ui.WidgetDisplayable;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
=======
import org.apache.hupa.shared.events.RefreshUnreadEvent;
import org.apache.hupa.shared.events.RefreshUnreadEventHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import com.google.inject.Inject;

public class FolderListActivity extends AppBaseActivity {

	@Inject private Displayable display;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
<<<<<<< HEAD
	}

	public interface Displayable extends WidgetDisplayable {
=======
		bindTo(eventBus);
	}

	private void bindTo(EventBus eventBus) {

		eventBus.addHandler(RefreshUnreadEvent.TYPE, new RefreshUnreadEventHandler() {
			@Override
			public void onRefreshEvent(RefreshUnreadEvent event) {
				display.refresh();
			}
		});
	}

	public interface Displayable extends IsWidget {
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
		void refresh();
	}
}
