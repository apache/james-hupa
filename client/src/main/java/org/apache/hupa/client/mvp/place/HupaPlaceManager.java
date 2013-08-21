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

package org.apache.hupa.client.mvp.place;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.DefaultPlaceManager;
import net.customware.gwt.presenter.client.place.TokenFormatter;

import com.google.inject.Inject;

/**
 * PlaceManager implementation for Hupa
 * 
 *
 */
public class HupaPlaceManager extends DefaultPlaceManager{
    @Inject
    public HupaPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, LoginPresenterPlace loginPresenterPlace, IMAPMessageListPresenterPlace messageListPresenterPlace, IMAPMessagePresenterPlace imapMessagePresenterPlace, MessageSendPresenterPlace sendPresenterPlace, ContactsPresenterPlace contactsPresenterPlace) {
        super(eventBus, tokenFormatter, loginPresenterPlace, messageListPresenterPlace, imapMessagePresenterPlace, sendPresenterPlace, contactsPresenterPlace);
    }

}
