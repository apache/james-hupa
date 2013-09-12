<<<<<<< HEAD
<<<<<<< HEAD
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

=======
package org.apache.hupa.client.place;

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 
=======
=======
>>>>>>> 1. improve the inbox folder place.
import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.shared.data.ImapFolderImpl;
<<<<<<< HEAD
import org.apache.hupa.shared.data.MessageDetails;
<<<<<<< HEAD
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;

>>>>>>> 1. improve the inbox folder place.
=======
package org.apache.hupa.client.place;

>>>>>>> 
=======
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.
=======
=======
>>>>>>> try to get message details, problem is:
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

>>>>>>> 1. improve the inbox folder place.
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

<<<<<<< HEAD
<<<<<<< HEAD
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
	
	

	
	
=======
public class MessageSendPlace extends Place {

	
	private User user ;
	private IMAPFolderProxy folder;
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

	public String toString() {
		return this.getClass().getName() + "->[MessageSend]";
	}

	public Place with(User user, IMAPFolderProxy folder, Message message, MessageDetails messageDetails, Type forward) {
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

	public IMAPFolderProxy getFolder() {
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
	
	

<<<<<<< HEAD
>>>>>>> 
=======
	
	
>>>>>>> 1. improve the inbox folder place.
=======
public class MessageSendPlace extends Place {

	
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

	public String toString() {
		return this.getClass().getName() + "->[MessageSend]";
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
	
	

<<<<<<< HEAD
>>>>>>> 
=======
	
	
>>>>>>> 1. improve the inbox folder place.
}
