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
import javax.mail.MessagingException;

import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.exception.HupaException;

import com.sun.mail.imap.IMAPStore;

public class DeleteFolderServiceImpl extends AbstractService implements DeleteFolderService {

    @Override
    public GenericResult delete(DeleteFolderAction action) throws HupaException, MessagingException {
        User user = getUser();
        ImapFolder folder = action.getFolder();
        IMAPStore store = cache.get(user);

        Folder f = store.getFolder(folder.getFullName());

        // close the folder if its open
        if (f.isOpen()) {
            f.close(false);
        }

        // recursive delete the folder
        if (f.delete(true)) {
            logger.info("Successfully delete folder " + folder + " for user " + user);
            return new GenericResultImpl();
        } else {
            throw new HupaException("Unable to delete folder " + folder + " for user " + user);
        }
    }

}
