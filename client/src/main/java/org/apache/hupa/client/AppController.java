package org.apache.hupa.client;

import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailInboxPlace;
import org.apache.hupa.client.ui.AppLayout;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;

/**
 * This is the main controller of the application.
 */
public class AppController{

  private final PlaceController placeController;
  private final PlaceHistoryHandler placeHistoryHandler;
  private final AppLayout appPanelView;
  private Place currentPlace;

  /**
   * All parameters are injected by GIN
   *
   * @param placeController the application's PlaceController
   * @param eventBus the application's EventBus
   * @param placeHistoryHandler the application's PlaceHistoryHandler
   * @param appLayout this is the application's main panel
   * @param mainMenuView this is the application's navigation top bar
   * @param activityManagerInitializer unused parameter, it's here just to force GIN's initialization of ActivityManagers
   */
  @Inject
  public AppController(PlaceController placeController,
                           EventBus eventBus,
                           PlaceHistoryHandler placeHistoryHandler,
                           AppLayout appLayout,
                           ActivityManagerInitializer activityManagerInitializer) {
    this.placeController = placeController;
    this.placeHistoryHandler = placeHistoryHandler;
    this.appPanelView = appLayout;
    eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceLayoutHandler());
  }

  public void start() {
    RootLayoutPanel.get().add(appPanelView.getMainLayoutPanel());
    placeHistoryHandler.handleCurrentHistory();
  }


  private final class PlaceLayoutHandler implements PlaceChangeEvent.Handler {
    @Override
    public void onPlaceChange(PlaceChangeEvent event) {
      Place newPlace = event.getNewPlace();
      if (newPlace != currentPlace) {
        if (newPlace instanceof MailInboxPlace) {
          appPanelView.setDefaultLayout();
        } else if (newPlace instanceof DefaultPlace){
          appPanelView.setLoginLayout();
        }
        currentPlace = newPlace;
      }
    }
  }

}
