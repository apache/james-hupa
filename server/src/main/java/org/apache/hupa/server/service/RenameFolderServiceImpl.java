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
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPStore;

public class RenameFolderServiceImpl extends AbstractService implements RenameFolderService {

    @Override
    public GenericResult rename(RenameFolderAction action) throws Exception {
        User user = getUser();
        ImapFolder folder = action.getFolder();
        String newName = action.getNewName();
        IMAPStore store = cache.get(user);
        com.sun.mail.imap.IMAPFolder iFolder = (com.sun.mail.imap.IMAPFolder) store.getFolder(folder.getFullName());
        Folder newFolder = store.getFolder(newName);

        if (iFolder.isOpen()) {
            iFolder.close(false);
        }
        if (iFolder.renameTo(newFolder)) {
            return new GenericResultImpl();
        }
        throw new Exception("Unable to rename Folder " + folder.getFullName() + " to " + newName + " for user " + user);
    }

}
