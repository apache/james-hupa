package org.apache.hupa.client.place;

import org.apache.hupa.shared.data.User;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MailInboxPlace extends MailPlace {

	private static final String PREFIX = "inbox";
	private User user;

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
		return this == otherPlace || (otherPlace != null && getClass() == otherPlace.getClass());
	}

	@Override
	public int hashCode() {
		return PREFIX.hashCode();
	}

	public String toString() {
		return this.getClass().getName() + "->[Inbox]";
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
			return new MailInboxPlace();
		}

		@Override
		public String getToken(MailInboxPlace place) {
			return PREFIX;
		}
	}
}
