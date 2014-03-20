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

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
import org.apache.hupa.server.ioc.demo.DemoGuiceServerModule.DemoIMAPStoreCache;
import org.apache.hupa.server.mock.MockConstants;
import org.apache.hupa.server.mock.MockHttpSessionProvider;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockLogProvider;
import org.apache.hupa.server.preferences.InSessionUserPreferencesStorage;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.service.CheckSessionServiceImpl;
import org.apache.hupa.server.service.CreateFolderServiceImpl;
import org.apache.hupa.server.service.DeleteFolderServiceImpl;
import org.apache.hupa.server.service.DeleteMessageAllServiceImpl;
import org.apache.hupa.server.service.DeleteMessageByUidServiceImpl;
import org.apache.hupa.server.service.FetchFoldersService;
import org.apache.hupa.server.service.FetchFoldersServiceImpl;
import org.apache.hupa.server.service.FetchMessagesServiceImpl;
import org.apache.hupa.server.service.GetMessageDetailsServiceImpl;
import org.apache.hupa.server.service.GetMessageRawServiceImpl;
import org.apache.hupa.server.service.ImapFolderServiceImpl;
import org.apache.hupa.server.service.LoginUserServiceImpl;
import org.apache.hupa.server.service.LogoutUserServiceImpl;
import org.apache.hupa.server.service.MoveMessageServiceImpl;
import org.apache.hupa.server.service.RenameFolderServiceImpl;
import org.apache.hupa.server.service.SendForwardMessageServiceImpl;
import org.apache.hupa.server.service.SendMessageBaseServiceImpl;
import org.apache.hupa.server.service.SendReplyMessageServiceImpl;
import org.apache.hupa.server.service.SetFlagServiceImpl;
import org.apache.hupa.server.servlet.DownloadAttachmentServlet;
import org.apache.hupa.server.servlet.MessageSourceServlet;
import org.apache.hupa.server.servlet.UploadAttachmentServlet;
import org.apache.hupa.server.utils.ConfigurationProperties;
import org.apache.hupa.shared.data.CreateFolderActionImpl;
import org.apache.hupa.shared.data.DeleteFolderActionImpl;
import org.apache.hupa.shared.data.DeleteMessageAllActionImpl;
import org.apache.hupa.shared.data.DeleteMessageByUidActionImpl;
import org.apache.hupa.shared.data.FetchMessagesActionImpl;
import org.apache.hupa.shared.data.FetchMessagesResultImpl;
import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.data.GetMessageDetailsActionImpl;
import org.apache.hupa.shared.data.GetMessageDetailsResultImpl;
import org.apache.hupa.shared.data.GetMessageRawActionImpl;
import org.apache.hupa.shared.data.GetMessageRawResultImpl;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.LogoutUserActionImpl;
import org.apache.hupa.shared.data.MailHeaderImpl;
import org.apache.hupa.shared.data.MessageAttachmentImpl;
import org.apache.hupa.shared.data.MessageDetailsImpl;
import org.apache.hupa.shared.data.MoveMessageActionImpl;
import org.apache.hupa.shared.data.RenameFolderActionImpl;
import org.apache.hupa.shared.data.SendForwardMessageActionImpl;
import org.apache.hupa.shared.data.SendMessageActionImpl;
import org.apache.hupa.shared.data.SendReplyMessageActionImpl;
import org.apache.hupa.shared.data.SetFlagActionImpl;
import org.apache.hupa.shared.data.SmtpMessageImpl;
import org.apache.hupa.shared.data.TagImpl;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.sun.mail.imap.IMAPStore;

public class GuiceServerTestModule extends AbstractGuiceTestModule {

    protected Class<? extends UserPreferencesStorage> userPreferencesStorageClass = InSessionUserPreferencesStorage.class;
    protected Class<? extends Provider<Log>> logProviderClass = MockLogProvider.class;
    protected Properties properties = MockConstants.mockProperties;
    @Override
    protected void configure() {

        ConfigurationProperties.validateProperties(properties);
        Names.bindProperties(binder(), properties);

        bind(HttpSession.class).toProvider(MockHttpSessionProvider.class);
        bind(Settings.class).toProvider(DefaultUserSettingsProvider.class);
        bind(Log.class).toProvider(logProviderClass).in(Singleton.class);


        if (properties == MockConstants.mockProperties) {
            bind(IMAPStoreCache.class).to(DemoIMAPStoreCache.class).in(
                    Singleton.class);
            bind(IMAPStore.class).to(MockIMAPStore.class);
        } else {
            bind(IMAPStoreCache.class).to(InMemoryIMAPStoreCache.class).in(
                    Singleton.class);
        }


        bind(MailHeaderImpl.class);

        bind(UserImpl.class);
        bind(ImapFolder.class).to(ImapFolderImpl.class);
        bind(TagImpl.class);
        bind(MessageDetailsImpl.class);
        bind(MessageAttachmentImpl.class);
        bind(SmtpMessageImpl.class);

        bind(CreateFolderAction.class).to(CreateFolderActionImpl.class);
        bind(GenericResultImpl.class);
        bind(FetchMessagesActionImpl.class);
        bind(FetchMessagesResultImpl.class);
//        bind(CreateFolderActionImpl.class);
        bind(DeleteFolderActionImpl.class);
        bind(RenameFolderActionImpl.class);
        bind(DeleteMessageAllActionImpl.class);
        bind(DeleteMessageByUidActionImpl.class);
        bind(GetMessageDetailsActionImpl.class);
        bind(GetMessageDetailsResultImpl.class);
        bind(SendMessageActionImpl.class);
        bind(SendForwardMessageActionImpl.class);
        bind(SendReplyMessageActionImpl.class);
        bind(GetMessageRawActionImpl.class);
        bind(GetMessageRawResultImpl.class);
        bind(LogoutUserActionImpl.class);
        bind(MoveMessageActionImpl.class);
        bind(SetFlagActionImpl.class);



        bind(CheckSessionServiceImpl.class);
        bind(LoginUserServiceImpl.class);
        bind(ImapFolderServiceImpl.class);
        bind(FetchFoldersServiceImpl.class);
        bind(FetchMessagesServiceImpl.class);
        bind(CreateFolderServiceImpl.class);
        bind(DeleteFolderServiceImpl.class);
        bind(RenameFolderServiceImpl.class);
        bind(DeleteMessageAllServiceImpl.class);
        bind(DeleteMessageByUidServiceImpl.class);
        bind(GetMessageDetailsServiceImpl.class);
        bind(SendMessageBaseServiceImpl.class);
        bind(SendForwardMessageServiceImpl.class);
        bind(SendReplyMessageServiceImpl.class);
        bind(GetMessageRawServiceImpl.class);
        bind(LogoutUserServiceImpl.class);
        bind(MoveMessageServiceImpl.class);
        bind(SetFlagServiceImpl.class);

        bind(FetchFoldersService.class).to(FetchFoldersServiceImpl.class);


        bind(DownloadAttachmentServlet.class).in(Singleton.class);
        bind(UploadAttachmentServlet.class).in(Singleton.class);
        bind(MessageSourceServlet.class).in(Singleton.class);

        bind(UserPreferencesStorage.class).to(userPreferencesStorageClass);
        bind(User.class).to(TestUser.class).in(Singleton.class);

    }

   @Provides
    protected Properties getProperties() {
        System.out.println("getProperties");
        return (Properties)properties.clone();
    }

}
