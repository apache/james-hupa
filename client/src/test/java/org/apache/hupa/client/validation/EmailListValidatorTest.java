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

import org.apache.hupa.client.HupaGwtTestCase;

public class EmailListValidatorTest extends HupaGwtTestCase {

    public void testEmailValidator() {
        assertTrue(EmailListValidator.isValidAddressList("abc@abc.def"));
        assertTrue(EmailListValidator.isValidAddressList(" abc@abc.def"));
        assertTrue(EmailListValidator.isValidAddressList("<abc@abc.def>"));
        assertTrue(EmailListValidator.isValidAddressList(" AAA <abc@abc.def> "));
        assertTrue(EmailListValidator.isValidAddressList(", , ,"));
        assertFalse(EmailListValidator.isValidAddressList("abc@abc.def ; ; MMM <mcm@aa>;;;"));
        assertTrue(EmailListValidator.isValidAddressList("abc@abc.def ; ; MMM <mcm@aa.co>;;;"));
        assertTrue(EmailListValidator.isValidAddressList("abc@abc.def\nMMM <mcm@aa.co>;;;"));
        assertTrue(EmailListValidator.isValidAddressList("server-dev-sc.1342023625.aldemmhlhmcipjmoflol-abc=gmail.com@james.apache.org"));
    }

}
