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

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MailFolderPlace extends AbstractPlace {

	private String uid;
	private String fullName;
	private static final String DELIMITER = "/";

	/**
	 * Folder places look like: #Mock-Inbox, #INBOX,<br>
	 * while message places: #Mock-Inbox&#47;10, #INBOX&#47;1234, #%5BGmail%5DDrafts&#47;18
	 * 
	 * @param token
	 */
	public MailFolderPlace(String token) {
		if (isMessagePlace(token)) {
			initPlace4Message(token);
		} else {
			initPlace4Folder(token);
		}
	}

	private boolean isMessagePlace(String token) {
		return token.contains(DELIMITER) && isEndWIthDigit(token);
	}

	private void initPlace4Folder(String token) {
		fullName = token;
	}

	private void initPlace4Message(String token) {
		fullName = token.substring(0, token.lastIndexOf(DELIMITER));
		uid = token.substring(token.lastIndexOf(DELIMITER) + 1);
	}

	private boolean isEndWIthDigit(String token) {
		return token.substring(token.lastIndexOf(DELIMITER) + 1)
				.matches("\\d+");
	}

	public String getUid() {
		return uid;
	}

	public String getFullName() {
		return fullName;
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
			StringBuilder token = new StringBuilder(place.getFullName());
			if (place.getUid() != null && place.getUid().length() > 0) {
				token.append(DELIMITER + place.getUid());
			}
			return token.toString();
		}
	}
}
