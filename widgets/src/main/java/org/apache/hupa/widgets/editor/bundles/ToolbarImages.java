package org.apache.hupa.widgets.editor.bundles;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;




    /**
     * This {@link ImageBundle} is used for all the button icons. Using an image
     * bundle allows all of these images to be packed into a single image, which
     * saves a lot of HTTP requests, drastically improving startup time.
     */
    public interface ToolbarImages extends ImageBundle {

        AbstractImagePrototype bold();

        AbstractImagePrototype createLink();

        AbstractImagePrototype hr();

        AbstractImagePrototype indent();

        AbstractImagePrototype insertImage();

        AbstractImagePrototype italic();

        AbstractImagePrototype justifyCenter();

        AbstractImagePrototype justifyLeft();

        AbstractImagePrototype justifyRight();

        AbstractImagePrototype ol();

        AbstractImagePrototype outdent();

        AbstractImagePrototype removeFormat();

        AbstractImagePrototype removeLink();

        AbstractImagePrototype strikeThrough();

        AbstractImagePrototype subscript();

        AbstractImagePrototype superscript();

        AbstractImagePrototype ul();

        AbstractImagePrototype underline();
        
        AbstractImagePrototype backColors();
        
        AbstractImagePrototype foreColors();
        
        AbstractImagePrototype fonts();
        
        AbstractImagePrototype fontSizes();
    }

