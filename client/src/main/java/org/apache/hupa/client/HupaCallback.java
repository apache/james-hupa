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

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.Display;
import net.customware.gwt.presenter.client.EventBus;

import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.ServerStatusEvent;
import org.apache.hupa.shared.events.ServerStatusEvent.ServerStatus;
import org.apache.hupa.shared.exception.InvalidSessionException;
import org.apache.hupa.shared.rpc.CheckSession;
import org.apache.hupa.shared.rpc.CheckSessionResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public abstract class HupaCallback<T> implements AsyncCallback<T> {
    
    private DispatchAsync dispatcher = null;
    private EventBus eventBus = null;
    private ServerStatusEvent available = new ServerStatusEvent(ServerStatus.Available); 
    private ServerStatusEvent unavailable = new ServerStatusEvent(ServerStatus.Unavailable); 

    @SuppressWarnings("unused")
    private Display display = null;

    @Inject
    public HupaCallback(DispatchAsync dispatcher, EventBus bus, Display display) {
        this(dispatcher, bus);
        this.display = display;
        
        //this.display.startProcessing();
    }
    
    @Inject
    public HupaCallback(DispatchAsync dispatcher, EventBus bus) {
        this.dispatcher = dispatcher;
        this.eventBus = bus;
    }

    /**
     * If you override this method, remember to call super.onFailure() 
     */
    public void onFailure(final Throwable originalCaught) {
        // Server's response is invalid due to:
        // server unaccessible, session error or server exception
        dispatcher.execute(new CheckSession(), new AsyncCallback<CheckSessionResult>() {
            public void onFailure(Throwable caught) {
                if (caught instanceof InvalidSessionException) {
                    eventBus.fireEvent(new LogoutEvent(null));
                } else {
                    // The server is unaccessible
                    eventBus.fireEvent(unavailable);
                }
                finish();
            }
            public void onSuccess(CheckSessionResult result) {
                if (!result.isValid()) {
                    // Server's connection is fine, but the user has not a valid session
                    eventBus.fireEvent(new LogoutEvent(null));
                } else {
                    // Server's connection is fine, and the user has a valid session
                    // So the original action failed because a server's exception 
                    eventBus.fireEvent(available);
                }
                finish();
            }

            private void finish() {
                callbackError(originalCaught);
                //if (display != null)
                    //display.stopProcessing();
            }
        });
    }
    
    /**
     * If you override this method, remember to call super.onSuccess() 
     */
    public void onSuccess(T result) {
        // Server's response is valid,
        eventBus.fireEvent(available);
        // Execute the original method
        callback(result);
        // If display is being used, stop it
        //if (display != null)
            //display.stopProcessing();
    }
    
    /**
     * The callback code which the user has to implement
     */
    public abstract void callback(T result); 

    /**
     * The callback code in the case of error
     * Override this method, if you need this feature.
     */
    public void callbackError(Throwable caught) {
        System.out.println("HupaCallBack Error: " + caught);
    }
}
