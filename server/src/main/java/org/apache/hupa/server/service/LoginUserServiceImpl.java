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
package org.apache.hupa.server.service;

<<<<<<< HEAD
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
=======
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.server.service;

import javax.mail.MessagingException;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

=======
>>>>>>> alert people "invilid login" for the wrong username and/or password, which should be improved with a gentle way
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;
<<<<<<< HEAD
import org.apache.hupa.shared.exception.HupaException;
=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

import com.google.inject.Inject;
import com.google.inject.Provider;

public class LoginUserServiceImpl extends AbstractService implements LoginUserService {

	@Inject private Provider<Settings> settingsProvider;

<<<<<<< HEAD
<<<<<<< HEAD
	public User login(String username, String password) throws HupaException, MessagingException {
		HttpSession httpSession = httpSessionProvider.get();
        SessionUtils.cleanSessionAttributes(httpSession);
		User user = new UserImpl();
		user.setName(username);
		user.setPassword(password);
		cache.get(user);
=======
	public User login(String username, String password) {
=======
	public User login(String username, String password) throws Exception {
>>>>>>> alert people "invilid login" for the wrong username and/or password, which should be improved with a gentle way
		SessionUtils.cleanSessionAttributes(httpSession);
		User user = new UserImpl();
		user.setName(username);
		user.setPassword(password);
		try {
			cache.get(user);
		} catch (Exception e) {
			logger.error("Unable to authenticate user: " + username, e);
			throw e;
		}
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
		user.setAuthenticated(true);
		user.setSettings(settingsProvider.get());
		httpSession.setAttribute(SConsts.USER_SESS_ATTR, user);
		logger.debug("Logged user: " + username);
		return user;
	}
}
