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
=======
>>>>>>> delete messages, make WestActivity Singleton
=======
>>>>>>> remove both of gwt-representer and gwt-dispatch dependencies, add license headers to all new files
package org.apache.hupa.server.service;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.shared.data.DeleteMessageResultImpl;
import org.apache.hupa.shared.domain.DeleteMessageAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
<<<<<<< HEAD
import org.apache.hupa.shared.exception.HupaException;
=======
>>>>>>> delete messages, make WestActivity Singleton

import com.sun.mail.imap.IMAPStore;

public abstract class DeleteMessageBaseServiceImpl extends AbstractService{
<<<<<<< HEAD
	public DeleteMessageResult delete(DeleteMessageAction action) throws HupaException {
=======
	public DeleteMessageResult delete(DeleteMessageAction action) throws Exception {
>>>>>>> delete messages, make WestActivity Singleton
    	ImapFolder folder = action.getFolder();
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
            return new DeleteMessageResultImpl(user, folder, mArray.length);

        } catch (MessagingException e) {
            logger.error("Error while deleting messages for user " + user
                    + " in folder" + action.getFolder(), e);
<<<<<<< HEAD
            throw new HupaException("Error while deleting messages");
=======
            throw new Exception("Error while deleting messages");
>>>>>>> delete messages, make WestActivity Singleton
        }
    }


    /**
     * Return an array holding all messages which should get deleted by the given action
     * 
     * @param action
     * @return messages
     */
<<<<<<< HEAD
    protected abstract Message[] getMessagesToDelete(DeleteMessageAction actionBase) throws HupaException;
=======
    protected abstract Message[] getMessagesToDelete(DeleteMessageAction actionBase) throws Exception;
>>>>>>> delete messages, make WestActivity Singleton
}
