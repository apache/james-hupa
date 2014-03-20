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
package org.apache.hupa.server.mock;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.google.inject.Inject;

@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession{
    private Map<String,Object> attributeMap = new HashMap<String, Object>();
    private Map<String,Object> valueMap = new HashMap<String, Object>();
    private long cTime;
    private String id;
    private static int seq = 0;

    @Inject
    public MockHttpSession() {
        cTime = System.currentTimeMillis();
        this.id = MockConstants.SESSION_ID + "-" + seq++;
    }

    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }


    @SuppressWarnings("rawtypes")
    public Enumeration getAttributeNames() {
        return new Enumeration() {
            Iterator it = attributeMap.keySet().iterator();
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            public Object nextElement() {
                return it.next();
            }

        };
    }

    public long getCreationTime() {
        return cTime;
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return 0;
    }

    public int getMaxInactiveInterval() {
        return 0;
    }

    public ServletContext getServletContext() {
        return null;
    }

    public HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getValue(String name) {
        return valueMap.get(name);
    }

    public String[] getValueNames() {
        List<String> names = new ArrayList<String>();
        Iterator<String> it = valueMap.keySet().iterator();
        while (it.hasNext()) {
            names.add(it.next());
        }
        return names.toArray(new String[names.size()]);
    }

    public void invalidate() {
    }

    public boolean isNew() {
        return false;
    }

    public void putValue(String name, Object value) {
        valueMap.put(name, value);
    }

    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }

    public void removeValue(String name) {
        valueMap.remove(name);

    }

    public void setAttribute(String name, Object value) {
        attributeMap.put(name, value);
    }

    public void setMaxInactiveInterval(int interval) {
        // TODO Auto-generated method stub

    }

}
