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

import java.util.List;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.server.handler.JavamailUtil;
import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.SetFlagAction;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPStore;

public class SetFlagServiceImpl extends AbstractService implements SetFlagService {

    @Override
    public GenericResult set(SetFlagAction action) throws Exception {
        User user = getUser();
        ImapFolder folder = action.getFolder();
        List<Long> uids = action.getUids();
        com.sun.mail.imap.IMAPFolder f = null;
        try {
            IMAPStore store = cache.get(user);

            f = (com.sun.mail.imap.IMAPFolder) store.getFolder(folder.getFullName());
            if (f.isOpen() == false) {
                f.open(Folder.READ_WRITE);
            }
            Message[] msgs = f.getMessagesByUID(toArray(uids));
            Flag flag = JavamailUtil.convert(action.getFlag());
            Flags flags = new Flags();
            flags.add(flag);

            f.setFlags(msgs, flags, action.getValue());
            return new GenericResultImpl();
        } catch (MessagingException e) {
            String errorMsg = "Error while setting flags of messages with uids " + uids + " for user " + user;
            logger.error(errorMsg, e);
            throw new Exception(errorMsg, e);
        } finally {
            if (f != null && f.isOpen()) {
                try {
                    f.close(false);
                } catch (MessagingException e) {
                    // ignore on close
                }
            }
        }
    }

    private long[] toArray(List<Long> uids) {
        long[] array = new long[uids.size()];
        for (int i = 0; i < uids.size(); i++) {
            array[i] = uids.get(i);
        }
        return array;
    }
}
