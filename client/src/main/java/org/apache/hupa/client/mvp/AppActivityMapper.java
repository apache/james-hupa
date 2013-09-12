package org.apache.hupa.client.mvp;

import javax.inject.Inject;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;


public class AppActivityMapper implements ActivityMapper {

	private EventBus eventBus;
	private PlaceController placeController;
	
	@Inject
	public AppActivityMapper(EventBus eventBus, PlaceController placeController) {
		super();
		this.eventBus = eventBus;
		this.placeController = placeController;
	}
	
	@Override
	public Activity getActivity(Place place) {
		if (place instanceof ActivityPlace) {
			Activity activity = ((ActivityPlace) place).getActivity();
            return activity;
        }

        return null;
		
	}

}
