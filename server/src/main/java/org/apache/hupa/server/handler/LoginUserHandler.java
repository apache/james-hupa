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

package org.apache.hupa.server.handler;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Handler for login a user via username and password
 * 
 */
public class LoginUserHandler implements
        ActionHandler<LoginUser, LoginUserResult> {

    private final IMAPStoreCache cache;
    private final Log logger;
    private final Provider<HttpSession> sessionProvider;
    private final Provider<Settings> settingsProvider;

    @Inject
    public LoginUserHandler(IMAPStoreCache cache, Log logger, Provider<HttpSession> sessionProvider, Provider<Settings> settingsProvider) {
        this.cache = cache;
        this.logger = logger;
        this.sessionProvider = sessionProvider;
        this.settingsProvider = settingsProvider;
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public LoginUserResult execute(LoginUser action, ExecutionContext context) throws ActionException {
        HttpSession session = sessionProvider.get();
        SessionUtils.cleanSessionAttributes(session);
        
        String username = action.getUserName();
        String password = action.getPassword();
        try {
            
            // construct a new user
            User user = new UserImpl();
            user.setName(username);
            user.setPassword(password);
            
            // login
            cache.get(user);
            
            user.setAuthenticated(true);
            user.setSettings(settingsProvider.get());
            
            // store the session id for later usage
            session.setAttribute(SConsts.USER_SESS_ATTR, user);
            
            logger.debug("Logged user: " + username);
            return new LoginUserResult(user);

        } catch (Exception e) {
            logger.error("Unable to authenticate user: " + username, e);
            throw new ActionException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#rollback(net.customware.gwt.dispatch.shared.Action, net.customware.gwt.dispatch.shared.Result, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public void rollback(LoginUser user, LoginUserResult result,
            ExecutionContext context) throws ActionException {
        // Nothing todo here
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<LoginUser> getActionType() {
        return LoginUser.class;
    }
}
