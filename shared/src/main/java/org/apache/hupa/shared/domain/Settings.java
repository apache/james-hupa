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

package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(Settings.class)
public interface Settings extends ValueProxy {

    void setInboxFolderName(String inboxFolder);
    void setSentFolderName(String sentFolder);
    void setTrashFolderName(String trashFolder);
    void setDraftsFolderName(String draftFolder);
    void setPostFetchMessageCount(int postCount);
    String getTrashFolderName();
    String getInboxFolderName();
    String getSentFolderName();
    String getDraftsFolderName();

    String getImapServer();
    void setImapServer(String imapServer);
    int getImapPort();
    void setImapPort(int imapPort);
    boolean getImapSecure();
    void setImapSecure(boolean imapSecure);
    String getSmtpServer();
    void setSmtpServer(String smptServer);
    int getSmtpPort();
    void setSmtpPort(int smtpPort);
    boolean getSmtpSecure();
    void setSmtpSecure(boolean smtpSecure);
    boolean getSmtpAuth();
    void setSmtpAuth(boolean smtpAuth);
}
