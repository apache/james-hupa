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

import java.util.ArrayList;
import java.util.List;

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.rf.HupaRequestFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public abstract class AppBaseActivity extends AbstractActivity {

    @Inject protected EventBus eventBus;
    @Inject protected HupaController hc;
    @Inject protected PlaceController pc;
    @Inject protected HupaRequestFactory rf;

    protected List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

    @Override
    public void onStop() {
        for (HandlerRegistration registration : registrations) {
            if(registration != null){
                registration.removeHandler();
            }
        }
        registrations.clear();
    }

    protected void registerHandler(HandlerRegistration handlerRegistration) {
        registrations.add(handlerRegistration);
    }

    public String mayStop() {
        return null;
    }

    public void onCancel() {
    }
}
