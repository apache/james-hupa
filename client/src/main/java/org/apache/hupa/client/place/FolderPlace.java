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

public class FolderPlace extends HupaPlace {

    private String token;

    public FolderPlace(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Prefix("folder")
    public static class Tokenizer implements PlaceTokenizer<FolderPlace> {

        @Override
        public FolderPlace getPlace(String token) {
            return new FolderPlace(token);
        }

        @Override
        public String getToken(FolderPlace place) {
            return place.getToken();
        }
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null)
//            return false;
//        if (o == this)
//            return true;
//        if (o.getClass() != getClass())
//            return false;
//        FolderPlace place = (FolderPlace) o;
//        return (token == place.token || (token != null && token.equals(place.token)));
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((token == null) ? 0 : token.hashCode());
//        return result;
//    }
}
