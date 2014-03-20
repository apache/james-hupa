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

import java.util.regex.Pattern;

import org.apache.hupa.shared.SConsts;

/**
 * A place where we centralize all compiled regular expressions which will be used in
 * server side for html and text transformations.
 */
public class RegexPatterns {

    public static final Pattern regex_lt = Pattern.compile("<");
    public static final String repl_lt = "&lt;";

    public static final Pattern regex_gt = Pattern.compile(">");
    public static final String repl_gt = "&gt;";

    public static final Pattern regex_nl = Pattern.compile("\n");
    public static final String repl_nl = "<br/>";

    public static final String HTML_LINK_REGEXP =  "\\b?(https?://[\\w\\d:#@%/;$\\(\\)~_\\?\\+\\-=\\\\.&]*)\\b?";
    public static final Pattern regex_htmllink = Pattern.compile(HTML_LINK_REGEXP);
    public static final String repl_htmllink = "<a href=\"$1\">$1</a>";

    public static final String EMAIL_REGEXP =  "\\b(?<![A-z0-9._%\\+\\-=])([A-z][A-z0-9._%\\+\\-]+@[A-z0-9\\.\\-]+\\.[A-z]{2,4})";
    public static final Pattern regex_email = Pattern.compile("\\b"+ EMAIL_REGEXP);
    public static final String repl_email = "<a href=\"mailto:$1\">$1</a>";

    public static final Pattern regex_inlineImg = Pattern.compile("(?si)(<\\s*img\\s+.*?src=)[\"']?cid:([^\"']+)[\"']?");
    public static final String repl_inlineImg = "$1'" + SConsts.HUPA + SConsts.SERVLET_DOWNLOAD
                                        + "?" + SConsts.PARAM_MODE + "=inline"
                                        + "&" + SConsts.PARAM_FOLDER + "=%%FOLDER%%"
                                        + "&" + SConsts.PARAM_UID + "=%%UID%%"
                                        + "&" + SConsts.PARAM_NAME + "=$2' name='cid:$2'";

    public static final Pattern regex_revertInlineImg = Pattern.compile("(?si)(<img\\s[^>]*src=)[^>]+name=([\"']cid:[^\"']+[\"'])");
    public static final String repl_revertInlineImg = "$1$2";

    public static final Pattern regex_badTags = Pattern.compile("(?si)<(script|style|head).*?</\\1\\s*>");
    public static final String repl_badTags = "";

    public static final Pattern regex_unneededTags = Pattern.compile("(?si)(</?(html|body)[^>]*?>)");
    public static final String repl_unneededTags = "";

    public static final String EVENT_ATTR_REGEX = "(?:on[dbl]*click)|(?:onmouse[a-z]+)|(?:onkey[a-z]+)";
    public static final Pattern regex_badAttrs = Pattern.compile("(?si)(<\\w+[^<>]*)\\s+("+ EVENT_ATTR_REGEX + ")=[\"']?([^\\s<>]+?)[\"']?([\\s>])");
    public static final String repl_badAttrs = "$1$4";

    public static final Pattern regex_orphandHttpLinks = Pattern.compile("(?si)(?!.*<a\\s?[^>]*?>.+</a\\s*>.*)(<[^<]*?>[^<>]*)" + HTML_LINK_REGEXP + "([^<>]*<[^>]*?>)");
    public static final String repl_orphandHttpLinks = "$1<a href=\"$2\">$2</a>$3";

    public static final Pattern regex_existingHttpLinks = Pattern.compile("(?si)<a\\s[^>]*?href=[\"']?" + HTML_LINK_REGEXP + "[\"']?");
    public static final String repl_existingHttpLinks = "<a onClick=\"openLink('$1');return false;\" href=\"$1\"";

    public static final Pattern regex_orphandEmailLinks = Pattern.compile("(?si)(?!.*<a\\s?[^>]*?>.+</a\\s*>.*)(<[^<]*?>[^<>]*)" + EMAIL_REGEXP + "([^<>]*<[^>]*?>)");
    public static final String repl_orphandEmailLinks = "$1<a href=\"mailto:$2\">$2</a>$3";

    public static final Pattern regex_existingEmailLinks = Pattern.compile("(?si)<a\\s[^>]*?href=[\"']*mailto:[\"']?([^\"]+)[\"']?");
    public static final String repl_existngEmailLinks = "<a onClick=\"mailTo('$1');return false;\" href=\"mailto:$1\"";

    public static String replaceAll(String txt, Pattern pattern, String replacement) {
        return pattern.matcher(txt).replaceAll(replacement);
    }

    public static String replaceAllRecursive(String txt, Pattern pattern, String replacement) {
        while (pattern.matcher(txt).find())
            txt = pattern.matcher(txt).replaceAll(replacement);
        return txt;
    }

    public static final Pattern regex_nl_tags = Pattern.compile("(?si)(<br\\s*?/?>)|(</div\\s*?>)");
    public static final Pattern regex_any_tag = Pattern.compile("(\\w)<.*?>(\\w)");
    public static final String repl_any_tag = "$1 $2";

//    s=s.replaceAll("\n", " ");
//    s=s.replaceAll("(?si)<br\\s*?/?>", "\n");
//    s=s.replaceAll("(?si)</div\\s*?>", "\n");
//    s=s.replaceAll("(\\w)<.*?>(\\w)", "$1 $2");
//    s=s.replaceAll("<.*?>", "");
//    s=s.replaceAll("[ \t]+", " ");


}
