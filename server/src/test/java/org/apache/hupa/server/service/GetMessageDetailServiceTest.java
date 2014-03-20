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

package org.apache.hupa.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.apache.hupa.server.HupaGuiceTestCase;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.MessageDetails;
import org.junit.Test;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class GetMessageDetailServiceTest extends HupaGuiceTestCase {

    @Test public void testTextDocumentToHtml() throws Exception {
        String msg = "...\nhttp://www.example.com/path/action.do;s=1;a=2?p=abcd\n...";
        String res = getMessageDetailsService.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"openLink('http://"));

        msg = "...\nnobody@subdomain.the-domain.org\n...";
        res = getMessageDetailsService.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"mailTo('nobody@"));

        res = getMessageDetailsService.txtDocumentToHtml("", "aFolder", 9999l);
        assertTrue(res.length()==0);

        msg = "...<atag>...";
        res = getMessageDetailsService.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertEquals("...&lt;atag&gt;...", res);
    }

    @Test public void testFilterHtmlDocument() throws Exception {
        String msg = "<div>...\nhttp://whatever\n...</div>";
        String res = getMessageDetailsService.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"openLink('http://whatever"));

        msg = "...\n<a\nhref=https://www.example.com/path/action.do;s=1;a=2?p=abcd\n...";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"openLink('https://"));

        msg = "...\n<a\nhref=mailTo:nobody@subdomain.the-domain.org\n...";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"mailTo('nobody@"));

        msg = "...\n...<img   \n   src=\"cid:1.1934304663@web28309.mail.ukl.yahoo.com\" width=200\n....";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertEquals("...\n...<img   \n   src='" +
                SConsts.HUPA + SConsts.SERVLET_DOWNLOAD + "?mode=inline&"
                + SConsts.PARAM_FOLDER + "=aFolder&"
                + SConsts.PARAM_UID + "=9999&"
                + SConsts.PARAM_NAME + "=1.1934304663@web28309.mail.ukl.yahoo.com' name='cid:1.1934304663@web28309.mail.ukl.yahoo.com' width=200\n....", res);

        msg = "\n\n.... <Script \ntype=\"whatever\"\n>\nalert('hello');\n</script > ---\n\n";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);

        msg = "\n\n.... <a \nid=\"whatever\"\nonclick=\"alert('hello');\"\n</a > ---\n\n";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);

        msg = "\n\n.... <style \ntype=\"whatever\"\n>\n.a{};\n</Style > ---\n\n";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);

        res = getMessageDetailsService.filterHtmlDocument("", "aFolder", 9999l);
        assertTrue(res.length()==0);
    }

    @Test public void testRegexEmailsInsideTagAttributes() {
        String msg, res;
        msg = ".. <a href=\"http://whatever?param1=whatever&email= dock@example.com&param3\">..</a> ..";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));

        msg = ".. <a href=bla > http://whatever?param1=whatever&email=dock@example.com&param3 </a> ..";
        res = getMessageDetailsService.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertFalse(res.contains("openLink("));

        // FIXME: disabled until we improve performance parsing
        // msg = ".. <div > http://whatever?param1=whatever&email=dock@example.com&param3 <p> ..";
        // res = getDetailsHandler.filterHtmlDocument(msg, "aFolder", 9999l);
        // assertFalse(res.contains("mailTo("));
        // assertTrue(res.contains("openLink("));

        msg = "http://accounts.myspace.com.deaaaf.me.uk/msp/index.php?fuseaction=update&code=78E2BL6-EKY5L893K4MHSA-74ESO-D743U41GYB18J-FA18EI698V4M&email=somehone@somewere.com";
        res = getMessageDetailsService.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertTrue(res.contains("openLink("));
    }

    @Test public void testFilterHtml_LargeBody_Performance() throws Exception {
        // messages with large bodies should be processed fast
        long start = System.currentTimeMillis();
        loadMessageDetails("8.msg");
        long end = System.currentTimeMillis();
        assertTrue("Large message bodies should be filtered fast", (end - start) < 1000);
    }

    private MessageDetails loadMessageDetails(String msgFile) throws Exception {
        return getMessageDetailsService.mimeToDetails(TestUtils.loadMessageFromFile(session,msgFile), "theFolder", 9999l, testUser);
    }

    @Test public void testMessageDetails_textPlain() throws Exception {
        MessageDetails details = loadMessageDetails("0.msg");
        assertTrue(details.getText().contains("demo message"));
    }

    @Test public void testMessageDetails_multiparMixed() throws Exception {
        MessageDetails details = loadMessageDetails("1.msg");
        assertEquals(1, details.getMessageAttachments().size());
        assertTrue(details.getText().contains("Regards"));
    }

    @Test public void testMessageDetails_multiparAlternative() throws Exception {
        MessageDetails details = loadMessageDetails("2.msg");
        assertEquals(0, details.getMessageAttachments().size());
        assertTrue(details.getText().length() > 0);
    }

    @Test public void testMessageDetails_charsetIso() throws Exception {
        MimeMessage message = TestUtils.loadMessageFromFile(session,"3.msg");
        String from = message.getFrom()[0].toString();
        assertTrue(from.contains("\u00AE"));

        MessageDetails details = loadMessageDetails("3.msg");
        assertEquals(0, details.getMessageAttachments().size());
        assertTrue(details.getText().length() > 0);
    }

    @Test public void testMessageDetails_textHtml() throws Exception {
        MessageDetails details = loadMessageDetails("4.msg");
        assertTrue(details.getText().length() > 0);
    }

    @Test public void testMessageDetails_multiparMixed_multipartAlternative() throws Exception {
        MessageDetails details = loadMessageDetails("6.msg");
        assertEquals(1, details.getMessageAttachments().size());
        assertTrue(details.getText().length() > 0);
    }

    @Test public void testMessageDetails_multiparMixed_multipartAlternative_textAttachment() throws Exception {
        MessageDetails details = loadMessageDetails("10.msg");
        assertEquals(1, details.getMessageAttachments().size());
        assertTrue(details.getText().contains("<span>"));
    }

    @Test public void testMessageDetails_html_with_inline_images() throws Exception {
        IMAPStore store = storeCache.get(testUser);

        IMAPFolder serverfolder = (IMAPFolder)store.getFolder("WHATEVER");
        serverfolder.create(Folder.HOLDS_MESSAGES);

        MimeMessage msg = TestUtils.loadMessageFromFile(session,"7.msg");
        serverfolder.addMessages(new Message[]{msg});

        org.apache.hupa.shared.domain.ImapFolder clientfolder = new org.apache.hupa.shared.data.ImapFolderImpl("WHATEVER");
        MessageDetails details = getMessageDetailsService.exposeMessage(testUser, clientfolder, 0);

        // inline images have to be downloaded from the server
        assertTrue(details.getText().contains("img src=\'" +
                SConsts.HUPA + SConsts.SERVLET_DOWNLOAD + "?mode=inline&" +
                SConsts.PARAM_FOLDER + "=WHATEVER&" +
                SConsts.PARAM_UID + "=0&" +
                SConsts.PARAM_NAME + "=1.1934304663@web28309.mail.ukl.yahoo.com'"));

        // inline images are not in the message list
        assertEquals(0, details.getMessageAttachments().size());

    }

    @Test public void testMessageDetails_links() throws Exception {
        MessageDetails details = loadMessageDetails("2.msg");

        String html = getMessageDetailsService.filterHtmlDocument(details.getText(), "foldername", 111l);
        assertFalse(html.contains("<script>"));
        assertFalse(html.contains("<style>"));
        assertTrue(html.contains("<a onClick=\"openLink('http://code.google.com/intl/es/webtoolkit/');return false;\" href=\"http://code.google.com/intl/es/webtoolkit/\""));
        assertTrue(html.contains("<a onClick=\"mailTo('donald@example.com');return false;\" href=\"mailto:donald@example.com\""));
    }

    @Test public void testMessageDetails_ecs_crypt() throws Exception {
        MessageDetails details = loadMessageDetails("12.msg");
        assertEquals("Secure Hello world<br>", details.getText());
    }

}
