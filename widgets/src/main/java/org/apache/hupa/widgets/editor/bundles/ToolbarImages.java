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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;




    /**
     * This {@link ClientBundle} is used for all the button icons. Using an image
     * bundle allows all of these images to be packed into a single image, which
     * saves a lot of HTTP requests, drastically improving startup time.
     */
    public interface ToolbarImages extends ClientBundle {

        ImageResource bold();

        ImageResource createLink();

        ImageResource hr();

        ImageResource indent();

        ImageResource insertImage();

        ImageResource italic();

        ImageResource justifyCenter();

        ImageResource justifyLeft();

        ImageResource justifyRight();

        ImageResource ol();

        ImageResource outdent();

        ImageResource removeFormat();

        ImageResource removeLink();

        ImageResource strikeThrough();

        ImageResource subscript();

        ImageResource superscript();

        ImageResource ul();

        ImageResource underline();

        ImageResource backColors();

        ImageResource foreColors();

        ImageResource fonts();

        ImageResource fontSizes();
    }

