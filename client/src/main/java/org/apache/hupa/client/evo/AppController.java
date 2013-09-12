package org.apache.hupa.client.evo;

import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.CheckSessionRequest;
import org.apache.hupa.client.ui.AppLayout;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class AppController {

	@Inject private PlaceHistoryHandler placeHistoryHandler;
	@Inject private AppLayout appPanelView;
	@Inject private PlaceController placeController;
	@Inject private HupaRequestFactory requestFactory;
	private Place currentPlace;

	@Inject
	public AppController(EventBus eventBus, ActivityManagerInitializer initializeActivityManagerByGin) {
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceLayoutHandler());
	}

	public void start() {
		RootLayoutPanel.get().add(appPanelView.getMainLayoutPanel());
		placeHistoryHandler.handleCurrentHistory();
	}

	private final class PlaceLayoutHandler implements PlaceChangeEvent.Handler {
		@Override
		public void onPlaceChange(PlaceChangeEvent event) {
			if (placeChange(event)) {
				checkSession();
			}
			refreshActivities(event);
		}

		private void refreshActivities(PlaceChangeEvent event) {
			Place newPlace = event.getNewPlace();
			if (newPlace != currentPlace) {
				if (isAuth(newPlace, currentPlace)) {
					appPanelView.setDefaultLayout();
				} else if (newPlace instanceof DefaultPlace) {
					appPanelView.setLoginLayout();
				}
				currentPlace = newPlace;
			}
		}

		private void checkSession() {
			CheckSessionRequest checkSession = requestFactory.sessionRequest();
			checkSession.isValid().fire(new Receiver<Boolean>() {
				@Override
				public void onSuccess(Boolean sessionValid) {
					if (!sessionValid) {
						AppController.this.placeController.goTo(new DefaultPlace());
					}
				}
			});
		}

		private boolean placeChange(PlaceChangeEvent event) {
			return currentPlace != null && !(currentPlace instanceof DefaultPlace) && event.getNewPlace() != currentPlace;
		}

		private boolean isAuth(Place newPlace, Place currentPlace) {
			return (newPlace instanceof MailFolderPlace) && !(currentPlace instanceof MailFolderPlace);
		}
	}

}
