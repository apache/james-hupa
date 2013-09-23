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

package org.apache.hupa.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public final class IocRfServiceDecorator extends ServiceLayerDecorator {
    @Inject
    private Injector injector;

    @Override
    public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    @Override
    public Object createServiceInstance(
            Class<? extends RequestContext> requestContext) {
        Class<? extends ServiceLocator> serviceLocatorClass;
        if ((serviceLocatorClass = getTop().resolveServiceLocator(
                requestContext)) != null) {
            return injector.getInstance(serviceLocatorClass).getInstance(
                    requestContext.getAnnotation(Service.class).value());
        } else {
            return null;
        }
    }

    @Override
    public <T> T createDomainObject(Class<T> clazz) {
        System.out.println("Create domain " + clazz);
        return injector.getInstance(clazz);
    }
}