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

import org.cobogw.gwt.user.client.ui.RoundedLinePanel;
import org.cobogw.gwt.user.client.ui.RoundedPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
/**
 * Generator of rounded panels using cobogw library.
 * 
 * It decorates the panel adding html elements because IE
 * doesn't support css rounded corners.
 * 
 * TODO: look for a way to make border color configurable in css
 *
 */
public class RndPanelGeneratorImplIE implements RndPanelGenerator  {

    static class MyRoundedLinePanel extends RoundedLinePanel {
        public MyRoundedLinePanel(int a, int b) {
            super(a,b);
        }
        public void addStyleName(String style){
            super.addStyleName(style);
            Element elem = super.getContainerElement();
            elem.setClassName(elem.getClassName() + " cgb-RPC-" + style);
        }
    };
    
    public FlowPanel createPanel() {
        return new FlowPanel();
    }

    public Widget roundPanel(Panel panel) {
        MyRoundedLinePanel rp = new MyRoundedLinePanel(RoundedPanel.ALL, 3);
        rp.setCornerColor("#7FAAFF", "");
        rp.setWidget(panel);
        return rp;
    }
    
}
