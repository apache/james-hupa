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

package org.apache.hupa.server.guice;

import java.util.Properties;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.demo.DemoGuiceServerModule.DemoIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.handler.AbstractSendMessageHandler;
import org.apache.hupa.server.handler.ContactsHandler;
import org.apache.hupa.server.handler.CreateFolderHandler;
import org.apache.hupa.server.handler.DeleteFolderHandler;
import org.apache.hupa.server.handler.DeleteMessageByUidHandler;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.handler.ForwardMessageHandler;
import org.apache.hupa.server.handler.GetMessageDetailsHandler;
import org.apache.hupa.server.handler.IdleHandler;
import org.apache.hupa.server.handler.LoginUserHandler;
import org.apache.hupa.server.handler.LogoutUserHandler;
import org.apache.hupa.server.handler.ReplyMessageHandler;
import org.apache.hupa.server.handler.SendMessageHandler;
import org.apache.hupa.server.mock.MockConstants;
import org.apache.hupa.server.mock.MockHttpSessionProvider;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockLogProvider;
import org.apache.hupa.server.preferences.InSessionUserPreferencesStorage;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.ConfigurationProperties;
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.Contacts;
import org.apache.hupa.shared.rpc.SendMessage;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.sun.mail.imap.IMAPStore;

/**
 * Guice module used in server tests.
 */
public class GuiceServerTestModule extends AbstractGuiceTestModule {

  @Override
  protected void configureHandlers() {
      Properties properties = MockConstants.mockProperties;
      ConfigurationProperties.validateProperties(properties);

      Names.bindProperties(binder(), properties);
      
      bind(Session.class).toProvider(JavaMailSessionProvider.class);
      bind(HttpSession.class).toProvider(MockHttpSessionProvider.class);
      bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(Singleton.class);
      bind(Log.class).toProvider(MockLogProvider.class).in(Singleton.class);

      bind(IMAPStore.class).to(MockIMAPStore.class);
      bind(IMAPStoreCache.class).to(DemoIMAPStoreCache.class).in(Singleton.class);

      bind(LoginUserHandler.class);
      bind(LogoutUserHandler.class);
      bind(IdleHandler.class);
      
      bind(FetchFoldersHandler.class);
      bind(CreateFolderHandler.class);
      bind(DeleteFolderHandler.class);
      bind(FetchMessagesHandler.class);
      bind(DeleteMessageByUidHandler.class);
      bind(GetMessageDetailsHandler.class);
      bind(AbstractSendMessageHandler.class).to(SendMessageHandler.class);
      bind(SendMessageHandler.class);
      bind(ReplyMessageHandler.class);
      bind(ForwardMessageHandler.class);
      
      bindHandler(Contacts.class, ContactsHandler.class);
      bindHandler(SendMessage.class, SendMessageHandler.class);
      
      bind(UserPreferencesStorage.class).to(InSessionUserPreferencesStorage.class);
      
      bind(User.class).to(TestUser.class).in(Singleton.class);
      bind(Properties.class).toInstance(properties);

  }

}