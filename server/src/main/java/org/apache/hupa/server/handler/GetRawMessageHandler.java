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

import java.io.ByteArrayOutputStream;

import javax.mail.Folder;
import javax.mail.Message;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.RawMessage;
import org.apache.hupa.shared.rpc.RawMessageResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class GetRawMessageHandler extends AbstractSessionHandler<RawMessage, RawMessageResult>{

    @Inject
    public GetRawMessageHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> sessionProvider) {
        super(cache, logger, sessionProvider);
    }

    @Override
    protected RawMessageResult executeInternal(RawMessage action,
            ExecutionContext context) throws ActionException {
        User user = getUser();
        long uid = action.getUid();
        org.apache.hupa.shared.data.ImapFolderImpl folder = action.getFolder();
        try {
            IMAPStore store = cache.get(user);
            IMAPFolder f = (IMAPFolder) store.getFolder(folder.getFullName());
            if (f.isOpen() == false) {
                f.open(Folder.READ_ONLY);
            }
             Message m = f.getMessageByUID(action.getUid());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            m.writeTo(out);
            if (f.isOpen()) {
                f.close(false);
            }
            return new RawMessageResult(out.toString());
        } catch (Exception e) {
            logger.error("Unable to get raw content of msg for user " + user
                    + " in folder " + folder + " with uid " + uid, e);
            throw new ActionException("Unable to et raw content of msg for user " + user
                    + " in folder " + folder + " with uid " + uid);
        }
        
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<RawMessage> getActionType() {
        return RawMessage.class;
    }

}
