<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
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

<<<<<<< HEAD
=======
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.client.ioc;

import java.util.logging.Logger;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.apache.hupa.client.HupaConstants;
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
>>>>>>> integrate them as a whole one - first: make the default place work
import org.apache.hupa.client.HupaController;
<<<<<<< HEAD
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
import org.apache.hupa.client.ui.ComposeToolBarView;
import org.apache.hupa.client.ui.ComposeView;
import org.apache.hupa.client.ui.ContactPropertiesView;
import org.apache.hupa.client.ui.ContactsListView;
import org.apache.hupa.client.ui.FolderListView;
import org.apache.hupa.client.ui.HupaLayout;
import org.apache.hupa.client.ui.HupaLayoutable;
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

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
=======
=======
>>>>>>> Change to new mvp framework - first step
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.ExceptionHandler;

<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.CachingDispatchAsync;
=======
import org.apache.hupa.client.HupaController;
>>>>>>> move new theme ui from experiment to hupa evo
=======
import org.apache.hupa.client.activity.FolderListActivity;
>>>>>>> integrate all of the views to their corresponding activities and mappers
import org.apache.hupa.client.activity.IMAPMessageActivity;
import org.apache.hupa.client.activity.IMAPMessageListActivity;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.activity.LogoActivity;
import org.apache.hupa.client.activity.MessageContentActivity;
import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.client.activity.MessageListFooterActivity;
import org.apache.hupa.client.activity.MessageSendActivity;
import org.apache.hupa.client.activity.NavigationActivity;
import org.apache.hupa.client.activity.StatusActivity;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.activity.TopActivity;
import org.apache.hupa.client.activity.TopBarActivity;
import org.apache.hupa.client.activity.WestActivity;
import org.apache.hupa.client.mapper.AppPlaceHistoryMapper;
<<<<<<< HEAD
import org.apache.hupa.client.mapper.CachingTopActivityMapper;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.client.mapper.CachingWestActivityMapper;
=======
import org.apache.hupa.client.mapper.LoginActivityMapper;
>>>>>>> make login page as one part of the overall layout & splite layout to little one
import org.apache.hupa.client.mapper.MainContentActivityMapper;
=======
import org.apache.hupa.client.AppController;
=======
>>>>>>> Make the evo more clear.
import org.apache.hupa.client.CachingDispatchAsync;
=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
import org.apache.hupa.client.activity.IMAPMessageActivity;
import org.apache.hupa.client.activity.IMAPMessageListActivity;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.activity.MessageSendActivity;
import org.apache.hupa.client.activity.TopActivity;
import org.apache.hupa.client.activity.WestActivity;
import org.apache.hupa.client.dnd.PagingScrollTableRowDragController;
<<<<<<< HEAD
import org.apache.hupa.client.mvp.AppPlaceHistoryMapper;
import org.apache.hupa.client.mvp.CachingTopActivityMapper;
import org.apache.hupa.client.mvp.CachingWestActivityMapper;
import org.apache.hupa.client.mvp.MainContentActivityMapper;
<<<<<<< HEAD
import org.apache.hupa.client.mvp.WestActivityMapper;
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
=======
import org.apache.hupa.client.evo.AppController;
import org.apache.hupa.client.mapper.AppPlaceHistoryMapper;
import org.apache.hupa.client.mapper.CachingTopActivityMapper;
import org.apache.hupa.client.mapper.MainContentActivityMapper;
<<<<<<< HEAD
>>>>>>> Make the evo more clear.
=======
=======
=======
>>>>>>> make message list view panel work as expected partly
import org.apache.hupa.client.mapper.FolderListActivityMapper;
import org.apache.hupa.client.mapper.LoginActivityMapper;
import org.apache.hupa.client.mapper.LogoActivityMapper;
import org.apache.hupa.client.mapper.MessageContentActivityMapper;
import org.apache.hupa.client.mapper.MessageListActivityMapper;
import org.apache.hupa.client.mapper.MessageListFooterActivityMapper;
import org.apache.hupa.client.mapper.NavigationActivityMapper;
import org.apache.hupa.client.mapper.StatusActivityMapper;
import org.apache.hupa.client.mapper.ToolBarActivityMapper;
import org.apache.hupa.client.mapper.TopBarActivityMapper;
<<<<<<< HEAD
>>>>>>> integrate all of the views to their corresponding activities and mappers
import org.apache.hupa.client.mapper.WestActivityMapper;
>>>>>>> delete messages, make WestActivity Singleton
=======
>>>>>>> make message list view panel work as expected partly
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.FolderListView;
import org.apache.hupa.client.ui.FoldersTreeViewModel;
import org.apache.hupa.client.ui.HupaLayout;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.client.ui.IMAPMessageListView;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 1. improve the inbox folder place.
import org.apache.hupa.client.ui.IMAPMessageView;
import org.apache.hupa.client.ui.LoginLayout;
import org.apache.hupa.client.ui.LoginLayoutable;
import org.apache.hupa.client.ui.LoginView;
import org.apache.hupa.client.ui.LogoView;
import org.apache.hupa.client.ui.MessageContentView;
import org.apache.hupa.client.ui.MessageListFooterView;
import org.apache.hupa.client.ui.MessageListView;
import org.apache.hupa.client.ui.MessageSendView;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.NavigationView;
import org.apache.hupa.client.ui.StatusView;
import org.apache.hupa.client.ui.ToolBarView;
import org.apache.hupa.client.ui.TopBarView;
import org.apache.hupa.client.ui.TopView;
import org.apache.hupa.client.ui.WestView;

