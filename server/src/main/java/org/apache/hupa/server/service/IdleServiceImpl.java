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

import javax.mail.MessagingException;

import org.apache.hupa.shared.data.IdleResultImpl;
import org.apache.hupa.shared.domain.IdleAction;
import org.apache.hupa.shared.domain.IdleResult;
import org.apache.hupa.shared.exception.HupaException;
=======
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.server.service;

import javax.mail.MessagingException;

import org.apache.hupa.shared.data.IdleResultImpl;
import org.apache.hupa.shared.domain.IdleAction;
import org.apache.hupa.shared.domain.IdleResult;
<<<<<<< HEAD
>>>>>>> other RFs
=======
import org.apache.hupa.shared.exception.HupaException;
>>>>>>> re-add server unit tests

import com.sun.mail.imap.IMAPStore;

public class IdleServiceImpl extends AbstractService implements IdleService {
	@Override
<<<<<<< HEAD
<<<<<<< HEAD
	public IdleResult idle(IdleAction action) throws HupaException, MessagingException {
=======
	public IdleResult idle(IdleAction action) throws Exception {
>>>>>>> other RFs
=======
	public IdleResult idle(IdleAction action) throws HupaException, MessagingException {
>>>>>>> re-add server unit tests
		try {
			IMAPStore store = cache.get(getUser());

			if (store.getURLName() != null) {
				// check if the store supports the IDLE command
				if (store.hasCapability("IDLE")) {
					// just send a noop to keep the connection alive
					store.idle();
				} else {
					return new IdleResultImpl(false);
				}
			}
			return new IdleResultImpl(true);
<<<<<<< HEAD
<<<<<<< HEAD
		} catch (HupaException e) {
			throw new HupaException("Unable to send NOOP " + e.getMessage());
=======
		} catch (Exception e) {
			throw new Exception("Unable to send NOOP " + e.getMessage());
>>>>>>> other RFs
=======
		} catch (HupaException e) {
			throw new HupaException("Unable to send NOOP " + e.getMessage());
>>>>>>> re-add server unit tests
		}
	}
}
