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

import java.net.URL;
import java.util.Properties;

import javax.mail.Session;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.handler.CreateFolderHandler;
import org.apache.hupa.server.handler.DeleteFolderHandler;
import org.apache.hupa.server.handler.DeleteMessageHandler;
import org.apache.hupa.server.handler.GetMessageDetailsHandler;
import org.apache.hupa.server.handler.FetchFoldersHandler;
import org.apache.hupa.server.handler.FetchMessagesHandler;
import org.apache.hupa.server.handler.FetchRecentMessagesHandler;
import org.apache.hupa.server.handler.ForwardMessageHandler;
import org.apache.hupa.server.handler.GetRawMessageHandler;
import org.apache.hupa.server.handler.LoginSessionHandler;
import org.apache.hupa.server.handler.LoginUserHandler;
import org.apache.hupa.server.handler.LogoutUserHandler;
import org.apache.hupa.server.handler.MoveMessageHandler;
import org.apache.hupa.server.handler.NoopHandler;
import org.apache.hupa.server.handler.RenameFolderHandler;
import org.apache.hupa.server.handler.ReplyMessageHandler;
import org.apache.hupa.server.handler.SendMessageHandler;
import org.apache.hupa.server.handler.TagMessagesHandler;
import org.apache.hupa.server.servlet.DownloadAttachmentServlet;
import org.apache.hupa.server.servlet.UploadAttachmentServlet;
import org.apache.hupa.shared.data.Settings;

import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * Module which binds the handlers and configurations
 * 
 * @author norman
 *
 */
public class ServerModul extends ActionHandlerModule{

	
	@Override
	protected void configureHandlers() {
		bindHandler(LoginUserHandler.class);
		bindHandler(FetchFoldersHandler.class);
		bindHandler(FetchMessagesHandler.class);
		bindHandler(FetchRecentMessagesHandler.class);
		bindHandler(LogoutUserHandler.class);
		bindHandler(GetMessageDetailsHandler.class);
		bindHandler(DeleteMessageHandler.class);
		bindHandler(SendMessageHandler.class);
		bindHandler(ReplyMessageHandler.class);
		bindHandler(ForwardMessageHandler.class);
		bindHandler(NoopHandler.class);
		bindHandler(LoginSessionHandler.class);
		bindHandler(MoveMessageHandler.class);
		bindHandler(RenameFolderHandler.class);
		bindHandler(DeleteFolderHandler.class);
		bindHandler(CreateFolderHandler.class);
		bindHandler(TagMessagesHandler.class);
		bindHandler(GetRawMessageHandler.class);
		bind(FileItemRegistry.class).in(Singleton.class);
		bind(IMAPStoreCache.class).to(InMemoryIMAPStoreCache.class).in(Singleton.class);
		bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
		bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(Singleton.class);
		bind(DownloadAttachmentServlet.class).in(Singleton.class);
		bind(UploadAttachmentServlet.class).in(Singleton.class);	
		bind(Session.class).toProvider(SessionProvider.class);
		// bind addresses and ports for imap and smtp
		Properties properties;
		try {
			properties = loadProperties();
			Names.bindProperties(binder(), properties);

		} catch (Exception e) {
			throw new RuntimeException("Unable to to configure",e);
		}
	}
	
	private Properties loadProperties() throws Exception {
		Properties properties = new Properties();
		String name = "config.properties";
 
		ClassLoader loader = ServerModul.class.getClassLoader();
		URL url = loader.getResource(name);
		if ( url == null ) {
			url = loader.getResource("/"+name);
		}
		properties.load(url.openStream());
		return properties;
	}

}