import com.google.gwt.activity.shared.ActivityManager;
<<<<<<< HEAD
>>>>>>> Change to new mvp framework - first step
=======
import org.apache.hupa.client.ui.LoginView;
import org.apache.hupa.client.ui.TopView;
import org.apache.hupa.client.ui.WestView;

import com.google.gwt.activity.shared.ActivityManager;
>>>>>>> Change to new mvp framework - first step
=======
import com.google.gwt.core.client.GWT;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.user.cellview.client.CellTree;
=======
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> Change to new mvp framework - first step
=======
import com.google.gwt.user.cellview.client.CellTree;
>>>>>>> refactoring.
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
@SuppressWarnings("deprecation")
=======
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> Change to new mvp framework - first step
=======
@SuppressWarnings("deprecation")
>>>>>>> add SuppressWarnings("depraction")
public class AppGinModule extends AbstractGinModule {
	public static Logger logger = Logger
			.getLogger(AppGinModule.class.getName());

	@Override
	protected void configure() {
		// Views
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		bind(HupaLayoutable.class).to(HupaLayout.class).in(Singleton.class);
		bind(LoginLayoutable.class).to(LoginLayout.class).in(Singleton.class);

		// Activities
		bind(LoginActivity.Displayable.class).to(LoginView.class);
<<<<<<< HEAD
		bind(TopBarActivity.Displayable.class).to(TopBarView.class).in(Singleton.class);
		bind(LogoActivity.Displayable.class).to(LogoView.class).in(Singleton.class);
		bind(NotificationActivity.Displayable.class).to(NotificationView.class).in(Singleton.class);
		bind(NavigationActivity.Displayable.class).to(NavigationView.class);
		bind(ToolBarActivity.Displayable.class).to(ToolBarView.class).in(Singleton.class);
		// bind(FolderListActivity.Displayable.class).to(FolderListView.class);
		bind(MessageListActivity.Displayable.class).to(MessageListView.class).in(Singleton.class);
		bind(MessageListFooterActivity.Displayable.class).to(MessageListFooterView.class);
		bind(MessageContentActivity.Displayable.class).to(MessageContentView.class);
		bind(StatusActivity.Displayable.class).to(StatusView.class);
		bind(ComposeToolBarActivity.Displayable.class).to(ComposeToolBarView.class);
		bind(ComposeActivity.Displayable.class).to(ComposeView.class);
		bind(SearchBoxActivity.Displayable.class).to(SearchBoxView.class);
		
		bind(LabelListActivity.Displayable.class).to(LabelListView.class).in(Singleton.class);
		bind(SettingNavActivity.Displayable.class).to(SettingNavView.class).in(Singleton.class);
		bind(LabelPropertiesActivity.Displayable.class).to(LabelPropertiesView.class).in(Singleton.class);
		bind(ContactsListActivity.Displayable.class).to(ContactsListView.class).in(Singleton.class);
		bind(ContactPropertiesActivity.Displayable.class).to(ContactPropertiesView.class).in(Singleton.class);

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
		

		bind(FolderListActivity.Displayable.class).to(FolderListView.class).in(Singleton.class);

		bind(MessagesCellTable.class).in(Singleton.class);
		bind(CellTree.Resources.class).to(CellTree.BasicResources.class);
		// Places
		bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class).in(Singleton.class);

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

