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

package org.apache.hupa.server.preferences;

import gwtupload.server.UploadServlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Hashtable;

import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * A user preferences storage which uses IMAP as repository data
 *
 * @author manolo
 */
public class InImapUserPreferencesStorage extends UserPreferencesStorage {


    // User preferences are saved in IMAP but there is a delay between a new
    // contact is added an the save action. It saves number of operations in
    // the IMAP server.
    // It's not final in order to override in tests to make them run faster
    protected static int IMAP_SAVE_DELAY = 10000;

    protected static final String MAGIC_SUBJECT_CONTACTS = "Hupa-Contacts";

    private static final String HUPA_DATA_MIME_TYPE = "application/hupa-data";


    private static Hashtable<User, Thread> threads = new Hashtable<User, Thread>();

    /**
     * Opens the IMAP folder and read messages until it founds the magic subject,
     * then gets the attachment which contains the data and return the serialized object stored.
     */
    protected static Object readUserPreferencesFromIMAP(Log logger, User user, IMAPStore iStore, String folderName, String magicType)
              throws MessagingException, IOException, ClassNotFoundException {
        Folder folder = iStore.getFolder(folderName);
        if (folder.exists()) {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }
            Message message = null;
            Message[] msgs = folder.getMessages();
            for (Message msg : msgs) {
                if (magicType.equals(msg.getSubject())) {
                    message = msg;
                    break;
                }
            }
            if (message != null) {
                Object con = message.getContent();
                if (con instanceof Multipart) {
                    Multipart mp = (Multipart) con;
                    for(int i=0; i<mp.getCount(); i++) {
                        BodyPart part = mp.getBodyPart(i);
                        if (part.getContentType().toLowerCase().startsWith(HUPA_DATA_MIME_TYPE)) {
                            ObjectInputStream ois = new ObjectInputStream(part.getInputStream());
                            Object o = ois.readObject();
                            ois.close();
                            logger.info("Returning user preferences of type " + magicType + " from imap folder + " + folderName + " for user " + user);
                            return o;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Opens the IMAP folder, deletes all messages which match the magic subject and
     * creates a new message with an attachment which contains the object serialized
     */
    protected static void saveUserPreferencesInIMAP(Log logger, User user, Session session, IMAPStore iStore, String folderName, String subject, Object object)
              throws MessagingException, IOException, InterruptedException {
        IMAPFolder folder = (IMAPFolder) iStore.getFolder(folderName);

        if (folder.exists() || folder.create(IMAPFolder.HOLDS_MESSAGES)) {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }

            // Serialize the object
            ByteArrayOutputStream fout = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(object);
            oos.close();
            ByteArrayInputStream is = new ByteArrayInputStream(fout.toByteArray());

            // Create a new message with an attachment which has the serialized object
            MimeMessage message = new MimeMessage(session);
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart txtPart = new MimeBodyPart();
            txtPart.setContent("This message contains configuration used by Hupa, do not delete it", "text/plain");
            multipart.addBodyPart(txtPart);
            FileItem item = createPreferencesFileItem(is, subject, HUPA_DATA_MIME_TYPE);
            multipart.addBodyPart(MessageUtils.fileitemToBodypart(item));
            message.setContent(multipart);
            message.saveChanges();

            // It seems it's not possible to modify the content of an existing message using the API
            // So I delete the previous message storing the preferences and I create a new one
            Message[] msgs = folder.getMessages();
            for (Message msg : msgs) {
                if (subject.equals(msg.getSubject())) {
                    msg.setFlag(Flag.DELETED, true);
                }
            }

            // It is necessary to copy the message before saving it (the same problem in AbstractSendMessageHandler)
            message = new MimeMessage((MimeMessage)message);
            message.setFlag(Flag.SEEN, true);
            folder.appendMessages(new Message[] { message });
            folder.close(true);
            logger.info("Saved preferences " + subject + " in imap folder " + folderName + " for user " + user);
        } else {
            logger.error("Unable to save preferences " + subject + " in imap folder " + folderName + " for user " + user);
        }
    }

    /**
     * Right now, using the same approach present in upload attachments to create the attachment
     */
    private static FileItem createPreferencesFileItem(InputStream is, String filename, String contentType) throws IOException {
        FileItemFactory f = new DiskFileItemFactory();
        FileItem item = f.createItem(filename, contentType, false, filename);
        UploadServlet.copyFromInputStreamToOutputStream(is, item.getOutputStream());
        return item;
    }

    private Log logger;

    private final IMAPStoreCache cache;

    private final Provider<HttpSession> sessionProvider;

    /**
     * Constructor
     */
    @Inject
    public InImapUserPreferencesStorage(IMAPStoreCache cache, Log logger, Provider<HttpSession> sessionProvider) {
        this.sessionProvider = sessionProvider;
        this.cache = cache;
        this.logger = logger;
    }

    /* (non-Javadoc)
     * @see org.apache.hupa.server.preferences.UserPreferencesStorage#addContact(org.apache.hupa.shared.rpc.ContactsResult.Contact[])
     */
    @Override
    public void addContact(Contact... contacts) {
        HashMap<String, Contact> contactsHash = getContactsHash();

        for (Contact contact : contacts) {
            if (!contactsHash.containsKey(contact.toKey())) {
                contactsHash.put(contact.toKey(), contact);
                saveContactsAsync((User) sessionProvider.get().getAttribute(SConsts.USER_SESS_ATTR));
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.hupa.server.preferences.UserPreferencesStorage#getContacts()
     */
    @Override
    public Contact[] getContacts() {
        HashMap<String, Contact> sessionContacts = getContactsHash();
        return sessionContacts.values().toArray(new Contact[sessionContacts.size()]);
    }

    /**
     * Returns the Hash of contacts getting it from the session if available, or from
     * the IMAP repository if it is the first time.
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, Contact> getContactsHash() {
        HttpSession session = sessionProvider.get();
        HashMap<String, Contact> contactHash = (HashMap<String, Contact>) session.getAttribute(SConsts.CONTACTS_SESS_ATTR);
        if (contactHash == null) {
            try {
                User user = (User) sessionProvider.get().getAttribute(SConsts.USER_SESS_ATTR);
                IMAPStore iStore = cache.get(user);
                Object o = readUserPreferencesFromIMAP(logger, user, iStore, user.getSettings().getDraftsFolderName(), MAGIC_SUBJECT_CONTACTS);
                contactHash = o != null ? (HashMap<String, Contact>) o : new HashMap<String, Contact>();
                session.setAttribute(SConsts.CONTACTS_SESS_ATTR, contactHash);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contactHash;
    }

    /**
     * Saves the contacts list in IMAP asynchronously, It is so because of two reasons:
     * 1.- User processes don't wait for it
     * 2.- It saves number of save operations, because the method addContact
     *  is called frequently when fetching a folder, so add these contacts are
     *  added to the session list, and a thread is delayed to store
     *  all the block.
     */
    private void saveContactsAsync(User user) {
        Thread thread = threads.get(user);
        if (thread == null || !thread.isAlive()) {
            thread = new SavePreferencesThread(user, MAGIC_SUBJECT_CONTACTS, getContactsHash());
            threads.put(user, thread);
            thread.start();
        }
    }

    /**
     * The thread class which saves asynchronously the user preferences
     */
    private class SavePreferencesThread extends Thread {
        private String folderName = null;
        private Object object = null;
        private String subject = null;
        private User user = null;

        public SavePreferencesThread(User user, String subject, Object object) {
            this.user = user;
            this.folderName = user.getSettings().getDraftsFolderName();
            this.subject = subject;
            this.object = object;
        }

        public void run(){
            try {
                sleep(IMAP_SAVE_DELAY);
                saveUserPreferencesInIMAP(logger, user, cache.getMailSession(user), cache.get(user), folderName, subject, object);
            } catch (Exception e) {
                logger.error("Error saving user's preferences: ", e);
            }
        }
    }
}
