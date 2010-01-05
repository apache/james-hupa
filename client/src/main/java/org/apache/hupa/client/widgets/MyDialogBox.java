package org.apache.hupa.client.widgets;

import org.apache.hupa.client.HupaCSS;

import com.google.gwt.user.client.ui.DialogBox;

public class MyDialogBox extends DialogBox implements HasDialog{
    public MyDialogBox () {
        super();
        super.addStyleName(HupaCSS.C_dialog);
    }
    
}
