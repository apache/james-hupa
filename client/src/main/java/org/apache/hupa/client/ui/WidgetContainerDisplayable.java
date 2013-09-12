package org.apache.hupa.client.ui;

import com.google.gwt.user.client.ui.Widget;

public interface WidgetContainerDisplayable extends WidgetDisplayable {

    void addWidget( Widget widget );

    void removeWidget( Widget widget );

    void showWidget( Widget widget );
}
