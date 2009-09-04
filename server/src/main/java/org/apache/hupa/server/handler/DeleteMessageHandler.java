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

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.DeleteMessage;
import org.apache.hupa.shared.rpc.DeleteMessageResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

/**
 * Handler which take care of deleting messages
 * 
 */
public class DeleteMessageHandler extends AbstractSessionHandler<DeleteMessage, DeleteMessageResult>{

	@Inject
	public DeleteMessageHandler(IMAPStoreCache cache, Log logger,Provider<HttpSession> provider) {
		super(cache,logger,provider);
	}


	/*
	 * (non-Javadoc)
	 * @see org.apache.hupa.server.handler.AbstractSessionHandler#executeInternal(org.apache.hupa.shared.rpc.Session, net.customware.gwt.dispatch.server.ExecutionContext)
	 */
	public DeleteMessageResult executeInternal(DeleteMessage action, ExecutionContext context)
			throws ActionException {
		IMAPFolder folder = action.getFolder();
		ArrayList<Long> uids = action.getMessageUids();
		User user = getUser(action.getSessionId());
		
		logger.info("Deleting messages with uids "+ action.getMessageUids() + " for user " + user + " in folder" + action.getFolder());

		try {
			IMAPStore store = cache.get(user);
			com.sun.mail.imap.IMAPFolder f = (com.sun.mail.imap.IMAPFolder)store.getFolder(folder.getFullName());
			// check if the folder is open, if not open it "rw"
			if (f.isOpen() == false) {
				f.open(com.sun.mail.imap.IMAPFolder.READ_WRITE);
			}
			
			// build up the list of messages to delete
			List<Message> messages = new ArrayList<Message>();
			for (int i = 0; i < uids.size();i++) {
				messages.add(f.getMessageByUID(uids.get(i)));
			}
			Message[] mArray = messages.toArray(new Message[messages.size()]);
			
			// check if the delete was triggered not in the trash folder
			if (folder.getFullName().equalsIgnoreCase(user.getSettings().getTrashFolderName()) == false) {
				com.sun.mail.imap.IMAPFolder trashFolder = (com.sun.mail.imap.IMAPFolder) store.getFolder(user.getSettings().getTrashFolderName());
				
				boolean trashFound = false;
				// if the trash folder does not exist we create it
				if (trashFolder.exists() == false) {
					trashFound = trashFolder.create(com.sun.mail.imap.IMAPFolder.READ_WRITE);
				} else {
					trashFound = true;
				}
				
				// Check if we are able to copy the messages to the trash folder
				if (trashFound) {
					// copy the messages to the trashfolder
					f.copyMessages(mArray,trashFolder);
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
		} catch (Exception e) {
			logger.error("Error while deleting messages with uids "+ action.getMessageUids() + " for user " + user + " in folder" + action.getFolder(),e);
			throw new ActionException("Error while deleting messages",e);
		}
		return new DeleteMessageResult(user,folder,uids);
	}

	/*
	 * (non-Javadoc)
	 * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
	 */
	public Class<DeleteMessage> getActionType() {
		return DeleteMessage.class;
	}

}
