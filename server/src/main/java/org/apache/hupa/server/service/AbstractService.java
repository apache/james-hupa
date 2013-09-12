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
=======
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.server.service;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> re-add server unit tests
import org.apache.hupa.shared.exception.HupaException;
import org.apache.hupa.shared.exception.InvalidSessionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
<<<<<<< HEAD
=======

import com.google.inject.Inject;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
>>>>>>> fix issue 5,6,8:

public abstract class AbstractService {
	
    @Inject protected IMAPStoreCache cache;
<<<<<<< HEAD
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
=======
    @Inject protected Provider<HttpSession> httpSessionProvider;
>>>>>>> fix issue 5,6,8:
    @Inject protected Log logger;

	protected User getUser() throws HupaException{

<<<<<<< HEAD
	protected HttpSession getHttpSession() {
		return httpSession;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.
=======
        User user = (User) httpSessionProvider.get().getAttribute(SConsts.USER_SESS_ATTR);
<<<<<<< HEAD
//        if (user == null) { TODO exception
//            throw new Exception("User not found in session with id " + httpSessionProvider.get().getId());
//        } else {
//            return user;
//        }
        return user;
>>>>>>> fix issue 5,6,8:
=======
        if (user == null) {
            throw new InvalidSessionException(getClass()+"User not found in session with id " + httpSessionProvider.get().getId());
        } else {
            return user;
        }
>>>>>>> re-add server unit tests
	}
}
