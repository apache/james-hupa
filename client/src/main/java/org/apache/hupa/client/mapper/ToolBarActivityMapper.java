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

import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
import org.apache.hupa.client.place.SettingPlace;
import org.apache.hupa.client.ui.ToolBarView.Parameters;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ToolBarActivityMapper extends _HupaActivityMapper {
    private final Provider<ToolBarActivity> toolBarActivityProvider;

    @Inject
    public ToolBarActivityMapper(Provider<ToolBarActivity> toolActivityProvider) {
        this.toolBarActivityProvider = toolActivityProvider;
    }

    @Override
    protected Activity asyncLoadActivity(final Place place) {
        if(place instanceof SettingPlace) return null;
        final ToolBarActivity tba = toolBarActivityProvider.get();
        if (place instanceof FolderPlace) { // might be from login page
            FolderPlace here = (FolderPlace) place;
            tba.getDisplay().setParameters(new Parameters(null, here.getToken(), null, null));
        }
        if(place instanceof MessagePlace){
            return tba.with(((MessagePlace)place).getTokenWrapper().getFolder());
        }

        return new ActivityAsyncProxy() {
            @Override
            protected void doAsync(RunAsyncCallback callback) {
                GWT.runAsync(callback);
            }

            @Override
            protected Activity createInstance() {
                String token = null;
                if (place instanceof FolderPlace) {
                    token = ((FolderPlace) place).getToken();
                }else if(place instanceof MessagePlace){
                    token = ((MessagePlace)place).getTokenWrapper().getFolder();
                }
                return tba.with(token);
            }
        };
    }
}
