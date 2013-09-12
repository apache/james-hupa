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


package org.apache.hupa.shared.rpc;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.data.SMTPMessage;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.SMTPMessage;
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.
=======
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.SmtpMessage;
>>>>>>> forward and reply message to use RF

public class ForwardMessage extends SendMessage {

    private static final long serialVersionUID = 1656671247843122192L;
    private long uid;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private IMAPFolderProxy folder;

    public ForwardMessage(SMTPMessage msg, IMAPFolderProxy folder, long uid) {
=======
    private IMAPFolder folder;

    public ForwardMessage(SMTPMessage msg, IMAPFolder folder, long uid) {
>>>>>>> first commit
=======
    private IMAPFolderProxy folder;

    public ForwardMessage(SMTPMessage msg, IMAPFolderProxy folder, long uid) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private ImapFolder folder;
    private String inReplyTo;
    private String references;

<<<<<<< HEAD
    public ForwardMessage(SMTPMessage msg, ImapFolder folder, long uid) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
    public ForwardMessage(SmtpMessage msg, ImapFolder folder, long uid) {
>>>>>>> forward and reply message to use RF
        super(msg);
        this.uid = uid;
        this.folder = folder;
    }
    
    protected ForwardMessage() {
    }

    public long getReplyMessageUid() {
        return uid;
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public IMAPFolderProxy getFolder() {
=======
    public IMAPFolder getFolder() {
>>>>>>> first commit
=======
    public IMAPFolderProxy getFolder() {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public ImapFolder getFolder() {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        return folder;
    }
    
    @Override
    public String getInReplyTo() {
		return inReplyTo;
	}

    @Override
    public String getReferences() {
		return references;
	}

  public ForwardMessage setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo;
		return this;
	}
   
  public ForwardMessage setReferences(String references) {
		this.references = references;
		return this;
	}
}
