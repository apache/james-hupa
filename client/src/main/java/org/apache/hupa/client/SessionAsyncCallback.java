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

import net.customware.gwt.presenter.client.EventBus;

import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.exception.InvalidSessionException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * AsyncCallback which wraps an other AsyncCallback and checks if an InvalidSessionException was thrown. If so
 * it will fire an InvalidSessionEvent, if not it will just call the wrapped AsyncCallback
 */
public class SessionAsyncCallback<T> implements AsyncCallback<T>{

	private EventBus bus;
	private User user;
	private AsyncCallback<T> callBack; 
	
	public SessionAsyncCallback(AsyncCallback<T> callBack, EventBus bus,User user) {
		this.callBack = callBack;
		this.bus = bus;
		this.user = user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
	 */
	public void onFailure(Throwable caught) {
		if (caught instanceof InvalidSessionException) {
			bus.fireEvent(new SessionExpireEvent(user));
		} else {
			callBack.onFailure(caught);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
	 */
	public void onSuccess(T result) {
		callBack.onSuccess(result);
	}

}
