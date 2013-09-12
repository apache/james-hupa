package org.apache.hupa.client.place;

import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MailInboxPlace extends Place {

	private static final String PREFIX = "inbox";
	private User user;
	private String mailId;
	
	public MailInboxPlace(String token){
		this.mailId = token;
	}
	public String getMailId(){
		return mailId;
	}
	public MailInboxPlace(){}

	/**
	 * equality test based on Class type, to let different instance of this
	 * Place class to be equals for CachingActivityMapper test on Place equality
	 * 
	 * @param otherPlace
	 *            the place to compare with
	 * @return true if this place and otherPlace are of the same Class type
	 */
//	@Override
//	public boolean equals(Object otherPlace) {
//		return this == otherPlace || (otherPlace != null && getClass() == otherPlace.getClass());
//	}
//
//	@Override
//	public int hashCode() {
//		return (PREFIX+mailId).hashCode();
//	}

	public String toString() {
		return this.getClass().getName() + "->[Inbox]" + mailId;
	}

	public MailInboxPlace with(User user) {
		this.user = user;
		return this;
	}

	public User getUser() {
		return user;
	}

	@Prefix(PREFIX)
	public static class Tokenizer implements PlaceTokenizer<MailInboxPlace> {

		@Override
		public MailInboxPlace getPlace(String token) {
			return new MailInboxPlace(token);
		}

		@Override
		public String getToken(MailInboxPlace place) {
			return place.getMailId();
		}
	}
	private IMAPFolder folder;
	private String searchValue;
	
	public IMAPFolder getFolder() {
		return folder;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public MailInboxPlace with(User user, IMAPFolder folder, String searchValue) {
		this.folder = folder;
		this.searchValue = searchValue;
		this.user = user;
		this.mailId = folder.getName();
		return this;
	}
}
