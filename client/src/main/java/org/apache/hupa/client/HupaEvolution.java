package org.apache.hupa.client;

import org.apache.hupa.client.ioc.AppGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class HupaEvolution implements EntryPoint {

	private final AppGinjector injector = GWT.create(AppGinjector.class);
	@Override
	public void onModuleLoad() {
		removeLoading();
	    AppController appController = injector.getAppController();
	    appController.start();
	}

	private void removeLoading() {
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
	}

}
