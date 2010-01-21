package org.apache.hupa.server.preferences;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

/**
 * A preferences storage which uses session as repository data
 */
public class InSessionUserPreferencesStorage implements UserPreferencesStorage {

    private final Provider<HttpSession> sessionProvider;
    
    @Inject
    public InSessionUserPreferencesStorage(IMAPStoreCache cache, Log logger, Provider<HttpSession> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void addContact(Contact contact) {
        
        HttpSession session = sessionProvider.get();
        
        @SuppressWarnings("unchecked")
        HashMap<String, Contact> sessionContacts = (HashMap<String, Contact>)session.getAttribute("contacts");
        if (sessionContacts==null)
            sessionContacts=new HashMap<String, Contact>();
        
        if (!sessionContacts.containsKey(contact.toKey())) {
            sessionContacts.put(contact.toKey(), contact);
            session.setAttribute("contacts", sessionContacts);
        }
    }

    public void addContact(String mail) {
        Contact contact = new Contact(mail);
        addContact(contact);
    }

    public Contact[] getContacts() {
        
        HttpSession session = sessionProvider.get();
        
        @SuppressWarnings("unchecked")
        HashMap<String, Contact> sessionContacts = (HashMap<String, Contact>)session.getAttribute("contacts");

        return sessionContacts == null ? new Contact[]{} : sessionContacts.values().toArray(new Contact[sessionContacts.size()]);
    }
    
}
