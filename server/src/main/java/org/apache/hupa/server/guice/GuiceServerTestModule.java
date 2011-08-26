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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import com.sun.mail.imap.IMAPStore;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
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
import org.apache.hupa.server.mock.MockHttpSession;
import org.apache.hupa.server.mock.MockHttpSessionProvider;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockLogProvider;
import org.apache.hupa.server.preferences.InSessionUserPreferencesStorage;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.Contacts;
import org.apache.hupa.shared.rpc.SendMessage;

import java.util.Properties;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

/**
 * Guice module used in tests.
 * 
 * Extend this class with your own, and redefine protected attributes 
 * if you need to change any of the class implementations in your test.
 * 
 * @author manolo
 *
 */
public class GuiceServerTestModule extends ActionHandlerModule {
        
    protected Class<? extends Provider<Session>> sessionClass = SessionProvider.class;
    protected Class<? extends HttpSession> httpSessionClass = MockHttpSession.class;
    protected Class<? extends Provider<Log>> logClass = MockLogProvider.class;
    protected Class<? extends IMAPStore> imapStoreClass = MockIMAPStore.class; 
    protected Class<? extends IMAPStoreCache> imapStoreCacheClass = InMemoryIMAPStoreCache.class; 
    protected Class<? extends Provider<Settings>> settingsProviderClass = DefaultUserSettingsProvider.class;
    protected Class<? extends UserPreferencesStorage> userPreferencesClass = InSessionUserPreferencesStorage.class;
    protected Properties properties = DemoModeConstants.demoProperties;

    @Override
    protected void configureHandlers() {
        Names.bindProperties(binder(), properties);
        
        bind(Session.class).toProvider(sessionClass);
        bind(HttpSession.class).toProvider(MockHttpSessionProvider.class);
        bind(Settings.class).toProvider(settingsProviderClass).in(Singleton.class);
        bind(Log.class).toProvider(logClass).in(Singleton.class);

        bind(IMAPStore.class).to(imapStoreClass);
        bind(IMAPStoreCache.class).to(imapStoreCacheClass).in(Singleton.class);

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
        
        bind(UserPreferencesStorage.class).to(userPreferencesClass);
        
        bind(User.class).to(TestUser.class).in(Singleton.class);
        
    }

    static class TestUser extends User {
        private static final long serialVersionUID = 1L;
        @Inject
        public TestUser(@Named("Username") String username, 
                        @Named("Password") String password, 
                        @Named("DefaultInboxFolder") final String inbox, 
                        @Named("DefaultSentFolder") final String sent,
                        @Named("DefaultTrashFolder") final String trash, 
                        @Named("DefaultDraftsFolder") final String draft) {
            setName(username);
            setPassword(password);
            setSettings(new Settings() {
                private static final long serialVersionUID = 1L;
                {
                    setInboxFolderName(inbox);
                    setSentFolderName(sent);
                    setTrashFolderName(trash);
                    setDraftsFolderName(draft);
                }
            });
        }
    }
    
    
    /**
     * Configuration of a Apache-James server.
     * Customize it for your integration tests.
     */
    public static final Properties jamesProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            /// SET THIS 
            put("Username","manolo");
            put("Password","***");
            ///
            
            put("IMAPServerAddress", "localhost");
            put("IMAPServerPort", "143");
            put("IMAPS", "false");
            
            put("TrustStore", "my-truststore");
            put("TrustStorePassword", "my-truststore-password");
            
            put("IMAPConnectionPoolSize", "4");
            put("IMAPConnectionPoolTimeout", "300000");
            
            put("DefaultInboxFolder", "INBOX");
            put("DefaultTrashFolder", "Trash");
            put("DefaultSentFolder", "Sent");
            put("DefaultDraftsFolder", "Drafts");
            
            put("PostFetchMessageCount", "0");

            put("SMTPServerAddress", "localhost");
            put("SMTPServerPort", "25");
            put("SMTPS", "false");
            put("SMTPAuth", "true");
            
            put("SessionDebug", "false");
            
            put("DefaultUserSessionId", "just_an_id");
        }
    };
    
    /**
     * Configuration of an IMAP server.
     * Customize it for your integration tests.
     */
    public static final Properties courierProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            /// SET THIS 
            put("Username","user");
            put("Password","password");
            ///
            
            put("IMAPServerAddress", "localhost");
            put("IMAPServerPort", "143");
            put("IMAPS", "false");
            
            put("TrustStore", "my-truststore");
            put("TrustStorePassword", "my-truststore-password");
            
            put("IMAPConnectionPoolSize", "4");
            put("IMAPConnectionPoolTimeout", "300000");
            
            put("DefaultInboxFolder", "INBOX");
            put("DefaultTrashFolder", "INBOX.Trash");
            put("DefaultSentFolder", "INBOX.Sent");
            put("DefaultDraftsFolder", "INBOX.Drafts");
            
            put("PostFetchMessageCount", "0");

            put("SMTPServerAddress", "mail.hotelsearch.com");
            put("SMTPServerPort", "25");
            put("SMTPS", "false");
            put("SMTPAuth", "true");
            
            put("SessionDebug", "false");
            
            put("DefaultUserSessionId", "just_an_id");
        }
    };
    
    /**
     * Configuration of GMail IMAP server.
     */
    public static final Properties gmailProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            ///// Use a valid gmail account 
            put("Username","doodootis@gmail.com");
            put("Password","******");
            /////
            
            put("IMAPServerAddress", "imap.gmail.com");
            put("IMAPServerPort", "993");
            put("IMAPS", "true");
            
            put("TrustStore", "my-truststore");
            put("TrustStorePassword", "my-truststore-password");
            
            put("IMAPConnectionPoolSize", "4");
            put("IMAPConnectionPoolTimeout", "300000");
            
            put("DefaultInboxFolder", "INBOX");
            put("DefaultTrashFolder", "[Gmail]/Trash");
            put("DefaultSentFolder", "[Gmail]/Sent Mail");
            put("DefaultDraftsFolder", "[Gmail]/Drafts");
            
            put("PostFetchMessageCount", "0");

            put("SMTPServerAddress", "smtp.gmail.com");
            put("SMTPServerPort", "465");
            put("SMTPS", "true");
            put("SMTPAuth", "true");
            
            put("SessionDebug", "false");
            
            put("DefaultUserSessionId", "just_an_id");
        }
    };

}
