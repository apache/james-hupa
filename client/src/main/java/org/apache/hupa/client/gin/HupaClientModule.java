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
import net.customware.gwt.presenter.client.place.ParameterTokenFormatter;
import net.customware.gwt.presenter.client.place.PlaceManager;
import net.customware.gwt.presenter.client.place.TokenFormatter;

import org.apache.hupa.client.CachingDispatchAsync;
import org.apache.hupa.client.dnd.PagingScrollTableRowDragController;
import org.apache.hupa.client.mvp.AppPresenter;
import org.apache.hupa.client.mvp.AppView;
import org.apache.hupa.client.mvp.ContactsPresenter;
import org.apache.hupa.client.mvp.ContactsView;
import org.apache.hupa.client.mvp.IMAPMessageListPresenter;
import org.apache.hupa.client.mvp.IMAPMessageListView;
import org.apache.hupa.client.mvp.IMAPMessagePresenter;
import org.apache.hupa.client.mvp.IMAPMessageView;
import org.apache.hupa.client.mvp.LoginPresenter;
import org.apache.hupa.client.mvp.LoginView;
import org.apache.hupa.client.mvp.MainPresenter;
import org.apache.hupa.client.mvp.MainView;
import org.apache.hupa.client.mvp.MessageSendPresenter;
import org.apache.hupa.client.mvp.MessageSendView;
import org.apache.hupa.client.mvp.MessageTableModel;
import org.apache.hupa.client.mvp.place.ContactsPresenterPlace;
import org.apache.hupa.client.mvp.place.HupaPlaceManager;
import org.apache.hupa.client.mvp.place.IMAPMessageListPresenterPlace;
import org.apache.hupa.client.mvp.place.LoginPresenterPlace;
import org.apache.hupa.client.mvp.place.MessageSendPresenterPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class HupaClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {        
        bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
        bind(PlaceManager.class).to(HupaPlaceManager.class);
        bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
        bindPresenter(LoginPresenter.class,LoginPresenter.Display.class, LoginView.class);
        bindPresenter(IMAPMessageListPresenter.class,IMAPMessageListPresenter.Display.class,IMAPMessageListView.class);
        bindPresenter(IMAPMessagePresenter.class,IMAPMessagePresenter.Display.class,IMAPMessageView.class);
        bindPresenter(MainPresenter.class,MainPresenter.Display.class,MainView.class);
        bindPresenter(MessageSendPresenter.class, MessageSendPresenter.Display.class, MessageSendView.class);
        bindPresenter(AppPresenter.class, AppPresenter.Display.class, AppView.class);
        bindPresenter(ContactsPresenter.class, ContactsPresenter.Display.class, ContactsView.class);
        bind(CachingDispatchAsync.class);
        bind(PagingScrollTableRowDragController.class).in(Singleton.class);
        bind(MessageTableModel.class).in(Singleton.class);
        bind(LoginPresenterPlace.class).in(Singleton.class);
        bind(IMAPMessageListPresenterPlace.class).in(Singleton.class);
        bind(MessageSendPresenterPlace.class).in(Singleton.class);
        bind(ContactsPresenterPlace.class).in(Singleton.class);
        
        bind(com.google.gwt.event.shared.EventBus.class)
            .to(SimpleEventBus.class)
            .in(Singleton.class);
        bind(HupaRequestFactory.class)
            .toProvider(HupaClientModule.RequestFactoryProvider.class)
            .in(Singleton.class);
    }
    
    public static class RequestFactoryProvider implements Provider<HupaRequestFactory> {
        private static final com.google.gwt.event.shared.EventBus eventBus = new SimpleEventBus();
        public HupaRequestFactory get() {
            HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
            rf.initialize(eventBus);
            return rf;
        }
    }

}
