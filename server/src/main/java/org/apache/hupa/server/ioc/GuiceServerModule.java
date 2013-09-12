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
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.server.ioc;

import java.util.Properties;

import javax.mail.Session;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import javax.servlet.http.HttpSession;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.server.preferences.InImapUserPreferencesStorage;
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
<<<<<<< HEAD
import org.apache.hupa.server.service.FetchFoldersService;
import org.apache.hupa.server.service.FetchFoldersServiceImpl;
=======
>>>>>>> delete messages, make WestActivity Singleton
import org.apache.hupa.server.service.FetchMessagesService;
import org.apache.hupa.server.service.FetchMessagesServiceImpl;
import org.apache.hupa.server.service.GetMessageDetailsService;
import org.apache.hupa.server.service.GetMessageDetailsServiceImpl;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> other RFs
import org.apache.hupa.server.service.GetMessageRawService;
import org.apache.hupa.server.service.GetMessageRawServiceImpl;
import org.apache.hupa.server.service.IdleService;
import org.apache.hupa.server.service.IdleServiceImpl;
<<<<<<< HEAD
=======
import org.apache.hupa.server.guice.providers.HttpSessionProvider;
=======
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.server.preferences.InImapUserPreferencesStorage;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.service.CheckSessionService;
import org.apache.hupa.server.service.CheckSessionServiceImpl;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
=======
import org.apache.hupa.server.service.CreateFolderService;
import org.apache.hupa.server.service.CreateFolderServiceImpl;
<<<<<<< HEAD
>>>>>>> Succeed creating new folder
=======
import org.apache.hupa.server.service.DeleteFolderService;
import org.apache.hupa.server.service.DeleteFolderServiceImpl;
>>>>>>> delete and rename folder
import org.apache.hupa.server.service.FetchMessagesService;
import org.apache.hupa.server.service.FetchMessagesServiceImpl;
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
=======
>>>>>>> try to get message details, problem is:
=======
>>>>>>> other RFs
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.server.service.ImapFolderServiceImpl;
import org.apache.hupa.server.service.LoginUserService;
import org.apache.hupa.server.service.LoginUserServiceImpl;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> other RFs
import org.apache.hupa.server.service.LogoutUserService;
import org.apache.hupa.server.service.LogoutUserServiceImpl;
import org.apache.hupa.server.service.MoveMessageService;
import org.apache.hupa.server.service.MoveMessageServiceImpl;
<<<<<<< HEAD
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
import org.apache.hupa.server.servlet.DownloadAttachmentServlet;
import org.apache.hupa.server.servlet.MessageSourceServlet;
import org.apache.hupa.server.servlet.UploadAttachmentServlet;
import org.apache.hupa.shared.data.CreateFolderActionImpl;
import org.apache.hupa.shared.data.DeleteFolderActionImpl;
import org.apache.hupa.shared.data.DeleteMessageAllActionImpl;
import org.apache.hupa.shared.data.DeleteMessageByUidActionImpl;
=======
=======
>>>>>>> other RFs
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
<<<<<<< HEAD
>>>>>>> delete and rename folder
=======
import org.apache.hupa.shared.data.DeleteMessageAllActionImpl;
import org.apache.hupa.shared.data.DeleteMessageByUidActionImpl;
>>>>>>> delete messages, make WestActivity Singleton
import org.apache.hupa.shared.data.FetchMessagesActionImpl;
import org.apache.hupa.shared.data.FetchMessagesResultImpl;
import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.data.GetMessageDetailsActionImpl;
import org.apache.hupa.shared.data.GetMessageDetailsResultImpl;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> other RFs
import org.apache.hupa.shared.data.GetMessageRawActionImpl;
import org.apache.hupa.shared.data.GetMessageRawResultImpl;
import org.apache.hupa.shared.data.IdleActionImpl;
import org.apache.hupa.shared.data.IdleResultImpl;
import org.apache.hupa.shared.data.ImapFolderImpl;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> other RFs
import org.apache.hupa.shared.data.LogoutUserActionImpl;
import org.apache.hupa.shared.data.MailHeaderImpl;
import org.apache.hupa.shared.data.MessageAttachmentImpl;
import org.apache.hupa.shared.data.MessageDetailsImpl;
import org.apache.hupa.shared.data.MoveMessageActionImpl;
import org.apache.hupa.shared.data.RenameFolderActionImpl;
import org.apache.hupa.shared.data.SendForwardMessageActionImpl;
import org.apache.hupa.shared.data.SendMessageActionImpl;
import org.apache.hupa.shared.data.SendReplyMessageActionImpl;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.data.SetFlagActionImpl;
import org.apache.hupa.shared.data.SmtpMessageImpl;
=======
=======
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MailHeaderImpl;
import org.apache.hupa.shared.data.MessageAttachmentImpl;
import org.apache.hupa.shared.data.MessageDetailsImpl;
>>>>>>> try to get message details, problem is:
import org.apache.hupa.shared.data.RenameFolderActionImpl;
>>>>>>> delete and rename folder
=======
=======
import org.apache.hupa.shared.data.SetFlagActionImpl;
>>>>>>> other RFs
import org.apache.hupa.shared.data.SmtpMessageImpl;
>>>>>>> forward and reply message to use RF
import org.apache.hupa.shared.data.TagImpl;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.DeleteFolderAction;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
=======
>>>>>>> delete and rename folder
=======
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
>>>>>>> delete messages, make WestActivity Singleton
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> other RFs
import org.apache.hupa.shared.domain.GetMessageRawAction;
import org.apache.hupa.shared.domain.GetMessageRawResult;
import org.apache.hupa.shared.domain.IdleAction;
import org.apache.hupa.shared.domain.IdleResult;
<<<<<<< HEAD
import org.apache.hupa.shared.domain.ImapFolder;
<<<<<<< HEAD
import org.apache.hupa.shared.domain.LogoutUserAction;
import org.apache.hupa.shared.domain.MailHeader;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.MoveMessageAction;
=======
=======
>>>>>>> other RFs
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.LogoutUserAction;
import org.apache.hupa.shared.domain.MailHeader;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;
<<<<<<< HEAD
>>>>>>> try to get message details, problem is:
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.domain.SendForwardMessageAction;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SendReplyMessageAction;
import org.apache.hupa.shared.domain.SetFlagAction;
=======
=======
import org.apache.hupa.shared.domain.MoveMessageAction;
>>>>>>> other RFs
import org.apache.hupa.shared.domain.RenameFolderAction;
<<<<<<< HEAD
>>>>>>> delete and rename folder
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.Tag;
=======
=======
import org.apache.hupa.shared.data.FetchMessagesActionImpl;
import org.apache.hupa.shared.data.FetchMessagesResultImpl;
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
=======
import org.apache.hupa.shared.data.CreateFolderActionImpl;
import org.apache.hupa.shared.data.FetchMessagesActionImpl;
import org.apache.hupa.shared.data.FetchMessagesResultImpl;
import org.apache.hupa.shared.data.GenericResultImpl;
>>>>>>> Succeed creating new folder
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.TagImpl;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
=======
import org.apache.hupa.shared.domain.SendForwardMessageAction;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SendReplyMessageAction;
import org.apache.hupa.shared.domain.SetFlagAction;
import org.apache.hupa.shared.domain.SmtpMessage;
>>>>>>> forward and reply message to use RF
import org.apache.hupa.shared.domain.Settings;
<<<<<<< HEAD
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
import org.apache.hupa.shared.domain.Tag;
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
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
		
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> try to get message details, problem is:
		bind(MailHeader.class).to(MailHeaderImpl.class);
		
		bind(User.class).to(UserImpl.class);
		bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(Singleton.class);
		bind(ImapFolder.class).to(ImapFolderImpl.class);
