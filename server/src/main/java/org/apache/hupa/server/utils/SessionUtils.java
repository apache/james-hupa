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

package org.apache.hupa.server.utils;


import org.apache.commons.logging.Log;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.shared.rpc.ContactsResult.Contact;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

/**
 * Utility methods for manage user's session objects
 */
public class SessionUtils {

    public static FileItemRegistry getSessionRegistry(Log logger, HttpSession session) {
        FileItemRegistry registry = (FileItemRegistry)session.getAttribute("registry");
        if (registry == null) {
            registry = new FileItemRegistry(logger);
            session.setAttribute("registry", registry);
        }
        return registry;
    }
   
    public static void addContact(HttpSession session, String mail) {
        String name = !mail.contains("<") ? "" : mail.replaceAll("<.+$", "")
                                                 .replaceAll("^[\\s\"']+","")
                                                 .replaceAll("[\\s\"']+$", "");
        String email = mail.replaceAll("^.*<([^>]+)>","$1");
        Contact contact = new Contact(name, email);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Contact> sessionContacts = (HashMap<String, Contact>)session.getAttribute("contacts");
        if (sessionContacts==null)
            sessionContacts=new HashMap<String, Contact>();
        
        if (!sessionContacts.containsKey(contact.toString())) {
            sessionContacts.put(contact.toString(), contact);
            session.setAttribute("contacts", sessionContacts);
        }
    }

    public static Contact[] getContacts(HttpSession session) {
        @SuppressWarnings("unchecked")
        HashMap<String, Contact> sessionContacts = (HashMap<String, Contact>)session.getAttribute("contacts");
        return sessionContacts == null ? new Contact[]{} : sessionContacts.values().toArray(new Contact[sessionContacts.size()]);
    }
    
}