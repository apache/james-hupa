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

package org.apache.hupa.server.mock;

import java.util.Properties;

import org.apache.hupa.shared.data.SettingsImpl;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;

/**
 * Constants and properties used for mock mode
 */
public class MockConstants {

    public static String SESSION_ID = "MockID";

    public final static Settings mockSettings = new SettingsImpl() {
        {
            setInboxFolderName(MockIMAPStore.MOCK_INBOX_FOLDER);
            setSentFolderName(MockIMAPStore.MOCK_SENT_FOLDER);
            setTrashFolderName(MockIMAPStore.MOCK_TRASH_FOLDER);
            setDraftsFolderName(MockIMAPStore.MOCK_DRAFTS_FOLDER);
            setImapServer("localhost");
            setImapPort(143);
            setImapSecure(false);
            setSmtpServer("localhost");
            setSmtpPort(25);
            setSmtpSecure(false);
            setSmtpAuth(false);
        }
    };

    public final static Properties mockProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            put("Username", MockIMAPStore.MOCK_LOGIN);
            put("Password", MockIMAPStore.MOCK_LOGIN);

            put("IMAPServerAddress", MockIMAPStore.MOCK_HOST);
            put("SMTPServerAddress", MockIMAPStore.MOCK_HOST);

            put("SessionDebug", "false");

            put("DefaultInboxFolder", MockIMAPStore.MOCK_INBOX_FOLDER);
            put("DefaultTrashFolder", MockIMAPStore.MOCK_TRASH_FOLDER);
            put("DefaultSentFolder", MockIMAPStore.MOCK_SENT_FOLDER);
            put("DefaultDraftsFolder", MockIMAPStore.MOCK_DRAFTS_FOLDER);

            put("PostFetchMessageCount", "0");
        }
    };

    public final static User mockUser = new UserImpl() {
        {
            setName(MockIMAPStore.MOCK_LOGIN);
            setPassword(MockIMAPStore.MOCK_LOGIN);
            setSettings(mockSettings);
        }
    };
}
