package org.apache.hupa.client.mvp;

import org.apache.hupa.client.activity.IMAPMessageActivity;
import org.apache.hupa.client.activity.IMAPMessageListActivity;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.activity.MessageSendActivity;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class MainContentActivityMapper implements ActivityMapper {

	private final Provider<LoginActivity> loginActivityProvider;
	private final Provider<IMAPMessageListActivity> messageListActivityProvider;
	private final Provider<MessageSendActivity> messageSendActivityProvider;
	private final Provider<IMAPMessageActivity> messageActivityProvider;

	@Inject
	public MainContentActivityMapper(Provider<IMAPMessageListActivity> messageListActivityProvider,
			Provider<LoginActivity> loginActivityProvider,Provider<MessageSendActivity> messageSendActivityProvider,Provider<IMAPMessageActivity> messageActivityProvider) {
		this.messageListActivityProvider = messageListActivityProvider;
		this.loginActivityProvider = loginActivityProvider;
		this.messageSendActivityProvider = messageSendActivityProvider;
		this.messageActivityProvider = messageActivityProvider;
	}

	public Activity getActivity(Place place) {
		if (place instanceof MailFolderPlace) {
//			return messageListActivityProvider.get().with(((MailInboxPlace) place).getUser());

			MailFolderPlace p = ((MailFolderPlace)place);
	    	if(p.getFolderName().equalsIgnoreCase("inbox")){
				return messageListActivityProvider.get().with(p.getUser(),p.getFolder(), p.getSearchValue());
	    	} else if(p.getFolderName().equalsIgnoreCase("trash")){
				return messageListActivityProvider.get().with(p.getUser(),p.getFolder(), p.getSearchValue());
	    	}
		} else if (place instanceof DefaultPlace) {
			return loginActivityProvider.get();
		} else if(place instanceof MessageSendPlace){
			return messageSendActivityProvider.get().with((MessageSendPlace)place);
		} else if(place instanceof IMAPMessagePlace){
			return messageActivityProvider.get().with((IMAPMessagePlace)place);
		}

		return null;
	}
}
