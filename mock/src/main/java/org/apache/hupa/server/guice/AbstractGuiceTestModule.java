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

import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.Settings;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Abstract Guice module.
 * Extend this class with your own, and set bindings to your need. E.g.
 *
 * <pre>
 *   @Override
 *   protected void configureHandlers() {
 *       Names.bindProperties(binder(), properties);
 *
 *       bind(Session.class).toProvider(sessionClass);
 *       bind(HttpSession.class).toProvider(MockHttpSessionProvider.class);
 *       bind(Settings.class).toProvider(settingsProviderClass).in(Singleton.class);
 *       bind(Log.class).toProvider(logClass).in(Singleton.class);
 *
 *       bind(IMAPStore.class).to(imapStoreClass);
 *       bind(IMAPStoreCache.class).to(imapStoreCacheClass).in(Singleton.class);
 *
 *       bind(LoginUserHandler.class);
 *       bind(LogoutUserHandler.class);
 *       bind(IdleHandler.class);
 *
 *       bind(FetchFoldersHandler.class);
 *       bind(CreateFolderHandler.class);
 *       bind(DeleteFolderHandler.class);
 *       bind(FetchMessagesHandler.class);
 *       bind(DeleteMessageByUidHandler.class);
 *       bind(GetMessageDetailsHandler.class);
 *       bind(AbstractSendMessageHandler.class).to(SendMessageHandler.class);
 *       bind(SendMessageHandler.class);
 *       bind(ReplyMessageHandler.class);
 *       bind(ForwardMessageHandler.class);
 *
 *       bindHandler(Contacts.class, ContactsHandler.class);
 *       bindHandler(SendMessage.class, SendMessageHandler.class);
 *
 *       bind(UserPreferencesStorage.class).to(userPreferencesClass);
 *
 *       bind(User.class).to(TestUser.class).in(Singleton.class);
 *
 *   }
 * </pre>
 *
 * @author manolo
 *
 */
public abstract class AbstractGuiceTestModule extends AbstractModule{

    protected static class TestUser extends UserImpl {

        @Inject
        public TestUser(@Named("Username") String username,
                        @Named("Password") String password,
                        Settings settings) {
            setName(username);
            setPassword(password);
            setSettings(settings);
        }
    }


    /**
     * Configuration of a Apache-James server.
     * Customize it for your integration tests.
     */
    public static final Properties jamesProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            // SET THIS
            put("Username","manolo");
            put("Password","***");
            ///

            put("IMAPServerAddress", "localhost");
            put("IMAPServerPort", "143");
            put("IMAPS", "false");

            put("DefaultInboxFolder", "INBOX");
            put("DefaultTrashFolder", "Trash");
            put("DefaultSentFolder", "Sent");
            put("DefaultDraftsFolder", "Drafts");

            put("SMTPServerAddress", "localhost");
            put("SMTPServerPort", "25");
            put("SMTPS", "false");
            put("SMTPAuth", "true");

            put("SessionDebug", "true");
            put("TrustSSL", "false");
        }
    };

    /**
     * Configuration of an IMAP server.
     * Customize it for your integration tests.
     */
    public static final Properties courierProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            // SET THIS
            put("Username","user");
            put("Password","password");
            ///

            put("IMAPServerAddress", "localhost");
            put("IMAPServerPort", "143");
            put("IMAPS", "false");

            put("DefaultInboxFolder", "INBOX");
            put("DefaultTrashFolder", "INBOX.Trash");
            put("DefaultSentFolder", "INBOX.Sent");
            put("DefaultDraftsFolder", "INBOX.Drafts");

            put("SMTPServerAddress", "mail.hotelsearch.com");
            put("SMTPServerPort", "25");
            put("SMTPS", "false");
            put("SMTPAuth", "true");

            put("SessionDebug", "true");
            put("TrustSSL", "false");
        }
    };

    /**
     * Configuration of GMail IMAP server.
     */
    public static final Properties gmailProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            // Use a valid gmail account
            put("Username","doodootis@gmail.com");
            put("Password","******");
            /////

            put("IMAPServerAddress", "imap.gmail.com");
            put("IMAPServerPort", "993");
            put("IMAPS", "true");

            put("IMAPConnectionPoolSize", "4");
            put("IMAPConnectionPoolTimeout", "300000");

            put("DefaultInboxFolder", "INBOX");
            put("DefaultTrashFolder", "[Gmail]/Trash");
            put("DefaultSentFolder", "[Gmail]/Sent Mail");
            put("DefaultDraftsFolder", "[Gmail]/Drafts");

            put("SMTPServerAddress", "smtp.gmail.com");
            put("SMTPServerPort", "465");
            put("SMTPS", "true");
            put("SMTPAuth", "true");

            put("SessionDebug", "true");
            put("TrustSSL", "false");
        }
    };

}
