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
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.hupa.server.mock.MockIMAPStore;
import org.apache.hupa.server.mock.MockSMTPTransport;
import org.junit.Before;
import org.junit.Test;

public class ServerModulTest {
    private String tmpDir = System.getProperty("java.io.tmpdir");
    private GuiceServerModule module = new GuiceServerModule(tmpDir);

    @Before
    public void setUp() {
        // create config directory
        File f = new File(tmpDir + File.separator + GuiceServerModule.CONF_DIR);
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
        fileName = tmpDir + File.separator + GuiceServerModule.CONF_DIR + File.separator + "foo2.properties";
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

        try {
            module.loadProperties(tmp.toString());
        } catch (IllegalArgumentException e) {
            // This must be thrown because of missing mandatory configuration properties
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException must be thrown because of missing mandatory configuration properties");
        }

        System.setProperty(GuiceServerModule.SYS_PROP_CONFIG_FILE, tmp.toString());
        try {
            module.loadProperties();
        } catch (IllegalArgumentException e) {
            // This must be thrown because of missing mandatory configuration properties
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException must be thrown because of missing mandatory configuration properties");
        }
        System.clearProperty(GuiceServerModule.SYS_PROP_CONFIG_FILE);

    }

    @Test
    public void testLoadDemoProperties() throws Exception {
        File tmp = File.createTempFile("foo", ".properties");
        tmp.deleteOnExit();
        Collection<String> lines = new ArrayList<String>();
        lines.add("IMAPServerAddress = " + MockSMTPTransport.MOCK_HOST);
        lines.add("IMAPServerPort = " + MockSMTPTransport.MOCK_PORT);
        lines.add("SMTPServerAddress = " + MockSMTPTransport.MOCK_HOST);
        lines.add("SMTPServerPort = " + MockSMTPTransport.MOCK_PORT);
        lines.add("DefaultInboxFolder = " + MockIMAPStore.MOCK_INBOX_FOLDER);
        lines.add("DefaultTrashFolder = " + MockIMAPStore.MOCK_TRASH_FOLDER);
        lines.add("DefaultSentFolder = " + MockIMAPStore.MOCK_SENT_FOLDER);
        lines.add("DefaultDraftsFolder = " + MockIMAPStore.MOCK_DRAFTS_FOLDER);
        FileUtils.writeLines(tmp, lines);

        System.setProperty(GuiceServerModule.SYS_PROP_CONFIG_FILE, tmp.getAbsolutePath());
        Properties p = module.loadProperties();
        Assert.assertNotNull(p);
        Assert.assertEquals(DemoModeConstants.mockSettings.getInboxFolderName(), p.get("DefaultInboxFolder"));
        Assert.assertEquals(DemoModeConstants.mockSettings.getTrashFolderName(), p.get("DefaultTrashFolder"));
        Assert.assertEquals(DemoModeConstants.mockSettings.getSentFolderName(), p.get("DefaultSentFolder"));
        
        System.clearProperty(GuiceServerModule.SYS_PROP_CONFIG_FILE);

    }

}
