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

package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.GenericResult;

public class GenericResultImpl implements GenericResult {

    private String message = null;
    private boolean success = true;

    public GenericResultImpl() {
    }
    public GenericResultImpl(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;

    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public void setError(String message) {
        setMessage(message);
        setSuccess(false);
    }

}
