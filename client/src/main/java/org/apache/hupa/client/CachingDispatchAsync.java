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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

/**
 * Dispatcher which support caching of data in memory
 * 
 */
public class CachingDispatchAsync implements DispatchAsync {

    private DispatchAsync dispatcher;
    private Map<Action<Result>, Result> cache = new HashMap<Action<Result>, Result>();

    @Inject
    public CachingDispatchAsync(DispatchAsync dispatcher) {
        this.dispatcher = dispatcher;
    }

    /*
     * (non-Javadoc)
     * @see net.customware.gwt.dispatch.client.DispatchAsync#execute(A, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    public <A extends Action<R>, R extends Result> void execute(final A action,
            final AsyncCallback<R> callback) {
        dispatcher.execute(action, callback);
    }

    /**
     * Execute the give Action. If the Action was executed before it will get fetched from the cache
     * 
     * @param <A> Action implementation
     * @param <R> Result implementation
     * @param action the action
     * @param callback the callback
     */
    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> void executeWithCache(
            final A action, final AsyncCallback<R> callback) {
        Result r = cache.get(action);
        if (r != null) {
            callback.onSuccess((R) r);
        } else {
            dispatcher.execute(action, new AsyncCallback<R>() {

                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void onSuccess(R result) {
                    cache.put((Action<Result>) action, (Result) result);
                    callback.onSuccess(result);
                }

            });
        }
    }

    /**
     * Clear the cache
     */
    public void clear() {
        cache.clear();
    }

}
