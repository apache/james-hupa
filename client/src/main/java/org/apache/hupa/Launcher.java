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
package org.apache.hupa;

import java.net.InetSocketAddress;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * When hupa is packaged with jetty this class is called to
 * start jetty server.
 */
public final class Launcher {
   public static void main(String[] args) throws Exception {

       int port = Integer.parseInt(System.getProperty("port", "8282"));
      String bindAddress = System.getProperty("host", "0.0.0.0");

      InetSocketAddress a = new InetSocketAddress(bindAddress, port);
      Server server = new Server(a);

      ProtectionDomain domain = Launcher.class.getProtectionDomain();
      URL location = domain.getCodeSource().getLocation();
      WebAppContext webapp = new WebAppContext();
      webapp.setContextPath("/");
      webapp.setWar(location.toExternalForm());

      server.setHandler(webapp);
      server.start();
      server.join();
   }
}
