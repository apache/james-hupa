package org.apache.hupa.server.handler;

import junit.framework.Assert;

import org.apache.hupa.server.HupaTestCase;
import org.apache.hupa.shared.rpc.Contacts;


public class ContactsHandlerTest extends HupaTestCase {
    
    public void testContactsHandler() throws Exception {
        Assert.assertEquals(0, contactsHandler.execute(new Contacts(), null).getContacts().length);
        userPreferences.addContact("Somebody <somebody@foo.com>");
        userPreferences.addContact(" Some.body   <somebody@foo.com>  ");
        Assert.assertEquals(1, contactsHandler.execute(new Contacts(), null).getContacts().length);
    }
    
}
