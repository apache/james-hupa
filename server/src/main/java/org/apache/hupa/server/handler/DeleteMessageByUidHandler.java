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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.DeleteMessageByUid;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which take care of deleting messages
 * 
 */
public class DeleteMessageByUidHandler extends
        AbstractDeleteMessageHandler<DeleteMessageByUid> {

    @Inject
    public DeleteMessageByUidHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> provider) {
        super(cache, logger, provider);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<DeleteMessageByUid> getActionType() {
        return DeleteMessageByUid.class;
    }

    @Override
    protected Message[] getMessagesToDelete(DeleteMessageByUid action)
            throws ActionException {
        IMAPFolder folder = action.getFolder();
        ArrayList<Long> uids = action.getMessageUids();
        User user = getUser();

        logger.info("Deleting messages with uids " + action.getMessageUids()
                + " for user " + user + " in folder " + action.getFolder());
        try {
            IMAPStore store = cache.get(user);
            com.sun.mail.imap.IMAPFolder f = (com.sun.mail.imap.IMAPFolder) store
                    .getFolder(folder.getFullName());
            // check if the folder is open, if not open it "rw"
            if (f.isOpen() == false) {
                f.open(com.sun.mail.imap.IMAPFolder.READ_WRITE);
            }
            // build up the list of messages to delete
            List<Message> messages = new ArrayList<Message>();
            for (Long uid : uids) {
                messages.add(f.getMessageByUID(uid));
            }
            Message[] mArray = messages.toArray(new Message[messages.size()]);
            return mArray;
        } catch (MessagingException e) {
            logger.error("Error while deleting messages with uids "
                    + action.getMessageUids() + " for user " + user
                    + " in folder" + action.getFolder(), e);
            throw new ActionException("Error while deleting messages", e);
        }

    }

}
