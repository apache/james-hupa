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

package org.apache.hupa.server.service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.server.utils.SettingsDiscoverer;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.exception.HupaException;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class LoginUserServiceImpl extends AbstractService implements LoginUserService {

    @Inject private Provider<Settings> settingsProvider;
    @Inject private SettingsDiscoverer settingsDiscoverer;

    public User login(String username, String password, Settings settings) throws HupaException, MessagingException {
        logger.debug("Login user: " + username + " " + password);
        try {
            HttpSession httpSession = httpSessionProvider.get();
            SessionUtils.cleanSessionAttributes(httpSession);
            User user = new UserImpl();
            user.setName(username);
            user.setPassword(password);
            user.setSettings(fix(settings));
            cache.get(user);
            user.setAuthenticated(true);
            httpSession.setAttribute(SConsts.USER_SESS_ATTR, user);
            logger.debug("Logged user: " + username);
            settingsDiscoverer.setValidSettings(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Settings fix(Settings a) {
        if (settingsProvider != null) {
            Settings b = settingsProvider.get();
            if (a == null) {
                return b;
            }
            a.setImapServer(or(a.getImapServer(), b.getImapServer()));
            a.setImapPort(or(a.getImapPort(), b.getImapPort()));
            a.setSmtpServer((or(a.getSmtpServer(), b.getSmtpServer())));
            a.setSmtpPort(or(a.getSmtpPort(), b.getSmtpPort()));

            a.setInboxFolderName(or(a.getInboxFolderName(), b.getInboxFolderName()));
            a.setSentFolderName(or(a.getSentFolderName(), b.getSentFolderName()));
            a.setTrashFolderName(or(a.getTrashFolderName(), b.getTrashFolderName()));
            a.setDraftsFolderName(or(a.getDraftsFolderName(), b.getDraftsFolderName()));
        }
        return a;
    }

    private <T> T or (T a, T b) {
        return a == null ? b : a;
    }

    @Override
    public Settings getSettings(String email) {
        if (settingsDiscoverer == null) {
            settingsDiscoverer = new SettingsDiscoverer();
        }
        return settingsDiscoverer.discoverSettings(email);
    }

}
