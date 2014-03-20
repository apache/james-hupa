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

package org.apache.hupa.client.validation;

import com.google.gwt.user.client.ui.HasText;

import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.Validator;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;

/**
 * Validator which handles the validation of Email Lists. Email addresses
 * needs to be comma separated
 *
 *
 */
public class EmailListValidator extends Validator<EmailListValidator>{
    private static final String emailRegex = "^(.*<)?[A-Za-z0-9._%'*/=+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(>)?\\s*$";

    private HasText text;
    public EmailListValidator(HasText text) {
        this.text = text;
    }

    @Override
    public void invokeActions(ValidationResult result) {
        for (ValidationAction<HasText> action : getFailureActions())
            action.invoke(result, text);
    }

    @Override
    public <V extends ValidationMessages> ValidationResult validate(V messages) {
        if (isValidAddressList(text.getText()) == false) {
            return new ValidationResult();
        }
        return null;
    }

    /**
     * Return if the give text is a valid Emailaddress list
     *
     * @param text
     * @return isValid
     */
    public static boolean isValidAddressList(String text) {
        if (text != null && text.length() > 0) {
            for (String address : text.split("[,;\r\n]+")) {
                if (address.trim().length() > 0 && isValidAddress(address.trim()) == false) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given value is a valid emailaddress
     *
     * @param email
     * @return isValid
     */
    private static boolean isValidAddress(String email) {
        return email.matches(emailRegex);
    }
}
