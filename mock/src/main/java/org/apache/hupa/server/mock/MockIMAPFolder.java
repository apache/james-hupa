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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class MockIMAPFolder extends IMAPFolder {

    public static final char SEPARATOR = '.';
    public static final String MOCK_DEFAULT_FOLDER = "";
    public static final String MOCK_MESSAGES_LOCATION = "mime/";

    public List<Message> messages = new ArrayList<Message>();
    private boolean closed = true;
    private boolean exists;

    public MockIMAPFolder(String fullName, IMAPStore store) {
        super(fullName, (MOCK_DEFAULT_FOLDER.equals(fullName) ? '\0' : SEPARATOR), store, false);
    }

    @Override
    public synchronized Message[] addMessages(Message[] mArray)
            throws MessagingException {
        checkExists();
        messages.addAll(Arrays.asList(mArray));
        return mArray;
    }

    @Override
    public void appendMessages(Message[] msgs) throws MessagingException {
        addMessages(msgs);
    }

    @Override
    public synchronized void close(boolean expunge) throws MessagingException {
        checkExists();
        if (expunge) {
            expunge();
        }
        closed = true;
    }

    @Override
    public Folder[] list() throws MessagingException {
        List<MockIMAPFolder> folders = ((MockIMAPStore) store).getChilds(this);
        return folders.toArray(new Folder[folders.size()]);
    }

    @Override
    public synchronized void copyMessages(Message[] messages, Folder folder)
            throws MessagingException {
        checkExists();
        ((MockIMAPFolder) folder).addMessages(messages);

    }

    public synchronized void loadDemoMessages(Session session) throws MessagingException {
        if (!exists()) {
            create(IMAPFolder.HOLDS_MESSAGES);
            open(Folder.READ_WRITE);
        }
        for(int i=0;;i++) {
            String name = MOCK_MESSAGES_LOCATION + i + ".msg";
            InputStream is =  Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            if (is == null) break;
            addMessages(new Message[]{new MimeMessage(session, is)});
        }
    }

    @Override
    public synchronized boolean create(int type) throws MessagingException {
        if (exists()) {
            throw new MessagingException("Folder already exists!");
        }
        exists = true;
        return ((MockIMAPStore) store).save(this);
    }

    @Override
    public synchronized boolean delete(boolean recursive)
            throws MessagingException {
        exists = false;
        return ((MockIMAPStore) store).delete(this, recursive);
    }

    @Override
    public synchronized boolean exists() throws MessagingException {
        return exists;
    }

    @Override
    public synchronized void fetch(Message[] msgs, FetchProfile fp)
            throws MessagingException {
        checkExists();
    }

    @Override
    public synchronized int getDeletedMessageCount() throws MessagingException {
        return 0;
    }

    @Override
    public Folder getFolder(String name) throws MessagingException {
        return super.getFolder(name);
    }

    @Override
    public synchronized String getFullName() {
        return fullName;
    }

    @Override
    public synchronized Message getMessage(int msgnum)
            throws MessagingException {
        checkExists();
        if (messages.size() <= msgnum) {
            throw new MessagingException();
        }
        return messages.get(msgnum);
    }

    @Override
    public synchronized Message getMessageByUID(long uid)
            throws MessagingException {
        checkExists();
        return getMessage((int)uid);
    }

    @Override
    public synchronized int getMessageCount() throws MessagingException {
        checkExists();
        return messages.size();
    }

    @Override
    public synchronized Message[] getMessagesByUID(long uidstart, long uidend)
            throws MessagingException {
        checkExists();
        return getMessages((int)uidstart, (int)uidend);
    }

    @Override
    public synchronized Message[] getMessagesByUID(long[] uids)
            throws MessagingException {
        checkExists();
        int ints[] = new int[uids.length];
        for (int i = 0; i < uids.length; i++) {
            ints[i] = (int)uids[i];
        }
        return getMessages(ints);
    }

    @Override
    public synchronized String getName() {
        return name;
    }

    @Override
    public synchronized int getNewMessageCount() throws MessagingException {
        checkExists();
        return 0;
    }

    @Override
    public synchronized Folder getParent() throws MessagingException {
        return ((MockIMAPStore) store).getParent(this);
    }

    @Override
    public void idle() throws MessagingException {
        checkExists();
    }

    @Override
    public synchronized boolean isOpen() {
        return closed == false;
    }

    @Override
    public synchronized boolean isSubscribed() {
        return true;
    }

    @Override
    public synchronized void open(int arg0) throws MessagingException {
        checkExists();
        closed = false;
    }

    @Override
    public synchronized boolean renameTo(Folder f) throws MessagingException {
        checkExists();
        return false;
    }

    @Override
    public synchronized Message[] search(SearchTerm arg0, Message[] arg1)
            throws MessagingException {
        checkExists();
        return arg1;
    }

    @Override
    public synchronized Message[] expunge() throws MessagingException {
        checkExists();
        return expunge(messages.toArray(new Message[messages.size()]));
    }

    @Override
    public synchronized Message[] expunge(Message[] msgs)
            throws MessagingException {
        checkExists();
        List<Message> mList = new ArrayList<Message>();
        for (Message message : msgs) {
            if (message.getFlags().contains(Flag.DELETED)) {
                if (messages.remove(message)) {
                    mList.add(message);
                }
            }
        }
        return mList.toArray(new Message[mList.size()]);
    }

    @Override
    public synchronized Message[] search(SearchTerm search)
            throws MessagingException {
        checkExists();
        ArrayList<Message> ret = new ArrayList<Message>();
        for (Message m : messages) {
            if (search.match(m)) {
                ret.add(m);
            }
        }
        return ret.toArray(new Message[0]);
    }

    @Override
    public synchronized void setFlags(Message[] mArray, Flags flags,
            boolean value) throws MessagingException {
        checkExists();
        for (Message message : mArray) {
            for (Message message2 : messages) {
                if (message2.equals(message)) {
                    message2.setFlags(flags, value);
                    break;
                }
            }
        }
    }

    @Override
    public synchronized Message[] getMessages() throws MessagingException {
        checkExists();
        return messages.toArray(new Message[messages.size()]);

    }

    @Override
    public synchronized Message[] getMessages(int start, int end)
            throws MessagingException {
        checkExists();
        Message[] array = new Message[end- --start];
        for (int i=0; start<end; i++,start++)
            array[i] = messages.get(start);
        return array;
    }

    @Override
    public synchronized Message[] getMessages(int[] ints)
            throws MessagingException {
        checkExists();
        Message[] array = new Message[ints.length];

        for (int i = 0; i < ints.length; i++) {
            int mInt = ints[i];
            if (mInt > messages.size() || mInt < 0) {
                throw new MessagingException();
            }
            array[i] = messages.get(ints[i]);
        }
        return array;
    }

    @Override
    public Store getStore() {
        return store;
    }

    @Override
    public synchronized void setFlags(int arg0, int arg1, Flags arg2,
            boolean arg3) throws MessagingException {
        checkExists();
    }

    @Override
    public synchronized void setFlags(int[] arg0, Flags arg1, boolean arg2)
            throws MessagingException {
        checkExists();
    }

    @Override
    public synchronized long getUID(Message message) throws MessagingException {
        checkExists();
        return messages.indexOf(message);
    }

    @Override
    public synchronized int getUnreadMessageCount() throws MessagingException {
        int ret = getMessageCount();
        for (Message msg: messages) {
            if (msg.getFlags().contains(Flag.SEEN))
                ret --;
        }
        return ret;
    }

    @Override
    protected void checkExists() throws MessagingException {
        if (exists() == false) {
            throw new MessagingException("Folder not exists");
        }
    }

}
