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

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * login page
 */
public class DefaultPlace extends AbstractPlace {

    String token;

    public DefaultPlace(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Prefix("")
    public static class Tokenizer implements PlaceTokenizer<DefaultPlace> {

        @Override
        public DefaultPlace getPlace(String token) {
            return new DefaultPlace(token);
        }

        @Override
        public String getToken(DefaultPlace place) {
            return place.getToken();
        }
    }

}
