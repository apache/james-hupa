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

import org.apache.hupa.client.activity.MessageContentActivity;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.place.MessagePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
=======
=======
import org.apache.hupa.client.place.DefaultPlace;
=======
>>>>>>> scrub code
=======
import org.apache.hupa.client.place.IMAPMessagePlace;
>>>>>>> try to rearrange the places and history managment.
import org.apache.hupa.client.place.MailFolderPlace;
<<<<<<< HEAD
>>>>>>> prepare for message content panel
=======
import org.apache.hupa.client.rf.HupaRequestFactory;
>>>>>>> make reload message content work, use the same place with folder list, while separated with slash, that looks like Gmail's

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
<<<<<<< HEAD
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
=======
import org.apache.hupa.client.place.MessagePlace;

import com.google.gwt.activity.shared.Activity;
>>>>>>> change place management and make refresh folder and message list more gentle
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
>>>>>>> support code split
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

<<<<<<< HEAD
<<<<<<< HEAD
public class MessageContentActivityMapper extends _MessageActivityMapper {
	private final Provider<MessageContentActivity> messageContentActivityProvider;

	@Inject
	public MessageContentActivityMapper(Provider<MessageContentActivity> messageContentActivityProvider) {
		this.messageContentActivityProvider = messageContentActivityProvider;
	}

	@Override
	protected Activity lazyLoadActivity(final Place place) {
		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}

			@Override
			protected Activity createInstance() {
				if (place instanceof MessagePlace) {
					return messageContentActivityProvider.get().with(((MessagePlace) place).getTokenWrapper());
				}
				return messageContentActivityProvider.get();
			}
		};
=======
public class MessageContentActivityMapper implements ActivityMapper {

	@Inject protected PlaceController placeController;
	@Inject protected HupaRequestFactory requestFactory;
=======
public class MessageContentActivityMapper extends _MessageActivityMapper {
>>>>>>> change place management and make refresh folder and message list more gentle
	private final Provider<MessageContentActivity> messageContentActivityProvider;

	@Inject
	public MessageContentActivityMapper(Provider<MessageContentActivity> messageContentActivityProvider) {
		this.messageContentActivityProvider = messageContentActivityProvider;
	}

<<<<<<< HEAD
<<<<<<< HEAD
	public Activity getActivity(Place place) {
<<<<<<< HEAD
<<<<<<< HEAD
		if(place instanceof DefaultPlace)return null;
		else if (place instanceof MailFolderPlace) return null;
		else if (place instanceof IMAPMessagePlace) return messageContentActivityProvider.get();
		return messageContentActivityProvider.get();
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
		if (place instanceof DefaultPlace)
			return null;
		else if (place instanceof MailFolderPlace) {
=======
		if (place instanceof MailFolderPlace) {
>>>>>>> scrub code
			return messageContentActivityProvider.get().with(
					(MailFolderPlace) place);
=======
	public Activity getActivity(final Place place) {
		if (place instanceof IMAPMessagePlace) {
			return new ActivityAsyncProxy() {
				@Override
				protected void doAsync(RunAsyncCallback callback) {
					GWT.runAsync(callback);
				}

				@Override
				protected Activity createInstance() {
					return messageContentActivityProvider.get().with(((IMAPMessagePlace)place).getToken());
				}
			};
>>>>>>> support code split
		}
		return null;
>>>>>>> make message content work as expected partly
=======
	@Override
	protected Activity lazyLoadActivity(final Place place) {
		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}

			@Override
			protected Activity createInstance() {
				if (place instanceof MessagePlace) {
					return messageContentActivityProvider.get().with(((MessagePlace) place).getTokenWrapper());
				}
				return messageContentActivityProvider.get();
			}
		};
>>>>>>> change place management and make refresh folder and message list more gentle
	}
}
