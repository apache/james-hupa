package org.apache.hupa.client;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.ServerStatusEvent;
import org.apache.hupa.shared.events.ServerStatusEvent.ServerStatus;
import org.apache.hupa.shared.rpc.CheckSession;
import org.apache.hupa.shared.rpc.CheckSessionResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public abstract class HupaCallback<T> implements AsyncCallback<T> {
	
	private DispatchAsync dispatcher = null;
	private EventBus eventBus = null;
	private ServerStatusEvent available = new ServerStatusEvent(ServerStatus.Available); 
	private ServerStatusEvent unavailable = new ServerStatusEvent(ServerStatus.Unavailable); 

	@Inject
	public HupaCallback(DispatchAsync dispatcher, EventBus bus) {
		this.dispatcher = dispatcher;
		this.eventBus = bus;
	}

	/**
	 * If you override this method, remember to call super.onFailure() 
	 */
	public void onFailure(final Throwable originalCaught) {
		// Server's response is invalid due to:
		// server unaccessible, session error or server exception
		dispatcher.execute(new CheckSession(), new AsyncCallback<CheckSessionResult>() {
			public void onFailure(Throwable caught) {
				// The server is unaccessible
				eventBus.fireEvent(unavailable);
				callbackError(originalCaught);
			}
			public void onSuccess(CheckSessionResult result) {
				if (!result.isValid()) {
					// Server's connection is fine, but the user has not a valid session
					eventBus.fireEvent(new LogoutEvent(null));
				} else {
					// Server's connection is fine, and the user has a valid session
					// So the original action failed because a server's exception 
					eventBus.fireEvent(available);
				}
				callbackError(originalCaught);
			}
		});
	}
	
	/**
	 * If you override this method, remember to call super.onSuccess() 
	 */
	public void onSuccess(T result) {
		// Server's response is valid,
		eventBus.fireEvent(available);
		// Execute the original method
		callback(result);
	}
	
	/**
	 * The callback code
	 * @param result
	 */
	public abstract void callback(T result); 

	/**
	 * The callback code in the case of error
	 * Override this method, if you need this feature.
	 *  
	 * @param result
	 */
	public void callbackError(Throwable caught) {
		GWT.log("Error", caught);
	}
}
