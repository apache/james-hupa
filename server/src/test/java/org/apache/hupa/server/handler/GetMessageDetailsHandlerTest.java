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

import org.apache.hupa.server.mock.MockIMAPFolder;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.MessageDetails;

public class GetMessageDetailsHandlerTest extends AbstractHandlerTest {
	
	GetMessageDetailsHandler handler = new GetMessageDetailsHandler(storeCache, logger, httpSessionProvider);

	public void testRegexHtml() {
        String txt, res;
        txt = "<!'https://www.aaa.1:#@%/;$()~_?+-=\\.&<br/>";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_http, "");
        assertEquals("<!'<br/>", res);

        txt = "... a b c http://somewhere d e f ...";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_http, GetMessageDetailsHandler.repl_http);
        assertEquals("... a b c <a href=\"http://somewhere\">http://somewhere</a> d e f ...", res);
    }

    public void testRegexEmail() {
        String txt, res;
        txt = "!'BcD091_%55-+.aa@abc01-01.dd-a.aBc+";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_email, "");
        assertEquals("!'+", res);

        txt = "!'=BcD091_%55-+.aa@abc01-01.dd-a.aBc+";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_email, "");
        assertEquals(txt, res);

        txt = "... a b c aaa@aaa.bbb d e f ...";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_email, GetMessageDetailsHandler.repl_email);
        assertEquals(res, "... a b c <a href=\"mailto:aaa@aaa.bbb\">aaa@aaa.bbb</a> d e f ...");
    }

    public void testRegexInlineImg() {
        String txt, res;
        txt = ".. <img\nsrc=\"cid:abcd\"\nwhatever=/>click</a\n> ..";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_inlineImg, GetMessageDetailsHandler.repl_inlineImg);
        assertEquals(".. <img\nsrc=\"hupa/downloadAttachmentServlet?folder=%%FOLDER%%&uid=%%UID%%&name=abcd\"\nwhatever=/>click</a\n> ..", res);
    }
    
    public void testRegexBadTags() {
        String txt, res;
        txt = "<html><head>h<tag></head><body>.<style>..</style>.<script type=>//</script></body>.</html>";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_badTags, GetMessageDetailsHandler.repl_badTags);
        res = handler.replaceAll(res, GetMessageDetailsHandler.regex_unneededTags, GetMessageDetailsHandler.repl_unneededTags);
        assertEquals("...", res);
    }

    public void testRegexBadAttributes() {

        String txt, res;
        txt = "... <div attr=a onClick=\"something('');\"> ...";
        res = handler.replaceAllRecursive(txt, GetMessageDetailsHandler.regex_badAttrs, GetMessageDetailsHandler.repl_badAttrs);
        assertEquals("... <div attr=a> ...", res);

        txt = "... <div attr=a onClick=\"something('');\" attr=b onMouseOver=whatever attr=c onKeyup=\"\" /> ...";
        res = handler.replaceAllRecursive(txt, GetMessageDetailsHandler.regex_badAttrs, GetMessageDetailsHandler.repl_badAttrs);
        assertEquals("... <div attr=a attr=b attr=c /> ...", res);
    }
    
    public void testRegexHtmlLinks() {
        String txt, res;
        txt = ".. <a href=\"http://whatever\">..</a> ..";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_existingHttpLinks, GetMessageDetailsHandler.repl_existingHttpLinks);
        assertEquals(".. <a onClick=\"openLink('http://whatever');return false;\" href=\"http://whatever\">..</a> ..", res);

        txt = "-- <div> .. <img src=\"http://whatever\"/> .. </div>";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_orphandHttpLinks, GetMessageDetailsHandler.repl_orphandHttpLinks);
        assertEquals(txt, res);
        
        txt = "-- <div> .. \"http://whatever\" .. </div>";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_orphandHttpLinks, GetMessageDetailsHandler.repl_orphandHttpLinks);
        assertEquals("-- <div> .. \"<a href=\"http://whatever\">http://whatever</a>\" .. </div>", res);

        res = handler.replaceAll(res, GetMessageDetailsHandler.regex_existingHttpLinks, GetMessageDetailsHandler.repl_existingHttpLinks);
        assertEquals("-- <div> .. \"<a onClick=\"openLink('http://whatever');return false;\" href=\"http://whatever\">http://whatever</a>\" .. </div>", res);
        
    }

    public void testRegexEmailLinks() {
        String txt, res;
        
        txt = ".. <a href=\"mailTo:someone@somedomain.com\">..</a> ..";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_existingEmailLinks, GetMessageDetailsHandler.repl_existngEmailLinks);
        assertEquals(".. <a onClick=\"mailTo('someone@somedomain.com');return false;\" href=\"mailto:someone@somedomain.com\">..</a> ..", res);
        
        txt = "-- <div> .. someone@somedomain.com .. </div>";
        res = handler.replaceAll(txt, GetMessageDetailsHandler.regex_orphandEmailLinks, GetMessageDetailsHandler.repl_orphandEmailLinks);
        assertEquals("-- <div> .. <a href=\"mailto:someone@somedomain.com\">someone@somedomain.com</a> .. </div>", res);

        res = handler.replaceAll(res, GetMessageDetailsHandler.regex_existingEmailLinks, GetMessageDetailsHandler.repl_existngEmailLinks);
        assertEquals("-- <div> .. <a onClick=\"mailTo('someone@somedomain.com');return false;\" href=\"mailto:someone@somedomain.com\">someone@somedomain.com</a> .. </div>", res);
        
    }

    public void testHtmlIsWrapped() throws Exception {
        
        String html = handler.filterHtmlDocument("whatever", "foldername", 111l);
        assertEquals("<div class='hupa-email-content'>\nwhatever\n</div>\n", html);
        
        html = handler.txtDocumentToHtml("whatever", "foldername", 111l);
        assertEquals("<div class='hupa-email-content'>\nwhatever\n</div>\n", html);
    }
    
	public void testTextDocumentToHtml() throws Exception {

		String msg = "...\nhttp://www.example.com/path/action.do;s=1;a=2?p=abcd\n...";
    	String res = handler.txtDocumentToHtml(msg, "aFolder", 9999l);
    	assertNotSame(msg, res);
    	assertTrue(res.contains("onClick=\"openLink('http://"));
    	
    	msg = "...\nnobody@subdomain.the-domain.org\n...";
    	res = handler.txtDocumentToHtml(msg, "aFolder", 9999l);
    	assertNotSame(msg, res);
    	assertTrue(res.contains("onClick=\"mailTo('nobody@"));
    	
    	res = handler.txtDocumentToHtml("", "aFolder", 9999l);
    	assertTrue(res.length()==0);
    	
    }

	public void testFilterHtmlDocument() throws Exception {

		String msg = "<div>...\nhttp://whatever\n...</div>";
    	String res = handler.txtDocumentToHtml(msg, "aFolder", 9999l);
    	assertNotSame(msg, res);
    	assertTrue(res.contains("onClick=\"openLink('http://whatever"));
		
		msg = "...\n<a\nhref=https://www.example.com/path/action.do;s=1;a=2?p=abcd\n...";
		res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
		assertNotSame(msg, res);
		assertTrue(res.contains("onClick=\"openLink('https://"));

		msg = "...\n<a\nhref=mailTo:nobody@subdomain.the-domain.org\n...";
		res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
		assertNotSame(msg, res);
		assertTrue(res.contains("onClick=\"mailTo('nobody@"));

		msg = "...\n...<img   \n   src=\"cid:1.1934304663@web28309.mail.ukl.yahoo.com\" width=200\n....";
		res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
		assertNotSame(msg, res);
		assertEquals("<div class='hupa-email-content'>\n...\n...<img   \n   src=\"" + 
				SConsts.HUPA + SConsts.SERVLET_DOWNLOAD + "?" 
				+ SConsts.PARAM_FOLDER + "=aFolder&" 
				+ SConsts.PARAM_UID + "=9999&"
		        + SConsts.PARAM_NAME + "=1.1934304663@web28309.mail.ukl.yahoo.com\" width=200\n....\n</div>\n", res);
		
		msg = "\n\n.... <Script \ntype=\"whatever\"\n>\nalert('hello');\n</script > ---\n\n";
		res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
		assertNotSame(msg, res);

		msg = "\n\n.... <a \nid=\"whatever\"\nonclick=\"alert('hello');\"\n</a > ---\n\n";
		res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
		assertNotSame(msg, res);

		msg = "\n\n.... <style \ntype=\"whatever\"\n>\n.a{};\n</Style > ---\n\n";
		res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
		assertNotSame(msg, res);
		
        res = handler.filterHtmlDocument("", "aFolder", 9999l);
	    assertTrue(res.length()==0);

	}

	public void testRegexEmailsInsideTagAttributes() {
        String msg, res;
        msg = ".. <a href=\"http://whatever?param1=whatever&email= dock@example.com&param3\">..</a> ..";
        res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));

        msg = ".. <a href=bla > http://whatever?param1=whatever&email=dock@example.com&param3 </a> ..";
        res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertFalse(res.contains("openLink("));

        msg = ".. <div > http://whatever?param1=whatever&email=dock@example.com&param3 <p> ..";
        res = handler.filterHtmlDocument(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertTrue(res.contains("openLink("));
        
        msg = "http://accounts.myspace.com.deaaaf.me.uk/msp/index.php?fuseaction=update&code=78E2BL6-EKY5L893K4MHSA-74ESO-D743U41GYB18J-FA18EI698V4M&email=somehone@somewere.com";
        res = handler.txtDocumentToHtml(msg, "aFolder", 9999l);
        assertFalse(res.contains("mailTo("));
        assertTrue(res.contains("openLink("));
        
    }

	private MessageDetails loadMessageDetails(String msgFile) throws Exception {
        return handler.mimeToDetails(loadMessage(msgFile), "theFolder", 9999l);
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
    	MimeMessage message = loadMessage("3.msg");
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
    	
    	MockIMAPStore store = (MockIMAPStore) storeCache.get(user);
        MockIMAPFolder serverfolder = (MockIMAPFolder)store.getFolder("WHATEVER"); 
        serverfolder.create(Folder.HOLDS_MESSAGES);
        
    	MimeMessage msg = loadMessage("7.msg");
    	serverfolder.addMessages(new Message[]{msg});

        IMAPFolder clientfolder = new IMAPFolder("WHATEVER");
        MessageDetails details = handler.exposeMessage(user, clientfolder, 0);
        assertTrue(details.getText().contains("img src=\"" + 
        		SConsts.HUPA + SConsts.SERVLET_DOWNLOAD + "?" +
        		SConsts.PARAM_FOLDER + "=WHATEVER&" + 
        		SConsts.PARAM_UID + "=0&" + 
        		SConsts.PARAM_NAME + "=1.1934304663@web28309.mail.ukl.yahoo.com\""));
    	
    }

    public void testMessageDetails_links() throws Exception {
    	MessageDetails details = loadMessageDetails("2.msg");

    	String html = handler.filterHtmlDocument(details.getText(), "foldername", 111l);
    	assertFalse(html.contains("<script>"));
    	assertFalse(html.contains("<style>"));
    	assertTrue(html.contains("<a onClick=\"openLink('http://code.google.com/intl/es/webtoolkit/');return false;\" href=\"http://code.google.com/intl/es/webtoolkit/\""));
    	assertTrue(html.contains("<a onClick=\"mailTo('donald@example.com');return false;\" href=\"mailto:donald@example.com\""));
    }

}