<<<<<<< HEAD
<<<<<<< HEAD
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> other RFs
		bind(GetMessageRawAction.class).to(GetMessageRawActionImpl.class);
		bind(GetMessageRawResult.class).to(GetMessageRawResultImpl.class);
		bind(IdleAction.class).to(IdleActionImpl.class);
		bind(IdleResult.class).to(IdleResultImpl.class);
		bind(LogoutUserAction.class).to(LogoutUserActionImpl.class);
		bind(MoveMessageAction.class).to(MoveMessageActionImpl.class);
		bind(SetFlagAction.class).to(SetFlagActionImpl.class);
<<<<<<< HEAD
=======
>>>>>>> forward and reply message to use RF
		
		
=======
		bind(User.class).to(UserImpl.class);
		bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(Singleton.class);
		bind(ImapFolder.class).to(ImapFolderImpl.class);
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
		bind(FetchMessagesAction.class).to(FetchMessagesActionImpl.class);
		bind(FetchMessagesResult.class).to(FetchMessagesResultImpl.class);
		bind(Tag.class).to(TagImpl.class);
<<<<<<< HEAD
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
=======
=======
		bind(Tag.class).to(TagImpl.class);
		bind(MessageDetails.class).to(MessageDetailsImpl.class);
		bind(MessageAttachment.class).to(MessageAttachmentImpl.class);
>>>>>>> try to get message details, problem is:
		bind(GenericResult.class).to(GenericResultImpl.class);
		bind(FetchMessagesAction.class).to(FetchMessagesActionImpl.class);
		bind(FetchMessagesResult.class).to(FetchMessagesResultImpl.class);
		bind(CreateFolderAction.class).to(CreateFolderActionImpl.class);
<<<<<<< HEAD
>>>>>>> Succeed creating new folder
=======
		bind(DeleteFolderAction.class).to(DeleteFolderActionImpl.class);
		bind(RenameFolderAction.class).to(RenameFolderActionImpl.class);
<<<<<<< HEAD
>>>>>>> delete and rename folder
=======
		bind(DeleteMessageAllAction.class).to(DeleteMessageAllActionImpl.class);
		bind(DeleteMessageByUidAction.class).to(DeleteMessageByUidActionImpl.class);
