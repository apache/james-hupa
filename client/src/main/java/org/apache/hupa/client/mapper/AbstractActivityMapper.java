package org.apache.hupa.client.mapper;

import org.apache.hupa.client.place.DefaultPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

abstract class AbstractActivityMapper implements ActivityMapper{

	@Override
	public Activity getActivity(Place place) {
		if(place instanceof DefaultPlace)return null;
		return getAppActivity(place);
	}
	
	abstract Activity getAppActivity(Place place);

}
