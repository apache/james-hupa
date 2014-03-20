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

package org.apache.hupa.client.ioc;

import java.util.logging.Logger;

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.AddressListActivity;
import org.apache.hupa.client.activity.ComposeActivity;
import org.apache.hupa.client.activity.ComposeToolBarActivity;
import org.apache.hupa.client.activity.ContactPropertiesActivity;
import org.apache.hupa.client.activity.ContactsListActivity;
import org.apache.hupa.client.activity.FolderListActivity;
import org.apache.hupa.client.activity.LabelListActivity;
import org.apache.hupa.client.activity.LabelPropertiesActivity;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.activity.LogoActivity;
import org.apache.hupa.client.activity.MessageContentActivity;
import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.client.activity.MessageListFooterActivity;
import org.apache.hupa.client.activity.NavigationActivity;
import org.apache.hupa.client.activity.NotificationActivity;
import org.apache.hupa.client.activity.SearchBoxActivity;
import org.apache.hupa.client.activity.SettingNavActivity;
import org.apache.hupa.client.activity.StatusActivity;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.activity.TopBarActivity;
import org.apache.hupa.client.mapper.AddressListActivityMapper;
import org.apache.hupa.client.mapper.AppPlaceHistoryMapper;
import org.apache.hupa.client.mapper.CachingTopBarActivityMapper;
import org.apache.hupa.client.mapper.ComposeActivityMapper;
import org.apache.hupa.client.mapper.ComposeToolBarActivityMapper;
import org.apache.hupa.client.mapper.ContactPropertiesActivityMapper;
import org.apache.hupa.client.mapper.ContactsListActivityMapper;
import org.apache.hupa.client.mapper.FolderListActivityMapper;
import org.apache.hupa.client.mapper.LabelListActivityMapper;
import org.apache.hupa.client.mapper.LabelPropertiesActivityMapper;
import org.apache.hupa.client.mapper.LoginActivityMapper;
import org.apache.hupa.client.mapper.LogoActivityMapper;
import org.apache.hupa.client.mapper.MessageContentActivityMapper;
import org.apache.hupa.client.mapper.MessageListActivityMapper;
import org.apache.hupa.client.mapper.MessageListFooterActivityMapper;
import org.apache.hupa.client.mapper.NavigationActivityMapper;
import org.apache.hupa.client.mapper.NotificationActivityMapper;
import org.apache.hupa.client.mapper.SearchBoxActivityMapper;
import org.apache.hupa.client.mapper.SettingNavActivityMapper;
import org.apache.hupa.client.mapper.StatusActivityMapper;
import org.apache.hupa.client.mapper.ToolBarActivityMapper;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.storage.AppCacheHTML5;
import org.apache.hupa.client.ui.AddressListView;
import org.apache.hupa.client.ui.ComposeToolBarView;
import org.apache.hupa.client.ui.ComposeView;
import org.apache.hupa.client.ui.ContactPropertiesView;
import org.apache.hupa.client.ui.ContactsListView;
import org.apache.hupa.client.ui.FolderListView;
import org.apache.hupa.client.ui.HupaLayout;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.client.ui.HupaPlugins;
import org.apache.hupa.client.ui.HupaPlugins.HupaDefaultPlugins;
import org.apache.hupa.client.ui.LabelListView;
import org.apache.hupa.client.ui.LabelPropertiesView;
import org.apache.hupa.client.ui.LoginLayout;
import org.apache.hupa.client.ui.LoginLayoutable;
import org.apache.hupa.client.ui.LoginView;
import org.apache.hupa.client.ui.LogoView;
import org.apache.hupa.client.ui.MessageContentView;
import org.apache.hupa.client.ui.MessageListFooterView;
import org.apache.hupa.client.ui.MessageListView;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.NavigationView;
import org.apache.hupa.client.ui.NotificationView;
import org.apache.hupa.client.ui.SearchBoxView;
import org.apache.hupa.client.ui.SettingNavView;
import org.apache.hupa.client.ui.StatusView;
import org.apache.hupa.client.ui.ToolBarView;
import org.apache.hupa.client.ui.TopBarView;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.storage.AppCache;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

