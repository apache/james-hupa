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

package org.apache.hupa.server.service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.exception.HupaException;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class LoginUserServiceImpl extends AbstractService implements LoginUserService {

	@Inject private Provider<Settings> settingsProvider;

	public User login(String username, String password) throws HupaException, MessagingException {
		HttpSession httpSession = httpSessionProvider.get();
        SessionUtils.cleanSessionAttributes(httpSession);
		User user = new UserImpl();
		user.setName(username);
		user.setPassword(password);
		cache.get(user);
		user.setAuthenticated(true);
		user.setSettings(settingsProvider.get());
		httpSession.setAttribute(SConsts.USER_SESS_ATTR, user);
		logger.debug("Logged user: " + username);
		return user;
	}
}
