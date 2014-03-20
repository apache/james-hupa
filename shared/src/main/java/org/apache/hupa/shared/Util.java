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

package org.apache.hupa.shared;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public final static String HTML_LT = "&lt;";
    public final static String STRING_LT = "<";
    public final static String HTML_GT = "&gt;";
    public final static String STRING_GT = ">";
    public final static String HTML_CR = "<br>";
    public final static String STRING_CR = "\n";

    public static String unEscapeHtmlTags(String string) {
        if (string != null) {
            string = string.replaceAll(HTML_LT, STRING_LT);
            string = string.replaceAll(HTML_GT, STRING_GT);
            string = string.replaceAll(HTML_CR, STRING_CR);
        }
        return string;
    }

    public static String escapeHtmlTags(String string) {
        if (string != null) {
            string = string.replaceAll(STRING_LT, HTML_LT);
            string = string.replaceAll(STRING_GT, HTML_GT);
        }
        return string;
    }

    public static <T> String listToString(List<T> list) {
        return list == null ? "" : new ArrayList<T>(list).toString().replaceFirst("^\\[(.*)\\]$", "$1");
    }
}
