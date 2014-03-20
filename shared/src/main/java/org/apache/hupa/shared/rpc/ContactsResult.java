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

package org.apache.hupa.shared.rpc;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;



public class ContactsResult{

    public static class Contact implements IsSerializable, Serializable{
        private static final long serialVersionUID = -8632580327693416473L;
        public String mail;
        public String realname;

        public Contact() {
        }

        public Contact(String address) {
            mail = address.replaceAll("^.*<([^>]+)>\\s*$", "$1");

            realname = mail.equals(address) ? mail : address
                    // remove the email part
                    .replaceAll("<[^<>]+>\\s*$", "")
                    // remove start symbols in the name
                    .replaceAll("^[\\s\"'<]+", "")
                    // remove end symbols in the name
                    .replaceAll("[\\s\"'>]+$", "")
                    ;

            if (realname.isEmpty())
                realname = mail;
        }

        public Contact(String realname, String mail) {
            this.realname = realname;
            this.mail = mail;
        }

        public String toString() {
            return realname != null && !realname.isEmpty() ? realname : mail;
        }

        public String toKey() {
            return toString().replaceAll("[^\\w\\d<@>]+", "").toLowerCase();
        }

        public String getName() {
            return realname;
        }

        public String toIsoAddress() {
            if (!mail.equals(realname))
                return realname + " <" + mail + ">";
            else
                return mail;
        }
    }

    private Contact[] contacts;

    public ContactsResult() {
    }

    public ContactsResult(Contact... contacts) {
        this.contacts = contacts;
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public void setContacts(Contact[] contacts) {
        this.contacts = contacts;
    }

}
