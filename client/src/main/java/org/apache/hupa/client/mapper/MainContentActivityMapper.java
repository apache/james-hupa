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

import org.apache.hupa.client.activity.IMAPMessageActivity;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.activity.MessageSendActivity;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class MainContentActivityMapper implements ActivityMapper {

	private final Provider<LoginActivity> loginActivityProvider;
	private final Provider<MessageSendActivity> messageSendActivityProvider;
	private final Provider<IMAPMessageActivity> messageActivityProvider;

	@Inject
	public MainContentActivityMapper(Provider<LoginActivity> loginActivityProvider,
			Provider<MessageSendActivity> messageSendActivityProvider,
			Provider<IMAPMessageActivity> messageActivityProvider) {
		this.loginActivityProvider = loginActivityProvider;
		this.messageSendActivityProvider = messageSendActivityProvider;
		this.messageActivityProvider = messageActivityProvider;
	}

	public Activity getActivity(Place place) {
		if (place instanceof MailFolderPlace) {
			// return
			// messageListActivityProvider.get().with((MailFolderPlace)place);
		} else if (place instanceof DefaultPlace) {
			return loginActivityProvider.get();
		} else if (place instanceof MessageSendPlace) {
			return messageSendActivityProvider.get().with((MessageSendPlace) place);
		} else if (place instanceof IMAPMessagePlace) {
			return messageActivityProvider.get().with((IMAPMessagePlace) place);
		}

		return null;
	}
}
