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

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MailFolderPlace extends AbstractPlace {

	private User user;
	private String searchValue;
	private String uid;
	private MessageDetails messageDetails;
	private String fullName;

	public MailFolderPlace(String token) {
		if (token.contains("/")
				&& token.substring(token.lastIndexOf("/") + 1).matches("\\d+")) {
			fullName = token.substring(0, token.lastIndexOf("/"));
			uid = token.substring(token.lastIndexOf("/") + 1);
		} else {
			fullName = token;
		}
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}

	public String getMessageId() {
		return uid;
	}

	public String getFullName() {
		return fullName;
	}

	public User getUser() {
		return user;
	}

	public String getSearchValue() {
		return searchValue;
	}

	// the main place use empty string such that colon'd disappear
	@Prefix("")
	public static class Tokenizer implements PlaceTokenizer<MailFolderPlace> {

		@Override
		public MailFolderPlace getPlace(String token) {
			return new MailFolderPlace(token);
		}

		@Override
		public String getToken(MailFolderPlace place) {
			String token = place.getFullName();
			if (place.getMessageId() != null
					&& place.getMessageId().length() > 0) {
				token += "/" + place.getMessageId();
			}
			return token;
		}
	}

	public static void main(String[] args) {
		System.out.println("123a".matches("\\d+"));
		// String lll = "test/asdf/123";
		// System.out.println(lll.substring(lll.lastIndexOf("/")+1));
		// System.out.println(lll.substring(0, lll.lastIndexOf("/")));
	}
}