<<<<<<< HEAD
>>>>>>> delete messages, make WestActivity Singleton
=======
		bind(GetMessageDetailsAction.class).to(GetMessageDetailsActionImpl.class);
		bind(GetMessageDetailsResult.class).to(GetMessageDetailsResultImpl.class);
=======
		
>>>>>>> other RFs
		
>>>>>>> try to get message details, problem is:
		
		bind(CheckSessionService.class).to(CheckSessionServiceImpl.class);
		bind(LoginUserService.class).to(LoginUserServiceImpl.class);
		bind(ImapFolderService.class).to(ImapFolderServiceImpl.class);
<<<<<<< HEAD
<<<<<<< HEAD
		bind(FetchFoldersService.class).to(FetchFoldersServiceImpl.class);
		bind(FetchMessagesService.class).to(FetchMessagesServiceImpl.class);
		bind(CreateFolderService.class).to(CreateFolderServiceImpl.class);
		bind(DeleteFolderService.class).to(DeleteFolderServiceImpl.class);
		bind(RenameFolderService.class).to(RenameFolderServiceImpl.class);
		bind(DeleteMessageAllService.class).to(DeleteMessageAllServiceImpl.class);
		bind(DeleteMessageByUidService.class).to(DeleteMessageByUidServiceImpl.class);
		bind(GetMessageDetailsService.class).to(GetMessageDetailsServiceImpl.class);
		bind(SendMessageService.class).to(SendMessageBaseServiceImpl.class);
		bind(SendForwardMessageService.class).to(SendForwardMessageServiceImpl.class);
		bind(SendReplyMessageService.class).to(SendReplyMessageServiceImpl.class);
		bind(GetMessageRawService.class).to(GetMessageRawServiceImpl.class);
		bind(IdleService.class).to(IdleServiceImpl.class);
		bind(LogoutUserService.class).to(LogoutUserServiceImpl.class);
		bind(MoveMessageService.class).to(MoveMessageServiceImpl.class);
		bind(SetFlagService.class).to(SetFlagServiceImpl.class);
		
		bind(IMAPStoreCache.class).to(getIMAPStoreCacheClass()).in(Singleton.class);

        bind(DownloadAttachmentServlet.class).in(Singleton.class);
        bind(UploadAttachmentServlet.class).in(Singleton.class);
        bind(MessageSourceServlet.class).in(Singleton.class);
        
		bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
		bind(Session.class).toProvider(JavaMailSessionProvider.class);
        bind(UserPreferencesStorage.class).to(InImapUserPreferencesStorage.class);
=======
=======
		bind(FetchMessagesService.class).to(FetchMessagesServiceImpl.class);
<<<<<<< HEAD
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
=======
		bind(CreateFolderService.class).to(CreateFolderServiceImpl.class);
<<<<<<< HEAD
>>>>>>> Succeed creating new folder
=======
		bind(DeleteFolderService.class).to(DeleteFolderServiceImpl.class);
		bind(RenameFolderService.class).to(RenameFolderServiceImpl.class);
<<<<<<< HEAD
>>>>>>> delete and rename folder
=======
		bind(DeleteMessageAllService.class).to(DeleteMessageAllServiceImpl.class);
		bind(DeleteMessageByUidService.class).to(DeleteMessageByUidServiceImpl.class);
<<<<<<< HEAD
>>>>>>> delete messages, make WestActivity Singleton
=======
		bind(GetMessageDetailsService.class).to(GetMessageDetailsServiceImpl.class);
<<<<<<< HEAD
>>>>>>> try to get message details, problem is:
=======
		bind(SendMessageService.class).to(SendMessageBaseServiceImpl.class);
		bind(SendForwardMessageService.class).to(SendForwardMessageServiceImpl.class);
		bind(SendReplyMessageService.class).to(SendReplyMessageServiceImpl.class);
<<<<<<< HEAD
>>>>>>> forward and reply message to use RF
=======
		bind(GetMessageRawService.class).to(GetMessageRawServiceImpl.class);
		bind(IdleService.class).to(IdleServiceImpl.class);
		bind(LogoutUserService.class).to(LogoutUserServiceImpl.class);
		bind(MoveMessageService.class).to(MoveMessageServiceImpl.class);
		bind(SetFlagService.class).to(SetFlagServiceImpl.class);
>>>>>>> other RFs
		
		bind(IMAPStoreCache.class).to(getIMAPStoreCacheClass()).in(Singleton.class);

		bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
		bind(Session.class).toProvider(JavaMailSessionProvider.class);
<<<<<<< HEAD
//		bind(HttpSession.class).toProvider(HttpSessionProvider.class);
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
		bind(Properties.class).toInstance(properties);
=======
>>>>>>> forward and reply message to use RF
        bind(UserPreferencesStorage.class).to(InImapUserPreferencesStorage.class);
		bind(Properties.class).toInstance(properties);
	}

	protected Class<? extends IMAPStoreCache> getIMAPStoreCacheClass() {
		return InMemoryIMAPStoreCache.class;
	}
}
