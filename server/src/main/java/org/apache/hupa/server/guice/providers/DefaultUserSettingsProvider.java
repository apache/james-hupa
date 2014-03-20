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

package org.apache.hupa.server.guice.providers;

import org.apache.hupa.shared.data.SettingsImpl;
import org.apache.hupa.shared.domain.Settings;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class DefaultUserSettingsProvider implements Provider<Settings> {

    @Inject @Named("DefaultInboxFolder") private String inboxFolder;
    @Inject @Named("DefaultSentFolder") private String sentFolder;
    @Inject @Named("DefaultTrashFolder") private String trashFolder;
    @Inject @Named("DefaultDraftsFolder") private String draftFolder;
    @Inject @Named("PostFetchMessageCount") private int postCount;
    @Inject @Named("IMAPServerAddress") private String imapAddress;
    @Inject @Named("IMAPServerPort") private int imapPort;
    @Inject @Named("IMAPS") private boolean imapSSL;
    @Inject @Named("SMTPServerAddress") private String smtpAddress;
    @Inject @Named("SMTPServerPort") private int smtpPort;
    @Inject @Named("SMTPS") private boolean smtpSSL;
    @Inject @Named("SMTPAuth") private boolean smtpAuth;

    /*
     * (non-Javadoc)
     * @see com.google.inject.Provider#get()
     */
    public Settings get() {
        Settings settings = new SettingsImpl();
        settings.setInboxFolderName(inboxFolder);
        settings.setSentFolderName(sentFolder);
        settings.setTrashFolderName(trashFolder);
        settings.setDraftsFolderName(draftFolder);
        settings.setPostFetchMessageCount(postCount);
        settings.setSmtpServer(smtpAddress);
        settings.setSmtpPort(smtpPort);
        settings.setSmtpSecure(smtpSSL);
        settings.setImapServer(imapAddress);
        settings.setImapPort(imapPort);
        settings.setImapSecure(imapSSL);
        settings.setSmtpAuth(smtpAuth);
        return settings;
    }

}