		bind(HupaController.class).in(Singleton.class);
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
=======
=======
>>>>>>> Change to new mvp framework - first step
		bind(AppLayout.class).to(AppLayoutImpl.class).in(Singleton.class);
=======
		bind(HupaLayout.class).to(HupaLayoutView.class).in(Singleton.class);
>>>>>>> move new theme ui from experiment to hupa evo
=======
		bind(HupaLayoutable.class).to(HupaOverallLayout.class).in(Singleton.class);
>>>>>>> refactoring
=======
		bind(HupaLayoutable.class).to(HupaLayout.class).in(Singleton.class);
<<<<<<< HEAD
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======
		bind(LoginLayoutable.class).to(LoginLayout.class).in(Singleton.class);
>>>>>>> integrate them as a whole one - first: make the default place work

		// Activities
		bind(LoginActivity.Displayable.class).to(LoginView.class);
<<<<<<< HEAD
<<<<<<< HEAD
=======
		bind(TopBarActivity.Displayable.class).to(TopBarView.class);
		bind(LogoActivity.Displayable.class).to(LogoView.class);
		bind(NavigationActivity.Displayable.class).to(NavigationView.class);
		bind(ToolBarActivity.Displayable.class).to(ToolBarView.class);
		bind(FolderListActivity.Displayable.class).to(FolderListView.class);
		bind(MessageListActivity.Displayable.class).to(MessageListView.class);
		bind(MessageListFooterActivity.Displayable.class).to(MessageListFooterView.class);
		bind(MessageContentActivity.Displayable.class).to(MessageContentView.class);
		bind(StatusActivity.Displayable.class).to(StatusView.class);
		
		bind(LoginActivity.class).in(Singleton.class);
		bind(TopBarActivity.class).in(Singleton.class);
		bind(LogoActivity.class).in(Singleton.class);
		bind(NavigationActivity.class).in(Singleton.class);
		bind(ToolBarActivity.class).in(Singleton.class);
		bind(FolderListActivity.class).in(Singleton.class);
		bind(MessageListActivity.class).in(Singleton.class);
//		bind(MessageListFooterActivity.class).in(Singleton.class);
//		bind(MessageContentActivity.class).in(Singleton.class);
//		bind(StatusActivity.class).in(Singleton.class);
		
		

>>>>>>> integrate all of the views to their corresponding activities and mappers
		bind(TopActivity.Displayable.class).to(TopView.class);
		bind(WestActivity.Displayable.class).to(WestView.class).in(
				Singleton.class);
		bind(IMAPMessageListActivity.Displayable.class).to(
				IMAPMessageListView.class);
		bind(MessageSendActivity.Displayable.class).to(MessageSendView.class);
		bind(IMAPMessageActivity.Displayable.class).to(IMAPMessageView.class);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
		bind(TopActivity.Displayable.class).to(TopView.class);
>>>>>>> introduce the top activity
		bind(WestActivity.Displayable.class).to(WestView.class);
		bind(IMAPMessageListActivity.Displayable.class).to(IMAPMessageListView.class);
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> 1. improve the inbox folder place.
		
		
=======
=======
		bind(MessageTableModel.class).in(Singleton.class);
>>>>>>> Can fetch messages if click the Refresh button, but a strange issue occur.

>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
		bind(PagingScrollTableRowDragController.class).in(Singleton.class);
=======
>>>>>>> forward and reply message to use RF

<<<<<<< HEAD
		bind(WestActivity.class).in(Singleton.class);
=======
		
=======

>>>>>>> integrate all of the views to their corresponding activities and mappers
		bind(LoginActivity.class).in(Singleton.class);
>>>>>>> remove gwt-incubator dependency in Messages List Model; 
=======
>>>>>>> remove the duplicate bind of LoginActivity
		bind(TopActivity.class).in(Singleton.class);
		bind(WestActivity.class).in(Singleton.class);
		bind(IMAPMessageListActivity.class).in(Singleton.class);
		bind(MessageSendActivity.class).in(Singleton.class);
		bind(IMAPMessageActivity.class).in(Singleton.class);

