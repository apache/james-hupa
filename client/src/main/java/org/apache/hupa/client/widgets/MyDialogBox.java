package org.apache.hupa.client.widgets;

import com.google.gwt.user.client.ui.DialogBox;

public class MyDialogBox extends DialogBox implements HasDialog{
    public static final String C_dialog = "hupa-dialog-box";
    public MyDialogBox () {
        super();
        super.addStyleName(C_dialog);
    }
    
}
