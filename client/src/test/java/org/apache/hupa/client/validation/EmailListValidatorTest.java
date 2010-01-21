package org.apache.hupa.client.validation;

import junit.framework.TestCase;

import org.apache.hupa.client.validation.EmailListValidator;


public class EmailListValidatorTest extends TestCase{
    
    public void testEmailValidator() {
        assertTrue(EmailListValidator.isValidAddressList("abc@abc.def"));
        assertTrue(EmailListValidator.isValidAddressList("<abc@abc.def>"));
        assertTrue(EmailListValidator.isValidAddressList(" AAA <abc@abc.def> "));
        assertFalse(EmailListValidator.isValidAddressList(", , ,"));
        assertFalse(EmailListValidator.isValidAddressList("abc@abc.def ; ; MMM <mcm@aa>;;;"));
        assertTrue(EmailListValidator.isValidAddressList("abc@abc.def ; ; MMM <mcm@aa.co>;;;"));
        assertTrue(EmailListValidator.isValidAddressList("abc@abc.def\nMMM <mcm@aa.co>;;;"));
    }

}
