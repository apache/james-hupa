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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.events.RefreshUnreadEvent;
import org.apache.hupa.shared.events.RefreshUnreadEventHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
=======
=======
import org.apache.hupa.client.ioc.FolderListFactory;
>>>>>>> use GinFactoryModuleBuilder to inject multiple displayable instances of some activities
=======
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list
import org.apache.hupa.client.ui.WidgetDisplayable;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
>>>>>>> replace with IsWidget
import com.google.inject.Inject;

public class FolderListActivity extends AppBaseActivity {

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	@Inject private Displayable display;
=======
	@Inject private FolderListFactory folderListFactory;
	private Place place;
	private Displayable display;
>>>>>>> use GinFactoryModuleBuilder to inject multiple displayable instances of some activities
=======
	@Inject private Displayable display;
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
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

<<<<<<< HEAD
	public interface Displayable extends IsWidget {
		void refresh();
	}
=======
=======
	@Inject private Displayable display;

>>>>>>> make reload mail folder place work, fixed issue #7
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
	}

<<<<<<< HEAD
<<<<<<< HEAD
	@Inject private Displayable display;
	
	public interface Displayable extends WidgetDisplayable {}
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
=======
	public FolderListActivity with(Place place) {
		if (display == null || !place.getClass().equals(this.place.getClass())) {
			display = folderListFactory.create(place);
			this.place = place;
		}
		return this;
	}

<<<<<<< HEAD
>>>>>>> use GinFactoryModuleBuilder to inject multiple displayable instances of some activities
=======
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list
	public interface Displayable extends WidgetDisplayable {
=======
	public interface Displayable extends IsWidget {
>>>>>>> replace with IsWidget
		void refresh();
	}
>>>>>>> make reload mail folder place work, fixed issue #7
}
