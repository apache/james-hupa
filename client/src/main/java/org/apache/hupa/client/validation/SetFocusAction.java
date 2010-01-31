package org.apache.hupa.client.validation;

import com.google.gwt.user.client.ui.Focusable;

import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;

/**
 * FocusAction uses FocusWidget instead of focusable, so it doesn't work
 * with customized widgets extending composite.
 * 
 */
public class SetFocusAction extends ValidationAction<Focusable> {
    @Override
    public void invoke(ValidationResult result, Focusable widget) {
        widget.setFocus(true);
    }
}
