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

package org.apache.hupa.server.mock;

import org.apache.commons.logging.Log;

public class MockLog implements Log{

    public void debug(Object arg0) {
        // TODO Auto-generated method stub

    }

    public void debug(Object arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    public void error(Object arg0) {
        // TODO Auto-generated method stub

    }

    public void error(Object arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    public void fatal(Object arg0) {
        // TODO Auto-generated method stub

    }

    public void fatal(Object arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    public void info(Object arg0) {
        // TODO Auto-generated method stub

    }

    public void info(Object arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    public boolean isDebugEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isErrorEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isFatalEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isInfoEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isTraceEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isWarnEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    public void trace(Object arg0) {
        log(arg0);

    }

    public void trace(Object arg0, Throwable arg1) {
        log(arg0);

    }

    public void warn(Object arg0) {
        log(arg0);

    }

    public void warn(Object arg0, Throwable arg1) {
        log(arg0);
    }

    private void log(Object msg) {
        if (msg != null) {
            System.out.println(msg);
        } else {
            System.out.println("null-value");
        }
    }

}
