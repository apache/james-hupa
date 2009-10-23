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
import org.apache.hupa.shared.rpc.FetchMessages;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Fetch Messages for a user. The Messages don't contain any body, just some
 * fields of the headers are fetched for perfomance reasons
 * 
 */
public class FetchMessagesHandler extends
        AbstractFetchMessagesHandler<FetchMessages> {

    @Inject
    public FetchMessagesHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> provider) {
        super(cache, logger, provider);
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
    protected Message[] getMessagesToConvert(com.sun.mail.imap.IMAPFolder f,
            FetchMessages action) throws MessagingException {
        String searchString = action.getSearchString();
        int start = action.getStart();
        int offset = action.getOffset();
        int end = start + offset;

        int exists = f.getMessageCount();

        if (end > exists) {
            end = exists;
        }

        int firstIndex = exists - end;
        if (firstIndex < 1) {
            firstIndex = 1;
        }
        int lastIndex = exists - start;
        Message[] messages;

        // check if a searchString was given, and if so use it
        if (searchString == null) {
            messages = f.getMessages(firstIndex, lastIndex);
        } else {
            SearchTerm subjectTerm = new SubjectTerm(searchString);
            SearchTerm fromTerm = new FromStringTerm(searchString);
            SearchTerm bodyTerm = new BodyTerm(searchString);
            SearchTerm orTerm = new OrTerm(new SearchTerm[] { subjectTerm,
                    fromTerm, bodyTerm });
            messages = f.search(orTerm);
            if (end > messages.length) {
                end = messages.length;
            }
            exists = messages.length;
        }
        return messages;
    }
}
