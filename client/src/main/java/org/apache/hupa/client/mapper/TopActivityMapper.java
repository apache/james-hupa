package org.apache.hupa.client.mapper;

import org.apache.hupa.client.activity.TopActivity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class TopActivityMapper implements ActivityMapper {
	private final Provider<TopActivity> topActivityProvider;

	@Inject
	public TopActivityMapper(Provider<TopActivity> topActivityProvider) {
		this.topActivityProvider = topActivityProvider;
	}

	public Activity getActivity(Place place) {
		return topActivityProvider.get();
	}
}
