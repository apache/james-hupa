
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

package org.apache.hupa.shared.events;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
=======
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
>>>>>>> first commit

import com.google.gwt.event.shared.GwtEvent;

public class LoadMessagesEvent extends GwtEvent<LoadMessagesEventHandler>{
    public final static Type<LoadMessagesEventHandler> TYPE = new Type<LoadMessagesEventHandler>();
    private User user;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private ImapFolder folder;
    private String searchValue;
    
    public LoadMessagesEvent(User user, ImapFolder folder) {
        this(user,folder,null);
    }
    
    public LoadMessagesEvent(User user, ImapFolder folder, String searchValue) {
=======
    private IMAPFolder folder;
=======
    private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
    private String searchValue;
    
    public LoadMessagesEvent(User user, IMAPFolderProxy folder) {
        this(user,folder,null);
    }
    
<<<<<<< HEAD
    public LoadMessagesEvent(User user, IMAPFolder folder, String searchValue) {
>>>>>>> first commit
=======
    public LoadMessagesEvent(User user, IMAPFolderProxy folder, String searchValue) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private IMAPFolder folder;
    private String searchValue;
    
    public LoadMessagesEvent(User user, IMAPFolder folder) {
        this(user,folder,null);
    }
    
    public LoadMessagesEvent(User user, IMAPFolder folder, String searchValue) {
>>>>>>> first commit
        this.user = user;
        this.folder = folder;
        this.searchValue = searchValue;
    }
    public User getUser() {
        return user;
    }
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public ImapFolder getFolder() {
=======
    public IMAPFolder getFolder() {
>>>>>>> first commit
=======
    public IMAPFolderProxy getFolder() {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public IMAPFolder getFolder() {
>>>>>>> first commit
        return folder;
    }
    
    public String getSearchValue() {
        return searchValue;
    }
    
    @Override
    protected void dispatch(LoadMessagesEventHandler handler) {
        handler.onLoadMessagesEvent(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<LoadMessagesEventHandler> getAssociatedType() {
        return TYPE;
    }

}
