package org.apache.hupa.client.place;

import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.ImapFolder;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MailFolderPlace extends Place {

	private static final String PREFIX = "folder";
	private User user;
	private String folderName = "";
	
	public String getFolderName(){
		return folderName;
	}

	public void setFolderName(String folderName){
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
		return this == otherPlace ;//|| (otherPlace != null && getClass() == otherPlace.getClass());
	}

	@Override
	public int hashCode() {
		return PREFIX.hashCode();
	}

	public String toString() {
		return this.getClass().getName() + "->[Inbox]" + folderName;
	}

	public MailFolderPlace with(User user) {
		this.user = user;
		return this;
	}

	public User getUser() {
		return user;
	}

	@Prefix(PREFIX)
	public static class Tokenizer implements PlaceTokenizer<MailFolderPlace> {

		@Override
		public MailFolderPlace getPlace(String token) {
			MailFolderPlace p = new MailFolderPlace();
			p.setFolderName(token);
			return p;
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
