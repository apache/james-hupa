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

package org.apache.hupa.client.place;

import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MessageSendPlace extends AbstractPlace {

	
	private User user ;
	private ImapFolder folder;
	private Message message;
	private MessageDetails messageDetails;
	private Type forward;
	
	
	@Prefix("send")
	public static class Tokenizer implements PlaceTokenizer<MessageSendPlace> {

		@Override
		public MessageSendPlace getPlace(String token) {
			return new MessageSendPlace();
		}

		@Override
		public String getToken(MessageSendPlace place) {
			return place.getForward().toString();
		}
	}

	public Place with(User user, ImapFolder folder, Message message, MessageDetails messageDetails, Type forward) {
		this.forward = forward;
		this.user = user;
		this.folder = folder;
		this.message = message;
		this.messageDetails = messageDetails;
		return this;
	}

	public User getUser() {
		return user;
	}

	public ImapFolder getFolder() {
		return folder;
	}

	public Message getMessage() {
		return message;
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}

	public Type getForward() {
		return forward;
	}
	
	

	
	
}
