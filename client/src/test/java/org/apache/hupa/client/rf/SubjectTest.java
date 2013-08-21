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
package org.apache.hupa.client.rf;

import org.apache.hupa.client.HupaMvpTestCase;

import com.google.web.bindery.requestfactory.shared.Receiver;

public class SubjectTest extends HupaMvpTestCase {

    public void testEcho() throws Exception {
        HupaRequestFactory rf = injector.getInstance(HupaRequestFactory.class);
        
        SubjectRequest req = rf.subjectRequest(); 
        SubjectProxy t = req.create(SubjectProxy.class);
        t.setTitle("New-Subject");
        req.echo(t, "from_manolo", "to_james").fire(new Receiver<String>() {
            public void onSuccess(String response) {
                assertTrue(response.contains("from_manolo"));
                assertTrue(response.contains("to_james"));
            }
        });
        
        req = rf.subjectRequest(); 
        t = req.create(SubjectProxy.class);
        t.setTitle("New-Subject");
        req.persist().using(t);
        req.countSubjects().to(new Receiver<Long>() {
            public void onSuccess(Long response) {
                assertEquals(1l, response.longValue());
            }
        }).fire();
    }
}

