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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.ioc.demo.DemoGuiceServerModule;
import org.apache.hupa.server.utils.ConfigurationProperties;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceListener extends GuiceServletContextListener {

    public static final String SYS_PROP_CONFIG_FILE = "hupa.config.file";
    public static final String CONFIG_FILE_NAME = "config.properties";
    public static final String CONFIG_DIR_IN_WAR = "WEB-INF/conf/";

    private String servletContextRealPath = "";

    protected Properties demoProperties = null;
    protected String demoHostName = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextRealPath = servletContextEvent.getServletContext().getRealPath("/");

        // We get the mock classes using reflection, so as we can package Hupa
        // without mock stuff.
        try {
            Class<?> mockConstants = Class.forName("org.apache.hupa.server.mock.MockConstants");
            demoProperties = (Properties) mockConstants.getField("mockProperties").get(null);
            demoHostName = demoProperties.getProperty("IMAPServerAddress");
        } catch (Exception noDemoAvailable) {
        }

        super.contextInitialized(servletContextEvent);
    }

    @Override
    protected Injector getInjector() {

        Properties prop = loadProperties();
        ConfigurationProperties.validateProperties(prop);

        boolean demo = prop.getProperty("IMAPServerAddress").equals(demoHostName);
        Module module = demo ? new DemoGuiceServerModule(prop) : new GuiceServerModule(prop);

        Injector injector = Guice.createInjector(module, new GuiceWebModule());

        String msg = ">> Started HUPA ";
        if (demo) {
            msg += "in DEMO-MODE";
        } else {
            msg += "with configuration -> " + prop;
        }
        injector.getInstance(Log.class).info(msg);
        return injector;
    }

    /**
     * Loads the first available configuration file.
     *
     * The preference order for the file is:
     * 1.- file specified in a system property (-Dhupa.config.file=full_path_to_file).
     * 2.- file in the user's home: $HOME/.hupa/config.properties.
     * 3.- global configuration in the os: /etc/default/hupa.
     * 4.- file provided in the .war distribution: "WEB-INF/conf/config.properties.
     * 5.- mock properties file which makes the Hupa work in demo mode.
     *
     * If the system property "mock-host" has been defined, and Hupa has been
     * packaged with the mock stuff, we always return the demo-mode
     * configuration.
     *
     */
    public Properties loadProperties() {
        Properties properties = null;
        if (demoHostName == null || System.getProperty(demoHostName) == null) {
            List<String> configurationList = new ArrayList<String>();
            configurationList.add(System.getProperty(SYS_PROP_CONFIG_FILE));
            configurationList.add(System.getenv("HOME") + "/.hupa/" + CONFIG_FILE_NAME);
            configurationList.add("/etc/default/hupa");
            configurationList.add(servletContextRealPath + "/" + CONFIG_DIR_IN_WAR + CONFIG_FILE_NAME);

            for (String name : configurationList) {
                System.out.println(name);
                properties = ConfigurationProperties.loadProperties(name);
                if (properties != null) {
                    break;
                }
            }
        }

        return properties == null ? demoProperties : properties;
    }
}
