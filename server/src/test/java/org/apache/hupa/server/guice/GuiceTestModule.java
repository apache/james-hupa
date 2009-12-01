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

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.handler.AbstractSendMessageHandler;
import org.apache.hupa.server.handler.ForwardMessageHandler;
import org.apache.hupa.server.handler.GetMessageDetailsHandler;
import org.apache.hupa.server.handler.SendMessageHandler;
import org.apache.hupa.server.mock.MockHttpSession;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.server.mock.MockLogProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.sun.mail.imap.IMAPStore;

public class GuiceTestModule extends AbstractModule {

    @Override
    protected void configure() {

        DemoModeConstants.demoProperties.put("DefaultUserSessionId", "just_an_id");
        Names.bindProperties(binder(), DemoModeConstants.demoProperties);

        bind(Log.class).toProvider(MockLogProvider.class).in(Singleton.class);
        bind(Session.class).toProvider(SessionProvider.class);
        bind(HttpSession.class).to(MockHttpSession.class).in(Singleton.class);

        bind(IMAPStoreCache.class).to(MockIMAPStoreCache.class).in(Singleton.class);
        bind(IMAPStore.class).to(MockIMAPStore.class);

        bind(GetMessageDetailsHandler.class);
        
        bind(AbstractSendMessageHandler.class).to(SendMessageHandler.class);
        bind(SendMessageHandler.class);
        bind(ForwardMessageHandler.class);

    }

    private Injector injector;
    public Injector getInjector() {
        if (injector == null)
            injector = Guice.createInjector(this);
        return injector;
    }


}
