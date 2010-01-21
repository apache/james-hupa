package org.apache.hupa.server.preferences;

import org.apache.hupa.shared.rpc.ContactsResult.Contact;

/**
 *
 * Interface which defines storage operations related 
 * with user preferences
 *
 */
public interface UserPreferencesStorage {
    
    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates 
     */
    public void addContact(Contact c);
    
    /**
     * Add a new contact to the list.
     * The implementation has to check for duplicates 
     */
    public void addContact(String s);
    
    /**
     * Get the list of contacts 
     */
    public Contact[] getContacts();

}
