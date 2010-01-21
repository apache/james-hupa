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
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.shared.rpc.FetchRecentMessages;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class FetchRecentMessagesHandler extends AbstractFetchMessagesHandler<FetchRecentMessages> {

    @Inject
    public FetchRecentMessagesHandler(IMAPStoreCache cache, Log logger,
            Provider<HttpSession> provider, UserPreferencesStorage preferences) {
        super(cache, logger, provider, preferences);
    }

    
    @Override
    protected MessageConvertArray getMessagesToConvert(com.sun.mail.imap.IMAPFolder f,
            FetchRecentMessages action) throws MessagingException {
        Message[] messages = f.search(new FlagTerm(new Flags(Flag.RECENT), true));
        return new MessageConvertArray(messages.length, messages);
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    public Class<FetchRecentMessages> getActionType() {
        return FetchRecentMessages.class;
    }

}
