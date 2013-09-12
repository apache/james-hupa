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

import org.apache.hupa.client.activity.FolderListActivity;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> attempt to add label setting feature
import org.apache.hupa.client.place.SettingPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
=======
=======
import org.apache.hupa.client.place.DefaultPlace;
=======
>>>>>>> support code split
=======
import org.apache.hupa.client.place.ComposePlace;
>>>>>>> composing composing panel
import org.apache.hupa.client.place.MailFolderPlace;
>>>>>>> make folder list panel work as expected

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
<<<<<<< HEAD
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
=======

import com.google.gwt.activity.shared.Activity;
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
>>>>>>> support code split
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
public class FolderListActivityMapper extends _HupaActivityMapper {
=======
public class FolderListActivityMapper implements ActivityMapper {
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
public class FolderListActivityMapper extends AbstractActivityMapper {
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list
=======
public class FolderListActivityMapper extends MainActivityMapper {
>>>>>>> fixed issue#11, write a subclass of SplitLayoutPanel to override its onResize but failed, use the native one, and then refactor some names
=======
public class FolderListActivityMapper extends _HupaActivityMapper {
>>>>>>> change place management and make refresh folder and message list more gentle
	private final Provider<FolderListActivity> folderListActivityProvider;

	@Inject
	public FolderListActivityMapper(Provider<FolderListActivity> folderListActivityProvider) {
		this.folderListActivityProvider = folderListActivityProvider;
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	@Override
	Activity asyncLoadActivity(final Place place) {
		if (place instanceof SettingPlace)
			return null;
=======
	@Override
<<<<<<< HEAD
	Activity getAppActivity(final Place place) {
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list
=======
	Activity asyncLoadActivity(final Place place) {
<<<<<<< HEAD
>>>>>>> fixed issue#11, write a subclass of SplitLayoutPanel to override its onResize but failed, use the native one, and then refactor some names
=======
		if (place instanceof SettingPlace)
			return null;
>>>>>>> attempt to add label setting feature
		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}
<<<<<<< HEAD

			@Override
			protected Activity createInstance() {
				return folderListActivityProvider.get();
			}
		};

=======
	public Activity getActivity(Place place) {
<<<<<<< HEAD
		if(place instanceof DefaultPlace)return null;
		else if (place instanceof MailFolderPlace) folderListActivityProvider.get();
		return folderListActivityProvider.get();
>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
=======
	public Activity getActivity(final Place place) {
<<<<<<< HEAD
>>>>>>> use GinFactoryModuleBuilder to inject multiple displayable instances of some activities
		if (place instanceof MailFolderPlace) {
=======
		if (place instanceof MailFolderPlace || place instanceof ComposePlace) {
>>>>>>> composing composing panel
			return new ActivityAsyncProxy() {
				@Override
				protected void doAsync(RunAsyncCallback callback) {
					GWT.runAsync(callback);
				}
=======
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list

			@Override
			protected Activity createInstance() {
				return folderListActivityProvider.get();
			}
		};

<<<<<<< HEAD
		}
		return null;
>>>>>>> support code split
=======
>>>>>>> fixed issue#45, issue#47, issue#51. change the layout of composite, don't use contact instead of folders list
	}
}
