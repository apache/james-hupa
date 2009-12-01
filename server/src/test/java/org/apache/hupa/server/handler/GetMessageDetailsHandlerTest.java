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

package org.apache.hupa.server.handler;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.apache.hupa.server.HupaTestCase;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.DemoModeConstants;
import org.apache.hupa.server.mock.MockIMAPStoreCache;
import org.apache.hupa.server.utils.TestUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class GetMessageDetailsHandlerTest extends HupaTestCase {
    
    public void testTextDocumentToHtml() throws Exception {

        String msg = "...\nhttp://www.example.com/path/action.do;s=1;a=2?p=abcd\n...";
        String res = getDetailsMsgHndl.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"openLink('http://"));
        
        msg = "...\nnobody@subdomain.the-domain.org\n...";
        res = getDetailsMsgHndl.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"mailTo('nobody@"));
        
        res = getDetailsMsgHndl.txtDocumentToHtml("", "aFolder", 9999l);
        assertTrue(res.length()==0);
        
    }

    public void testFilterHtmlDocument() throws Exception {

        String msg = "<div>...\nhttp://whatever\n...</div>";
        String res = getDetailsMsgHndl.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"openLink('http://whatever"));
        
        msg = "...\n<a\nhref=https://www.example.com/path/action.do;s=1;a=2?p=abcd\n...";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"openLink('https://"));

        msg = "...\n<a\nhref=mailTo:nobody@subdomain.the-domain.org\n...";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertTrue(res.contains("onClick=\"mailTo('nobody@"));

        msg = "...\n...<img   \n   src=\"cid:1.1934304663@web28309.mail.ukl.yahoo.com\" width=200\n....";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        assertEquals("...\n...<img   \n   src='" + 
                SConsts.HUPA + SConsts.SERVLET_DOWNLOAD + "?" 
                + SConsts.PARAM_FOLDER + "=aFolder&" 
                + SConsts.PARAM_UID + "=9999&"
                + SConsts.PARAM_NAME + "=1.1934304663@web28309.mail.ukl.yahoo.com' name='cid:1.1934304663@web28309.mail.ukl.yahoo.com' width=200\n....", res);
        
        msg = "\n\n.... <Script \ntype=\"whatever\"\n>\nalert('hello');\n</script > ---\n\n";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);

        msg = "\n\n.... <a \nid=\"whatever\"\nonclick=\"alert('hello');\"\n</a > ---\n\n";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);

        msg = "\n\n.... <style \ntype=\"whatever\"\n>\n.a{};\n</Style > ---\n\n";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertNotSame(msg, res);
        
        res = getDetailsMsgHndl.filterHtmlDocument("", "aFolder", 9999l);
        assertTrue(res.length()==0);

    }

    public void testRegexEmailsInsideTagAttributes() {
        String msg, res;
        msg = ".. <a href=\"http://whatever?param1=whatever&email= dock@example.com&param3\">..</a> ..";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));

        msg = ".. <a href=bla > http://whatever?param1=whatever&email=dock@example.com&param3 </a> ..";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertFalse(res.contains("openLink("));

        msg = ".. <div > http://whatever?param1=whatever&email=dock@example.com&param3 <p> ..";
        res = getDetailsMsgHndl.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertTrue(res.contains("openLink("));
        
        msg = "http://accounts.myspace.com.deaaaf.me.uk/msp/index.php?fuseaction=update&code=78E2BL6-EKY5L893K4MHSA-74ESO-D743U41GYB18J-FA18EI698V4M&email=somehone@somewere.com";
        res = getDetailsMsgHndl.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertTrue(res.contains("openLink("));
        
    }

    private MessageDetails loadMessageDetails(String msgFile) throws Exception {
        return getDetailsMsgHndl.mimeToDetails(TestUtils.loadMessageFromFile(session,msgFile), "theFolder", 9999l);
    }
    
    public void testMessageDetails_textPlain() throws Exception {
        MessageDetails details = loadMessageDetails("0.msg");
        assertTrue(details.getText().contains("demo message"));
    }

    public void testMessageDetails_multiparMixed() throws Exception {
        MessageDetails details = loadMessageDetails("1.msg");
        assertEquals(1, details.getMessageAttachments().size());
        assertTrue(details.getText().contains("Regards"));
    }

    public void testMessageDetails_multiparAlternative() throws Exception {
        MessageDetails details = loadMessageDetails("2.msg");
        assertEquals(0, details.getMessageAttachments().size());
        assertTrue(details.getText().length() > 0);
    }

    public void testMessageDetails_charsetIso() throws Exception {
        MimeMessage message = TestUtils.loadMessageFromFile(session,"3.msg");
        String from = message.getFrom()[0].toString();
        assertTrue(from.contains("\u00AE"));
        
        MessageDetails details = loadMessageDetails("3.msg");
        assertEquals(0, details.getMessageAttachments().size());
        assertTrue(details.getText().length() > 0);
    }

    public void testMessageDetails_textHtml() throws Exception {
        MessageDetails details = loadMessageDetails("4.msg");
        assertTrue(details.getText().length() > 0);
    }
    
    public void testMessageDetails_multiparMixed_multipartAlternative() throws Exception {
        MessageDetails details = loadMessageDetails("6.msg");
        assertEquals(1, details.getMessageAttachments().size());
        assertTrue(details.getText().length() > 0);
    }
    
    public void testMessageDetails_html_with_inline_images() throws Exception {

        IMAPStoreCache storeCache = injector.getInstance(IMAPStoreCache.class);
        IMAPStore store = injector.getInstance(IMAPStore.class);

        User demouser = DemoModeConstants.demoUser;
        ((MockIMAPStoreCache)storeCache).addValidUser(demouser, store);
        
        IMAPFolder serverfolder = (IMAPFolder)store.getFolder("WHATEVER"); 
        serverfolder.create(Folder.HOLDS_MESSAGES);
        
        MimeMessage msg = TestUtils.loadMessageFromFile(session,"7.msg");
        serverfolder.addMessages(new Message[]{msg});
        
        org.apache.hupa.shared.data.IMAPFolder clientfolder = new org.apache.hupa.shared.data.IMAPFolder("WHATEVER");
        MessageDetails details = getDetailsMsgHndl.exposeMessage(demouser, clientfolder, 0);
        
        // inline images have to be downloaded from the server
        assertTrue(details.getText().contains("img src=\'" + 
                SConsts.HUPA + SConsts.SERVLET_DOWNLOAD + "?" +
                SConsts.PARAM_FOLDER + "=WHATEVER&" + 
                SConsts.PARAM_UID + "=0&" + 
                SConsts.PARAM_NAME + "=1.1934304663@web28309.mail.ukl.yahoo.com'"));
        
        // inline images are not in the message list
        assertEquals(0, details.getMessageAttachments().size());
        
    }

    public void testMessageDetails_links() throws Exception {
        MessageDetails details = loadMessageDetails("2.msg");

        String html = getDetailsMsgHndl.filterHtmlDocument(details.getText(), "foldername", 111l);
        assertFalse(html.contains("<script>"));
        assertFalse(html.contains("<style>"));
        assertTrue(html.contains("<a onClick=\"openLink('http://code.google.com/intl/es/webtoolkit/');return false;\" href=\"http://code.google.com/intl/es/webtoolkit/\""));
        assertTrue(html.contains("<a onClick=\"mailTo('donald@example.com');return false;\" href=\"mailto:donald@example.com\""));
    }

}
