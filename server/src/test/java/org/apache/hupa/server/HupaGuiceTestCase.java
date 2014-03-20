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

package org.apache.hupa.server;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.guice.GuiceServerTestModule;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.service.CreateFolderService;
import org.apache.hupa.server.service.CreateFolderServiceImpl;
import org.apache.hupa.server.service.DeleteFolderService;
import org.apache.hupa.server.service.DeleteFolderServiceImpl;
import org.apache.hupa.server.service.DeleteMessageByUidService;
import org.apache.hupa.server.service.DeleteMessageByUidServiceImpl;
import org.apache.hupa.server.service.FetchFoldersService;
import org.apache.hupa.server.service.FetchFoldersServiceImpl;
import org.apache.hupa.server.service.FetchMessagesService;
import org.apache.hupa.server.service.FetchMessagesServiceImpl;
import org.apache.hupa.server.service.GetMessageDetailsServiceImpl;
import org.apache.hupa.server.service.LoginUserService;
import org.apache.hupa.server.service.LoginUserServiceImpl;
import org.apache.hupa.server.service.LogoutUserService;
import org.apache.hupa.server.service.LogoutUserServiceImpl;
import org.apache.hupa.server.service.SendMessageBaseServiceImpl;
import org.apache.hupa.server.service.SendReplyMessageServiceImpl;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;
import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.mail.imap.IMAPStore;

public class HupaGuiceTestCase {

    protected Injector injector = Guice.createInjector(getModules());

    protected CreateFolderService createFolderService;
    protected FetchFoldersService fetchFoldersService;
    protected FetchMessagesService fetchMessagesService;
    protected DeleteFolderService deleteFolderService;
    protected DeleteMessageByUidService deleteMessageByUidService;
    protected GetMessageDetailsServiceImpl  getMessageDetailsService;
    protected LoginUserService loginUserService;
    protected LogoutUserService logoutUserService;
    protected SendReplyMessageServiceImpl sendReplyMessageService;
    protected SendMessageBaseServiceImpl sendMessageService;

    protected Log logger;
    protected IMAPStoreCache storeCache;
    protected User testUser;
    protected UserPreferencesStorage userPreferences;
    protected IMAPStore store;
    protected HttpSession httpSession;
    protected Session session;
    protected Module[] getModules() {
        return new Module[]{new GuiceServerTestModule()};
    }

    protected FileItemRegistry registry;


    @Before
    public void setUp(){

        try {
            createFolderService = injector.getInstance(CreateFolderServiceImpl.class);
            fetchFoldersService = injector.getInstance(FetchFoldersServiceImpl.class);
            fetchMessagesService = injector.getInstance(FetchMessagesServiceImpl.class);
            deleteFolderService = injector.getInstance(DeleteFolderServiceImpl.class);
            deleteMessageByUidService = injector.getInstance(DeleteMessageByUidServiceImpl.class);
            getMessageDetailsService = injector.getInstance(GetMessageDetailsServiceImpl.class);
            loginUserService = injector.getInstance(LoginUserServiceImpl.class);
            logoutUserService = injector.getInstance(LogoutUserServiceImpl.class);
            sendReplyMessageService = injector.getInstance(SendReplyMessageServiceImpl.class);
            sendMessageService = injector.getInstance(SendMessageBaseServiceImpl.class);

            logger = injector.getInstance(Log.class);
            storeCache = injector.getInstance(IMAPStoreCache.class);
            userPreferences = injector.getInstance(UserPreferencesStorage.class);

            httpSession = injector.getInstance(HttpSession.class);
            SessionUtils.cleanSessionAttributes(httpSession);
            testUser = injector.getInstance(User.class);
            store = storeCache.get(testUser);
            httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
            session = storeCache.getMailSession(testUser);

            registry = SessionUtils.getSessionRegistry(logger, httpSession);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
