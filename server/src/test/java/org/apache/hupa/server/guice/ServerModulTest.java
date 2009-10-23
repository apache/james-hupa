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
package org.apache.hupa.server.guice;

import java.io.File;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ServerModulTest {
    private String tmpDir = System.getProperty("java.io.tmpdir");
    private ServerModul module = new ServerModul(tmpDir);

    @Before
    public void setUp() {
        // create config directory
        File f = new File(tmpDir + File.separator + ServerModul.CONF_DIR);
        f.delete();
        f.deleteOnExit();
        f.mkdirs();
    }
    
    @Test
    public void testLoadProperties() throws Exception {

        String fileName = tmpDir + File.separator +"foo.properties";
        File file = new File(fileName);
        file.createNewFile();
        Properties p = module.loadProperties(fileName);
        Assert.assertNotNull(p);
        Assert.assertNull(p.get("IMAPServerAddress"));
        file.delete();
        
        // load file from not absolute file
        fileName = tmpDir + File.separator + ServerModul.CONF_DIR + File.separator + "foo2.properties";
        file = new File(fileName);
        file.createNewFile();
        p = module.loadProperties(file.getName());
        Assert.assertNotNull(p);
        Assert.assertNull(p.get("IMAPServerAddress"));
        file.delete();
    }

    @Test
    public void testLoadPropertiesWithEmptyFile() throws Exception {
        File tmp = File.createTempFile("foo", ".properties");
        tmp.deleteOnExit();

        Properties p = module.loadProperties(tmp.toString());
        Assert.assertNotNull(p);
        Assert.assertNull(p.get("IMAPServerAddress"));

        System.setProperty(ServerModul.SYS_PROP_CONFIG_FILE, tmp.toString());
        p = module.loadProperties();
        Assert.assertNotNull(p);
        Assert.assertNull(p.get("IMAPServerAddress"));
        System.clearProperty(ServerModul.SYS_PROP_CONFIG_FILE);

    }

}
