<<<<<<< HEAD
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

=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
package org.apache.hupa.server.service;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;
<<<<<<< HEAD
import org.apache.hupa.shared.exception.HupaException;
import org.apache.hupa.shared.exception.InvalidSessionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
=======

import com.google.inject.Inject;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

public abstract class AbstractService {
	
    @Inject protected IMAPStoreCache cache;
<<<<<<< HEAD
    @Inject protected Provider<HttpSession> httpSessionProvider;
    @Inject protected Log logger;

	protected User getUser() throws HupaException{

        User user = (User) httpSessionProvider.get().getAttribute(SConsts.USER_SESS_ATTR);
        if (user == null) {
            throw new InvalidSessionException(getClass()+"User not found in session with id " + httpSessionProvider.get().getId());
        } else {
            return user;
        }
=======
    @Inject protected HttpSession httpSession;
    @Inject protected Log logger;

	protected User getUser() {
		return (User) getHttpSession().getAttribute(SConsts.USER_SESS_ATTR);
	}

	protected HttpSession getHttpSession() {
		return httpSession;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
	}
}
