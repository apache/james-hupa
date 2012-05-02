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

import java.util.ArrayList;
import java.util.Enumeration;

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

    /**
     * Remove session attributes, it has to be done in the login and logout actions
     * @param session
     */
    public static void cleanSessionAttributes(HttpSession session) {
        @SuppressWarnings("rawtypes")
        Enumeration en = session.getAttributeNames();
        ArrayList<String> toRemove = new ArrayList<String>();
        while (en.hasMoreElements()) {
            toRemove.add(en.nextElement().toString());
        }
        for (String attr: toRemove) {
            session.removeAttribute(attr);
        }
    }
    
}