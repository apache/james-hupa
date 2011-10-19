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
package org.apache.hupa.server.rf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Subject {

    public static HashMap<Long, Subject> subjects = new HashMap<Long, Subject>();

    private static long cont = 0;
    private Integer version = 0;
    private Long id = cont++;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void flush() {
    }

    public void persist() {
        version++;
        subjects.put(getId(), this);
    }

    public void remove() {
        subjects.remove(getId());
    }

    public static List<Subject> findAllSubjects() {
        return new ArrayList<Subject>(subjects.values());
    }

    public static long countSubjects() {
        return subjects.size();
    }

    public static Subject findSubject(Long id) {
        return subjects.get(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String echo(Subject subject, String from, String to) {
        String msg = "In server side: " + subject.getTitle() + ",from: " + from
                + " To: " + to;
        return msg;
    }
}
