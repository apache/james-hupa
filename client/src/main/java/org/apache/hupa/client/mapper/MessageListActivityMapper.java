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

import org.apache.hupa.client.activity.MessageListActivity;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
=======
=======
import org.apache.hupa.client.place.DefaultPlace;
=======
>>>>>>> support code split
import org.apache.hupa.client.place.MailFolderPlace;
<<<<<<< HEAD
>>>>>>> make message list view panel work as expected partly

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
<<<<<<< HEAD
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
=======
import org.apache.hupa.client.place.SettingPlace;
=======
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
>>>>>>> change place management and make refresh folder and message list more gentle

import com.google.gwt.activity.shared.Activity;
>>>>>>> try to rearrange the places and history managment.
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
>>>>>>> support code split
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
public class MessageListActivityMapper extends _MessageActivityMapper {
=======
public class MessageListActivityMapper implements ActivityMapper {
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
public class MessageListActivityMapper extends MainActivityMapper {
>>>>>>> try to rearrange the places and history managment.
=======
public class MessageListActivityMapper extends _MessageActivityMapper {
>>>>>>> change place management and make refresh folder and message list more gentle
	private final Provider<MessageListActivity> messageListActivityProvider;

	@Inject
	public MessageListActivityMapper(Provider<MessageListActivity> messageListActivityProvider) {
		this.messageListActivityProvider = messageListActivityProvider;
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	@Override
	protected Activity lazyLoadActivity(final Place place) {
		if (place instanceof FolderPlace) {
			return messageListActivityProvider.get().with(((FolderPlace) place).getToken());
		} else if(place instanceof MessagePlace){
			return messageListActivityProvider.get().with(((MessagePlace) place).getTokenWrapper().getFolder());
		}
		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}

			@Override
			protected Activity createInstance() {
				if (place instanceof FolderPlace) {
					return messageListActivityProvider.get().with(((FolderPlace) place).getToken());
				} else if(place instanceof MessagePlace){
					return messageListActivityProvider.get().with(((MessagePlace) place).getTokenWrapper().getFolder());
				}
				return messageListActivityProvider.get();
			}
		};
=======
	public Activity getActivity(Place place) {
		if(place instanceof DefaultPlace)return null;
		else if (place instanceof MailFolderPlace) return messageListActivityProvider.get().with((MailFolderPlace)place);
		return messageListActivityProvider.get();
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
	public Activity getActivity(final Place place) {
		if (!(place instanceof MailFolderPlace))
=======
	@Override
<<<<<<< HEAD
	Activity asyncLoadActivity(final Place place) {
		if (place instanceof SettingPlace)
>>>>>>> try to rearrange the places and history managment.
			return null;
=======
	protected Activity lazyLoadActivity(final Place place) {
>>>>>>> change place management and make refresh folder and message list more gentle
		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}

<<<<<<< HEAD
				@Override
				protected Activity createInstance() {
					return messageListActivityProvider.get().with(((MailFolderPlace) place).getFullName());
				}
			};
		}
		return null;
>>>>>>> support code split
=======
			@Override
			protected Activity createInstance() {
				if (place instanceof FolderPlace) {
					return messageListActivityProvider.get().with(((FolderPlace) place).getToken());
				} else if(place instanceof MessagePlace){
					return messageListActivityProvider.get().with(((MessagePlace) place).getTokenWrapper().getFolder());
				}
				return messageListActivityProvider.get();
			}
		};
>>>>>>> fixed issue#57 - really disable the tools in toolbar
	}
}
