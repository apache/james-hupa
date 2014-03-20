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

import junit.framework.TestCase;

public class RegexPatternsTest extends TestCase {

    public void testRegexHtml() {
        String txt, res;
        txt = "<!'https://www.aaa.1:#@%/;$()~_?+-=\\.&<br/>";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_htmllink, "");
        assertEquals("<!'<br/>", res);

        txt = "... a b c http://somewhere d e f ...";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_htmllink, RegexPatterns.repl_htmllink);
        assertEquals("... a b c <a href=\"http://somewhere\">http://somewhere</a> d e f ...", res);
    }

    public void testRegexEmail() {
        String txt, res;
        txt = "!'BcD091_%55-+.aa@abc01-01.dd-a.aBc+";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_email, "");
        assertEquals("!'+", res);

        txt = "!'=BcD091_%55-+.aa@abc01-01.dd-a.aBc+";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_email, "");
        assertEquals(txt, res);

        txt = "... a b c aaa@aaa.bbb d e f ...";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_email, RegexPatterns.repl_email);
        assertEquals(res, "... a b c <a href=\"mailto:aaa@aaa.bbb\">aaa@aaa.bbb</a> d e f ...");
    }

    public void testRegexInlineImg() {
        String txt, res;
        txt = ".. <img\nsrc=\"cid:abcd\"\nwhatever=/>click</a\n> ..";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_inlineImg, RegexPatterns.repl_inlineImg);
        assertEquals(".. <img\nsrc='hupa/downloadAttachmentServlet?mode=inline&folder=%%FOLDER%%&uid=%%UID%%&name=abcd' name='cid:abcd'\nwhatever=/>click</a\n> ..", res);
    }

    public void testRegexRestoreInlineLinks() {
        String txt, res;
        txt = ".. <img\nsrc='cid:abcd'\nwhatever=/>click</a\n> ..";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_inlineImg, RegexPatterns.repl_inlineImg);
        assertNotSame(txt, res);

        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_revertInlineImg, RegexPatterns.repl_revertInlineImg);
        assertEquals(txt, res);
    }


    public void testRegexBadTags() {
        String txt, res;
        txt = "<html><head>h<tag></head><body>.<style>..</style>.<script type=>//</script></body>.</html>";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_badTags, RegexPatterns.repl_badTags);
        res = RegexPatterns.replaceAll(res, RegexPatterns.regex_unneededTags, RegexPatterns.repl_unneededTags);
        assertEquals("...", res);
    }

    public void testRegexBadAttributes() {

        String txt, res;
        txt = "... <div attr=a onClick=\"something('');\"> ...";
        res = RegexPatterns.replaceAllRecursive(txt, RegexPatterns.regex_badAttrs, RegexPatterns.repl_badAttrs);
        assertEquals("... <div attr=a> ...", res);

        txt = "... <div attr=a onClick=\"something('');\" attr=b onMouseOver=whatever attr=c onKeyup=\"\" /> ...";
        res = RegexPatterns.replaceAllRecursive(txt, RegexPatterns.regex_badAttrs, RegexPatterns.repl_badAttrs);
        assertEquals("... <div attr=a attr=b attr=c /> ...", res);
    }

    public void testRegexHtmlLinks() {
        String txt, res;
        txt = ".. <a href=\"http://whatever\">..</a> ..";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_existingHttpLinks, RegexPatterns.repl_existingHttpLinks);
        assertEquals(".. <a onClick=\"openLink('http://whatever');return false;\" href=\"http://whatever\">..</a> ..", res);

        txt = "-- <div> .. <img src=\"http://whatever\"/> .. </div>";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_orphandHttpLinks, RegexPatterns.repl_orphandHttpLinks);
        assertEquals(txt, res);

        txt = "-- <div> .. \"http://whatever\" .. </div>";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_orphandHttpLinks, RegexPatterns.repl_orphandHttpLinks);
        assertEquals("-- <div> .. \"<a href=\"http://whatever\">http://whatever</a>\" .. </div>", res);

        res = RegexPatterns.replaceAll(res, RegexPatterns.regex_existingHttpLinks, RegexPatterns.repl_existingHttpLinks);
        assertEquals("-- <div> .. \"<a onClick=\"openLink('http://whatever');return false;\" href=\"http://whatever\">http://whatever</a>\" .. </div>", res);

    }

    public void testRegexEmailLinks() {
        String txt, res;

        txt = ".. <a href=\"mailTo:someone@somedomain.com\">..</a> ..";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_existingEmailLinks, RegexPatterns.repl_existngEmailLinks);
        assertEquals(".. <a onClick=\"mailTo('someone@somedomain.com');return false;\" href=\"mailto:someone@somedomain.com\">..</a> ..", res);

        txt = "-- <div> .. someone@somedomain.com .. </div>";
        res = RegexPatterns.replaceAll(txt, RegexPatterns.regex_orphandEmailLinks, RegexPatterns.repl_orphandEmailLinks);
        assertEquals("-- <div> .. <a href=\"mailto:someone@somedomain.com\">someone@somedomain.com</a> .. </div>", res);

        res = RegexPatterns.replaceAll(res, RegexPatterns.regex_existingEmailLinks, RegexPatterns.repl_existngEmailLinks);
        assertEquals("-- <div> .. <a onClick=\"mailTo('someone@somedomain.com');return false;\" href=\"mailto:someone@somedomain.com\">someone@somedomain.com</a> .. </div>", res);

    }

}