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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

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
        return injector.getInstance(clazz);
    }

    static int count = 0;
    @Override
    public Object invoke(Method domainMethod, Object... args) {
        int n = count ++;
        long start = System.currentTimeMillis();
        boolean doLog = !"login".equals(domainMethod.getName());
        if (doLog) System.out.println(n + " >>>>>>>>> Invoking  RF "  + domainMethod.getDeclaringClass() + " >>" + domainMethod.getName() + " " + new ArrayList<Object>(Arrays.asList(args)));
        Object ret = null;
        try {
            ret =  super.invoke(domainMethod, args);
        } catch (Throwable e) {
            long l = System.currentTimeMillis() - start;
            e.printStackTrace();
            if (doLog) System.out.println(n + " << " + String.format("%6d", l) +" Returning RF ERROR "  + domainMethod.getDeclaringClass().getSimpleName() + " <<" + domainMethod.getName() + " " + new ArrayList<Object>(Arrays.asList(args)));
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        } finally {
            long l = System.currentTimeMillis() - start;
            if (doLog) System.out.println(n + " << " + String.format("%6d", l) +" Returning RF "  + domainMethod.getDeclaringClass().getSimpleName() + " <<" + domainMethod.getName() + " " + new ArrayList<Object>(Arrays.asList(args)));
        }
        return ret;
    }
}