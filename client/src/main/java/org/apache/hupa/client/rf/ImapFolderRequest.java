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

package org.apache.hupa.client.rf;

import java.util.List;

<<<<<<< HEAD:client/src/main/java/org/apache/hupa/client/rf/ImapFolderRequestContext.java
<<<<<<< HEAD
<<<<<<< HEAD:client/src/main/java/org/apache/hupa/client/rf/IMAPFolderRequestContext.java
import org.apache.hupa.server.service.IMAPFolderService;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.:client/src/main/java/org/apache/hupa/client/rf/ImapFolderRequestContext.java
=======
import org.apache.hupa.server.locator.ImapFolderServiceLocator;
=======
import org.apache.hupa.server.ioc.IocRfServiceLocator;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.:client/src/main/java/org/apache/hupa/client/rf/ImapFolderRequest.java
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = ImapFolderService.class, locator = IocRfServiceLocator.class)
public interface ImapFolderRequest extends RequestContext {
	Request<List<ImapFolder>> requestFolders();
}
