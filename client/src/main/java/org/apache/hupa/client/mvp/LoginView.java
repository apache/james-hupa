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
import org.apache.hupa.widgets.ui.Loading;
import org.apache.hupa.widgets.ui.RndPanel;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.ButtonBar;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * View which shows the Login from
 *
 */
public class LoginView extends Composite implements KeyUpHandler,LoginPresenter.Display{
    
    private Button loginButton;
    private Button resetButton;
    private TextBox usernameTextBox = new TextBox();
    private PasswordTextBox passwordTextBox = new PasswordTextBox();
    private Loading loading;
    @Inject
    public LoginView(HupaConstants constants) {
        
        VerticalPanel mainContainer = new VerticalPanel();
        RndPanel rPanel = new RndPanel();
        FlexTable formPanel = new FlexTable();
        ButtonBar buttonBar = new ButtonBar();
        loginButton = new Button(constants.loginButton());
        resetButton = new Button(constants.resetButton());  
        loading = new Loading(constants.loading());
        
        mainContainer.setStyleName(HupaCSS.C_login_container);
        formPanel.addStyleName(HupaCSS.C_login_form);
        usernameTextBox.addStyleName(HupaCSS.C_login_box);
        passwordTextBox.addStyleName(HupaCSS.C_login_box);
        
        buttonBar.add(loginButton);
        buttonBar.add(resetButton);

        formPanel.setText(0, 0, constants.usernameLabel());
        formPanel.setWidget(0, 1, usernameTextBox);
        formPanel.setText(1, 0, constants.passwordLabel());
        formPanel.setWidget(1, 1, passwordTextBox);
        formPanel.getFlexCellFormatter().setColSpan(2, 0, 2);
        formPanel.setWidget(2, 0, buttonBar);

        rPanel.add(formPanel);
        mainContainer.add(rPanel);
        mainContainer.add(loading);
        initWidget(mainContainer);
        
        usernameTextBox.addKeyUpHandler(this);
        usernameTextBox.setFocus(true);
        passwordTextBox.addKeyUpHandler(this);

        loading.hide();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event
     * .dom.client.KeyUpEvent)
     */
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource().equals(usernameTextBox)) {
                passwordTextBox.setFocus(true);
            }  else if (event.getSource().equals(passwordTextBox)) {
                loginButton.click();
            }
        }
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
    public void setLoading(boolean load) {
        if (load) {
            loading.show();
        } else {
            loading.hide();

        }
    }
    
    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Composite#onAttach()
     */
    @Override
    public void onAttach() {
       super.onAttach();
       usernameTextBox.setFocus(true);
    }

}
