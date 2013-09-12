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
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
import org.apache.hupa.shared.data.IMAPFolder;
>>>>>>> first commit
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.

public class FetchRecentMessages extends FetchMessages{

    private static final long serialVersionUID = 4380357285905033821L;

    protected FetchRecentMessages() {
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public FetchRecentMessages(IMAPFolderProxy folder, int start,
=======
    public FetchRecentMessages(IMAPFolder folder, int start,
>>>>>>> first commit
=======
    public FetchRecentMessages(IMAPFolderProxy folder, int start,
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public FetchRecentMessages(ImapFolder folder, int start,
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
            int offset, String searchString) {
        super(folder, start, offset, searchString);
    }

}