		bind(MessagesCellTable.class).in(Singleton.class);
		bind(FoldersTreeViewModel.class).in(Singleton.class);
		bind(CellTree.Resources.class).to(CellTree.BasicResources.class);
		// Places
		bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class).in(
				Singleton.class);

		// Application EventBus
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

		// Application Controller
<<<<<<< HEAD
<<<<<<< HEAD
		bind(AppController.class).in(Singleton.class);
<<<<<<< HEAD
		
		bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
<<<<<<< HEAD
>>>>>>> Change to new mvp framework - first step
	}

=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
//		bind(AppController.class).in(Singleton.class);
=======
		// bind(AppController.class).in(Singleton.class);
>>>>>>> integrate all of the views to their corresponding activities and mappers
		bind(HupaController.class).in(Singleton.class);
>>>>>>> move new theme ui from experiment to hupa evo

		// bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
	}
<<<<<<< HEAD
<<<<<<< HEAD

<<<<<<< HEAD
=======
	@Provides
	@Singleton
	@Named("TopRegion")
	public ActivityManager getTopRegionActivityMapper(CachingTopActivityMapper activityMapper,
			EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}
	
>>>>>>> introduce the top activity
=======
	
=======

>>>>>>> integrate all of the views to their corresponding activities and mappers
	@Provides
	@Singleton
	@Named("LoginPage")
	public ActivityManager getLoginActivityMapper(
			LoginActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("TopBarRegion")
	public ActivityManager getTopBarActivityMapper(
			TopBarActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("LogoRegion")
	public ActivityManager getLogoActivityMapper(
			LogoActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}
<<<<<<< HEAD
	
>>>>>>> make login page as one part of the overall layout & splite layout to little one
=======

	@Provides
	@Singleton
	@Named("NavigationRegion")
	public ActivityManager getNavigationActivityMapper(
			NavigationActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("ToolBarRegion")
	public ActivityManager getToolBarActivityMapper(
			ToolBarActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("FolderListRegion")
	public ActivityManager getFolderListActivityMapper(
			FolderListActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("MessageListRegion")
	public ActivityManager getMessageListActivityMapper(
			MessageListActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("MessageListFooterRegion")
	public ActivityManager getMessageListFooterActivityMapper(
			MessageListFooterActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("MessageContentRegion")
	public ActivityManager getMessageContentActivityMapper(
			MessageContentActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("StatusRegion")
	public ActivityManager getStatusActivityMapper(
			StatusActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}
<<<<<<< HEAD

>>>>>>> integrate all of the views to their corresponding activities and mappers
=======
/*
>>>>>>> track the folder list tree model issue of invalid session, TODO how to retrieve folders data using GIN
	@Provides
	@Singleton
	@Named("TopRegion")
	public ActivityManager getTopRegionActivityMapper(
			CachingTopActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
<<<<<<< HEAD
	@Named("MessageContentRegion")
	public ActivityManager getMessageContentActivityMapper(MessageContentActivityMapper activityMapper,
=======
	@Named("WestRegion")
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	public ActivityManager getVerticalMasterRegionActivityMapper(WestActivityMapper activityMapper,
>>>>>>> Change to new mvp framework - first step
=======
	public ActivityManager getWestRegionActivityMapper(WestActivityMapper activityMapper,
>>>>>>> introduce the top activity
=======
	public ActivityManager getWestRegionActivityMapper(CachingWestActivityMapper activityMapper,
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
=======
	}

	@Provides
	@Singleton
	@Named("WestRegion")
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	public ActivityManager getVerticalMasterRegionActivityMapper(WestActivityMapper activityMapper,
>>>>>>> Change to new mvp framework - first step
=======
	public ActivityManager getWestRegionActivityMapper(WestActivityMapper activityMapper,
>>>>>>> introduce the top activity
=======
	public ActivityManager getWestRegionActivityMapper(CachingWestActivityMapper activityMapper,
>>>>>>> At first make the inbox work, but only when click the refresh button. The page also be working, the other folder will be like the same.
			EventBus eventBus) {
=======
	public ActivityManager getWestRegionActivityMapper(CachingWestActivityMapper activityMapper, EventBus eventBus) {
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
	public ActivityManager getWestRegionActivityMapper(WestActivityMapper activityMapper, EventBus eventBus) {
>>>>>>> delete messages, make WestActivity Singleton
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
<<<<<<< HEAD
<<<<<<< HEAD
	@Named("StatusRegion")
	public ActivityManager getStatusActivityMapper(StatusActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("ComposeRegion")
	public ActivityManager getComposeActivityMapper(ComposeActivityMapper activityMapper, EventBus eventBus) {
=======
	public ActivityManager getWestRegionActivityMapper(
			WestActivityMapper activityMapper, EventBus eventBus) {
>>>>>>> integrate all of the views to their corresponding activities and mappers
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("ComposeToolBarRegion")
	public ActivityManager getComposeToolBarActivityMapper(ComposeToolBarActivityMapper activityMapper,
=======
	@Named("MainContentRegion")
<<<<<<< HEAD
	public ActivityManager getVerticalMasterRegionActivityMapper(MainContentActivityMapper activityMapper,
>>>>>>> Change to new mvp framework - first step
=======
	public ActivityManager getMainContentRegionActivityMapper(MainContentActivityMapper activityMapper,
>>>>>>> introduce the top activity
=======
	@Named("MainContentRegion")
<<<<<<< HEAD
<<<<<<< HEAD
	public ActivityManager getVerticalMasterRegionActivityMapper(MainContentActivityMapper activityMapper,
>>>>>>> Change to new mvp framework - first step
=======
	public ActivityManager getMainContentRegionActivityMapper(MainContentActivityMapper activityMapper,
<<<<<<< HEAD
>>>>>>> introduce the top activity
			EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
<<<<<<< HEAD
<<<<<<< HEAD
	@Named("SearchBoxRegion")
	public ActivityManager getSearchBoxActivityMapper(SearchBoxActivityMapper activityMapper,
			EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
=======
	protected DispatchAsync provideDispatchAsync(ExceptionHandler exceptionHandler) {
		return new CachingDispatchAsync(exceptionHandler);
>>>>>>> Change to new mvp framework - first step
=======
	protected DispatchAsync provideDispatchAsync(ExceptionHandler exceptionHandler) {
		return new CachingDispatchAsync(exceptionHandler);
>>>>>>> Change to new mvp framework - first step
	}
=======
	        EventBus eventBus) {
=======
	public ActivityManager getMainContentRegionActivityMapper(
			MainContentActivityMapper activityMapper, EventBus eventBus) {
>>>>>>> integrate all of the views to their corresponding activities and mappers
		return new ActivityManager(activityMapper, eventBus);
	}
<<<<<<< HEAD

<<<<<<< HEAD
	// @Provides
	// @Singleton
	// protected DispatchAsync provideDispatchAsync(ExceptionHandler
	// exceptionHandler) {
	// return new CachingDispatchAsync(exceptionHandler);
	// }
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

=======
>>>>>>> add SuppressWarnings("depraction")
=======
*/
>>>>>>> track the folder list tree model issue of invalid session, TODO how to retrieve folders data using GIN
	@Provides
	@Singleton
	public PlaceController getPlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}

	@Provides
	@Singleton
<<<<<<< HEAD
	public PlaceHistoryHandler getHistoryHandler(PlaceController placeController, PlaceHistoryMapper historyMapper,
	        EventBus eventBus) {
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
<<<<<<< HEAD
<<<<<<< HEAD
		historyHandler.register(placeController, eventBus, new DefaultPlace("@"));
		return historyHandler;
	}

	@Provides
	@Singleton
	HupaRequestFactory getRequestFactory(EventBus eventBus) {
		HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
		rf.initialize(eventBus);
		return rf;
	}

=======
=======
>>>>>>> Change to new mvp framework - first step
=======
	public PlaceHistoryHandler getHistoryHandler(
			PlaceController placeController, PlaceHistoryMapper historyMapper,
			EventBus eventBus) {
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(
				historyMapper);
>>>>>>> integrate all of the views to their corresponding activities and mappers
		historyHandler.register(placeController, eventBus, new DefaultPlace());
		return historyHandler;
	}

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> Change to new mvp framework - first step
=======
>>>>>>> Change to new mvp framework - first step
=======
	@Provides
	@Singleton
	HupaRequestFactory getRequestFactory(EventBus eventBus) {
		HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
		rf.initialize(eventBus);
		return rf;
	}

>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
}
