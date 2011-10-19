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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
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
import org.apache.hupa.server.utils.ConfigurationProperties;
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

    public static final String SYS_PROP_CONFIG_FILE = "hupa.config.file";

    public static final String CONFIG_FILE_NAME = "config.properties";
    public static final String[] CONFIG_PROPERTIES = {
            System.getenv("HOME") + "/.hupa/" + CONFIG_FILE_NAME,
            "/etc/default/hupa"
    };
    public static final String CONF_DIR = "WEB-INF/conf/";

    private String configDir;
    
    public GuiceServerModule(String rootPath) {
        configDir = rootPath + "/" + CONF_DIR;
    }

    @Override
    protected void configureHandlers() {
        Properties properties;
        try {
            // Bind addresses and ports for imap and smtp
            properties = loadProperties();
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
        bind(IMAPStoreCache.class).to(InMemoryIMAPStoreCache.class).in(
                Singleton.class);
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

    protected Properties loadProperties() throws Exception {
        Properties properties = null;

        String fileName = System.getProperty(SYS_PROP_CONFIG_FILE);
        if (fileName != null) {
            properties = loadProperties(fileName);
        }

        if (properties == null) {
            for (String name : CONFIG_PROPERTIES) {
                properties = loadProperties(name);
                if (properties != null)
                    break;
            }
        }

        if (properties == null) {
            properties = loadProperties(configDir + CONFIG_FILE_NAME);
        }

        // Validate for mandatory and complete properties with default values
        return validateProperties(properties);
    }

    protected Properties validateProperties(Properties properties) {
        List<String> errors = new ArrayList<String>();

        // Test for mandatory and complete properties with default values when
        // missing
        for (ConfigurationProperties confProps : ConfigurationProperties
                .values()) {
            if (confProps.isMandatory()) {
                if (properties.get(confProps.getProperty()) == null) {
                    errors.add("The mandatory Property '"
                            + confProps.getProperty() + "' is not set.");
                }
            } else if (properties.get(confProps.getProperty()) == null) {
                properties.setProperty(confProps.getProperty(),
                    confProps.getPropValue());
            }
        }

        // Test for unknown properties set in configuration
        for (Object key : properties.keySet()) {
            if (ConfigurationProperties.lookup((String) key) == null) {
                errors.add("The Property '" + key
                        + "' has no configuration impacts, it's unknown");
            }
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }

        return properties;
    }
    
    protected Properties loadProperties(String name) {

        if (name == null)
            return null;

        Properties properties = null;
        File file = new File(name);
        
        // check if the file is absolute. If not prefix it with the default config dir
        if (file.isAbsolute() == false) {
            file = new File(configDir + File.separator + file.getName());
        }
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                properties = new Properties();
                fis = new FileInputStream(file);
                properties.load(fis);
            } catch (Exception e) {
                properties = null;    
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // Empty on purpose
                    }
                }
            }
        }
        
        return properties;
    }

}
