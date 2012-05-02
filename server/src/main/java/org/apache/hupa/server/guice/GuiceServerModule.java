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

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.server.handler.CheckSessionHandler;
import org.apache.hupa.server.handler.ContactsHandler;
import org.apache.hupa.server.handler.CreateFolderHandler;
import org.apache.hupa.server.handler.DeleteAllMessagesHandler;
import org.apache.hupa.server.handler.DeleteFolderHandler;
import org.apache.hupa.server.handler.DeleteMessageByUidHandler;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.handler.FetchRecentMessagesHandler;
import org.apache.hupa.server.handler.ForwardMessageHandler;
import org.apache.hupa.server.handler.GetMessageDetailsHandler;
import org.apache.hupa.server.handler.IdleHandler;
import org.apache.hupa.server.handler.LoginUserHandler;
import org.apache.hupa.server.handler.LogoutUserHandler;
import org.apache.hupa.server.handler.MoveMessageHandler;
import org.apache.hupa.server.handler.RenameFolderHandler;
import org.apache.hupa.server.handler.ReplyMessageHandler;
import org.apache.hupa.server.handler.SendMessageHandler;
import org.apache.hupa.server.handler.SetFlagsHandler;
import org.apache.hupa.server.handler.TagMessagesHandler;
import org.apache.hupa.server.preferences.InImapUserPreferencesStorage;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.servlet.DownloadAttachmentServlet;
import org.apache.hupa.server.servlet.MessageSourceServlet;
import org.apache.hupa.server.servlet.UploadAttachmentServlet;
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.rpc.CheckSession;
import org.apache.hupa.shared.rpc.Contacts;
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.DeleteAllMessages;
import org.apache.hupa.shared.rpc.DeleteFolder;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchMessages;
import org.apache.hupa.shared.rpc.FetchRecentMessages;
import org.apache.hupa.shared.rpc.ForwardMessage;
import org.apache.hupa.shared.rpc.GetMessageDetails;
import org.apache.hupa.shared.rpc.Idle;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LogoutUser;
import org.apache.hupa.shared.rpc.MoveMessage;
import org.apache.hupa.shared.rpc.RenameFolder;
import org.apache.hupa.shared.rpc.ReplyMessage;
import org.apache.hupa.shared.rpc.SendMessage;
import org.apache.hupa.shared.rpc.SetFlag;
import org.apache.hupa.shared.rpc.TagMessage;

import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * Module which binds the handlers and configurations
 * 
 * 
 */
public class GuiceServerModule extends ActionHandlerModule {

    Properties properties;
    public GuiceServerModule(Properties properties) {
        this.properties = properties;
    }
    
    protected Class<? extends IMAPStoreCache> getIMAPStoreCacheClass() {
        return InMemoryIMAPStoreCache.class;
    }

    @Override
    protected void configureHandlers() {
        try {
            // Bind addresses and ports for imap and smtp
            Names.bindProperties(binder(), properties);
        } catch (Exception e) {
            throw new RuntimeException("Unable to to configure hupa server," +
                    "\nmake sure that you have a valid /etc/default/hupa file" +
                    "\nor the web container has been started with the appropriate parameter:" +
                    " -Dhupa.config.file=your_hupa_properties_file", e);
        }
        
        bindHandler(CheckSession.class, CheckSessionHandler.class);
        bindHandler(LoginUser.class, LoginUserHandler.class);
        bindHandler(FetchFolders.class, FetchFoldersHandler.class);
        bindHandler(FetchMessages.class, FetchMessagesHandler.class);
        bindHandler(FetchRecentMessages.class, FetchRecentMessagesHandler.class);
        bindHandler(LogoutUser.class, LogoutUserHandler.class);
        bindHandler(GetMessageDetails.class, GetMessageDetailsHandler.class);
        bindHandler(DeleteMessageByUid.class, DeleteMessageByUidHandler.class);
        bindHandler(DeleteAllMessages.class, DeleteAllMessagesHandler.class);
        bindHandler(SendMessage.class, SendMessageHandler.class);
        bindHandler(ReplyMessage.class, ReplyMessageHandler.class);
        bindHandler(ForwardMessage.class, ForwardMessageHandler.class);
        bindHandler(Idle.class, IdleHandler.class);
        bindHandler(MoveMessage.class, MoveMessageHandler.class);
        bindHandler(RenameFolder.class, RenameFolderHandler.class);
        bindHandler(DeleteFolder.class, DeleteFolderHandler.class);
        bindHandler(CreateFolder.class, CreateFolderHandler.class);
        bindHandler(TagMessage.class, TagMessagesHandler.class);
        bindHandler(SetFlag.class, SetFlagsHandler.class);
        bindHandler(Contacts.class, ContactsHandler.class);
        
        bind(IMAPStoreCache.class).to(getIMAPStoreCacheClass()).in(Singleton.class);
        
        bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
        bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(
                Singleton.class);
        bind(DownloadAttachmentServlet.class).in(Singleton.class);
        bind(UploadAttachmentServlet.class).in(Singleton.class);
        bind(MessageSourceServlet.class).in(Singleton.class);
        bind(Session.class).toProvider(JavaMailSessionProvider.class);
        bind(UserPreferencesStorage.class).to(InImapUserPreferencesStorage.class);
        bind(Properties.class).toInstance(properties);
    }
}
