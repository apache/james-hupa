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

import org.apache.hupa.shared.data.Settings;
import org.apache.hupa.shared.data.User;

import java.util.Properties;

/**
 * Constants and properties used for demo mode
 */
public class DemoModeConstants {
    
    private static final long serialVersionUID = 1L;

    public static final String DEMO_MODE = "demo-mode";
    public static final String DEMO_LOGIN = "demo";
    
    public static final String DEMO_MODE_SENT_FOLDER = "Demo-Sent";
    public static final String DEMO_MODE_TRASH_FOLDER = "Demo-Trash";
    public static final String DEMO_MODE_INBOX_FOLDER = "Demo-Inbox";
    public static final String DEMO_MODE_DRAFTS_FOLDER = "Demo-Drafts";
    public static final String DEMO_MODE_DEFAULT_FOLDER = "";
    
    public static final String DEMO_MODE_MESSAGES_LOCATION = "mime/";
    
    public final static Settings mockSettings = new Settings() {
        private static final long serialVersionUID = 1L;
        {
            setInboxFolderName(DEMO_MODE_INBOX_FOLDER);
            setSentFolderName(DEMO_MODE_SENT_FOLDER);
            setTrashFolderName(DEMO_MODE_TRASH_FOLDER);
            setDraftsFolderName(DEMO_MODE_DRAFTS_FOLDER);
        }
    };
    
    public final static Properties demoProperties = new Properties() {
        private static final long serialVersionUID = 1L;
        {
            put("Username",DEMO_LOGIN);
            put("Password",DEMO_LOGIN);

            put("IMAPServerAddress", DEMO_MODE);
            put("IMAPServerPort", "143");
            put("IMAPS", "false");
            
            put("TrustStore", "my-truststore");
            put("TrustStorePassword", "my-truststore-password");
            
            put("SMTPServerAddress", DEMO_MODE);
            put("SMTPServerPort", "25");
            put("SMTPS", "false");
            put("SMTPAuth", "false");
            
            put("SessionDebug", "false");
            
            put("IMAPConnectionPoolSize", "4");
            put("IMAPConnectionPoolTimeout", "300000");

            put("DefaultInboxFolder", DEMO_MODE_INBOX_FOLDER);
            put("DefaultTrashFolder", DEMO_MODE_TRASH_FOLDER);
            put("DefaultSentFolder", DEMO_MODE_SENT_FOLDER);
            put("DefaultDraftsFolder", DEMO_MODE_DRAFTS_FOLDER);
            
            put("PostFetchMessageCount", "0");
            
            put("DefaultUserSessionId", "DEMO_ID");
        }
    };

    public final static Settings demoUserSettings = new Settings() {
        private static final long serialVersionUID = 1L;
        {
            setInboxFolderName(DEMO_MODE_INBOX_FOLDER);
            setSentFolderName(DEMO_MODE_SENT_FOLDER);
            setTrashFolderName(DEMO_MODE_TRASH_FOLDER);
            setDraftsFolderName(DEMO_MODE_DRAFTS_FOLDER);
        }
    };
    
    public final static User demoUser = new User() {
        private static final long serialVersionUID = 1L;
        {
            setName(DEMO_LOGIN);
            setPassword(DEMO_LOGIN);
            setSettings(demoUserSettings);
        }
    };
    
}
