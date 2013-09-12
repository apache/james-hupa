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

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchService;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.server.guice.GuiceDispatch;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.customware.gwt.dispatch.shared.Result;
import net.customware.gwt.presenter.client.DefaultEventBus;
import net.customware.gwt.presenter.client.Display;
import net.customware.gwt.presenter.client.EventBus;

import org.apache.hupa.client.HupaMessages;
import org.apache.hupa.client.activity.MessageSendActivity;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.MessageSendView;
import org.easymock.EasyMock;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

/**
 * Guice module used to test presenters
 * 
 * @author manolo
 *
 */
public class GuiceMvpTestModule extends AbstractModule {
    
    // Override either, to change module behavior
    protected DispatchAsync dispatchAsyncInstance = null;
    protected Class<? extends DispatchAsync> dispatchAsyncClass = DispatchTestAsync.class;
    
    @Override
    protected void configure() {
        if (dispatchAsyncInstance == null) {
            bind(DispatchAsync.class).to(dispatchAsyncClass).in(Singleton.class);
        } else {
            bind(DispatchAsync.class).toInstance(dispatchAsyncInstance);
        }

        bind(EventBus.class).to(DefaultEventBus.class);
        
        bind(DispatchTestAsync.class);
        
        HupaMessages messages = EasyMock.createNiceMock(HupaMessages.class);
        bind(HupaMessages.class).toInstance(messages);

//        bindDisplay(ContactsPresenter.Display.class);
        bind(MessageSendActivity.Displayable.class).to(MessageSendView.class);
        
        bind(com.google.gwt.event.shared.EventBus.class)
            .to(SimpleEventBus.class)
            .in(Singleton.class);
        bind(HupaRequestFactory.class)
            .toProvider(RequestFactoryProvider.class)
            .in(Singleton.class);
    }
    
    public static class RequestFactoryProvider implements Provider<HupaRequestFactory> {
        private static final com.google.gwt.event.shared.EventBus eventBus = new SimpleEventBus();
        public HupaRequestFactory get() {
            HupaRequestFactory rf = RequestFactorySource.create(HupaRequestFactory.class);
            ServiceLayer serviceLayer = ServiceLayer.create();
            SimpleRequestProcessor processor = new SimpleRequestProcessor(serviceLayer);
            rf.initialize(eventBus, new InProcessRequestTransport(processor));
            return rf;
        }
    }

    protected <D extends Display> void bindDisplay(final Class<D> display) {
        final D mockDisplay = EasyMock.createNiceMock(display);
        bind(display).toInstance(mockDisplay);
    }

    static class DispatchTestService implements StandardDispatchService {
        private Dispatch dispatch;

        @Inject
        public DispatchTestService(Dispatch dispatch) {
            this.dispatch = dispatch;
        }

        public Result execute(Action<?> action) throws DispatchException {
            Result result = dispatch.execute(action);
            return result;
        }
    }

    static public class DispatchTestAsync implements DispatchAsync {
        private GuiceDispatch dispatch;

        @Inject
        public DispatchTestAsync(Dispatch dispatch) {
            this.dispatch = (GuiceDispatch)dispatch;
        }

        public <A extends Action<R>, R extends Result> void execute(A action, AsyncCallback<R> callback) {
            try {
                R result = dispatch.execute(action);
                callback.onSuccess(result);
            } catch (DispatchException e) {
                callback.onFailure(e);
            }
        }
    }
}