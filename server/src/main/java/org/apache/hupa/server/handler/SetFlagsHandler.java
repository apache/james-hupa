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

import javax.mail.Flags;
<<<<<<< HEAD
<<<<<<< HEAD
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
=======
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Flags.Flag;
>>>>>>> first commit
=======
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.SetFlag;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

public class SetFlagsHandler extends AbstractSessionHandler<SetFlag, GenericResult>{

    @Inject
    public SetFlagsHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> sessionProvider) {
        super(cache, logger, sessionProvider);
    }

    @Override
    protected GenericResult executeInternal(SetFlag action,
            ExecutionContext context) throws ActionException {
        User user = getUser();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        IMAPFolderProxy folder = action.getFolder();
=======
        IMAPFolder folder = action.getFolder();
>>>>>>> first commit
=======
        IMAPFolderProxy folder = action.getFolder();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
        ImapFolder folder = action.getFolder();
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        ArrayList<Long> uids = action.getUids();
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
            return new GenericResult();
        } catch (MessagingException e) {
            String errorMsg = "Error while setting flags of messages with uids " + uids + " for user " + user;
            logger.error(errorMsg,e);
            throw new ActionException(errorMsg,e);
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

    public Class<SetFlag> getActionType() {
        return SetFlag.class;
    }
    
    private long[] toArray(ArrayList<Long> uids) {
        long[] array = new long[uids.size()];
        for (int i = 0; i < uids.size(); i++) {
            array[i] = uids.get(i);
        }
        return array;
    }

}