@SuppressWarnings("deprecation")
public class AppGinModule extends AbstractGinModule {

    /**
     * Gin implementation of our AppInjector
     */
    @GinModules(AppGinModule.class)
    public static interface AppGinjector extends AppInjector {}

    public static Logger logger = Logger.getLogger(AppGinModule.class.getName());

    @Override
    protected void configure() {
        // Views
        bind(HupaLayoutable.class).to(HupaLayout.class).in(Singleton.class);
        bind(LoginLayoutable.class).to(LoginLayout.class).in(Singleton.class);

        // Activities
        bind(LoginActivity.Displayable.class).to(LoginView.class).in(Singleton.class);
        bind(TopBarActivity.Displayable.class).to(TopBarView.class).in(Singleton.class);
        bind(LogoActivity.Displayable.class).to(LogoView.class).in(Singleton.class);
        bind(NotificationActivity.Displayable.class).to(NotificationView.class).in(Singleton.class);
        bind(NavigationActivity.Displayable.class).to(NavigationView.class).in(Singleton.class);
        bind(ToolBarActivity.Displayable.class).to(ToolBarView.class).in(Singleton.class);
        // bind(FolderListActivity.Displayable.class).to(FolderListView.class);
        bind(MessageListActivity.Displayable.class).to(MessageListView.class).in(Singleton.class);
        bind(MessageListFooterActivity.Displayable.class).to(MessageListFooterView.class).in(Singleton.class);
        bind(MessageContentActivity.Displayable.class).to(MessageContentView.class).in(Singleton.class);
        bind(StatusActivity.Displayable.class).to(StatusView.class).in(Singleton.class);
        bind(ComposeToolBarActivity.Displayable.class).to(ComposeToolBarView.class).in(Singleton.class);
        bind(ComposeActivity.Displayable.class).to(ComposeView.class).in(Singleton.class);
        bind(SearchBoxActivity.Displayable.class).to(SearchBoxView.class).in(Singleton.class);

        bind(LabelListActivity.Displayable.class).to(LabelListView.class).in(Singleton.class);
        bind(SettingNavActivity.Displayable.class).to(SettingNavView.class).in(Singleton.class);
        bind(LabelPropertiesActivity.Displayable.class).to(LabelPropertiesView.class).in(Singleton.class);
        bind(ContactsListActivity.Displayable.class).to(ContactsListView.class).in(Singleton.class);
        bind(ContactPropertiesActivity.Displayable.class).to(ContactPropertiesView.class).in(Singleton.class);
        bind(AddressListActivity.Displayable.class).to(AddressListView.class).in(Singleton.class);

        bind(LoginActivity.class).in(Singleton.class);
        bind(TopBarActivity.class).in(Singleton.class);
        bind(LogoActivity.class).in(Singleton.class);
        bind(NotificationActivity.class).in(Singleton.class);
        bind(NavigationActivity.class).in(Singleton.class);
        bind(ToolBarActivity.class).in(Singleton.class);
        bind(FolderListActivity.class).in(Singleton.class);
        bind(MessageListActivity.class).in(Singleton.class);
        bind(ComposeToolBarActivity.class).in(Singleton.class);
        bind(ComposeActivity.class).in(Singleton.class);
        bind(SearchBoxActivity.class).in(Singleton.class);

        bind(LabelListActivity.class).in(Singleton.class);
        bind(SettingNavActivity.class).in(Singleton.class);
        bind(LabelPropertiesActivity.class).in(Singleton.class);
        bind(ContactsListActivity.class).in(Singleton.class);
        bind(ContactPropertiesActivity.class).in(Singleton.class);
        bind(AddressListActivity.class).in(Singleton.class);


        bind(FolderListActivity.Displayable.class).to(FolderListView.class).in(Singleton.class);

        bind(MessagesCellTable.class).in(Singleton.class);
        bind(CellTree.Resources.class).to(CellTree.BasicResources.class).in(Singleton.class);
        // Places
        bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class).in(Singleton.class);

        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

