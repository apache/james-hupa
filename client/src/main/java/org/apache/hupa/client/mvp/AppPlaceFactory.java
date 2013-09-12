package org.apache.hupa.client.mvp;

import org.apache.hupa.client.place.LoginPlace;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * A place factory which knows about all the tokenizers in the app
 * 
 */
public class AppPlaceFactory {

	@Inject
	LoginPlace.Tokenizer loginPlaceTokenizer;

	@Inject
	Provider<LoginPlace> loginProvider;

	// contact place
	public LoginPlace.Tokenizer getLoginPlaceTokenizer() {
		return loginPlaceTokenizer;
	}

	public LoginPlace getLoginPlace() {
		return loginProvider.get();
	}

}
