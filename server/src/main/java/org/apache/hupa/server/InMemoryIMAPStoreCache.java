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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.commons.logging.Log;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPStore;

@Singleton
public class InMemoryIMAPStoreCache implements IMAPStoreCache {

    protected Session session;
    protected Log logger;
    private final Map<String, CachedIMAPStore> pool = new HashMap<String ,CachedIMAPStore>();
    private String address;
    private int port;
    private boolean useSSL = false;
    
    @Inject
    public InMemoryIMAPStoreCache(Log logger,
            @Named("IMAPServerAddress") String address,
            @Named("IMAPServerPort") int port, 
            @Named("IMAPS") boolean useSSL,
            @Named("IMAPConnectionPoolSize") int connectionPoolSize,
            @Named("IMAPConnectionPoolTimeout") int timeout,
            @Named("SessionDebug") boolean debug,
            @Named("TrustStore") String truststore,
            @Named("TrustStorePassword") String truststorePassword,
            Session session) {
        this.logger = logger;
        this.address = address;
        this.port = port;
        this.useSSL = useSSL;
        this.session = session;
        if (debug && logger.isDebugEnabled()) {
            this.session.setDebug(true);
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
    	IMAPStore ret =  get(user.getName(),user.getPassword());
    	
    	// TODO: this is a hack, we should have a default domain suffix in configuration files
    	if (address.contains("gmail.com") && !user.getName().contains("@")) {
    		user.setName(user.getName() + "@gmail.com");
    	}
    	return ret;
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
    
    public CachedIMAPStore createCachedIMAPStore() throws NoSuchProviderException {
        return new CachedIMAPStore((IMAPStore)session.getStore(useSSL ? "imaps" : "imap"),300);
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

    public Transport getMailTransport(boolean useSSL) throws NoSuchProviderException {
        return session.getTransport(useSSL ? "smtps" : "smtp");
    }

    public Session getMailSession() {
        return session;
    }

}
