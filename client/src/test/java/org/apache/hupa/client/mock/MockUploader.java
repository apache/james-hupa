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

package org.apache.hupa.client.mock;

import gwtupload.client.IFileInput;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;

import java.util.Iterator;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class MockUploader implements IUploader {

    public HandlerRegistration addOnCancelUploadHandler(OnCancelUploaderHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    public HandlerRegistration addOnChangeUploadHandler(OnChangeUploaderHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    public HandlerRegistration addOnFinishUploadHandler(OnFinishUploaderHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    public HandlerRegistration addOnStartUploadHandler(OnStartUploaderHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    public HandlerRegistration addOnStatusChangedHandler(OnStatusChangedHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    public void avoidRepeatFiles(boolean avoidRepeatFiles) {
        // TODO Auto-generated method stub

    }

    public void cancel() {
        // TODO Auto-generated method stub

    }

    public String fileUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getBasename() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getInputName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServerResponse() {
        // TODO Auto-generated method stub
        return null;
    }

    public Status getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    public void reset() {
        // TODO Auto-generated method stub

    }

    public void setFileInputPrefix(String prefix) {
        // TODO Auto-generated method stub
    }

    public void setI18Constants(UploaderConstants strs) {
        // TODO Auto-generated method stub
    }

    public void setServletPath(String path) {
        // TODO Auto-generated method stub
    }

    public void setStatusWidget(IUploadStatus status) {
        // TODO Auto-generated method stub
    }

    public void setValidExtensions(String... ext) {
        // TODO Auto-generated method stub
    }

    public void submit() {
        // TODO Auto-generated method stub
    }

    public JavaScriptObject getData() {
        // TODO Auto-generated method stub
        return null;
    }

    public void add(Widget w) {
        // TODO Auto-generated method stub
    }

    public void clear() {
        // TODO Auto-generated method stub
    }

    public Iterator<Widget> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean remove(Widget w) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setFileInputSize(int size) {
        // TODO Auto-generated method stub
    }

    public void setFileInput(gwtupload.client.IFileInput input) {
        // TODO Auto-generated method stub
    }

    public IFileInput getFileInput() {
        // TODO Auto-generated method stub
        return null;
    }

    public IUploadStatus getStatusWidget() {
        // TODO Auto-generated method stub
        return null;
    }

    public Widget getWidget() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setUploaded(UploadedInfo uinfo) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void add(Widget widget, int index) {
        // TODO Auto-generated method stub
    }

    @Override
    public UploadedInfo getServerInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAutoSubmit(boolean b) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setEnabled(boolean b) {
        // TODO Auto-generated method stub
    }
}
