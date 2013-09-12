package org.apache.hupa.client.place;

import org.apache.hupa.client.activity.IMAPMessageActivity;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class IMAPMessagePlace extends Place {
	
	private Message message;
	private MessageDetails messageDetails;
	private IMAPFolder folder;
	private User user;

  public Message getMessage() {
		return message;
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}

	public IMAPFolder getFolder() {
		return folder;
	}

	public User getUser() {
		return user;
	}

@Prefix("message")
  public static class Tokenizer implements PlaceTokenizer<IMAPMessagePlace> {

    @Override
    public IMAPMessagePlace getPlace(String token) {
      return new IMAPMessagePlace();
    }

    @Override
    public String getToken(IMAPMessagePlace place) {
      return String.valueOf(place.getMessage().getUid());
    }
  }
  
  public String toString(){
	  return this.getClass().getName()+"->[IMAPMessage]";
  }

	public IMAPMessagePlace with(User user, IMAPFolder folder, Message message, MessageDetails messageDetails){
      this.message = message;
      this.messageDetails = messageDetails;
      this.folder = folder;
      this.user = user;
      return this;
	}

}
