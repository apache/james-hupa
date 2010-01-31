package org.apache.hupa.client.validation;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.UIObject;

import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.actions.StyleAction;

/**
 * It's like StyleAction but with a timer to remove the style
 */
public class AddStyleAction extends StyleAction {
    
    Timer removeTimer = new Timer() {
        @Override
        public void run() {
            reset();
        }
    };
    
    int removePeriod = 0;
   
    public AddStyleAction(String add, int millisecs) {
        super(add);
        this.removePeriod = millisecs;
    }
    
    @Override
    public void invoke(ValidationResult result, UIObject object) {
        super.invoke(result, object);
        if (removePeriod > 0) {
            removeTimer.schedule(removePeriod);
        }
    }
    
}
