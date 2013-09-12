package org.apache.hupa.client.mvp;

import org.apache.hupa.client.activity.WestActivity;
import org.apache.hupa.client.place.MailInboxPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class WestActivityMapper implements ActivityMapper {
  private final Provider<WestActivity> westActivityProvider;

  @Inject
  public WestActivityMapper(Provider<WestActivity> westActivityProvider) {
    this.westActivityProvider = westActivityProvider;
  }

  public Activity getActivity(Place place) {
    if (place instanceof MailInboxPlace) {
      return westActivityProvider.get().with(((MailInboxPlace)place).getUser());
    }

    return null;
  }
}
