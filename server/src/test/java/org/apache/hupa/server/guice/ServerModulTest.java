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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.hupa.server.ioc.GuiceListener;
import org.apache.hupa.server.mock.MockConstants;
import org.apache.hupa.server.utils.ConfigurationProperties;
import org.junit.Before;
import org.junit.Test;

public class ServerModulTest {
    private String tmpDir = System.getProperty("java.io.tmpdir");
//    private GuiceServerModule module = new GuiceServerModule(tmpDir);

    private String configDir = GuiceListener.CONFIG_DIR_IN_WAR;


    @Before
    public void setUp() {
        // create config directory
        File f = new File(tmpDir + File.separator + configDir);
        f.delete();
        f.deleteOnExit();
        f.mkdirs();
    }

    @Test
    public void testLoadProperties() throws Exception {
        String fileName = tmpDir + File.separator +"foo.properties";
        File file = new File(fileName);
        file.createNewFile();
        Properties p = ConfigurationProperties.loadProperties(fileName);
        Assert.assertNotNull(p);
        Assert.assertNull(p.get("IMAPServerAddress"));
        file.delete();
    }

    @Test
    public void testLoadPropertiesWithEmptyFile() throws Exception {
        GuiceListener sconfig = new GuiceListener();

        File tmp = File.createTempFile("foo", ".properties");
        tmp.deleteOnExit();

        try {
            ConfigurationProperties.loadProperties(tmp.toString());
        } catch (IllegalArgumentException e) {
            // This must be thrown because of missing mandatory configuration properties
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException must be thrown because of missing mandatory configuration properties");
        }

        System.setProperty(GuiceListener.SYS_PROP_CONFIG_FILE, tmp.toString());
        try {
            sconfig.loadProperties();
        } catch (IllegalArgumentException e) {
            // This must be thrown because of missing mandatory configuration properties
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException must be thrown because of missing mandatory configuration properties");
        }
        System.clearProperty(GuiceListener.SYS_PROP_CONFIG_FILE);
    }

    @Test
    public void testLoadDemoProperties() throws Exception {
        File tmp = File.createTempFile("foo", ".properties");
        tmp.deleteOnExit();
        Properties p = MockConstants.mockProperties;
        Collection<String> lines = new ArrayList<String>();
        for (Entry<Object, Object> e : p.entrySet()) {
            lines.add(e.getKey() + " = " + e.getValue());
        }
        FileUtils.writeLines(tmp, lines);

        System.setProperty(GuiceListener.SYS_PROP_CONFIG_FILE, tmp.getAbsolutePath());
        p = new GuiceListener().loadProperties();
        Assert.assertNotNull(p);
        Assert.assertEquals(MockConstants.mockSettings.getInboxFolderName(), p.get("DefaultInboxFolder"));
        Assert.assertEquals(MockConstants.mockSettings.getTrashFolderName(), p.get("DefaultTrashFolder"));
        Assert.assertEquals(MockConstants.mockSettings.getSentFolderName(), p.get("DefaultSentFolder"));
        System.clearProperty(GuiceListener.SYS_PROP_CONFIG_FILE);
    }

}
