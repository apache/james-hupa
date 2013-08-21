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
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.Result;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.exception.InvalidSessionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
/**
 * Abstract class which take care of checking if the session is still valid before
 * executing the handler
 * 
 */
public abstract class AbstractSessionHandler<A extends Action<R>,R extends Result> implements ActionHandler<A, R> {

    protected final Provider<HttpSession> httpSessionProvider;
    protected final IMAPStoreCache cache;
    protected final Log logger;

    @Inject
    public AbstractSessionHandler(IMAPStoreCache cache, Log logger, Provider<HttpSession> httpSessionProvider) {
        this.httpSessionProvider = httpSessionProvider;
        this.cache = cache;
        this.logger = logger;
    }

    /**
     * Execute executeInternal method
     */
    public R execute(A action, ExecutionContext context) throws ActionException {
        return executeInternal(action, context);
    }
    
    /**
     * Not implemented. Should get overridden if needed
     */
    public void rollback(A action, R result,
            ExecutionContext context) throws ActionException {
        // Not implemented
    }
    
    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    protected abstract R executeInternal(A action, ExecutionContext context) throws ActionException;
    
    /**
     * Return the User stored in session with the given id
     * 
     * @return user
     * @throws ActionException
     */
    protected User getUser() throws ActionException{
        User user = (User) httpSessionProvider.get().getAttribute(SConsts.USER_SESS_ATTR);
        if (user == null) {
            throw new InvalidSessionException("User not found in session with id " + httpSessionProvider.get().getId());
        } else {
            return user;
        }
    }
}
