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

package org.apache.hupa.client.place;

import org.apache.hupa.client.ui.ToolBarView.Parameters;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ComposePlace extends AbstractPlace {

    private String token;
    private Parameters parameters;
    private String subject;
    private String body;

    public ComposePlace(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Prefix("compose")
    public static class Tokenizer implements PlaceTokenizer<ComposePlace> {

        @Override
        public ComposePlace getPlace(String token) {
            return new ComposePlace(token);
        }

        @Override
        public String getToken(ComposePlace place) {
            return place.getToken();
        }
    }

    public Place with(Parameters parameters) {
        this.parameters = parameters;
        return this;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public ComposePlace with(String subject, String body) {
        this.parameters = new Parameters(null, null, null, null);
        this.subject = subject;
        this.body = body;
        return this;
    }

    public String getSubject(){
        return subject;
    }

    public String getBody(){
        return body;
    }

}
