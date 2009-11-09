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

package org.apache.hupa.client.gin;

import net.customware.gwt.presenter.client.DefaultEventBus;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.gin.AbstractPresenterModule;
import net.customware.gwt.presenter.client.place.PlaceManager;

import org.apache.hupa.client.CachingDispatchAsync;
import org.apache.hupa.client.dnd.PagingScrollTableRowDragController;
import org.apache.hupa.client.mvp.AppPresenter;
import org.apache.hupa.client.mvp.AppView;
import org.apache.hupa.client.mvp.IMAPMessageListPresenter;
import org.apache.hupa.client.mvp.IMAPMessageListView;
import org.apache.hupa.client.mvp.IMAPMessagePresenter;
import org.apache.hupa.client.mvp.MessageSendPresenter;
import org.apache.hupa.client.mvp.MessageSendView;
import org.apache.hupa.client.mvp.IMAPMessageView;
import org.apache.hupa.client.mvp.LoginPresenter;
import org.apache.hupa.client.mvp.LoginView;
import org.apache.hupa.client.mvp.MainPresenter;
import org.apache.hupa.client.mvp.MainView;
import org.apache.hupa.client.mvp.MessageTableModel;
import org.apache.hupa.client.mvp.place.HupaPlaceManager;
import org.apache.hupa.client.mvp.place.IMAPMessageListPresenterPlace;
import org.apache.hupa.client.mvp.place.LoginPresenterPlace;
import org.apache.hupa.client.mvp.place.MainPresenterPlace;

import com.google.inject.Singleton;

public class HupaClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {        
        bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
        bind(PlaceManager.class).to(HupaPlaceManager.class);
        bindPresenter(LoginPresenter.class,LoginPresenter.Display.class, LoginView.class);
        bindPresenter(IMAPMessageListPresenter.class,IMAPMessageListPresenter.Display.class,IMAPMessageListView.class);
        bindPresenter(IMAPMessagePresenter.class,IMAPMessagePresenter.Display.class,IMAPMessageView.class);
        bindPresenter(MainPresenter.class,MainPresenter.Display.class,MainView.class);
        bindPresenter(MessageSendPresenter.class, MessageSendPresenter.Display.class, MessageSendView.class);
        bindPresenter(AppPresenter.class, AppPresenter.Display.class, AppView.class);
        bind(CachingDispatchAsync.class);
        bind(PagingScrollTableRowDragController.class).in(Singleton.class);
        bind(MessageTableModel.class).in(Singleton.class);
        bind(LoginPresenterPlace.class).in(Singleton.class);
        bind(MainPresenterPlace.class).in(Singleton.class);
        bind(IMAPMessageListPresenterPlace.class).in(Singleton.class);
    }

}
