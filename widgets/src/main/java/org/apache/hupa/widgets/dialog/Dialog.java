package org.apache.hupa.widgets.dialog;

import org.apache.hupa.widgets.WidgetsCSS;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class Dialog {

    private static PopupPanel alert;
    private static HTML msgContent;
    private static Button okButton;
    private static Button cancelButton;

    private static Command okCommand;
    private static Command cancelCommand;
    private static boolean confirm = false;

    public static void alert(Object msg) {
        alert(msg, null);
    }

    public static void alert(Object msg, Command callback) {
        dialog(msg, false,  null, null);
        okButton.setFocus(true);
    }

    public static void confirm(Object msg) {
        confirm(msg, null, null);
    }

    public static void confirm(Object msg, Command ok) {
        confirm(msg, ok, null);
    }

    public static void confirm(Object msg, Command ok, Command cancel) {
        dialog(msg, true,  ok, cancel);
        cancelButton.setFocus(true);
    }

    public static void dialog(Object msg, final boolean isConfirm, Command ok, Command cancel) {
        if (alert == null) {
            VerticalPanel alertGrid = new VerticalPanel();
            alert = new PopupPanel();
            alert.setWidget(alertGrid);
            alert.setStyleName(WidgetsCSS.C_hupa_dialog);
            alert.setModal(true);
            alert.setAutoHideEnabled(false);
            alert.setGlassEnabled(true);
            alertGrid.add(msgContent = new HTML());
            alertGrid.add(cancelButton = new Button("Cancel", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    alert.hide();
                    if (cancelCommand != null) {
                        cancelCommand.execute();
                    }
                }
            }));
            alertGrid.add(okButton = new Button("OK", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    alert.hide();
                    if (okCommand != null) {
                        okCommand.execute();
                    }
                }
            }));
            Element okp = okButton.getElement().getParentElement();
            okp.appendChild(cancelButton.getElement());
        }
        okCommand = ok;
        cancelCommand = cancel;
        confirm = isConfirm;
        msgContent.setHTML(String.valueOf(msg).replace("\n", "<br/>"));
        cancelButton.setVisible(confirm);
        alert.center();
    }
}
