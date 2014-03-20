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
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.URLName;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPStore;

public class MockIMAPStore extends IMAPStore{

    public final static String MOCK_INBOX_FOLDER = "Mock-Inbox";
    public final static String MOCK_SENT_FOLDER = "Mock-Sent";
    public final static String MOCK_TRASH_FOLDER = "Mock-Trash";
    public final static String MOCK_DRAFTS_FOLDER = "Mock-Drafts";
    public static final String MOCK_HOST = "hupa.demo";
    public final static String MOCK_LOGIN = "demo";
    private Map<String, String> validLogins = new HashMap<String, String>();
    private boolean connected = false;
    private List<MockIMAPFolder> folders = new ArrayList<MockIMAPFolder>();
    private List<String> capList;
    static final URLName demoUrl = new URLName(null, MOCK_HOST, 0, null, null, null);

    /**
     * Default constructor, it creates the folder structure and loads messages for demo
     */
    @Inject
    public MockIMAPStore(Properties prop) {
        this(Session.getInstance(prop), demoUrl);
    }

    public MockIMAPStore(Session ses) {
        this(ses, demoUrl);
    }
    /**
     * Customized constructor
     */
    public MockIMAPStore(Session session, URLName url) {
        super(session, url);
        if (url != null && MOCK_HOST.equals(url.getHost())) {
            validLogins.put(MOCK_LOGIN, MOCK_LOGIN);
            try {
                new MockIMAPFolder(MOCK_INBOX_FOLDER, this).create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
                new MockIMAPFolder(MOCK_SENT_FOLDER, this).create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
                new MockIMAPFolder(MOCK_TRASH_FOLDER, this).create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
                new MockIMAPFolder(MOCK_DRAFTS_FOLDER, this).create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES);
                ((MockIMAPFolder)getFolder(MOCK_INBOX_FOLDER)).loadDemoMessages(session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Provider getProvider() {
        return new Provider(Provider.Type.STORE,"mockimap",MockIMAPStore.class.getName(),"","");
    }

    public synchronized boolean save(MockIMAPFolder folder) {
        for (MockIMAPFolder iFolder : folders) {
            if (iFolder.getFullName().equals(folder.getFullName())) {
                return false;
            }
        }
        folders.add(folder);
        return true;
    }

    public synchronized boolean delete(MockIMAPFolder folder, boolean recursive) {
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
        for (MockIMAPFolder f : folders) {
            if ((f.getFullName() + MockIMAPFolder.SEPARATOR + folder.getName()).equals(folder.getFullName())) {
                return f;
            }
        }
        return null;
    }

    public synchronized List<MockIMAPFolder> getChilds(MockIMAPFolder folder) {
        List<MockIMAPFolder> childs = new ArrayList<MockIMAPFolder>();
        if (MockIMAPFolder.MOCK_DEFAULT_FOLDER.equals(folder.getFullName())) {
            for(MockIMAPFolder f: folders) {
                if (! MockIMAPFolder.MOCK_DEFAULT_FOLDER.equals(f.getFullName()))
                    childs.add(f);
            }
            return folders;
        } else {
            for (MockIMAPFolder f : folders) {
                if (f.getFullName().startsWith(
                        folder.getFullName() + MockIMAPFolder.SEPARATOR)) {
                    childs.add(f);

                }
            }
        }
        return childs;
    }

    public synchronized void setValidLogins(Map<String,String> validLogins) {
        this.validLogins = validLogins;
    }

    public synchronized void clear() {
        folders.clear();
    }

    @Override
    public synchronized void close() throws MessagingException {
        connected = false;
    }

    @Override
    public synchronized Folder getDefaultFolder() throws MessagingException {
        return getFolder(MockIMAPFolder.MOCK_DEFAULT_FOLDER);
    }

    @Override
    public synchronized Folder getFolder(String name) {
        for (MockIMAPFolder mfolder : folders) {
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
    public synchronized void connect() throws MessagingException {
        connected = true;
    }

    @Override
    public synchronized void connect(String host, int port, String username,
            String password) throws MessagingException {
        connect(username,password);
    }

    @Override
    public synchronized void connect(String host, String user, String password)
            throws MessagingException {
        connect(user,password);
    }

    @Override
    public synchronized void connect(String user, String password) throws MessagingException {
        String pass = validLogins.get(user);
        if (pass != null && validLogins.get(user).equals(password)) {
            connect();
            return;
        }
        throw new AuthenticationFailedException("Invalid login, remember user demo/demo");
    }

    @Override
    public synchronized boolean hasCapability(String capability) throws MessagingException {
        if (capList == null) return false;
        return capList.contains(capability);
    }

    public synchronized void setCapabilities(List<String> capList) {
        this.capList = capList;
    }

}
