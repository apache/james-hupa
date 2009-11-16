package org.apache.hupa.client.mvp;

import org.apache.hupa.shared.data.User;

import com.google.inject.Inject;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetContainerDisplay;
import net.customware.gwt.presenter.client.widget.WidgetContainerPresenter;

public class ContainerPresenter extends WidgetContainerPresenter<WidgetContainerDisplay>{

    private MainPresenter presenter;
    
    @Inject
    public ContainerPresenter(WidgetContainerDisplay display, EventBus eventBus, MainPresenter presenter, ContactsPresenter contactPresenter) {
        super(display, eventBus, presenter,contactPresenter);
        this.presenter = presenter;
        // TODO Auto-generated constructor stub
    }

    public void revealDisplay(User user) {
        revealDisplay();
        presenter.revealDisplay(user);
        
    }

}
