package org.apache.hupa.client.mock;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;

import java.util.ArrayList;

public class MockWidget implements HasClickHandlers, HasText, HasHTML, Focusable {

    ArrayList<ClickHandler> handlers = new ArrayList<ClickHandler>();
    String text="", html="";

    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        handlers.add(handler);
        return new HandlerRegistration() {
            public void removeHandler() {
                handlers.remove(handler);
            }
        };
    }

    public void fireEvent(GwtEvent<?> event) {
        for(ClickHandler h: handlers) {
            h.onClick(null);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHTML() {
        return html;
    }

    public void setHTML(String html) {
        this.html = html;   
    }

    public int getTabIndex() {
        return 0;
    }

    public void setAccessKey(char key) {
    }

    public void setFocus(boolean focused) {
    }

    public void setTabIndex(int index) {
    }
}
