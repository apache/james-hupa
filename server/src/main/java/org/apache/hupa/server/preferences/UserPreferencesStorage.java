package org.apache.hupa.server.preferences;

import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Abstract class which defines storage operations related 
 * with user preferences
 *
 */
public abstract class UserPreferencesStorage {
    
    protected static final String CONTACTS_ATTR = "contacts";
        
    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates 
     */
    abstract public void addContact(Contact... c);
    
    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates 
     */
    final public void addContact(String... mails) {
        if (mails != null) {
            addContact(Arrays.asList(mails));
        }
    }

    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates 
     */
    final public void addContact(List<String> mails) {
        if (mails != null) {
            for (String mail: mails) {
                Contact contact = new Contact(mail);
                addContact(contact);
            }
        }
    }
    
    /**
     * Get the list of contacts 
     */
    abstract public Contact[] getContacts();

}
