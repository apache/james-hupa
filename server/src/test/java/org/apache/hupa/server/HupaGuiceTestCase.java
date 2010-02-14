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
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.SendMessage;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

public abstract class HupaGuiceTestCase extends TestCase {

    protected Injector injector = Guice.createInjector(getModule());

    protected HttpSession httpSession = injector.getInstance(HttpSession.class);
    
    protected ContactsHandler contactsHandler = injector.getInstance(ContactsHandler.class);

    protected IdleHandler idleHandler = injector.getInstance(IdleHandler.class);

    protected CreateFolderHandler createFolderHandler = injector.getInstance(CreateFolderHandler.class);
    
    protected DeleteFolderHandler deleteFolderHandler = injector.getInstance(DeleteFolderHandler.class);
    
    protected FetchFoldersHandler fetchFoldersHandler = injector.getInstance(FetchFoldersHandler.class);
    
    protected FetchMessagesHandler fetchMessagesHandler = injector.getInstance(FetchMessagesHandler.class);
    
    protected DeleteMessageByUidHandler deleteMessageByUidHandler = injector.getInstance(DeleteMessageByUidHandler.class);
    
    protected AbstractSendMessageHandler<SendMessage> sendMessageHandler = injector.getInstance(SendMessageHandler.class);
    
    protected ForwardMessageHandler forwardMessageHandler = injector.getInstance(ForwardMessageHandler.class);
    
    protected GetMessageDetailsHandler getDetailsHandler = injector.getInstance(GetMessageDetailsHandler.class);
    
    protected Log logger = injector.getInstance(Log.class);
    
    protected LoginUserHandler loginUser = injector.getInstance(LoginUserHandler.class);
    
    protected LogoutUserHandler logoutUser = injector.getInstance(LogoutUserHandler.class);
    
    protected ReplyMessageHandler reMsgHndl = injector.getInstance(ReplyMessageHandler.class);
    
    protected Session session = injector.getInstance(Session.class);
    
    protected IMAPStoreCache storeCache = injector.getInstance(IMAPStoreCache.class);
    
    protected User testUser;
    
    protected UserPreferencesStorage userPreferences = injector.getInstance(UserPreferencesStorage.class);
    
    protected IMAPStore store;
    
    
    @Override
    protected void setUp() throws Exception {
        try {
            testUser = injector.getInstance(User.class);
            store = storeCache.get(testUser);
            httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
        } catch (Exception e) {
        }
    }

    protected Module getModule() {
        return new GuiceServerTestModule();
    }
}
