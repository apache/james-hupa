/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

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

