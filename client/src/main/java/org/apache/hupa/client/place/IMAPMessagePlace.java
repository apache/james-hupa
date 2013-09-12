package org.apache.hupa.client.place;

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
import org.apache.hupa.shared.data.Message;
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
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;
>>>>>>> Make chechsession and login work with RF, with refactoring fetch folders.

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
	}

<<<<<<< HEAD
=======
>>>>>>> 
=======
>>>>>>> 1. improve the inbox folder place.
}
