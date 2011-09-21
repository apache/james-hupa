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
import javax.mail.search.BodyTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.shared.rpc.FetchMessages;

import com.google.inject.Inject;
import com.google.inject.Provider;

import net.customware.gwt.dispatch.shared.ActionException;

/**
 * Fetch Messages for a user. The Messages don't contain any body, just some
 * fields of the headers are fetched for perfomance reasons
 * 
 */
public class FetchMessagesHandler extends
        AbstractFetchMessagesHandler<FetchMessages> {
    

    @Inject
    public FetchMessagesHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> provider, UserPreferencesStorage preferences) {
        super(cache, logger, provider, preferences);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<FetchMessages> getActionType() {
        return FetchMessages.class;
    }

    @Override
    protected MessageConvertArray getMessagesToConvert(com.sun.mail.imap.IMAPFolder f,
            FetchMessages action) throws MessagingException, ActionException {
        String searchString = action.getSearchString();
        int start = action.getStart();
        int offset = action.getOffset();
        int end = start + offset;
        Message[] messages;
        int exists;
        // check if a searchString was given, and if so use it
        if (searchString == null) {
            exists = f.getMessageCount();

            if (end > exists) {
                end = exists;
            }

            int firstIndex = exists - end + 1;
            if (firstIndex < 1) {
                firstIndex = 1;
            }
            int lastIndex = exists - start;
            
            messages = f.getMessages(firstIndex, lastIndex);
        } else {
            SearchTerm subjectTerm = new SubjectTerm(searchString);
            SearchTerm fromTerm = new FromStringTerm(searchString);
            SearchTerm bodyTerm = new BodyTerm(searchString);
            SearchTerm orTerm = new OrTerm(new SearchTerm[] { subjectTerm,
                    fromTerm, bodyTerm });
            Message[] tmpMessages = f.search(orTerm);
            if (end > tmpMessages.length) {
                end = tmpMessages.length;
            }
            exists = tmpMessages.length;

            int firstIndex = exists - end;        
            
            if (tmpMessages.length > firstIndex) {
                List<Message> mList = new ArrayList<Message>();
                for (int i = firstIndex; i < tmpMessages.length; i++) {
                    if (i == end) break;
                    mList.add(tmpMessages[i]);
                }
                messages = mList.toArray(new Message[mList.size()]);
            } else {
                messages = new Message[0];
            }
          
        }
        logger.debug("Fetching messages for user: " + getUser() + " returns: " + messages.length + " messages in " + f.getFullName());

        return new MessageConvertArray(exists, messages);
    }
}
