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
import org.apache.hupa.client.activity.StatusActivity;

=======
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
import org.apache.hupa.client.activity.StatusActivity;

>>>>>>> integrate all of the views to their corresponding activities and mappers
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

<<<<<<< HEAD
<<<<<<< HEAD
public class StatusView extends Composite implements StatusActivity.Displayable {
=======
public class StatusView extends Composite {
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
public class StatusView extends Composite implements StatusActivity.Displayable {
>>>>>>> integrate all of the views to their corresponding activities and mappers

	public StatusView() {
		initWidget(binder.createAndBindUi(this));
	}

	interface StatusUiBinder extends UiBinder<HTMLPanel, StatusView> {
	}

	private static StatusUiBinder binder = GWT.create(StatusUiBinder.class);

}
