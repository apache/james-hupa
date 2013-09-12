package org.apache.hupa.client.gin;

import org.apache.hupa.client.mvp.AppPlaceFactory;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;

@GinModules({HupaEvoClientModule.class})
public interface HupaEvoGinjector  extends Ginjector {

	ActivityMapper getActivityMapper();
	
	PlaceController getPlaceController();
	
	EventBus getEventBus();
	
	AppPlaceFactory getAppPlaceFactory();
}
