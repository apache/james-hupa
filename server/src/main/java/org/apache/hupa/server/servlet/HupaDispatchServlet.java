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

package org.apache.hupa.server.servlet;

import org.apache.commons.logging.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.server.guice.GuiceStandardDispatchServlet;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.Result;

/**
 * Just a wrapper for the Dispatcher servlet in order to log received actions. 
 *
 */
@Singleton
public class HupaDispatchServlet extends GuiceStandardDispatchServlet {
    
    private Log logger;
    
    @Inject
    public HupaDispatchServlet( Dispatch dispatch, Log logger) {
        super(dispatch);
        this.logger = logger;
    }

    
    @Override
    public Result execute( Action<?> action ) throws ActionException {
        try {
            logger.info("HupaDispatchServlet: executing: " + action.getClass().getName().replaceAll("^.*\\.",""));
            Result res = super.execute(action);
            logger.info("HupaDispatchServlet: finished: " + action.getClass().getName().replaceAll("^.*\\.",""));
            return res;
        } catch (ActionException e) {
            logger.error("HupaDispatchServlet returns an ActionException:" + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("HupaDispatchServlet unexpected Exception:" + e.getMessage());
            return null;
        }
    }

    private static final long serialVersionUID = 1L;

}
