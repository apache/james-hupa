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

package org.apache.hupa.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import com.sun.mail.imap.IMAPStore;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.data.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

@Singleton
public class InMemoryIMAPStoreCache implements IMAPStoreCache{

    private Session session;
    protected Log logger;
    private final Map<String, CachedIMAPStore> pool = new HashMap<String ,CachedIMAPStore>();
    private String address;
    private int port;
    private boolean useSSL = false;
    
    @Inject
    public InMemoryIMAPStoreCache(Log logger, @Named("IMAPServerAddress") String address, @Named("IMAPServerPort") int port, @Named("IMAPS") boolean useSSL, @Named("IMAPConnectionPoolSize") int connectionPoolSize, @Named("IMAPConnectionPoolTimeout") int timeout, @Named("SessionDebug") boolean debug,
            @Named("TrustStore") String truststore, @Named("TrustStorePassword") String truststorePassword, Provider<Session> sessionProvider) {
        this.logger = logger;
        this.address = address;
        this.port = port;
        this.useSSL = useSSL;
      
        session = sessionProvider.get();
        if (debug && logger.isDebugEnabled()) {
            session.setDebug(true);
        }
        
        Properties props = session.getProperties();
        
        props.setProperty("mail.mime.decodetext.strict", "false");
        if (useSSL) {
            props.setProperty("mail.store.protocol", "imaps");
            props.setProperty("mail.imaps.connectionpoolsize", connectionPoolSize +"");
            props.setProperty("mail.imaps.connectionpooltimeout", timeout + "");
            if (!truststore.isEmpty()) {
        	System.setProperty("javax.net.ssl.trustStore", truststore);
            }
            if (!truststorePassword.isEmpty()) {
                System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);
            }
        } else {
            props.setProperty("mail.imap.connectionpoolsize", connectionPoolSize + "");
            props.setProperty("mail.imap.connectionpooltimeout", timeout + "");

        }
        System.setProperty("mail.mime.decodetext.strict", "false");
      
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.IMAPStoreCache#get(org.apache.hupa.shared.data.User)
     */
    public IMAPStore get(User user) throws MessagingException {
        return get(user.getName(),user.getPassword());
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.IMAPStoreCache#get(java.lang.String, java.lang.String)
     */
    public IMAPStore get(String username, String password) throws MessagingException {

        CachedIMAPStore cstore = pool.get(username);

        if (cstore == null) {
            logger.debug("No cached store found for user " +username);
            cstore = createCachedIMAPStore();
        } else {
            if (cstore.isExpired() == false) {
                try {
                    cstore.validate();
                } catch (MessagingException e) {
                    cstore = createCachedIMAPStore();
                }
            } else {
                pool.remove(username);
                try {
                    if (cstore != null) cstore.getStore().close();
                } catch (MessagingException e) {
                    // ignore on close
                }
                cstore = createCachedIMAPStore();
            }
        }
        
        if (cstore.getStore().isConnected() == false) {
            try {
                cstore.getStore().connect(address, port, username, password);
            } catch (MessagingException e) {
                    throw (e);
            }
        }
        pool.put(username, cstore);
        return cstore.getStore();
    }
    
    private CachedIMAPStore createCachedIMAPStore() throws NoSuchProviderException {
        CachedIMAPStore cstore;
        if (DemoModeConstants.DEMO_MODE.equals(this.address)) {
            cstore = new CachedIMAPStore(new MockIMAPStore(session), 300);
        } else if (useSSL) {
            cstore = new CachedIMAPStore((IMAPStore)session.getStore("imaps"),300);
        } else {
            cstore = new CachedIMAPStore((IMAPStore)session.getStore("imap"),300);
        }
        return cstore;
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.IMAPStoreCache#delete(org.apache.hupa.shared.data.User)
     */
    public synchronized void delete(User user) {
        delete(user.getName());
    }
    
    /*
     * (non-Javadoc)
     * @see org.apache.hupa.server.IMAPStoreCache#delete(java.lang.String)
     */
    public synchronized void delete(String username) {
        CachedIMAPStore cstore = pool.get(username);
        if (cstore != null && cstore.getStore().isConnected()) {
            try {
                cstore.getStore().close();
            } catch (MessagingException e) {
                // Ignore on close
            }
        }
        pool.remove(username);
    }
    
    private final class CachedIMAPStore {
        private long validUntil;
        private int validForMillis;
        private IMAPStore store;
        
        public CachedIMAPStore(IMAPStore store, int validForSeconds) {
            this.store = store;
            this.validForMillis = validForSeconds * 1000;
            this.validUntil = System.currentTimeMillis() + validForMillis;
        }
        
        public boolean isExpired() {
            if (validUntil > System.currentTimeMillis() && store.isConnected()) {
                return false;
            }
            return true;
        }
        
        public void validate() throws MessagingException {
            validUntil = System.currentTimeMillis() + validForMillis;
        }
        
        public IMAPStore getStore() {
            return store;
        }
    }

}
