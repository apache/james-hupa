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

package org.apache.hupa.client.bundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

public interface HupaResources extends ClientBundle {

	public static final HupaResources INSTANCE = GWT
			.create(HupaResources.class);

	@NotStrict
	@Source("styles.css")
	public Css stylesheet();
	
    @Source("hupa-logo-49-transparent.png")
    ImageResource logo49PNG();
    @Source("hupa-logo-64-transparent.png")
    ImageResource logo64PNG();

	public interface Css extends CssResource {
		String loginForm();
		String boxInner();
		String tdTitle();
		String tdInput();
		String pFormbuttons();
		String submitButton();
		String boxBottom();
		String messageBox();
		String bottomLine();
	}

}
