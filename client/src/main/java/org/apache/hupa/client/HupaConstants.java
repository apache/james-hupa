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

package org.apache.hupa.client;

import gwtupload.client.IUploader.UploaderConstants;

import org.apache.hupa.widgets.PagingOptionsConstants;
import org.apache.hupa.widgets.editor.ToolbarConstants;

import com.google.gwt.i18n.client.Constants;

public interface HupaConstants extends Constants, UploaderConstants, PagingOptionsConstants, ToolbarConstants {

    public String usernameLabel();

    public String passwordLabel();

    public String saveLoginLabel();

    public String loginButton();

    public String resetButton();

    public String logoutButton();

    public String deleteMailButton();

    public String newMailButton();

    public String replyMailButton();

    public String replyAllMailButton();

    public String headerTo();

    public String headerCc();

    public String headerSubject();

    public String headerReceivedDate();

    public String productName();

    public String headerFrom();
    public String mailTableFrom();
    public String mailTableSubject();
    public String mailTableDate();

    public String loginInvalid();

    public String searchButton();

    public String emptyMailTable();

    public String attachments();

    public String headerBcc();

    public String sendButton();

    public String forwardMailButton();

    public String loading();

    public String okButton();

    public String cancelButton();

    public String select();

    public String all();

    public String none();

    public String newFolder();
    public String renameFolder();
    public String deleteFolder();


    public String loginAs();

    public String backButton();

    public String rawButton();

    public String sessionTimedOut();
    public String rawTitle();

    public String deleteAll();

    public String markSeen();
    public String markUnseen();

    public String refresh();

    public String contactsTab();

    public String mailTab();

    public String welcome();

    public String footer();

}
