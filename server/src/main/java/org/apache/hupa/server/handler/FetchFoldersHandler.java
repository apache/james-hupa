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

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which fetch all Folders for an user
 * 
 */
public class FetchFoldersHandler extends AbstractSessionHandler<FetchFolders, FetchFoldersResult>{

    @Inject
    public FetchFoldersHandler(IMAPStoreCache cache, Log logger,Provider<HttpSession> provider) {
        super(cache,logger,provider);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public FetchFoldersResult executeInternal(FetchFolders action, ExecutionContext arg1)
    throws ActionException {
        User user = getUser();
        ArrayList<IMAPFolder> fList = new ArrayList<IMAPFolder>();
        try {

            // get the store for the user
            IMAPStore store = cache.get(user);
            com.sun.mail.imap.IMAPFolder folder = (com.sun.mail.imap.IMAPFolder) store.getDefaultFolder();
            
            // loop over all folders
            for (Folder f : folder.list()) {
                createIMAPFolderTree(fList, createFolder(f), f.list());
            }
            logger.debug("Fetching folders for user: " + user + " returns: " + fList.size() + " folders");
            return new FetchFoldersResult(fList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to get folders for User " + user,e);
            throw new ActionException("Unable to get folders for User "
                    + user);
        }
    }


    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<FetchFolders> getActionType() {
        return FetchFolders.class;
    }

    /**
     * Create a new IMAPFolder from the given Folder
     * 
     * @param folder
     * @return imapFolder
     * @throws ActionException 
     * @throws MessagingException
     */
    private IMAPFolder createFolder(Folder folder) throws ActionException {

        String fullName = folder.getFullName();
        String delimiter;
        IMAPFolder iFolder = null;
        
        try {
            logger.debug("Creating folder: " + fullName + " for user: " + getUser());
            delimiter = String.valueOf(folder.getSeparator());
            iFolder = new IMAPFolder(fullName);
            iFolder.setDelimiter(delimiter);
            if("[Gmail]".equals(folder.getFullName()))
                return iFolder;
            iFolder.setMessageCount(folder.getMessageCount());
            iFolder.setSubscribed(folder.isSubscribed());
            iFolder.setUnseenMessageCount(folder.getUnreadMessageCount());
        } catch (MessagingException e) {
            logger.error("Unable to construct folder " + folder.getFullName(),e);
        }
        
        
        return iFolder;
    }
    
    /**
     * Create a folder tree 
     * 
     * @param fList
     * @param iFolder
     * @param childFolders
     * @throws MessagingException
     */
    private void createIMAPFolderTree(List<IMAPFolder> fList,
            IMAPFolder iFolder, Folder[] childFolders) throws MessagingException, ActionException {

        for (Folder f : childFolders) {
            IMAPFolder folder = createFolder(f);
            if (folder != null) {
                iFolder.getChildIMAPFolders().add(createFolder(f));
            }
        }
        fList.add(iFolder);
    }

}
