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

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.shared.events.ServerStatusEvent.ServerStatus;
import org.apache.hupa.widgets.ui.RndPanel;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * AppView is the main WidgetContainer which show the LoginView or the MainView depending on if 
 * the user is logged in or not
 *
 */
public class AppView extends Composite implements AppPresenter.Display {
    
    private FlowPanel appContainer = new FlowPanel();
    
    private HTML logoContainer = new HTML(" "); 

    private RndPanel topWrapper = new RndPanel();
    private SimplePanel topContainer = new SimplePanel();
    private HorizontalPanel buttonContainer = new HorizontalPanel();
    private HorizontalPanel infoContainer = new HorizontalPanel();
    
    private RndPanel centralContainer = new RndPanel();

    private HorizontalPanel loginInfoPanel = new HorizontalPanel();
    private Label loginLabel = new Label();
    private Label loginUserLabel = new Label();
    private Hyperlink mainButton = new Hyperlink();
    private Hyperlink contactsButton = new Hyperlink();
    private Hyperlink logoutButton = new Hyperlink();
    private Label appnameLabel = new Label();
    
    private RndPanel flashContainer = new RndPanel();
    private Label flashLabel = new Label();
    private Label extraLabel = new Label();
    
    private FlowPanel bottomContainer = new FlowPanel();
    private HTML footLabel = new HTML();
    
    @Inject
    public AppView(HupaConstants constants) {

        // add class names
        appContainer.addStyleName(HupaCSS.C_app_container);
        logoContainer.addStyleName(HupaCSS.C_logo_container);
        topContainer.addStyleName(HupaCSS.C_top_container);
        centralContainer.addStyleName(HupaCSS.C_main_container);
        bottomContainer.addStyleName(HupaCSS.C_bottom_container);
        
        topWrapper.addStyleName(HupaCSS.C_top_container + "-wrap");
        buttonContainer.addStyleName(HupaCSS.C_button_container);
        infoContainer.addStyleName(HupaCSS.C_info_container);
        loginInfoPanel.addStyleName(HupaCSS.C_login_info_container);
        loginLabel.addStyleName(HupaCSS.C_login_info_label);
        loginUserLabel.addStyleName(HupaCSS.C_login_info_user);
        logoutButton.addStyleName(HupaCSS.C_menu_button);
        contactsButton.addStyleName(HupaCSS.C_menu_button);
        mainButton.addStyleName(HupaCSS.C_menu_button);
        appnameLabel.addStyleName(HupaCSS.C_header);
        flashContainer.addStyleName(HupaCSS.C_flash);
        
        // internationalize elements
        logoutButton.setText(constants.logoutButton());
        loginLabel.setText(constants.loginAs() + ": ");
        appnameLabel.setText(constants.productName());
        contactsButton.setText(constants.contactsTab());
        mainButton.setText(constants.mailTab());
        footLabel.setHTML(constants.footer());
        
        // Layout containers and panels
        buttonContainer.add(loginInfoPanel);
        loginInfoPanel.add(loginLabel);
        loginInfoPanel.add(loginUserLabel);
        buttonContainer.add(contactsButton);
        buttonContainer.add(mainButton);
        buttonContainer.add(logoutButton);
        buttonContainer.setCellWidth(loginInfoPanel, "100%");
        
        infoContainer.add(appnameLabel);
        infoContainer.add(extraLabel);
        
        topWrapper.add(buttonContainer);
        topWrapper.add(infoContainer);
        topWrapper.add(flashContainer);
        topContainer.add(topWrapper);
        
        flashContainer.add(flashLabel);
        
        bottomContainer.add(footLabel);
        
        appContainer.add(logoContainer);
        appContainer.add(topContainer);
        appContainer.add(centralContainer);
        appContainer.add(bottomContainer);
        initWidget(appContainer);
        
        showTopNavigation(false);
    }


    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    public Widget asWidget() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.hupa.client.mvp.AppPresenter.Display#getLogoutClick()
     */
    public HasClickHandlers getLogoutClick() {
        return logoutButton;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.hupa.client.mvp.AppPresenter.Display#showTopNavigation(boolean
     * )
     */
    public void showTopNavigation(boolean show) {
        buttonContainer.setVisible(show);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.hupa.client.mvp.AppPresenter.Display#getUserText()
     */
    public HasText getUserText() {
        return loginUserLabel;
    }


    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetContainerDisplay#addWidget(com.google.gwt.user.client.ui.Widget)
     */
    public void addWidget(Widget widget) {
        widget.addStyleName(HupaCSS.C_main_container);
        centralContainer.setWidget(widget);
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetContainerDisplay#removeWidget(com.google.gwt.user.client.ui.Widget)
     */
    public void removeWidget(Widget widget) {
        centralContainer.remove(widget);
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.presenter.client.widget.WidgetContainerDisplay#showWidget(com.google.gwt.user.client.ui.Widget)
     */
    public void showWidget(Widget widget) {
        centralContainer.setWidget(widget);
    }


    public HasClickHandlers getContactsClick() {
        return contactsButton;
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.AppPresenter.Display#setServerStatus(org.apache.hupa.shared.events.ServerStatusEvent.ServerStatus)
     */
    public void setServerStatus(ServerStatus status) {
        if (status == ServerStatus.Available) 
            showMessage("Server available.", 2000);
        else 
            showMessage("Server unavailable", 0);
    }

    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.AppPresenter.Display#showMessage(java.lang.String, int)
     */
    public void showMessage(String message, int millisecs) {
        flashLabel.setText(message);
        flashContainer.setVisible(true);
        if (millisecs > 0)
            hideMessage.schedule(millisecs);
    }
    
    private final Timer hideMessage = new Timer() {
        public void run() {
            flashContainer.setVisible(false);
            flashLabel.setText("");
        }
    };

    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.AppPresenter.Display#showContactsButton()
     */
    public void showContactsButton() {
        mainButton.setVisible(false);
        contactsButton.setVisible(true);
    }


    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.AppPresenter.Display#showMainButton()
     */
    public void showMainButton() {
        mainButton.setVisible(true);
        contactsButton.setVisible(false);
    }


    /* (non-Javadoc)
     * @see org.apache.hupa.client.mvp.AppPresenter.Display#getMainClick()
     */
    public HasClickHandlers getMainClick() {
        return mainButton;
    }

}
