package org.apache.hupa.server;

import javax.mail.MessagingException;

import com.sun.mail.imap.IMAPStore;

public class CachedIMAPStore {
    private long validUntil;
    private int validForMillis;
    private IMAPStore store;
    
    private CachedIMAPStore() {
    }
    
    public static CachedIMAPStore getInstance() {
        return new CachedIMAPStore();
    }

    public CachedIMAPStore(IMAPStore store, int validForSeconds) {
        this.store = store;
        this.validForMillis = validForSeconds * 1000;
        this.validUntil = System.currentTimeMillis() + validForMillis;
    }

    public boolean isExpired() {
        if (validUntil > System.currentTimeMillis() && store.isConnected()) {
            return false;
        }
        return true;
    }

    public void validate() throws MessagingException {
        validUntil = System.currentTimeMillis() + validForMillis;
    }

    public IMAPStore getStore() {
        return store;
    }
}