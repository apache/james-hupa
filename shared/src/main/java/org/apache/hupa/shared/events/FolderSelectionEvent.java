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
<<<<<<< HEAD
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
=======
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
>>>>>>> first commit
=======
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.

import com.google.gwt.event.shared.GwtEvent;

public class FolderSelectionEvent extends GwtEvent<FolderSelectionEventHandler>{
    
    private User user;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private ImapFolder folder;
    public final static Type<FolderSelectionEventHandler> TYPE = new Type<FolderSelectionEventHandler>();
    public FolderSelectionEvent(User user, ImapFolder folder) {
=======
    private IMAPFolder folder;
    public final static Type<FolderSelectionEventHandler> TYPE = new Type<FolderSelectionEventHandler>();
    public FolderSelectionEvent(User user, IMAPFolder folder) {
>>>>>>> first commit
=======
    private IMAPFolderProxy folder;
    public final static Type<FolderSelectionEventHandler> TYPE = new Type<FolderSelectionEventHandler>();
    public FolderSelectionEvent(User user, IMAPFolderProxy folder) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private IMAPFolder folder;
    public final static Type<FolderSelectionEventHandler> TYPE = new Type<FolderSelectionEventHandler>();
    public FolderSelectionEvent(User user, IMAPFolder folder) {
>>>>>>> first commit
=======
    private IMAPFolderProxy folder;
    public final static Type<FolderSelectionEventHandler> TYPE = new Type<FolderSelectionEventHandler>();
    public FolderSelectionEvent(User user, IMAPFolderProxy folder) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private ImapFolder folder;
    public final static Type<FolderSelectionEventHandler> TYPE = new Type<FolderSelectionEventHandler>();
    public FolderSelectionEvent(User user, ImapFolder folder) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        this.user =user;
        this.folder = folder;
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
    public IMAPFolderProxy getFolder() {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    public ImapFolder getFolder() {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
        return folder;
    }
    
    public User getUser() {
        return user;
    }

    @Override
    protected void dispatch(FolderSelectionEventHandler handler) {
        handler.onFolderSelectionEvent(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<FolderSelectionEventHandler> getAssociatedType() {
        return TYPE;
    }

}
