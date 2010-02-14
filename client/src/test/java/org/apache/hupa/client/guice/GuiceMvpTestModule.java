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
package org.apache.hupa.client.guice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.service.DispatchService;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.Result;
import net.customware.gwt.presenter.client.Display;
import net.customware.gwt.presenter.client.EventBus;

import static org.easymock.EasyMock.createStrictMock;

import org.apache.hupa.client.mvp.ContactsPresenter;
import org.easymock.EasyMock;

/**
 * Guice module used to test the presenter
 * 
 * @author manolo
 *
 */
public class GuiceMvpTestModule extends AbstractModule {
    
    @Override
    protected void configure() {

        bind(DispatchService.class).to(DispatchTestService.class).in(Singleton.class);
        bind(DispatchAsync.class).to(DispatchTestAsync.class).in(Singleton.class);

        final EventBus eventBus = createStrictMock(EventBus.class);
        bind(EventBus.class).toInstance(eventBus);

        bindDisplay(ContactsPresenter.Display.class);
    }

    public <D extends Display> void bindDisplay(final Class<D> display) {
        final D mockDisplay = EasyMock.createNiceMock(display);
        bind(display).toInstance(mockDisplay);
    }

    static class DispatchTestService implements DispatchService {
        private Dispatch dispatch;

        @Inject
        public DispatchTestService(Dispatch dispatch) {
            this.dispatch = dispatch;
        }

        public Result execute(Action<?> action) throws Exception {
            Result result = dispatch.execute(action);
            return result;
        }
    }

    static class DispatchTestAsync implements DispatchAsync {
        private Dispatch dispatch;

        @Inject
        public DispatchTestAsync(Dispatch dispatch) {
            this.dispatch = dispatch;
        }

        public <A extends Action<R>, R extends Result> void execute(A action, AsyncCallback<R> callback) {
            try {
                R result = dispatch.execute(action);
                callback.onSuccess(result);
            } catch (ActionException e) {
                callback.onFailure(e);
            }
        }
    }
}