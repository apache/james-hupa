package org.apache.hupa.client.mock;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

import gwtupload.client.IFileInput;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.IUploadStatus.Status;

import java.util.Iterator;

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

    public void setValidExtensions(String[] ext) {
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

}
