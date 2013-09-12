package org.apache.hupa.client.evo;

import org.apache.hupa.client.ioc.AppGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class HupaEvo implements EntryPoint {
	@Override
	public void onModuleLoad() {
		handleExceptionsAsync();
		removeLoading();
	    AppController appController = injector.getAppController();
	    appController.start();
	}
	
	private void handleExceptionsAsync(){
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(Throwable e) {
                e.printStackTrace();
            }
        });
	}

	private void removeLoading() {
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
	}

	private final AppGinjector injector = GWT.create(AppGinjector.class);

}
