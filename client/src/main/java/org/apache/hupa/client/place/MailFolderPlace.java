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
import org.apache.hupa.shared.domain.User;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MailFolderPlace extends AbstractPlace {

	// this can remove the colon ":"
	private static final String PREFIX = "folder";
	private User user;
	private String folderName = "";

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	/**
	 * equality test based on Class type, to let different instance of this
	 * Place class to be equals for CachingActivityMapper test on Place equality
	 * 
	 * @param otherPlace
	 *            the place to compare with
	 * @return true if this place and otherPlace are of the same Class type
	 */
	@Override
	public boolean equals(Object otherPlace) {
		return this == otherPlace;// || (otherPlace != null && getClass() ==
									// otherPlace.getClass());
	}

	@Override
	public int hashCode() {
		return PREFIX.hashCode();
	}

	public MailFolderPlace with(User user) {
		this.user = user;
		return this;
	}

	public MailFolderPlace with(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public MailFolderPlace with(ImapFolder folder) {
		this.folder = folder;
		this.folderName = folder.getName();
		return this;
	}

	public User getUser() {
		return user;
	}

	@Prefix(PREFIX)
	public static class Tokenizer implements PlaceTokenizer<MailFolderPlace> {

		@Override
		public MailFolderPlace getPlace(String token) {
			// TODO create place from token rather than with methods such that we can get place we want.
			return new MailFolderPlace().with(token);
		}

		@Override
		public String getToken(MailFolderPlace place) {
			return place.getFolderName();
		}
	}

	private ImapFolder folder;
	private String searchValue;

	public ImapFolder getFolder() {
		return folder;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public MailFolderPlace with(User user, ImapFolder folder, String searchValue) {
		this.folder = folder;
		this.searchValue = searchValue;
		this.user = user;
		this.folderName = folder.getName();
		return this;
	}
}
