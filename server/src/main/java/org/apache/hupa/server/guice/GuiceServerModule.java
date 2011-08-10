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

import com.google.inject.Singleton;
import com.google.inject.name.Names;

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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;

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
    
    private boolean demoMode = false;
    
    public GuiceServerModule(String rootPath) {
        configDir = rootPath + "/" + CONF_DIR;
    }

    @Override
    protected void configureHandlers() {
        bindHandler(CheckSessionHandler.class);
        bindHandler(LoginUserHandler.class);
        bindHandler(FetchFoldersHandler.class);
        bindHandler(FetchMessagesHandler.class);
        bindHandler(FetchRecentMessagesHandler.class);
        bindHandler(LogoutUserHandler.class);
        bindHandler(GetMessageDetailsHandler.class);
        bindHandler(DeleteMessageByUidHandler.class);
        bindHandler(DeleteAllMessagesHandler.class);
        bindHandler(SendMessageHandler.class);
        bindHandler(ReplyMessageHandler.class);
        bindHandler(ForwardMessageHandler.class);
        bindHandler(IdleHandler.class);
        bindHandler(MoveMessageHandler.class);
        bindHandler(RenameFolderHandler.class);
        bindHandler(DeleteFolderHandler.class);
        bindHandler(CreateFolderHandler.class);
        bindHandler(TagMessagesHandler.class);
        bindHandler(SetFlagsHandler.class);
        bindHandler(ContactsHandler.class);
        bind(IMAPStoreCache.class).to(InMemoryIMAPStoreCache.class).in(
                Singleton.class);
        bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
        bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(
                Singleton.class);
        bind(DownloadAttachmentServlet.class).in(Singleton.class);
        bind(UploadAttachmentServlet.class).in(Singleton.class);
        bind(MessageSourceServlet.class).in(Singleton.class);
        bind(Session.class).toProvider(SessionProvider.class);
        bind(UserPreferencesStorage.class).to(InImapUserPreferencesStorage.class);

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

        // Put Hupa in demo mode
        demoMode = DemoModeConstants.DEMO_MODE.equals(properties.get("IMAPServerAddress"));
        if (demoMode) {
            properties = DemoModeConstants.demoProperties;
        }
        
        // Validate for mandatory and complete properties with default values
        return validateProperties(properties);
    }

    protected Properties validateProperties(Properties properties) {
	List<String> errors = new ArrayList<String>();
	
        // Test for mandatory and complete properties with default values when missing
	for (ConfigurationProperties confProps : ConfigurationProperties.values()) {
	    if (confProps.isMandatory()) {
		if (properties.get(confProps.getProperty()) == null) {
		    errors.add("The mandatory Property '" + confProps.getProperty() + "' is not set.");
		}
	    } else {
		if (properties.get(confProps.getProperty()) == null) {
		    properties.setProperty(confProps.getProperty(), confProps.getPropValue());
		}
	    }
	}

	// Test for unknown properties set in configuration
	for (Object key : properties.keySet()) {
	    if (ConfigurationProperties.lookup((String)key) == null) {
		errors.add("The Property '" + key + "' has no configuration impacts, it's unknown");
	    }
	}
	if (!demoMode && !errors.isEmpty()) {
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
            try {
                properties = new Properties();
                properties.load(new FileInputStream(file));
            } catch (Exception e) {
                properties = null;    
                e.printStackTrace();
            }
        }
        
        return properties;
    }

}
