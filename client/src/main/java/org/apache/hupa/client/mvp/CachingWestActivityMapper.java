package org.apache.hupa.client.mvp;

import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailFolderPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.CachingActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

public class CachingWestActivityMapper implements ActivityMapper {

	private ActivityMapper filteredActivityMapper;

	@Inject
	public CachingWestActivityMapper(WestActivityMapper westActivityMapper) {

		FilteredActivityMapper.Filter filter = new FilteredActivityMapper.Filter() {
			@Override
			public Place filter(Place place) {
				return (place instanceof DefaultPlace || place instanceof MailFolderPlace) ? place
						: new MailFolderPlace();
			}
		};

		CachingActivityMapper cachingActivityMapper = new CachingActivityMapper(westActivityMapper);
		filteredActivityMapper = new FilteredActivityMapper(filter, cachingActivityMapper);
	}

	@Override
	public Activity getActivity(Place place) {
		return filteredActivityMapper.getActivity(place);
	}

}
