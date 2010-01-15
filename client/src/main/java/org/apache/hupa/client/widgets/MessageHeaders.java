package org.apache.hupa.client.widgets;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;

import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel which displays the headers of a message.
 */
public class MessageHeaders extends Composite {
    
    private FlexTable detailGrid = new FlexTable();
    private String headerNames[];
    private int rowCounter = 0;
    
    public MessageHeaders (HupaConstants constants) {
       headerNames = new String[] {
                constants.headerFrom(),
                constants.headerTo(),
                constants.headerCc(),
                constants.headerBcc(),
                constants.headerSubject(),
                constants.attachments(),
                "nothing"
        };
        detailGrid.setWidth("100%");
        detailGrid.addStyleName(HupaCSS.C_msg_headers);
        initWidget(detailGrid);
    }
    
    public void setValues(Widget from, Widget to, Widget cc, Widget bcc, Widget subject, Widget attachments) {
       addValues(from, to, cc, bcc, subject, attachments);    
    }
    
    private void addValues(Widget...widgets) {
        detailGrid.clearAll();
        rowCounter = 0;
        for (int i=0; i<widgets.length; i++)
            addRow(headerNames[i], widgets[i]);
    }
    
    private void addRow(String name, Widget widget) {
        if (widget == null)
            return;
        
        if (widget instanceof TextBox || widget instanceof SuggestBox){
            widget.setWidth("100%");
        } else if (widget instanceof HasText) {
            if (((HasText)widget).getText().isEmpty())
                return;
        } else if (widget instanceof Panel) {
            if (((Panel)widget).iterator().hasNext() == false)
                return;
        }
        detailGrid.setText(rowCounter, 0, name + ":");
        detailGrid.setWidget(rowCounter, 1, widget);
        detailGrid.getCellFormatter().setStyleName(rowCounter, 0, "label");
        detailGrid.getCellFormatter().setStyleName(rowCounter, 1, "value");
        rowCounter ++;
    }
}
