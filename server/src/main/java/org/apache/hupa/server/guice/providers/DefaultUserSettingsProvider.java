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
    private String inboxFolder;
    private String sentFolder;
    private String trashFolder;
    private String draftFolder;
    private int postCount;

    @Inject
    public DefaultUserSettingsProvider(@Named("DefaultInboxFolder") String inboxFolder, @Named("DefaultSentFolder") String sentFolder, @Named("DefaultTrashFolder") String trashFolder, @Named("DefaultDraftsFolder") String draftFolder, @Named("PostFetchMessageCount") int postCount) {
        this.inboxFolder = inboxFolder;
        this.sentFolder = sentFolder;
        this.trashFolder = trashFolder;
        this.draftFolder = draftFolder;
        this.postCount = postCount;
    }
    
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
        return settings;
    }

}
