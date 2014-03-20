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

package org.apache.hupa.server.integration;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import junit.framework.Assert;

import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockLog;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.User;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class StoreBugTest {

    static final String imapServer = "imap.gmail.com";
    static final int imapPort = 993;
    static final String imapUser = "nobody@gmail.com";
    static final String imapPass = "******";
    static final boolean isSSl = true;
    static final String truststore = "";
    static final String truststorePassword = "";

    static int nthreads = 5;
    static int threadTimeout = 15000;

    Session session = Session.getDefaultInstance(new Properties(), null);
    static InMemoryIMAPStoreCache cache = new InMemoryIMAPStoreCache(new MockLog(), 2, 60000, false, truststore, truststorePassword, false);
    static User user = new UserImpl() {
       {setName(imapUser); setPassword(imapPass);}
    };

    @Test
    public void testMockIMAPStore() throws Exception {
        TestThread[] threads = testIMAPStoreIdle(getStore(true), "Mock-Inbox", 100);
        Assert.assertNotNull(threads);
        Assert.assertFalse(getThreadsSpentTime(threads).contains("-1"));
    }

    @Test @Ignore
    public void testIMAPStoreIdleHungs() throws Exception {
        IMAPStore store = null;
        try {
            store = getStore(false);
        } catch (Exception e) {
            Throwable t = e;
            while (e.getCause() != null) t = e.getCause();
            String msg = t.getClass().getName() + " " + t.getMessage();
            System.out.println("Skipping integration test: " + msg);
            return;
        }
        TestThread[] threads = testIMAPStoreIdle(store, "INBOX", 30000);
        if (threads == null)
            return;
        System.out.println(getThreadsSpentTime(threads));
        Assert.assertTrue(getThreadsSpentTime(threads).contains("-1"));
    }

    public TestThread[] testIMAPStoreIdle(final IMAPStore store, final String folder, int timeout) throws Exception {
        // Start the threads
        TestThread[] threads = new TestThread[nthreads];
        for (int i = 0; i<nthreads; i++) {
            TestThread t = new TestThread(){
                void execute() throws Exception {
                    store.idle();
                    executeSomeFolderOperations(store, folder);
                }
            };
            threads[i] = t;
            t.start();
        }

        // wait until all threads have finished
        waitForThreads(threads, timeout);
        return threads;
    }

    @Test @Ignore
    public void testInMemoryImapStoreCacheDoesntHung() throws Exception {
        try {
            cache.get(user);
        } catch (Exception e) {
            Throwable t = e;
            while (e.getCause() != null) t = e.getCause();
            String msg = t.getClass().getName() + " " + t.getMessage();
            System.out.println("Skipping integration test: " + msg);
            return;
        }

        // Start the threads
        TestThread[] threads = new TestThread[10];
        for (int i = 0; i<threads.length; i++) {
            TestThread t = new TestThread(){
                void execute() throws Exception {
                  IMAPStore store =   cache.get(user);
                  String folder = "INBOX";
                  executeSomeFolderOperations(store, folder);
                }
            };
            threads[i] = t;
            t.start();
        }

        // wait until all threads have finished
        waitForThreads(threads, 30000);

        Assert.assertFalse(getThreadsSpentTime(threads).contains("-1"));
    }

    private abstract class TestThread extends Thread {
        public long spent_millisecs = -1;
        abstract void execute() throws Exception;
        public void run() {
            try {
                long start = System.nanoTime();
                execute();
                long end = System.nanoTime();
                long diff_nanosecs = end - start;
                long diff_millsecs = diff_nanosecs/1000000;
                spent_millisecs = diff_millsecs;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void executeSomeFolderOperations(IMAPStore store, String folder) throws MessagingException, IOException {
        final IMAPFolder inbox = (IMAPFolder) store.getFolder(folder);
        inbox.open(IMAPFolder.READ_WRITE);
        // count the number of messages in the inbox
        int count = inbox.getMessageCount();
        // get the list of messages
        Message[] messages = inbox.getMessages(1, count);
        // get a randomly selected message in the list
        Message msg = messages[getRandom(1, count)];
        // download the message from the server
        long uid = inbox.getUID(msg);
        Message message = inbox.getMessageByUID(uid);
        // write it to a null stream
        message.writeTo(devNull);
    }

    // Create a new imap store connection (mock or real)
    private IMAPStore getStore(boolean mock) throws Exception {
        IMAPStore store = null;
        if (mock) {
            store = new MockIMAPStore(session);
        } else {
            // Configure session
            session.setDebug(false);
            Properties props = session.getProperties();
            props.setProperty("mail.mime.decodetext.strict", "false");
            props.setProperty("mail.imap.connectionpoolsize", 2 + "");
            props.setProperty("mail.imap.connectionpooltimeout", 30000 + "");
            props.setProperty("mail.imaps.connectionpoolsize", 2 + "");
            props.setProperty("mail.imaps.connectionpooltimeout", 30000 + "");
            System.setProperty("mail.mime.decodetext.strict", "false");
            // Create the imap connection
            store = (IMAPStore)session.getStore(isSSl ? "imaps" : "imap");
            store.connect(imapServer, imapPort, imapUser, imapPass);
        }
        return store;
    }

    @SuppressWarnings("deprecation")
    void waitForThreads(TestThread[] threads, int timeout) throws Exception {
        long start = System.currentTimeMillis();
        while(true) {
            boolean done = true;
            long ellapsed = System.currentTimeMillis() - start;
            for (TestThread t : threads) {
                t.join(1);
                if (t.isAlive())
                    done = false;

                if (ellapsed > timeout)  {
                    t.stop();
                    t.interrupt();
                }
                if (ellapsed > (timeout + 4000)) {
                    System.out.println("It seems there are locked threads.");
                    return;
                }
            }
            if (done)
                break;
            Thread.sleep(100);
        }
    }

    String getThreadsStatus(TestThread[] threads){
        String ret = "";
        for (TestThread t : threads) {
            if (t.isAlive())
                ret += " ACT";
            else if (t.isInterrupted())
                ret += " INT";
            else if (t.spent_millisecs < 0)
                ret += " ERR";
            else
                ret += " OK ";
        }
        return ret;
    }

    String getThreadsSpentTime(TestThread[] threads){
        String ret = "";
        for (TestThread t : threads)
            ret += " " + t.spent_millisecs;
        return ret;
    }

    int getRandom(int floor, int ceil) {
        return floor + (int)(Math.random() * Math.abs(ceil -floor));
    }

    OutputStream devNull = new OutputStream() {
        public void write(int b) throws IOException {
        }
    };

}
