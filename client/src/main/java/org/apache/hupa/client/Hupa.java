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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> first commit
=======
>>>>>>> Change to new mvp framework - first step
import net.customware.gwt.presenter.client.place.PlaceManager;

import org.apache.hupa.client.gin.HupaGinjector;
import org.apache.hupa.client.mvp.AppPresenter;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
import org.apache.hupa.client.gin.HupaEvoGinjector;
import org.apache.hupa.client.mvp.AppPlaceFactory;
import org.apache.hupa.client.mvp.AppPlaceHistoryMapper;
import org.apache.hupa.client.place.LoginPlace;
<<<<<<< HEAD
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> first commit
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
>>>>>>> Change to new mvp framework - first step

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Change to new mvp framework - first step
public class Hupa implements EntryPoint {
	 private final HupaGinjector injector = GWT.create(HupaGinjector.class);

//	private SimplePanel appWidget = new SimplePanel();
<<<<<<< HEAD

	public void onModuleLoad() {
		// remove the loading message from the browser
		com.google.gwt.user.client.Element loading = DOM.getElementById("loading");

		DOM.removeChild(RootPanel.getBodyElement(), loading);

		AppPresenter aPres = injector.getAppPresenter();
		aPres.bind();

		RootPanel.get().add(aPres.getDisplay().asWidget());

		PlaceManager placeManager = injector.getPlaceManager();
		placeManager.fireCurrentPlace();

<<<<<<< HEAD
		AppPlaceFactory factory = injector.getAppPlaceFactory();
		LoginPlace defaultPlace = factory.getLoginPlace();
		
		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
		historyMapper.setFactory(factory);
		
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);
		
		RootPanel.get().add(appWidget);
		
		historyHandler.handleCurrentHistory();
    }
>>>>>>> first commit
=======
	}
>>>>>>> Change to new mvp framework - first step
=======
public class Hupa implements EntryPoint{
//    private final HupaGinjector injector = GWT.create(HupaGinjector.class);
=======
>>>>>>> Change to new mvp framework - first step

	public void onModuleLoad() {
		// remove the loading message from the browser
		com.google.gwt.user.client.Element loading = DOM.getElementById("loading");

		DOM.removeChild(RootPanel.getBodyElement(), loading);

		AppPresenter aPres = injector.getAppPresenter();
		aPres.bind();

		RootPanel.get().add(aPres.getDisplay().asWidget());

		PlaceManager placeManager = injector.getPlaceManager();
		placeManager.fireCurrentPlace();

<<<<<<< HEAD
		AppPlaceFactory factory = injector.getAppPlaceFactory();
		LoginPlace defaultPlace = factory.getLoginPlace();
		
		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
		historyMapper.setFactory(factory);
		
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);
		
		RootPanel.get().add(appWidget);
		
		historyHandler.handleCurrentHistory();
    }
>>>>>>> first commit
=======
	}
>>>>>>> Change to new mvp framework - first step

}
