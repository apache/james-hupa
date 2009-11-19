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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.URLName;

import org.apache.hupa.server.InMemoryIMAPStoreCache;

import com.sun.mail.imap.IMAPStore;

public class MockIMAPStore extends IMAPStore{

    private Map<String, String> validLogins = new HashMap<String, String>();
    private Map<String, Integer> validServers = new HashMap<String, Integer>();
    private boolean connected = false;
    private List<MockIMAPFolder> folders = new ArrayList<MockIMAPFolder>();
    static final URLName demoUrl = new URLName(null, InMemoryIMAPStoreCache.DEMO_MODE, 143, null, null, null);
    
    /**
     * Demo mode constructor
     */
    public MockIMAPStore(Session session) {
        this(session, demoUrl);
    }

    /**
     * Default constructor
     */
    public MockIMAPStore(Session session, URLName url) {
        super(session, url);
        
        if (InMemoryIMAPStoreCache.DEMO_MODE.equals(url.getHost())) {
            validServers.put(InMemoryIMAPStoreCache.DEMO_MODE, 143);
            validLogins.put("demo", "demo");
            try {
                new MockIMAPFolder(MockIMAPFolder.DEMO_MODE_INBOX_FOLDER, this).create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
                new MockIMAPFolder(MockIMAPFolder.DEMO_MODE_SENT_FOLDER, this).create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
                new MockIMAPFolder(MockIMAPFolder.DEMO_MODE_TRASH_FOLDER, this).create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
                ((MockIMAPFolder)getFolder(MockIMAPFolder.DEMO_MODE_INBOX_FOLDER)).loadDemoMessages(session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Provider getProvider() {
        return new Provider(Provider.Type.STORE,"mockimap",MockIMAPStore.class.getName(),"","");
    }
    
    public boolean save(MockIMAPFolder folder) {
        for (int i= 0; i < folders.size(); i++) {
            if (folders.get(i).getFullName().equals(folder.getFullName())) {
                return false;
            }
        }
        folders.add(folder);
        return true;
    } 
    
    public boolean delete(MockIMAPFolder folder, boolean recursive) {
        boolean found = false;
        for (int i= 0; i < folders.size(); i++) {
            if (folders.get(i).getFullName().equals(folder.getFullName())) {
                folders.remove(i);
                found = true;
                if(recursive == false) {
                    break;
                }
            } else if (folders.get(i).getFullName().startsWith(folder.getFullName() + MockIMAPFolder.SEPARATOR)) {
                folders.remove(i);
            }
            
        }
        return found;
    } 
    
    public MockIMAPFolder getParent(MockIMAPFolder folder) {
        for (int i = 0; i < folders.size(); i++) {
            MockIMAPFolder f = folders.get(i);
            if ((f.getFullName() + MockIMAPFolder.SEPARATOR + folder.getName()).equals(folder.getFullName())) {
                return f;
            }
        }
        return null;
    }
    
    public List<MockIMAPFolder> getChilds(MockIMAPFolder folder) {
        List<MockIMAPFolder> childs = new ArrayList<MockIMAPFolder>();
        if (MockIMAPFolder.DEMO_MODE_DEFAULT_FOLDER.equals(folder.getFullName())) {
            for(MockIMAPFolder f: folders) {
                if (! MockIMAPFolder.DEMO_MODE_DEFAULT_FOLDER.equals(f.getFullName()))
                    childs.add(f);
            }
            return folders;
        } else {
            for (int i = 0; i < folders.size(); i++) {
                MockIMAPFolder f = folders.get(i);
                if (f.getFullName().startsWith(
                        folder.getFullName() + MockIMAPFolder.SEPARATOR)) {
                    childs.add(f);
                    
                }
            }
        }
        return childs;
    }
    
    public void setValidLogins(Map<String,String> validLogins) {
        this.validLogins = validLogins;
    }

    public void setValidServers(Map<String,Integer> validServers) {
        this.validServers = validServers;
    }
    
    public void clear() {
        validLogins.clear();
        validServers.clear();
        folders.clear();
        connected = false;
    }
    
    @Override
    public synchronized void close() throws MessagingException {
        connected = false;
    }

    @Override
    public synchronized Folder getDefaultFolder() throws MessagingException {
        return getFolder(MockIMAPFolder.DEMO_MODE_DEFAULT_FOLDER);
    }

    @Override
    public synchronized Folder getFolder(String name) {
        for (int i = 0; i < folders.size(); i++) {
            MockIMAPFolder mfolder = folders.get(i);
            if (mfolder.getFullName().equals(name)) {
                return mfolder;
            }
        }
        return new MockIMAPFolder(name,this);
    }

    @Override
    public void idle() throws MessagingException {
        // nothing todo
    }

    @Override
    public synchronized boolean isConnected() {
        return connected;
    }

    @Override
    public void connect() throws MessagingException {
        connected = true;
    }

    @Override
    public synchronized void connect(String host, int port, String username,
            String password) throws MessagingException {
        Integer myPort = validServers.get(host);
        if (myPort != null && myPort.intValue() == port) {
            connect(username,password);
        } else {
            throw new MessagingException("Can't connect to host");
        }
    }

    @Override
    public void connect(String host, String user, String password)
            throws MessagingException {
        if (validServers.containsKey(host) == false) {
            throw new MessagingException("Can't connect to host");
        }
        connect(user,password);
    }

    @Override
    public void connect(String user, String password) throws MessagingException {
        String pass = validLogins.get(user);
        if (pass != null && validLogins.get(user).equals(password)) {
            connect();
            return;
        } 
        throw new AuthenticationFailedException("Invalid login");
    }

}
