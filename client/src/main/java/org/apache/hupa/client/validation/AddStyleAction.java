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
