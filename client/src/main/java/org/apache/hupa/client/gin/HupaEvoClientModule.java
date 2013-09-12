package org.apache.hupa.client.gin;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.ExceptionHandler;

import org.apache.hupa.client.CachingDispatchAsync;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.mvp.AppActivityMapper;
import org.apache.hupa.client.mvp.AppPlaceFactory;
import org.apache.hupa.client.ui.LoginView;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class HupaEvoClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		// bind the EventBus
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PlaceController.class).to(InjectablePlaceController.class).in(Singleton.class);

		bind(AppPlaceFactory.class).in(Singleton.class);

		// bind the mapper
		bind(ActivityMapper.class).to(AppActivityMapper.class).in(Singleton.class);

		// bind the views
		bind(LoginActivity.Display.class).to(LoginView.class);
        // Used by dispatch. Note that GWT 2.4 has its own ExceptionHandler etc in other namespace
        bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);

	}
    @Provides
    @Singleton
    protected DispatchAsync provideDispatchAsync(ExceptionHandler exceptionHandler) {
        return new CachingDispatchAsync( exceptionHandler );
    }

}
