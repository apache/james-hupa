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

package org.apache.hupa.server.guice;

import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockSMTPTransport;
import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;

import java.util.Properties;

/**
 * Constants and properties used for mock mode
 */
public class DemoModeConstants {
     
    public final static Settings mockSettings = new Settings() {
        private static final long serialVersionUID = 1L;
        {
            setInboxFolderName(MockIMAPStore.MOCK_INBOX_FOLDER);
            setSentFolderName(MockIMAPStore.MOCK_SENT_FOLDER);
            setTrashFolderName(MockIMAPStore.MOCK_TRASH_FOLDER);
            setDraftsFolderName(MockIMAPStore.MOCK_DRAFTS_FOLDER);
        }
    };
    
    public final static Properties demoProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            put("Username",MockIMAPStore.MOCK_LOGIN);
            put("Password",MockIMAPStore.MOCK_LOGIN);

            put("IMAPServerAddress", MockSMTPTransport.MOCK_HOST);
            put("IMAPServerPort", MockSMTPTransport.MOCK_PORT.toString());
            put("IMAPS", "false");
            
            put("TrustStore", "my-truststore");
            put("TrustStorePassword", "my-truststore-password");
            
            put("SMTPServerAddress", MockSMTPTransport.MOCK_HOST);
            put("SMTPServerPort", "25");
            put("SMTPS", "false");
            put("SMTPAuth", "false");
            
            put("SessionDebug", "false");
            
            put("IMAPConnectionPoolSize", "4");
            put("IMAPConnectionPoolTimeout", "300000");

            put("DefaultInboxFolder", MockIMAPStore.MOCK_INBOX_FOLDER);
            put("DefaultTrashFolder", MockIMAPStore.MOCK_TRASH_FOLDER);
            put("DefaultSentFolder", MockIMAPStore.MOCK_SENT_FOLDER);
            put("DefaultDraftsFolder", MockIMAPStore.MOCK_DRAFTS_FOLDER);
            
            put("PostFetchMessageCount", "0");
            
            put("DefaultUserSessionId", "DEMO_ID");
        }
    };

    public final static Settings demoUserSettings = new Settings() {
        private static final long serialVersionUID = 1L;
        {
            setInboxFolderName(MockIMAPStore.MOCK_INBOX_FOLDER);
            setSentFolderName(MockIMAPStore.MOCK_SENT_FOLDER);
            setTrashFolderName(MockIMAPStore.MOCK_TRASH_FOLDER);
            setDraftsFolderName(MockIMAPStore.MOCK_DRAFTS_FOLDER);
        }
    };
    
    public final static User demoUser = new User() {
        private static final long serialVersionUID = 1L;
        {
            setName(MockIMAPStore.MOCK_LOGIN);
            setPassword(MockIMAPStore.MOCK_LOGIN);
            setSettings(demoUserSettings);
        }
    };
    
}
