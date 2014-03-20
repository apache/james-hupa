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

import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.shared.domain.Settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite implements LoginActivity.Displayable {

    @UiField FlowPanel innerBox;
    @UiField HTMLPanel message;
    @UiField SubmitButton submitButton;
    @UiField Button setupButton;

    @UiField PopupPanel settingsPopup;
    @UiField TextBox imapServer;
    @UiField TextBox imapPort;
    @UiField CheckBox imapSecure;
    @UiField TextBox smtpServer;
    @UiField TextBox smtpPort;
    @UiField CheckBox smtpSecure;
    private LoginActivity activity;

    /*
     * We wrap login/password boxes with a form which must be in the html
     * document, in this way, the browser knows that we are sending a login form
     * and offers the save password dialog to the user
     */
    @UiField(provided = true) TextBox usernameTextBox;
    @UiField(provided = true) PasswordTextBox passwordTextBox;

    public LoginView() {
        // Wrapped elements from the html document
        usernameTextBox = TextBox.wrap(DOM.getElementById("email"));
        passwordTextBox = PasswordTextBox.wrap(DOM.getElementById("password"));

        initWidget(binder.createAndBindUi(this));
        imapPort.setText("");
        smtpPort.setText("");

        usernameTextBox.setFocus(true);

        setLoading(false);

        settingsPopup.setVisible(false);
    }

    @UiHandler("usernameTextBox")
    protected void onUser(KeyPressEvent e) {
        if (e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            new Timer() {
                public void run() {
                    passwordTextBox.setFocus(true);
                }
            }.schedule(100);
        }
    }

    @UiHandler("usernameTextBox")
    protected void onUser(ChangeEvent e) {
        activity.loadSettings();
    }

    @UiHandler("passwordTextBox")
    protected void onPassword(KeyDownEvent e) {
        if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            submitButton.click();
        }
    }

    @UiHandler("submitButton")
    protected void onSubmit(ClickEvent e) {
        System.out.println("ON sub");
        if (!usernameTextBox.getValue().isEmpty() &&
            !passwordTextBox.getValue().isEmpty()) {
            if (!settingsPopup.isShowing() &&
                    (imapServer.getValue().isEmpty()
                    || smtpServer.getValue().isEmpty()
                    || imapPort.getValue().isEmpty()
                    || smtpPort.getValue().isEmpty()
                    )) {
                       settingsPopup.showRelativeTo(setupButton);
            } else {
                setLoading(true);
                activity.doLogin();
            }
        }
    }

    @UiHandler("setupButton")
    protected void onSetup(ClickEvent e) {
        if (settingsPopup.isShowing()) {
            settingsPopup.hide();
        } else {
            settingsPopup.showRelativeTo(setupButton);
        }
    }

    @Override
    public HasValue<String> getUserNameValue() {
        return usernameTextBox;
    }

    @Override
    public HasValue<String> getPasswordValue() {
        return passwordTextBox;
    }

    @Override
    public void setLoading(boolean load) {
        if (load) {
            message.setVisible(true);
            submitButton.setEnabled(false);
        } else {
            message.setVisible(false);
            submitButton.setEnabled(true);
        }
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    interface LoginViewUiBinder extends UiBinder<FlowPanel, LoginView> {
    }

    private static LoginViewUiBinder binder = GWT.create(LoginViewUiBinder.class);

    interface Style extends CssResource {
        String imapSetting();
    }

    @Override
    public void setSettings(Settings s) {
        System.out.println("Settings " + settingsPopup.isShowing());
        imapServer.setValue(s.getImapServer());
        imapPort.setValue("" + (s.getImapPort() > 0 ? s.getImapPort() : ""));
        imapSecure.setValue(s.getImapSecure());
        smtpServer.setValue(s.getSmtpServer());
        smtpPort.setValue("" + (s.getSmtpPort() > 0 ? s.getSmtpPort() : ""));
        smtpSecure.setValue(s.getSmtpSecure());
    }

    @Override
    public Settings getSettings(Settings s) {
        s.setImapServer(imapServer.getValue());
        imapPort.setValue(imapPort.getValue().replaceAll("[^\\d]+", ""));
        if(imapPort.getValue().matches("\\d+"))
            s.setImapPort(Integer.valueOf(imapPort.getValue()));
        s.setImapSecure(imapSecure.getValue());
        s.setSmtpServer(smtpServer.getValue());
        smtpPort.setValue(smtpPort.getValue().replaceAll("[^\\d]+", ""));
        if(smtpPort.getValue().matches("\\d+"))
            s.setSmtpPort(Integer.valueOf(smtpPort.getValue()));
        s.setSmtpSecure(smtpSecure.getValue());
        return s;
    }

    @Override
    public void setActivity(LoginActivity loginActivity) {
        activity = loginActivity;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        new Timer() {public void run() {
            activity.loadSettings();
        }}.schedule(800);
    }
}
