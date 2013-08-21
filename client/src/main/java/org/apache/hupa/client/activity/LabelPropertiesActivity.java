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

import java.util.List;

import org.apache.hupa.client.ui.LabelNode;
<<<<<<< HEAD
import org.apache.hupa.client.ui.WidgetDisplayable;
=======
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
<<<<<<< HEAD
=======
import com.google.gwt.user.client.ui.IsWidget;
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
import com.google.inject.Inject;

public class LabelPropertiesActivity extends AppBaseActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
	}

	@Inject private Displayable display;

<<<<<<< HEAD
	public interface Displayable extends WidgetDisplayable {
=======
	public interface Displayable extends IsWidget {
>>>>>>> 7635f4a0e76a4bbbeb6a4029aff92087f00eb09f
		void cascade(LabelNode labelNode, List<LabelNode> list, int cascadeTypeAdd);
		HasClickHandlers getSave();
	}
}
