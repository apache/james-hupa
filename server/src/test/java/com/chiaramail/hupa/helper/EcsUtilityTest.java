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

package com.chiaramail.hupa.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import junit.framework.Assert;

import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockLog;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.User;
import org.junit.Test;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class EcsUtilityTest {
  
    static final String imapServer = "imap.gmail.com";
    static final int imapPort = 993;
    static final boolean isSSl = true;
    static final String truststore = "";
    static final String truststorePassword = "";
    
    static final String imapUser = "mcdodot@gmail.com";
    static final String imapPass = "5233manuel";

    static final String ecsUser = "mcdodot@gmail.com";
    static final String ecsPass = "PNP*8M*!";
    
    static final String testKey = "92210546391265182698995644151691";
    static final String testCryptedText = "ta9WViD2NGlGXbMwiCwz5czVzq+4inQl2RCBFjLOs/rCIbPg1lLcemKaEYRwlxKHajbBi+jKJy6NHd/4Kw6diHLhQ/QVs2C9lTz2toV8SAabr1+sKwGXVqOqtyJLACIP0Hu5Jo7E6t0y3muJGVXgq5leA41kd2z/g9ViXXyuXwCSNImcsA4QFxBm+i/9DAACqrwi3uUR9wbVSOkAfHjEwT3Qpr/O8ClH/82miMsgusq/NcoRFx5TAgf0UFbTZ2mUGbMNFFcD/ZBkzEiDOUlwRlyfP+cYAFJWuXp8W6GAFsE=";
    
    
    Session session = Session.getDefaultInstance(new Properties(), null);
    static InMemoryIMAPStoreCache cache = new InMemoryIMAPStoreCache(new MockLog(), imapServer, imapPort, isSSl, 2, 60000, false,
        truststore, truststorePassword, new JavaMailSessionProvider().get());

    static User user = new UserImpl() {
       {setName(imapUser); setPassword(imapPass);}
    };
    
    static Account ecsAccount = new Account() {
        {email = ecsUser; password = ecsPass;}
    };
    
    @Test
    public void testEncryptDecrypt() throws Exception {
        String clear = "Hello world";
        String key = Utility.generateEncryptionKey();
        String b64Encrypted = Utility.encrypt(key.getBytes(), clear.getBytes());
        byte[] encrypted = Utility.base64DecodeToBytes(b64Encrypted);
        byte[] result = Utility.decrypt(key.getBytes(), encrypted);
        Assert.assertEquals(clear, new String(result));
    }
    
    @Test
    public void testDecryptEcsData() throws Exception {
        byte[] encrypted = Utility.base64DecodeToBytes(testCryptedText);
        byte[] result = Utility.decrypt(testKey.getBytes(), encrypted);
        Assert.assertEquals(215, result.length);
        Assert.assertTrue(new String(result).contains("Manolo"));
    }
    
    @Test
    public void testIsUserRegistered() throws Exception {
        Assert.assertEquals("true", Utility.isUserRegistered(ecsAccount, ecsAccount.email));
    }
    
    @Test
    public void testFetchContent() throws Exception {
        
        IMAPStore store = getStore(true);
        List<Message> messages = getAllEcsMessagesInInbox(store);

        Assert.assertTrue(messages.size() > 0);
        
        for (Message m : messages) {
            // Important: Remove RealName part from the email since ECS server understands just the sender email address
            String fromEmail = m.getFrom()[0].toString().replaceFirst("^.*<(.*)>.*$", "$1").trim();
            
            String[] tmp = m.getHeader(Utility.CONTENT_SERVER_NAME);
            String serverName = tmp != null && tmp.length > 0 ? tmp[0] : Utility.DEFAULT_CONTENT_SERVER_NAME;
            
            tmp = m.getHeader(Utility.CONTENT_SERVER_PORT);
            String serverPort = tmp != null && tmp.length > 0 ? tmp[0] : Utility.DEFAULT_CONTENT_SERVER_PORT;
            
            tmp = m.getHeader(Utility.ENCRYPTION_KEY);
            String key = tmp != null && tmp.length > 0 ? tmp[0] : null;
            
            tmp = m.getHeader(Utility.CONTENT_POINTER);
            String pointer = tmp != null && tmp.length > 0 ? tmp[0] : "";
            String[] pointers = pointer.split("\\s+");
            
            // Getting just the content using utility method in Utility class
            System.out.println("+++ Fetching Content for Message: " + fromEmail + " " + pointer + " " + key);
            String s = Utility.fetchBodyContent(m, ecsAccount, pointers, serverName, serverPort, key);
            Assert.assertNotNull(s);
            // System.out.println(s);
            
            // Demonstration of how to get body and attachments using directly the askServer method
            

            
            for (int i = 0; i < pointers.length; i++) {
                String[] responseLines = Utility.askServer(
                        "https://" + serverName + ":" + serverPort + Utility.CONTENT_SERVER_APP, 
                        Utility.FETCH_CONTENT + Utility.BLANK + fromEmail + Utility.BLANK + pointers[i], 
                        ecsAccount.getEmail(), Utility.base64Encode(ecsAccount.getPassword()),
                        i > 0, fromEmail + "_" + pointers[0] + "_" + pointers[i] + ".ecs", key);
                
                Assert.assertEquals("3", responseLines[0]);
                // System.out.println("++ Ecs server Response for part: " + i + " " + pointers[i]);
                // for (String t : responseLines) {
                   // System.out.println(t);
                //}
            }
        }
    }

    private List<Message> getAllEcsMessagesInInbox(IMAPStore store) throws MessagingException, IOException {
        
        String folder = store instanceof MockIMAPStore ? MockIMAPStore.MOCK_INBOX_FOLDER : "INBOX";
        
        final IMAPFolder imapFolder = (IMAPFolder) store.getFolder(folder);
        imapFolder.open(IMAPFolder.READ_WRITE);
        
        // Get all Messages in folder (light-weight references)
        // Warning: if you run the test with a real IMAP account you could have thousands of messages.
        int count = imapFolder.getMessageCount();
        Message[] messages = imapFolder.getMessages(1, count);

        // Filter all messages to return only valid ECS messages 
        List<Message> ret = new ArrayList<Message>();
        for (Message m : messages) {
            if (Utility.validateHeaders(m)) {
                ret.add(m);
            }
        }
        return ret;
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
}
