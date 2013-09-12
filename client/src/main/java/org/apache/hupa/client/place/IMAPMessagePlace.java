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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.apache.hupa.client.activity.IMAPMessageActivity;
>>>>>>> 1. improve the inbox folder place.
=======
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
import org.apache.hupa.shared.data.ImapFolderImpl;
<<<<<<< HEAD
import org.apache.hupa.shared.data.MessageDetails;
<<<<<<< HEAD
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
=======
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.
=======
=======
>>>>>>> try to get message details, problem is:
=======
>>>>>>> forward and reply message to use RF
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

=======
>>>>>>> 
=======

<<<<<<< HEAD
>>>>>>> 1. improve the inbox folder place.
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class IMAPMessagePlace extends Place {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	
	private Message message;
	private MessageDetails messageDetails;
	private IMAPFolderProxy folder;
=======
	
=======
=======
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class IMAPMessagePlace extends AbstractPlace {
>>>>>>> remove some warnings and create the AbstractPlace that can give place infomation

>>>>>>> forward and reply message to use RF
	private Message message;
	private MessageDetails messageDetails;
<<<<<<< HEAD
<<<<<<< HEAD
	private IMAPFolder folder;
>>>>>>> 1. improve the inbox folder place.
=======
	private IMAPFolderProxy folder;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
	private ImapFolder folder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
	private User user;
	private String messageId;

	public IMAPMessagePlace(String token) {
		this.messageId = token;
	}
	
	public IMAPMessagePlace(MessageDetails messageDetails){
		this.messageDetails = messageDetails;
	}

	public Message getMessage() {
		return message;
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}
<<<<<<< HEAD

	public IMAPFolderProxy getFolder() {
		return folder;
	}

	public User getUser() {
		return user;
	}

@Prefix("message")
=======

  @Prefix("IMAPMessage")
>>>>>>> 
=======

	public ImapFolder getFolder() {
		return folder;
	}

	public User getUser() {
		return user;
	}

<<<<<<< HEAD
@Prefix("message")
>>>>>>> 1. improve the inbox folder place.
  public static class Tokenizer implements PlaceTokenizer<IMAPMessagePlace> {

    @Override
    public IMAPMessagePlace getPlace(String token) {
      return new IMAPMessagePlace();
    }

    @Override
    public String getToken(IMAPMessagePlace place) {
<<<<<<< HEAD
<<<<<<< HEAD
      return String.valueOf(place.getMessage().getUid());
=======
      return "IMAPMessage";
>>>>>>> 
=======
      return String.valueOf(place.getMessage().getUid());
>>>>>>> 1. improve the inbox folder place.
    }
  }
  
  public String toString(){
	  return this.getClass().getName()+"->[IMAPMessage]";
  }

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	public IMAPMessagePlace with(User user, IMAPFolderProxy folder, Message message, MessageDetails messageDetails){
=======
	public IMAPMessagePlace with(User user, IMAPFolder folder, Message message, MessageDetails messageDetails){
>>>>>>> 1. improve the inbox folder place.
=======
	public IMAPMessagePlace with(User user, IMAPFolderProxy folder, Message message, MessageDetails messageDetails){
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
	public IMAPMessagePlace with(User user, ImapFolder folder, Message message, MessageDetails messageDetails){
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.
      this.message = message;
      this.messageDetails = messageDetails;
      this.folder = folder;
      this.user = user;
      return this;
=======
	@Prefix("message")
	public static class Tokenizer implements PlaceTokenizer<IMAPMessagePlace> {

		@Override
		public IMAPMessagePlace getPlace(String token) {
			return new IMAPMessagePlace(token);
		}

		@Override
		public String getToken(IMAPMessagePlace place) {
			return place.getMessageDetails().getMessageId();
		}
	}

	public IMAPMessagePlace with(User user, ImapFolder folder, Message message, MessageDetails messageDetails) {
		this.message = message;
		this.messageDetails = messageDetails;
		this.folder = folder;
		this.user = user;
		return this;
>>>>>>> forward and reply message to use RF
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 
=======
>>>>>>> 1. improve the inbox folder place.
=======
	public String getMessageId() {
		return messageId;
	}

>>>>>>> prepare for message content panel
=======
>>>>>>> make reload message content work, use the same place with folder list, while separated with slash, that looks like Gmail's
}
