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
import java.util.HashSet;
import java.util.Map;

import org.apache.hupa.shared.rpc.GetMessageDetails;

import net.customware.gwt.dispatch.client.ExceptionHandler;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Dispatcher which support caching of data in memory.
 * 
 * It also avoids simultaneous executions of the same action, which 
 * is very useful in development.
 * 
 */
public class CachingDispatchAsync extends StandardDispatchAsync {

    
    @Inject
    public CachingDispatchAsync(ExceptionHandler exceptionHandler) {
        super(exceptionHandler);
    }

    private Map<Action<Result>, Result> cache = new HashMap<Action<Result>, Result>();
    
    private HashSet<Class<?>> running = new HashSet<Class<?>>();

    @Override
    public <A extends Action<R>, R extends Result> void execute(final A action,
            final AsyncCallback<R> callback) {
        
        if (action instanceof GetMessageDetails) {
            executeWithCache(action, callback);
        } else {
            if (GWT.isProdMode()) {
                super.execute(action, callback);
            } else {
                executeOneRequestPerAction(action, callback);
            }
        }
    }
    
    /**
     * Avoid parallel executions of the same action
     */
    public <A extends Action<R>, R extends Result> void executeOneRequestPerAction (
            final A action, final AsyncCallback<R> callback) {

        final Class<?> clz = action.getClass();
        if (running.contains(clz)) {
            System.err.println("ATTENTION: avoiding a parallel execution of the action: " + action.getClass().getName());
            new RuntimeException().printStackTrace();
            
            return;
        } else {
            running.add(clz);
            super.execute(action, new AsyncCallback<R>() {
                public void onFailure(Throwable caught) {
                    running.remove(clz);
                    callback.onFailure(caught);
                }
                public void onSuccess(final R result) {
                    running.remove(clz);
                    callback.onSuccess(result);
                }
            });
         }
    }

    /**
     * If the Action was executed before it will get fetched from the cache
     */
    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> void executeWithCache(
            final A action, final AsyncCallback<R> callback) {
        Result r = cache.get(action);

        final Class<?> clz = action.getClass();
        if (running.contains(clz)) {
            System.out.println("Contanins " + clz);
            return;
        } else {
            System.out.println("new " + clz);
            running.add(clz);
        }
        
        if (r != null) {
            callback.onSuccess((R) r);
        } else {
            super.execute(action, new AsyncCallback<R>() {
                public void onFailure(Throwable caught) {
                    running.remove(clz);
                    callback.onFailure(caught);
                }

                public void onSuccess(R result) {
                    running.remove(clz);
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
