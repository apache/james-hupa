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

import org.apache.hupa.client.activity.MessageListFooterActivity;

import com.google.gwt.activity.shared.Activity;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
=======
import com.google.gwt.activity.shared.ActivityMapper;
<<<<<<< HEAD
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
=======
>>>>>>> fixed issue#66 and remove one useless class, make MessageListFooterActivityMapper do not map anything when it comes to setting place
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
>>>>>>> support code split
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

<<<<<<< HEAD
<<<<<<< HEAD
public class MessageListFooterActivityMapper extends _MessageActivityMapper {
=======
public class MessageListFooterActivityMapper implements ActivityMapper {
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
public class MessageListFooterActivityMapper extends _MessageActivityMapper {
>>>>>>> fixed issue#66 and remove one useless class, make MessageListFooterActivityMapper do not map anything when it comes to setting place
	private final Provider<MessageListFooterActivity> messageListFooterActivityProvider;

	@Inject
	public MessageListFooterActivityMapper(
			Provider<MessageListFooterActivity> messageListFooterActivityProvider) {
		this.messageListFooterActivityProvider = messageListFooterActivityProvider;
	}

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> fixed issue#66 and remove one useless class, make MessageListFooterActivityMapper do not map anything when it comes to setting place
	@Override
	protected Activity lazyLoadActivity(Place place) {return new ActivityAsyncProxy() {
		@Override
		protected void doAsync(RunAsyncCallback callback) {
			GWT.runAsync(callback);
		}
<<<<<<< HEAD

		@Override
		protected Activity createInstance() {
			return messageListFooterActivityProvider.get();
		}
	};}
=======
	public Activity getActivity(Place place) {
		if(place instanceof DefaultPlace) return null;
		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}

			@Override
			protected Activity createInstance() {
				return messageListFooterActivityProvider.get();
			}
		};
	}
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======

		@Override
		protected Activity createInstance() {
			return messageListFooterActivityProvider.get();
		}
	};}
>>>>>>> fixed issue#66 and remove one useless class, make MessageListFooterActivityMapper do not map anything when it comes to setting place
}
