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

package org.apache.hupa.client;

<<<<<<< HEAD
import org.apache.hupa.client.ioc.AppGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class Hupa implements EntryPoint {
	@Override
	public void onModuleLoad() {
		handleExceptionsAsync();
		initApp();
	}

	private void initApp() {
		replaceLoading();
		injector.getHupaController().start();
	}

	private void handleExceptionsAsync() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				e.printStackTrace();
			}
		});
	}

	private void replaceLoading() {
		DOM.removeChild(RootPanel.getBodyElement(),
				DOM.getElementById("loading"));
	}

	private final AppGinjector injector = GWT.create(AppGinjector.class);
=======
import net.customware.gwt.presenter.client.place.PlaceManager;

import org.apache.hupa.client.gin.HupaGinjector;
import org.apache.hupa.client.mvp.AppPresenter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class Hupa implements EntryPoint{
    private final HupaGinjector injector = GWT.create(HupaGinjector.class);
    
    public void onModuleLoad() {
        // remove the loading message from the browser
        com.google.gwt.user.client.Element loading = DOM.getElementById("loading");

        DOM.removeChild(RootPanel.getBodyElement(), loading);

        AppPresenter aPres = injector.getAppPresenter();
        aPres.bind();
       
        RootPanel.get().add(aPres.getDisplay().asWidget());

        PlaceManager placeManager = injector.getPlaceManager();
        placeManager.fireCurrentPlace();
    }
>>>>>>> first commit

}
