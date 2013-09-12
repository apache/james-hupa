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

public class ReplyMessage extends ForwardMessage {

    private static final long serialVersionUID = -383135476236902779L;

    
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public ReplyMessage(SMTPMessage msg, IMAPFolderProxy folder, long uid) {
=======
    public ReplyMessage(SMTPMessage msg, IMAPFolder folder, long uid) {
>>>>>>> first commit
=======
    public ReplyMessage(SMTPMessage msg, IMAPFolderProxy folder, long uid) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public ReplyMessage(SMTPMessage msg, ImapFolder folder, long uid) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        super(msg, folder, uid);
    }

    protected ReplyMessage() {
    }
    
}
