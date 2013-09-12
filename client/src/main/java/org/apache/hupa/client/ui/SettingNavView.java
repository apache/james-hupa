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

package org.apache.hupa.client.ui;

import org.apache.hupa.client.activity.SettingNavActivity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class SettingNavView extends Composite implements SettingNavActivity.Displayable {
	
	@UiField Element navLabelsItem;
<<<<<<< HEAD
	@UiField Element navEcsItem;
	
	@UiField Anchor labelsAnchor;
	@UiField Anchor ecsAnchor;
=======
	@UiField Anchor labelsAnchor;
>>>>>>> make a skeleton in the setting place, for more setting items

	public SettingNavView() {
		initWidget(binder.createAndBindUi(this));
	}

	interface Binder extends UiBinder<HTMLPanel, SettingNavView> {
	}

	private static Binder binder = GWT.create(Binder.class);

	@Override
	public HasClickHandlers getLabelsAchor() {
		return labelsAnchor;
	}

	@Override
<<<<<<< HEAD
	public HasClickHandlers getEcsAchor() {
		return ecsAnchor;
	}


	@Override
	public void singleSelect(int i) {
		switch(i){
		case 2:selectEcsItem();break;
=======
	public void singleSelect(int i) {
		switch(i){
>>>>>>> make a skeleton in the setting place, for more setting items
		default:selectNavLabelItem();
		}
	}

<<<<<<< HEAD
	private void selectEcsItem() {
		String labelClass = navLabelsItem.getAttribute("class");
		navLabelsItem.setAttribute("class", labelClass.replace("selected", ""));
		String ecsClass = navEcsItem.getAttribute("class");
		navEcsItem.setAttribute("class", ecsClass + " selected");
		
	}

	private void selectNavLabelItem() {
		String ecsClass = navEcsItem.getAttribute("class");
		navEcsItem.setAttribute("class", ecsClass.replace("selected", ""));
		String labelClass = navLabelsItem.getAttribute("class");
		navLabelsItem.setAttribute("class", labelClass + " selected");
	}
=======
	private void selectNavLabelItem() {
		String clazz = navLabelsItem.getAttribute("class");
		navLabelsItem.setAttribute("class", clazz + " selected");
	}

>>>>>>> make a skeleton in the setting place, for more setting items
}
