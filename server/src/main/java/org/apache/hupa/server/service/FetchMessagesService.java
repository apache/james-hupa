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

import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.exception.HupaException;

public interface FetchMessagesService {
	FetchMessagesResult fetch(FetchMessagesAction action) throws HupaException;
	List<org.apache.hupa.shared.domain.Message> convert(int offset, com.sun.mail.imap.IMAPFolder folder, Message[] messages) throws MessagingException;
=======
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;

public interface FetchMessagesService {
<<<<<<< HEAD
	public FetchMessagesResult fetch(FetchMessagesAction action);
>>>>>>> try to fetch messages, yet can not fire the login event in ModelTable such that just get a NullPointerException in it.
=======
	FetchMessagesResult fetch(FetchMessagesAction action);
>>>>>>> Succeed creating new folder
}