        bind(HupaController.class).in(Singleton.class);

        bind(AppCache.class).to(AppCacheHTML5.class).in(Singleton.class);

        bind(RequestFactory.class).to(HupaRequestFactory.class).in(Singleton.class);

        bind(HupaPlugins.class).to(HupaDefaultPlugins.class).in(Singleton.class);
    }



    @Provides
    @Singleton
    @Named("AddressListRegion")
    public ActivityManager getAddressListActivityMapper(AddressListActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("ContactsListRegion")
    public ActivityManager getContactsListActivityMapper(ContactsListActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("ContactPropertiesRegion")
    public ActivityManager getContactPropertiesActivityMapper(ContactPropertiesActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("LabelListRegion")
    public ActivityManager getLabelListActivityMapper(LabelListActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("LabelPropertiesRegion")
    public ActivityManager getLabelPropertiesActivityMapper(LabelPropertiesActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }


    @Provides
    @Singleton
    @Named("SettingNavRegion")
    public ActivityManager getSettingNavActivityMapper(SettingNavActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("LoginPage")
    public ActivityManager getLoginActivityMapper(LoginActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("TopBarRegion")
    public ActivityManager getTopBarActivityMapper(CachingTopBarActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("LogoRegion")
    public ActivityManager getLogoActivityMapper(LogoActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("NotificationRegion")
    public ActivityManager getNotificationActivityMapper(NotificationActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }
    @Provides
    @Singleton
    @Named("NavigationRegion")
    public ActivityManager getNavigationActivityMapper(NavigationActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("ToolBarRegion")
    public ActivityManager getToolBarActivityMapper(ToolBarActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("FolderListRegion")
    public ActivityManager getFolderListActivityMapper(FolderListActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("MessageListRegion")
    public ActivityManager getMessageListActivityMapper(MessageListActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("MessageListFooterRegion")
    public ActivityManager getMessageListFooterActivityMapper(MessageListFooterActivityMapper activityMapper,
            EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("MessageContentRegion")
    public ActivityManager getMessageContentActivityMapper(MessageContentActivityMapper activityMapper,
            EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("StatusRegion")
    public ActivityManager getStatusActivityMapper(StatusActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("ComposeRegion")
    public ActivityManager getComposeActivityMapper(ComposeActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("ComposeToolBarRegion")
    public ActivityManager getComposeToolBarActivityMapper(ComposeToolBarActivityMapper activityMapper,
            EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    @Named("SearchBoxRegion")
    public ActivityManager getSearchBoxActivityMapper(SearchBoxActivityMapper activityMapper,
            EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Provides
    @Singleton
    public PlaceController getPlaceController(EventBus eventBus) {
        return new PlaceController(eventBus);
    }

    @Provides
    @Singleton
    public PlaceHistoryHandler getHistoryHandler(PlaceController placeController, PlaceHistoryMapper historyMapper,
            EventBus eventBus) {
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, new DefaultPlace("@"));
        return historyHandler;
    }

    @Provides
    @Singleton
    HupaRequestFactory getRequestFactory(final EventBus eventBus) {
        HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
        rf.initialize(eventBus, new DefaultRequestTransport() {
            @Override
            protected RequestCallback createRequestCallback(TransportReceiver receiver) {
                final RequestCallback superCallback = super.createRequestCallback(receiver);
                return new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        if (response.getText().contains(User.NOT_FOUND)) {
                            eventBus.fireEvent(new LogoutEvent(null));
                        } else {
                            superCallback.onResponseReceived(request, response);
                        }
                    }

                    @Override
                    public void onError(Request request, Throwable exception) {
                        superCallback.onError(request, exception);
                    }
                };
            }
        });
        return rf;
    }

}
