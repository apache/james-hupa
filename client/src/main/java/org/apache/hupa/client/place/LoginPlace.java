package org.apache.hupa.client.place;

import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.mvp.ActivityPlace;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class LoginPlace extends ActivityPlace<LoginActivity> {

	@Inject
	public LoginPlace(LoginActivity activity) {
		super(activity);
	}

	@Prefix("Login")
	public static class Tokenizer implements PlaceTokenizer<LoginPlace> {

		private final Provider<LoginPlace> placeProvider;

		@Inject
		public Tokenizer(Provider<LoginPlace> placeProvider) {
			this.placeProvider = placeProvider;
		}

		@Override
		public LoginPlace getPlace(String token) {
			LoginPlace place = placeProvider.get();
			place.setPlaceName(token);
			return place;
		}

		@Override
		public String getToken(LoginPlace place) {
			return place.getPlaceName();
		}

	}

}
