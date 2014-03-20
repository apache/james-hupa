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

package org.apache.hupa.client;

import org.apache.hupa.client.ioc.AppGinModule.AppGinjector;
import org.apache.hupa.client.ioc.AppInjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;

public class Hupa implements EntryPoint {

    @Override
    public void onModuleLoad() {
        handleExceptionsAsync();
        initApp();
    }

    protected void initApp() {
        DOM.getElementById("loading").removeFromParent();
        createInjector().getHupaController();
        bindEvents();
    }

    protected void bindEvents() {
    }

    protected AppInjector createInjector() {
        return GWT.create(AppGinjector.class);
    }

    protected void handleExceptionsAsync() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(Throwable e) {
                e.printStackTrace();
                GQuery.console.log(stackTraceToString(e));
            }
        });
    }

    private String stackTraceToString(Throwable throwable) {
        String ret = "";
        while (throwable != null) {
            if (ret != "")
                ret += "\nCaused by: ";
            ret += throwable.toString();
            for (StackTraceElement sTE : throwable.getStackTrace())
                ret += "\n  at " + sTE;
            throwable = throwable.getCause();
        }
        return ret;
    }
}
