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
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
=======
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
>>>>>>> first commit

import com.google.gwt.event.shared.GwtEvent;

public class DecreaseUnseenEvent extends GwtEvent<DecreaseUnseenEventHandler>{

    public final static Type<DecreaseUnseenEventHandler> TYPE = new Type<DecreaseUnseenEventHandler>();
    private User user;
<<<<<<< HEAD
    private ImapFolder folder;
    private int amount;
    
    public DecreaseUnseenEvent(User user, ImapFolder folder) {
        this(user, folder, 1);
    }
    
    public DecreaseUnseenEvent(User user, ImapFolder folder, int amount) {
=======
    private IMAPFolder folder;
    private int amount;
    
    public DecreaseUnseenEvent(User user, IMAPFolder folder) {
        this(user, folder, 1);
    }
    
    public DecreaseUnseenEvent(User user, IMAPFolder folder, int amount) {
>>>>>>> first commit
        this.user =user;
        this.folder = folder;
        this.amount = amount;
    }
    
<<<<<<< HEAD
    public ImapFolder getFolder() {
=======
    public IMAPFolder getFolder() {
>>>>>>> first commit
        return folder;
    }
    
    public User getUser() {
        return user;
    }
    
    public int getAmount() {
        return amount;
    }
    
    @Override
    protected void dispatch(DecreaseUnseenEventHandler handler) {
        handler.onDecreaseUnseenEvent(this);
        
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DecreaseUnseenEventHandler> getAssociatedType() {
        return TYPE;
    }

}
