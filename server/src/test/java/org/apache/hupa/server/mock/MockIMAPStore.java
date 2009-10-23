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

import com.sun.mail.imap.IMAPStore;

public class MockIMAPStore extends IMAPStore{

    private Map<String, String> validLogins = new HashMap<String, String>();
    private Map<String, Integer> validServers = new HashMap<String, Integer>();
    private boolean connected = false;
    private List<MockIMAPFolder> folders = new ArrayList<MockIMAPFolder>();
    
    public MockIMAPStore(Session session, URLName url) {
        super(session, url);
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
            } else if (folders.get(i).getFullName().startsWith(folder.getFullName() + MockIMAPFolder.SEPERATOR)) {
                folders.remove(i);
            }
            
        }
        return found;
    } 
    
    public MockIMAPFolder getParent(MockIMAPFolder folder) {
        for (int i = 0; i < folders.size(); i++) {
            MockIMAPFolder f = folders.get(i);
            if ((f.getFullName() +MockIMAPFolder.SEPERATOR + folder.getName()).equals(folder.getFullName())) {
                return f;
            }
        }
        return null;
    }
    
    public List<MockIMAPFolder> getChilds(MockIMAPFolder folder) {
        List<MockIMAPFolder> childs = new ArrayList<MockIMAPFolder>();
        for (int i = 0; i < folders.size(); i++) {
            MockIMAPFolder f = folders.get(i);
            if (f.getFullName().startsWith(folder.getFullName() + MockIMAPFolder.SEPERATOR)) {
                childs.add(f);
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
        return getFolder("INBOX");
    }

    @Override
    public synchronized Folder getFolder(String name) throws MessagingException {
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
        }
        throw new MessagingException("Can't connect to host");

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
