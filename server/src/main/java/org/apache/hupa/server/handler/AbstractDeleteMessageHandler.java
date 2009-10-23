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

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.DeleteMessage;
import org.apache.hupa.shared.rpc.DeleteMessageResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Abstract class which should get extended by all handlers which needs to handle message deletion
 *
 * @param <Action>
 */
public abstract class AbstractDeleteMessageHandler<Action extends DeleteMessage>
        extends AbstractSessionHandler<Action, DeleteMessageResult> {

    @Inject
    public AbstractDeleteMessageHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> sessionProvider) {
        super(cache, logger, sessionProvider);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    public DeleteMessageResult executeInternal(Action action,
            ExecutionContext context) throws ActionException {
        org.apache.hupa.shared.data.IMAPFolder folder = action.getFolder();
        User user = getUser();
        try {
            IMAPStore store = cache.get(user);
            com.sun.mail.imap.IMAPFolder f = (com.sun.mail.imap.IMAPFolder) store
                    .getFolder(folder.getFullName());
            // check if the folder is open, if not open it "rw"
            if (f.isOpen() == false) {
                f.open(com.sun.mail.imap.IMAPFolder.READ_WRITE);
            }

            Message[] mArray = getMessagesToDelete(action);
            
            // check if the delete was triggered not in the trash folder
            if (folder.getFullName().equalsIgnoreCase(
                    user.getSettings().getTrashFolderName()) == false) {
                com.sun.mail.imap.IMAPFolder trashFolder = (com.sun.mail.imap.IMAPFolder) store
                        .getFolder(user.getSettings().getTrashFolderName());

                boolean trashFound = false;
                // if the trash folder does not exist we create it
                if (trashFolder.exists() == false) {
                    trashFound = trashFolder
                            .create(com.sun.mail.imap.IMAPFolder.READ_WRITE);
                } else {
                    trashFound = true;
                }

                // Check if we are able to copy the messages to the trash folder
                if (trashFound) {
                    // copy the messages to the trashfolder
                    f.copyMessages(mArray, trashFolder);
                }
            }

            
            // delete the messages from the folder
            f.setFlags(mArray, new Flags(Flags.Flag.DELETED), true);
            
            try {
                f.expunge(mArray);
                f.close(false);
            } catch (MessagingException e) {
                // prolly UID expunge is not supported
                f.close(true);
            }
            return new DeleteMessageResult(user, folder, mArray.length);

        } catch (MessagingException e) {
            logger.error("Error while deleting messages for user " + user
                    + " in folder" + action.getFolder(), e);
            throw new ActionException("Error while deleting messages");
        }
    }

    /**
     * Return an array holding all messages which should get deleted by the given action
     * 
     * @param action
     * @return messages
     * @throws ActionException
     */
    protected abstract Message[] getMessagesToDelete(Action action) throws ActionException;
}
