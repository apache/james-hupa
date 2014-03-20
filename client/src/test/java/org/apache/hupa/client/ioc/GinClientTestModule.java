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

package org.apache.hupa.client.ioc;

import java.util.logging.Logger;

import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.server.ioc.IocRfServiceDecorator;
import org.apache.hupa.shared.storage.AppCache;
import org.apache.hupa.shared.storage.AppCacheMemory;
import org.apache.hupa.shared.storage.AppSerializer;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

public class GinClientTestModule extends AbstractModule {

    public static Logger logger = Logger.getLogger(GinClientTestModule.class.getName());

    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(RequestFactory.class).to(HupaRequestFactory.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    HupaRequestFactory getRequestFactory(EventBus eventBus, IocRfServiceDecorator decorator) {
        HupaRequestFactory requestFactory = RequestFactorySource.create(HupaRequestFactory.class);
        SimpleRequestProcessor processor = new SimpleRequestProcessor(ServiceLayer.create(decorator));
        processor.setExceptionHandler(new ExceptionHandler() {
            public ServerFailure createServerFailure(Throwable throwable) {
              throwable.printStackTrace();
              return null;
            }
          });
        requestFactory.initialize(eventBus, new InProcessRequestTransport(processor));
        return requestFactory;
    }

    @Provides
    @Singleton
    AutoBeanFactory getAutoBeanFactory() {
        return AutoBeanFactorySource.create(AutoBeanFactory.class);
    }

    @Provides
    @Singleton
    AppCache getAppCache(AppSerializer serializer) {
        return new AppCacheMemory(serializer);
    }
}
