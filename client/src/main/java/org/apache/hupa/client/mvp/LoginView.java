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

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.widgets.Loading;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.ButtonBar;
import org.cobogw.gwt.user.client.ui.RoundedPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View which shows the Login from
 *
 */
public class LoginView extends Composite implements KeyUpHandler,LoginPresenter.Display{
    
    private HupaConstants constants = GWT.create(HupaConstants.class);
    private Button loginButton = new Button(constants.loginButton());
    private Button resetButton = new Button(constants.resetButton());
    private TextBox usernameTextBox = new TextBox();
    private PasswordTextBox passwordTextBox = new PasswordTextBox();
    private CheckBox saveBox = new CheckBox(constants.saveLoginLabel());
    private Label errorLabel = new Label();
    private Loading loading = new Loading(false);
    private int minUsernameLength = 1;
    private int minPasswordLength = 1;

    public LoginView() {
    
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.setSpacing(5);
        vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        vPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

        VerticalPanel formPanel = new VerticalPanel();
        formPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        formPanel.setSpacing(5);
        Grid grid = new Grid(3, 2);
        grid.setStyleName("hupa-LoginForm");
        grid.setText(0, 0, constants.usernameLabel());
        grid.setWidget(0, 1, usernameTextBox);
        grid.setText(1, 0, constants.passwordLabel());
        grid.setWidget(1, 1, passwordTextBox);
        grid.setWidget(2, 1, saveBox);
        grid.getCellFormatter().setStyleName(0, 0, "hupa-LoginForm");
        grid.getCellFormatter().setStyleName(0, 1, "hupa-LoginForm");
        grid.getCellFormatter().setStyleName(1, 0, "hupa-LoginForm");
        grid.getCellFormatter().setStyleName(1, 1, "hupa-LoginForm");
        grid.getCellFormatter().setStyleName(1, 0, "hupa-LoginForm");
        grid.getCellFormatter().setStyleName(2, 1, "hupa-LoginForm");
        grid.getCellFormatter().setHorizontalAlignment(0,0, VerticalPanel.ALIGN_RIGHT);
        grid.getCellFormatter().setHorizontalAlignment(1,0, VerticalPanel.ALIGN_RIGHT);

        usernameTextBox.addKeyUpHandler(this);
        usernameTextBox.setFocus(true);
        
        passwordTextBox.addKeyUpHandler(this);

        formPanel.add(grid);
        formPanel.add(loading);
        loading.hide();
        
        ButtonBar buttonBar = new ButtonBar();

        buttonBar.add(loginButton);
        buttonBar.add(resetButton);

        loginButton.setEnabled(false);
       
        formPanel.add(buttonBar);
        
        errorLabel.setStyleName("hupa-ErrorLabel");
        formPanel.add(errorLabel);
        
        RoundedPanel rPanel = new RoundedPanel(RoundedPanel.ALL,1);
        rPanel.setBorder();
        rPanel.add(formPanel);
        vPanel.add(rPanel);
        vPanel.add(loading);
        initWidget(vPanel);
        
        // TODO: move width to style sheet to be customizable
        vPanel.setWidth("100%");
        rPanel.setWidth("400px");
        usernameTextBox.setWidth("250px");
        passwordTextBox.setWidth("250px");
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event
     * .dom.client.KeyUpEvent)
     */
    public void onKeyUp(KeyUpEvent event) {
        if (usernameTextBox.getText().length() >= minUsernameLength
                && passwordTextBox.getText().length() >= minPasswordLength) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
        
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource().equals(usernameTextBox)) {
                passwordTextBox.setFocus(true);
            }  else if (event.getSource().equals(passwordTextBox)) {
                if (loginButton.isEnabled()) {
                    loginButton.click();
                }
            }
        }
        
    }
 

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.LoginPresenter.Display#getErrorText()
     */
    public HasText getErrorText() {
        return errorLabel;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.LoginPresenter.Display#getLoginClick()
     */
    public HasClickHandlers getLoginClick() {
        return loginButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.LoginPresenter.Display#getPasswordValue()
     */
    public HasValue<String> getPasswordValue() {
        return passwordTextBox;
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.LoginPresenter.Display#getResetClick()
     */
    public HasClickHandlers getResetClick() {
        return resetButton;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.LoginPresenter.Display#getUserNameValue()
     */
    public HasValue<String> getUserNameValue() {
        return usernameTextBox;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.mvp.LoginPresenter.Display#getUserNameFocus()
     */
    public Focusable getUserNameFocus() {
        return usernameTextBox;
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
     * @see org.apache.hupa.client.HupaWidgetDisplay#startProcessing()
     */
    public void startProcessing() {
        loading.show();
    }


    /*
     * (non-Javadoc)
     * @see org.apache.hupa.client.HupaWidgetDisplay#stopProcessing()
     */
    public void stopProcessing() {
        loading.hide();
    }
}
