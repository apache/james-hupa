package org.apache.hupa.client.mvp;

import org.apache.hupa.client.activity.IMAPMessageListActivity;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailInboxPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class MainContentActivityMapper implements ActivityMapper {

	private final Provider<LoginActivity> loginActivityProvider;
	private final Provider<IMAPMessageListActivity> messageListActivityProvider;

	@Inject
	public MainContentActivityMapper(Provider<IMAPMessageListActivity> messageListActivityProvider,
			Provider<LoginActivity> loginActivityProvider) {
		this.messageListActivityProvider = messageListActivityProvider;
		this.loginActivityProvider = loginActivityProvider;
	}

	public Activity getActivity(Place place) {
		if (place instanceof MailInboxPlace) {
			return messageListActivityProvider.get().with(((MailInboxPlace) place).getUser());
		} else if (place instanceof DefaultPlace) {
			return loginActivityProvider.get();
		}

		return null;
	}
}
