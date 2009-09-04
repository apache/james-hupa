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
import org.apache.hupa.shared.rpc.LoginSession;
import org.apache.hupa.shared.rpc.LoginUserResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Login via a stored session
 */
public class LoginSessionHandler implements ActionHandler<LoginSession, LoginUserResult>{

	private final Log logger;
	private final Provider<HttpSession> sessionProvider;

	@Inject
	public LoginSessionHandler(Log logger,
			Provider<HttpSession> sessionProvider) {
		this.logger = logger;
		this.sessionProvider = sessionProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action, net.customware.gwt.dispatch.server.ExecutionContext)
	 */
	public LoginUserResult execute(LoginSession action,
			ExecutionContext context) throws ActionException {
		HttpSession session = sessionProvider.get();
		
		// check if the session is still valid
		if (action.getSessionId().equals(session.getId())) {
			// get the stored user and return it
			User user = (User)sessionProvider.get().getAttribute("user");
			if (user != null) {
				return new LoginUserResult(user);
			} else {
				logger.info("No user was stored in session with id  " + action.getSessionId());
				throw new ActionException("No user was stored in session with id  " + action.getSessionId());
			}
		} else {
			logger.error("Sessin id " + action.getSessionId() + " is not valid anymore");
			throw new ActionException("Session id " + action.getSessionId() + " is not valid anymore");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
	 */
	public Class<LoginSession> getActionType() {
		return LoginSession.class;
	}

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.dispatch.server.ActionHandler#rollback(net.customware.gwt.dispatch.shared.Action, net.customware.gwt.dispatch.shared.Result, net.customware.gwt.dispatch.server.ExecutionContext)
	 */
	public void rollback(LoginSession arg0, LoginUserResult arg1,
			ExecutionContext arg2) throws ActionException {
		// not implemented
	}

}
