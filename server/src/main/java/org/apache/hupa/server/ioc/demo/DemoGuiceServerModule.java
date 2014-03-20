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

package org.apache.hupa.server.ioc.demo;

import java.lang.reflect.Constructor;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.CachedIMAPStore;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPStore;

/**
 * Module which binds the handlers and configurations for demo mode
 */
public class DemoGuiceServerModule extends org.apache.hupa.server.ioc.GuiceServerModule {

    public DemoGuiceServerModule(Properties properties) {
        super(properties);
        new RuntimeException().printStackTrace();
    }

    protected Class<? extends IMAPStoreCache> getIMAPStoreCacheClass() {
        return DemoIMAPStoreCache.class;
    }

    /**
     * IMAPStoreCache implementation for Demo mode.
     *
     * It uses mock module which emulates fake imap and smtp servers based on
     * a set of example messages in filesystem and used for unit tests.
     *
     * We use here reflection so as we can deliver Hupa without mock stuff.
     *
     */
    public static class DemoIMAPStoreCache extends InMemoryIMAPStoreCache {
        @Inject
        public DemoIMAPStoreCache(Log logger,
                @Named("IMAPConnectionPoolSize") int connectionPoolSize,
                @Named("IMAPConnectionPoolTimeout") int timeout,
                @Named("SessionDebug") boolean debug,
                @Named("TrustStore") String truststore,
                @Named("TrustStorePassword") String truststorePassword,
                @Named("TrustSSL") boolean trustSSL) {
            super(logger, connectionPoolSize, timeout, debug, truststore, truststorePassword, trustSSL);
        }

        @Override
        public CachedIMAPStore createCachedIMAPStore(User user)
                throws NoSuchProviderException {
            try {
                Class<?> clz = Class.forName("org.apache.hupa.server.mock.MockIMAPStore");
                Constructor<?> cons = clz.getConstructors()[1];
                IMAPStore store = (IMAPStore) cons.newInstance(new Object[] { Session.getInstance(new Properties()) });
                return new CachedIMAPStore(store, 300);
            } catch (ClassNotFoundException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.createCachedIMAPStore(user);
        }

        @Override
        public void sendMessage(Message msg) throws MessagingException {
            try {
                Class.forName("org.apache.hupa.server.mock.MockIMAPStore");
                return;
            } catch (ClassNotFoundException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.sendMessage(msg);
        }
    }

}
