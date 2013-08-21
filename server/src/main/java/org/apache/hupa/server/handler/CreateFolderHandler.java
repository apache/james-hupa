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
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.CreateFolder;
import org.apache.hupa.shared.rpc.GenericResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Handle creation of folders
 * 
 *
 */
public class CreateFolderHandler extends AbstractSessionHandler<CreateFolder, GenericResult>{

    @Inject
    public CreateFolderHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> sessionProvider) {
        super(cache, logger, sessionProvider);
    }

    @Override
    protected GenericResult executeInternal(CreateFolder action,
            ExecutionContext context) throws ActionException {
        User user = getUser();
        IMAPFolder folder = action.getFolder();
        
        try {
            IMAPStore store = cache.get(user);
            Folder f = store.getFolder(folder.getFullName());
            if (f.create(Folder.HOLDS_MESSAGES)) {
                logger.info("Successfully create folder " + folder + " for user " + user);
                return new GenericResult();
            } else {
                logger.info("Unable to create folder " + folder + " for user " + user);
                throw new ActionException("Unable to create folder " + folder + " for user " + user);

            }
        } catch (Exception e) {
            logger.error("Error while creating folder " + folder + " for user " + user, e);
            throw new ActionException("Error while creating folder " + folder + " for user " + user, e);
        }
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<CreateFolder> getActionType() {
        return CreateFolder.class;
    }

}
