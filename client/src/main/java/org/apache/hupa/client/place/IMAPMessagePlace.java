package org.apache.hupa.client.place;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.apache.hupa.client.activity.IMAPMessageActivity;
>>>>>>> 1. improve the inbox folder place.
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.MessageDetails;
import org.apache.hupa.shared.data.User;
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;

=======
>>>>>>> 
=======

>>>>>>> 1. improve the inbox folder place.
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class IMAPMessagePlace extends Place {
<<<<<<< HEAD
<<<<<<< HEAD
	
	private Message message;
	private MessageDetails messageDetails;
	private IMAPFolderProxy folder;
=======
	
	private Message message;
	private MessageDetails messageDetails;
	private IMAPFolder folder;
>>>>>>> 1. improve the inbox folder place.
	private User user;

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

	public IMAPFolder getFolder() {
		return folder;
	}

	public User getUser() {
		return user;
	}

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
	public IMAPMessagePlace with(User user, IMAPFolderProxy folder, Message message, MessageDetails messageDetails){
=======
	public IMAPMessagePlace with(User user, IMAPFolder folder, Message message, MessageDetails messageDetails){
>>>>>>> 1. improve the inbox folder place.
      this.message = message;
      this.messageDetails = messageDetails;
      this.folder = folder;
      this.user = user;
      return this;
	}

<<<<<<< HEAD
=======
>>>>>>> 
=======
>>>>>>> 1. improve the inbox folder place.
}
