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


import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.shared.SConsts;

/**
 * Utility methods for manage user's session objects
 */
public class SessionUtils {

    public static FileItemRegistry getSessionRegistry(Log logger, HttpSession session) {
        FileItemRegistry registry = (FileItemRegistry)session.getAttribute(SConsts.REG_SESS_ATTR);
        if (registry == null) {
            registry = new FileItemRegistry(logger);
            session.setAttribute(SConsts.REG_SESS_ATTR, registry);
        }
        return registry;
    }

    /**
     * Remove session attributes, it has to be done in the login and logout actions
     * @param session
     */
    public static void cleanSessionAttributes(HttpSession session) {
        if (session != null) {
            @SuppressWarnings("rawtypes")
            Enumeration en = session.getAttributeNames();
            ArrayList<String> toRemove = new ArrayList<String>();
            while (en.hasMoreElements()) {
                String s = en.nextElement().toString();
                if (s.startsWith("hupa")) {
                    toRemove.add(s);
                }
            }
            for (String attr: toRemove) {
                session.removeAttribute(attr);
            }
        }
    }

}