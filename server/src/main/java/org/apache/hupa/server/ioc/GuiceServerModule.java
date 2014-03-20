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

package org.apache.hupa.server.ioc;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.server.preferences.InSessionUserPreferencesStorage;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.service.CheckSessionService;
import org.apache.hupa.server.service.CheckSessionServiceImpl;
import org.apache.hupa.server.service.CreateFolderService;
import org.apache.hupa.server.service.CreateFolderServiceImpl;
import org.apache.hupa.server.service.DeleteFolderService;
import org.apache.hupa.server.service.DeleteFolderServiceImpl;
import org.apache.hupa.server.service.DeleteMessageAllService;
import org.apache.hupa.server.service.DeleteMessageAllServiceImpl;
import org.apache.hupa.server.service.DeleteMessageByUidService;
import org.apache.hupa.server.service.DeleteMessageByUidServiceImpl;
import org.apache.hupa.server.service.FetchFoldersService;
import org.apache.hupa.server.service.FetchFoldersServiceImpl;
import org.apache.hupa.server.service.FetchMessagesService;
import org.apache.hupa.server.service.FetchMessagesServiceImpl;
import org.apache.hupa.server.service.GetMessageDetailsService;
import org.apache.hupa.server.service.GetMessageDetailsServiceImpl;
import org.apache.hupa.server.service.GetMessageRawService;
import org.apache.hupa.server.service.GetMessageRawServiceImpl;
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.server.service.ImapFolderServiceImpl;
import org.apache.hupa.server.service.LoginUserService;
import org.apache.hupa.server.service.LoginUserServiceImpl;
import org.apache.hupa.server.service.LogoutUserService;
import org.apache.hupa.server.service.LogoutUserServiceImpl;
import org.apache.hupa.server.service.MoveMessageService;
import org.apache.hupa.server.service.MoveMessageServiceImpl;
import org.apache.hupa.server.service.RenameFolderService;
import org.apache.hupa.server.service.RenameFolderServiceImpl;
import org.apache.hupa.server.service.SendForwardMessageService;
import org.apache.hupa.server.service.SendForwardMessageServiceImpl;
import org.apache.hupa.server.service.SendMessageBaseServiceImpl;
import org.apache.hupa.server.service.SendMessageService;
import org.apache.hupa.server.service.SendReplyMessageService;
import org.apache.hupa.server.service.SendReplyMessageServiceImpl;
import org.apache.hupa.server.service.SetFlagService;
import org.apache.hupa.server.service.SetFlagServiceImpl;
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
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.GetMessageRawAction;
import org.apache.hupa.shared.domain.GetMessageRawResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.LogoutUserAction;
import org.apache.hupa.shared.domain.MailHeader;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.MoveMessageAction;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.domain.SendForwardMessageAction;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SendReplyMessageAction;
import org.apache.hupa.shared.domain.SetFlagAction;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.Tag;
import org.apache.hupa.shared.domain.User;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

/**
 */
public class GuiceServerModule extends AbstractModule {

    Properties properties;

    public GuiceServerModule(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {

        try {
            // Bind addresses and ports for imap and smtp
            Names.bindProperties(binder(), properties);
        } catch (Exception e) {
            throw new RuntimeException("Unable to to configure hupa server,"
                    + "\nmake sure that you have a valid /etc/default/hupa file"
                    + "\nor the web container has been started with the appropriate parameter:"
                    + " -Dhupa.config.file=your_hupa_properties_file", e);
        }
        bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
        bind(ServiceLayerDecorator.class).to(IocRfServiceDecorator.class);
        bind(IocRfServiceLocator.class);

        bind(MailHeader.class).to(MailHeaderImpl.class);

        bind(User.class).to(UserImpl.class);
        bind(Settings.class).toProvider(DefaultUserSettingsProvider.class);
        bind(ImapFolder.class).to(ImapFolderImpl.class);
        bind(Tag.class).to(TagImpl.class);
        bind(MessageDetails.class).to(MessageDetailsImpl.class);
        bind(MessageAttachment.class).to(MessageAttachmentImpl.class);
        bind(SmtpMessage.class).to(SmtpMessageImpl.class);

        bind(GenericResult.class).to(GenericResultImpl.class);
        bind(FetchMessagesAction.class).to(FetchMessagesActionImpl.class);
        bind(FetchMessagesResult.class).to(FetchMessagesResultImpl.class);
        bind(CreateFolderAction.class).to(CreateFolderActionImpl.class);
        bind(DeleteFolderAction.class).to(DeleteFolderActionImpl.class);
        bind(RenameFolderAction.class).to(RenameFolderActionImpl.class);
        bind(DeleteMessageAllAction.class).to(DeleteMessageAllActionImpl.class);
        bind(DeleteMessageByUidAction.class).to(DeleteMessageByUidActionImpl.class);
        bind(GetMessageDetailsAction.class).to(GetMessageDetailsActionImpl.class);
        bind(GetMessageDetailsResult.class).to(GetMessageDetailsResultImpl.class);
        bind(SendMessageAction.class).to(SendMessageActionImpl.class);
        bind(SendForwardMessageAction.class).to(SendForwardMessageActionImpl.class);
        bind(SendReplyMessageAction.class).to(SendReplyMessageActionImpl.class);
        bind(GetMessageRawAction.class).to(GetMessageRawActionImpl.class);
        bind(GetMessageRawResult.class).to(GetMessageRawResultImpl.class);
        bind(LogoutUserAction.class).to(LogoutUserActionImpl.class);
        bind(MoveMessageAction.class).to(MoveMessageActionImpl.class);
        bind(SetFlagAction.class).to(SetFlagActionImpl.class);

        bind(FetchMessagesService.class).to(FetchMessagesServiceImpl.class);
        bind(SendMessageService.class).to(SendMessageBaseServiceImpl.class);
        bind(SendForwardMessageService.class).to(SendForwardMessageServiceImpl.class);
        bind(SendReplyMessageService.class).to(SendReplyMessageServiceImpl.class);
        bind(GetMessageDetailsService.class).to(GetMessageDetailsServiceImpl.class);


        bind(CheckSessionService.class).to(CheckSessionServiceImpl.class);
        bind(LoginUserService.class).to(LoginUserServiceImpl.class);
        bind(ImapFolderService.class).to(ImapFolderServiceImpl.class);
        bind(FetchFoldersService.class).to(FetchFoldersServiceImpl.class);
        bind(CreateFolderService.class).to(CreateFolderServiceImpl.class);
        bind(DeleteFolderService.class).to(DeleteFolderServiceImpl.class);
        bind(RenameFolderService.class).to(RenameFolderServiceImpl.class);
        bind(DeleteMessageAllService.class).to(DeleteMessageAllServiceImpl.class);
        bind(DeleteMessageByUidService.class).to(DeleteMessageByUidServiceImpl.class);
        bind(GetMessageRawService.class).to(GetMessageRawServiceImpl.class);
        bind(LogoutUserService.class).to(LogoutUserServiceImpl.class);
        bind(MoveMessageService.class).to(MoveMessageServiceImpl.class);
        bind(SetFlagService.class).to(SetFlagServiceImpl.class);

        bind(IMAPStoreCache.class).to(getIMAPStoreCacheClass()).in(Singleton.class);

        bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
        bind(UserPreferencesStorage.class).to(InSessionUserPreferencesStorage.class);
        bind(Properties.class).toInstance(properties);
    }

    protected Class<? extends IMAPStoreCache> getIMAPStoreCacheClass() {
        return InMemoryIMAPStoreCache.class;
    }
}
