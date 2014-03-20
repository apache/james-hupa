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

import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.SendMessageAction;

public class SendMessageActionImpl implements SendMessageAction{

    private SmtpMessage message;

    public SendMessageActionImpl(SmtpMessage msg) {
        this.message = msg;
    }

    protected SendMessageActionImpl() {

    }

    public SmtpMessage getMessage() {
        return message;
    }

    public void setMessage(SmtpMessage message) {
        this.message = message;
    }

    public String getInReplyTo() {
        return null;
    }

    public String getReferences() {
        return null;
    }

    @Override
    public void setInReplyTo(String inReplyTo) {
    }

    @Override
    public void setReferences(String references) {
    }

}
