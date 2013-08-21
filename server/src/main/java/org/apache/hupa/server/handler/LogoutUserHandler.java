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

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.LogoutUser;
import org.apache.hupa.shared.rpc.LogoutUserResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Handler for handle logout requests
 * 
 */
public class LogoutUserHandler extends AbstractSessionHandler<LogoutUser, LogoutUserResult> {
    
    
    @Inject
    public LogoutUserHandler(IMAPStoreCache cache, Log logger,Provider<HttpSession> provider) {
        super(cache,logger,provider);
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public LogoutUserResult executeInternal(LogoutUser action, ExecutionContext arg1)
            throws ActionException {
        User user = getUser();
        user.setAuthenticated(false);
        
        // delete cached store
        cache.delete(user);
        
        // remove user attributes from session
        SessionUtils.cleanSessionAttributes(httpSessionProvider.get());
        
        return new LogoutUserResult(user);
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<LogoutUser> getActionType() {
        return LogoutUser.class;
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#rollback(net.customware.gwt.dispatch.shared.Action, net.customware.gwt.dispatch.shared.Result, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public void rollback(LogoutUser arg0, LogoutUserResult arg1,
            ExecutionContext arg2) throws ActionException {
        // not implemented
    }
}
