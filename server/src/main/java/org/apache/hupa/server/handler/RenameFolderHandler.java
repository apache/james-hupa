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

package org.apache.hupa.server.handler;

import javax.mail.Folder;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.RenameFolder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which handle renaming of folders
 *
 */
public class RenameFolderHandler extends AbstractSessionHandler<RenameFolder, GenericResult>{

    @Inject
    public RenameFolderHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> sessionProvider) {
        super(cache, logger, sessionProvider);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    protected GenericResult executeInternal(RenameFolder action,
            ExecutionContext context) throws ActionException {
        User user = getUser();
        ImapFolderImpl folder = action.getFolder();
        String newName = action.getNewName();
        try {
            IMAPStore store = cache.get(user);
            com.sun.mail.imap.IMAPFolder iFolder = (com.sun.mail.imap.IMAPFolder) store.getFolder(folder.getFullName());
            Folder newFolder = store.getFolder(newName);
            
            if (iFolder.isOpen()) {
                iFolder.close(false);
            }
            if (iFolder.renameTo(newFolder)) {
                return new GenericResult();
            }
            throw new ActionException("Unable to rename Folder " + folder.getFullName() + " to " + newName + " for user " + user);

        } catch (Exception e) {
            logger.error("Error while renaming Folder " + folder.getFullName() + " to " + newName + " for user " + user,e);
            throw new ActionException("Error while renaming Folder " + folder.getFullName() + " to " + newName + " for user " + user,e);
        }

    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<RenameFolder> getActionType() {
        return RenameFolder.class;
    }

}
