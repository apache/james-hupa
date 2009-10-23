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
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.MoveMessage;
import org.apache.hupa.shared.rpc.MoveMessageResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which handle moving of messages
 *
 */
public class MoveMessageHandler extends AbstractSessionHandler<MoveMessage, MoveMessageResult>{

    @Inject
    public MoveMessageHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> sessionProvider) {
        super(cache, logger, sessionProvider);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
     */
    protected MoveMessageResult executeInternal(MoveMessage action,
            ExecutionContext context) throws ActionException {
        User user = getUser();
        try {
            IMAPStore store = cache.get(user);
            IMAPFolder folder = (IMAPFolder)store.getFolder(action.getOldFolder().getFullName());
            if (folder.isOpen() == false) {
                folder.open(Folder.READ_WRITE);
            }
            Message m = folder.getMessageByUID(action.getMessageUid());
            Message[] mArray = new Message[] {m};
            folder.copyMessages(mArray, store.getFolder(action.getNewFolder().getFullName()));
            folder.setFlags(mArray, new Flags(Flags.Flag.DELETED), true);
            try {
                folder.expunge(mArray);
                folder.close(false);
            } catch (MessagingException e) {
                // prolly UID expunge is not supported
                folder.close(true);
            }
            return new MoveMessageResult();
        } catch (MessagingException e) {
            logger.error("Error while moving message " + action.getMessageUid() + " from folder " + action.getOldFolder() + " to " + action.getNewFolder(),e);
            throw new ActionException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<MoveMessage> getActionType() {
        return MoveMessage.class;
    }

}
