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

package org.apache.hupa.widgets.ui.impl;

import org.apache.hupa.widgets.WidgetsCSS;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * Simple generator of rounded panels using css.
 * It works in FF, safari, chrome and opera.
 *
 * It is needed to define this in your css.
 * <pre>
 *  div.hupa-rounded {
 *       border: 1px solid #7FAAFF;
 *       border-radius: 8px;
 *     }
 * </pre>
 *
 */
public class RndPanelGeneratorCss3 implements RndPanelGenerator {

    public Panel roundPanel(Panel panel) {
        panel.addStyleName(WidgetsCSS.C_hupa_rnd_container);
        return panel;
    }

    public FlowPanel createPanel() {
        return new FlowPanel() {
            @Override
            public void setStyleName(String style) {
                super.setStyleName(style);
                super.addStyleName(WidgetsCSS.C_hupa_rnd_container);
            }
        };
    }
}
