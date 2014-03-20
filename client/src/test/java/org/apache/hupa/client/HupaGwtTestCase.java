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

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Base class for testing hupa in client side.
 *
 * @author manolo
 *
 */
public abstract class HupaGwtTestCase extends GWTTestCase {

    /**
     * Although tests extending this class should work in either the jvm or the browser,
     * it is better to run them in the jvm because of performance reasons.
     *
     * Change the return value if you what to run them in browser, but
     * be sure to commit this class returning null
     *
     *   TODO: put some code to return the adequate value based on an external
     *   property. System.getProperty doesn't work because the test
     *   is compiled to javascript when not returning null
     */
    public String getModuleName() {
        // return "org.apache.hupa.Hupa";
        return null;
    }
}
