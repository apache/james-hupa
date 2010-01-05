package org.apache.hupa.client.widgets;

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.widgets.ui.Loading;
import org.cobogw.gwt.user.client.ui.Button;
import org.cobogw.gwt.user.client.ui.ButtonBar;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel with three parts, 
 * left has a button bar
 * right has a bunch of links 
 * center has a loading widget
 */
public class CommandsBar extends Composite {
    
    Grid buttonPanel = new Grid(1, 3);
    ButtonBar buttonBarLeft = new ButtonBar();
    ButtonBar buttonBarRight = new ButtonBar();
    
    public CommandsBar() {
        
        buttonPanel.addStyleName(HupaCSS.C_commands_bar);
        buttonBarLeft.addStyleName(HupaCSS.C_buttons);
        buttonBarRight.addStyleName(HupaCSS.C_buttons);

        buttonPanel.setWidget(0, 0, buttonBarLeft);
        buttonPanel.setWidget(0, 2, buttonBarRight);
        
        buttonPanel.getCellFormatter().setWidth(0, 1, "100%"); 
        buttonPanel.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER); 
        buttonPanel.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT); 

        initWidget(buttonPanel);
    }
    
    public void add(Widget w) {
        if (w instanceof Button) {
            addLeft(w);
        } else if ( w instanceof Loading) {
            buttonPanel.setWidget(0, 1, w);
        } else {
            addRight(w);
        }
    }
    
    public void addLeft(Widget w) {
        buttonBarLeft.add(w);
    }

    public void addRight(Widget w) {
        buttonBarRight.add(w);
    }

    public void clear() {
        buttonBarLeft.clear();
        buttonBarRight.clear();
        buttonPanel.setText(0, 1, "");
    }

    public boolean remove(Widget w) {
        return false;
    }
    
    
    
}
