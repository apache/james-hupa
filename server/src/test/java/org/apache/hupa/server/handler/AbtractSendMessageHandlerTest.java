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

package org.apache.hupa.server.handler;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.shared.rpc.SendMessage;

public class AbtractSendMessageHandlerTest extends AbstractHandlerTest {
    
    
    FileItemRegistry registry = null;
    
    AbstractSendMessageHandler<SendMessage> handler = new AbstractSendMessageHandler<SendMessage>(logger, registry, storeCache, httpSessionProvider, "demo-mode", 25, false, false){
        protected Message createMessage(Session session, SendMessage action) throws AddressException, MessagingException, ActionException {
            return null;
        }
        public Class<SendMessage> getActionType() {
            return null;
        }
    };

    public void testHtmlmessageToText() {
        String txt, res;
        txt = "<div>Hola:</div>Como \n estas<br/>Adios\n\n";
        res = handler.htmlToText(txt);
        assertEquals("Hola:\nComo estas\nAdios ", res);
    }

}
