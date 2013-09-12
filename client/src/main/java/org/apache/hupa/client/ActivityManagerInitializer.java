package org.apache.hupa.client;

import org.apache.hupa.client.ui.AppLayout;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class is responsible for ActivityManager instantiations through GIN,
 * it also associate every ActivityManager with the corresponding display region
 */
public class ActivityManagerInitializer {

  @Inject
  public ActivityManagerInitializer(AppLayout appPanelView,
                                    @Named("WestRegion") ActivityManager verticalMasterActivityManager,
                                    @Named("MainContentRegion") ActivityManager mainContentActivityManager) {
    verticalMasterActivityManager.setDisplay(appPanelView.getWestContainer());
    mainContentActivityManager.setDisplay(appPanelView.getMainContainer());
  }

}
