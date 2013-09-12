package org.apache.hupa.client.gin;

import javax.inject.Inject;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;


/**
 * The injectable PlaceController to be able to inject the eventbus into
 *
 */
public class InjectablePlaceController extends PlaceController {

	@Inject
	public InjectablePlaceController(EventBus eventBus) {
		super(eventBus);
	}

}
