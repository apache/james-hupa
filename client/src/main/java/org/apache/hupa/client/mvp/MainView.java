/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.hupa.client.mvp;

import java.util.ArrayList;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.bundles.IMAPTreeImages;
import org.apache.hupa.client.widgets.Loading;
import org.apache.hupa.shared.data.Message;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.RoundedPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainView extends Composite implements MainPresenter.Display{
	
	private DockPanel dockPanel;
	private VerticalPanel north;
	private HupaConstants constants = GWT.create(HupaConstants.class);
	private RoundedPanel west;
	private IMAPTreeImages tImages = GWT.create(IMAPTreeImages.class);
	private Tree folderTree = new Tree(tImages,true);
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(" ,@");  
	private SuggestBox searchBox = new SuggestBox(oracle);
	private Button searchButton = new Button(constants.searchButton());
	private Loading loading = new Loading(false);
	private Widget centerWidget;
	private RoundedPanel center;
	private IMAPMessageListView mListView;
	
	@Inject
	public MainView() {
		
		dockPanel = new DockPanel();
		
		dockPanel.setSpacing(10);
		dockPanel.setWidth("100%");

		createNorth();
		createWest();
		createCenter();

		dockPanel.add(north, DockPanel.NORTH);
		dockPanel.add(west, DockPanel.WEST);
		dockPanel.add(center, DockPanel.CENTER);
		dockPanel.setCellHorizontalAlignment(north, DockPanel.ALIGN_RIGHT);
		dockPanel.setCellHorizontalAlignment(center, DockPanel.ALIGN_LEFT);
		
		initWidget(dockPanel);
	}

	private void createWest() {
			west = new RoundedPanel(RoundedPanel.ALL,1);
			west.add(folderTree);
			west.setWidth("150px");	
	}
	
	private void createNorth() {
		north = new VerticalPanel();
		north.setWidth("100%");
		
		
		HorizontalPanel barPanel = new HorizontalPanel();
		barPanel.setWidth("100%");

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	
		
		searchBox.setAnimationEnabled(true);
		searchBox.setAutoSelectEnabled(false);
		searchBox.setWidth("250px");
		searchBox.setLimit(20);
		searchBox.addKeyUpHandler(new KeyUpHandler() {

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchButton.click();
				}
			}
			
		});
		hPanel.add(searchBox);
		hPanel.add(searchButton);
		barPanel.add(hPanel);
		barPanel.setCellHorizontalAlignment(hPanel, HorizontalPanel.ALIGN_LEFT);
		barPanel.add(loading);
		barPanel.setCellHorizontalAlignment(loading,HorizontalPanel.ALIGN_RIGHT);
		barPanel.setCellVerticalAlignment(loading, HorizontalPanel.ALIGN_MIDDLE);
		
		north.add(barPanel);
		
	}

	private void createCenter() {
		center = new RoundedPanel(RoundedPanel.ALL, 1);
		center.setBorder();
		center.setWidth("100%");
		center.add(mListView);

	}

	public HasClickHandlers getSearchClick() {
		return searchButton;
	}
	public HasValue<String> getSearchValue() {
		return searchBox;
	}
	

	public void fillOracle(ArrayList<Message> messages) {
		for (int i = 0; i < messages.size();i++) {
			String subject = messages.get(i).getSubject();
			String from = messages.get(i).getFrom();
			if (subject != null && subject.trim().length() > 0) {
				oracle.add(subject.trim());
			}
			if (from != null && from.trim().length() > 0) {
				oracle.add(from.trim());
			}
		}
		searchBox.setText("");
	}

	public void setCenter(Widget widget) {
		centerWidget = widget;
		center.setWidget(centerWidget);
	}

	public void setWest(Widget widget) {
		west.clear();
		west.setWidget(widget);
	}
	
	public Widget asWidget() {
		return this;
	}

	public void startProcessing() {
		loading.show();
	}

	public void stopProcessing() {
		loading.hide();
	}
}
