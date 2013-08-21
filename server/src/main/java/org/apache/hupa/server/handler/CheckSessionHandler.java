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
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.CheckSession;
import org.apache.hupa.shared.rpc.CheckSessionResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Handler for asking the server if the session is valid
 */
public class CheckSessionHandler implements ActionHandler<CheckSession, CheckSessionResult> {
    
    protected final Provider<HttpSession> sessionProvider;
    protected final Log logger;
    
    @Inject
    public CheckSessionHandler(Log logger, Provider<HttpSession> provider) {
        this.sessionProvider = provider;
        this.logger = logger;
    }

    public CheckSessionResult execute(CheckSession arg0, ExecutionContext arg1) throws ActionException {
        CheckSessionResult ret = new CheckSessionResult();
        try {
            User user = (User) sessionProvider.get().getAttribute("user");
            if (user != null && user.getAuthenticated())
                ret.setUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("CheckSession returns: " + ret.isValid());
        return ret;
    }

    public Class<CheckSession> getActionType() {
        return CheckSession.class;
    }

    public void rollback(CheckSession arg0, CheckSessionResult arg1, ExecutionContext arg2) throws ActionException {
    }


}
