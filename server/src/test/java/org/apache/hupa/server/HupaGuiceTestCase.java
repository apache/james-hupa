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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.sun.mail.imap.IMAPStore;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.guice.GuiceServerTestModule;
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
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.SendMessage;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

public abstract class HupaGuiceTestCase extends TestCase {

    protected Injector injector = Guice.createInjector(getModules());
    
    protected ContactsHandler contactsHandler;
    protected IdleHandler idleHandler;
    protected CreateFolderHandler createFolderHandler;
    protected DeleteFolderHandler deleteFolderHandler;
    protected FetchFoldersHandler fetchFoldersHandler;
    protected FetchMessagesHandler fetchMessagesHandler;
    protected DeleteMessageByUidHandler deleteMessageByUidHandler;
    protected AbstractSendMessageHandler<SendMessage> sendMessageHandler;
    protected ForwardMessageHandler forwardMessageHandler;
    protected GetMessageDetailsHandler getDetailsHandler;
    protected Log logger;
    protected LoginUserHandler loginUser;
    protected LogoutUserHandler logoutUser;
    protected ReplyMessageHandler reMsgHndl;
    protected Session session;
    protected IMAPStoreCache storeCache;
    protected User testUser;
    protected UserPreferencesStorage userPreferences;
    protected IMAPStore store;
    protected HttpSession httpSession;
    
    protected Module[] getModules() {
        return new Module[]{new GuiceServerTestModule()};
    }
    
    @Override
    protected void setUp() throws Exception {
        try {
            contactsHandler = injector.getInstance(ContactsHandler.class);
            idleHandler = injector.getInstance(IdleHandler.class);
            createFolderHandler = injector.getInstance(CreateFolderHandler.class);
            deleteFolderHandler = injector.getInstance(DeleteFolderHandler.class);
            fetchFoldersHandler = injector.getInstance(FetchFoldersHandler.class);
            fetchMessagesHandler = injector.getInstance(FetchMessagesHandler.class);
            deleteMessageByUidHandler = injector.getInstance(DeleteMessageByUidHandler.class);
            sendMessageHandler = injector.getInstance(SendMessageHandler.class);
            forwardMessageHandler = injector.getInstance(ForwardMessageHandler.class);
            getDetailsHandler = injector.getInstance(GetMessageDetailsHandler.class);
            logger = injector.getInstance(Log.class);
            loginUser = injector.getInstance(LoginUserHandler.class);
            logoutUser = injector.getInstance(LogoutUserHandler.class);
            reMsgHndl = injector.getInstance(ReplyMessageHandler.class);
            session = injector.getInstance(Session.class);
            storeCache = injector.getInstance(IMAPStoreCache.class);
            userPreferences = injector.getInstance(UserPreferencesStorage.class);
            
            httpSession = injector.getInstance(HttpSession.class);
            SessionUtils.cleanSessionAttributes(httpSession);
            testUser = injector.getInstance(User.class);
            store = storeCache.get(testUser);
            httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
            
        } catch (Exception e) {
        }
    }

}
