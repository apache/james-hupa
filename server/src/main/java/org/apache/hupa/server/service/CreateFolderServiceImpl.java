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

import javax.mail.Folder;

import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPStore;

public class CreateFolderServiceImpl extends AbstractService implements CreateFolderService {

    @Override
    public GenericResult create(CreateFolderAction action) throws Exception {
        User user = getUser();
        ImapFolder folder = action.getFolder();
        IMAPStore store = cache.get(user);
        Folder f = store.getFolder(folder.getFullName());
        if (f.create(Folder.HOLDS_MESSAGES)) {
            logger.info("Successfully create folder " + folder + " for user " + user);
            return new GenericResultImpl();
        } else {
            logger.info("Unable to create folder " + folder + " for user " + user);
            throw new Exception("Unable to create folder " + folder + " for user " + user);

        }
    }

}
