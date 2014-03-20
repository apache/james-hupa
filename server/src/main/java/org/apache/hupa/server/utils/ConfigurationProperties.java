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

package org.apache.hupa.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Enumeration of valid configuration properties
 */
public enum ConfigurationProperties {
    // Mandatory configuration properties
    IMAP_SERVER_ADDRESS("IMAPServerAddress", true, null),
    SMTP_SERVER_ADDRESS("SMTPServerAddress", true, null),
    IMAP_SERVER_PORT("IMAPServerPort", false, "143"),
    SMTP_SERVER_PORT("SMTPServerPort", false, "25"),

    // Optional configuration properties
    IMAP_CONNECTION_POOL_SIZE("IMAPConnectionPoolSize", false, "4"),
    IMAP_CONNECTION_POOL_TIMEOUT("IMAPConnectionPoolTimeout", false, "300000"),
    IMAPS("IMAPS", false, "false"),
    TRUST_STORE("TrustStore", false, ""),
    TRUST_STORE_PASSWORD("TrustStorePassword", false, ""),
    DEFAULT_SENT_FOLDER("DefaultSentFolder", false, "\\Sent"),
    DEFAULT_TRASH_FOLDER("DefaultTrashFolder", false, "\\Trash"),
    DEFAULT_DRAFTS_FOLDER("DefaultDraftsFolder", false, "\\Drafts"),
    DEFAULT_INBOX_FOLDER("DefaultInboxFolder", false, "INBOX"),
    POST_FETCH_MESSAGE_COUNT("PostFetchMessageCount", false, "0"),
    SESSION_DEBUG("SessionDebug", false, "false"),
    SMTP_AUTH("SMTPAuth", false, "true"),
    SMTPS("SMTPS", false, "false"),
    TRUST_SSL("TrustSSL", false, "false"),

    // Used only in demo mode
    USERNAME("Username", false, null),
    PASSWORD("Password", false, null);

    private String property;
    private boolean mandatory;
    private String propValue;

    private ConfigurationProperties (String property, boolean mandatory, String propValue) {
        this.property = property;
        this.mandatory = mandatory;
        this.propValue = propValue;
    }

    /**
     * Return a ConfigurationProperties enumeration that matches the passed command.
     *
     * @param property
     *            The property to use for lookup.
     * @return the ConfigurationProperties enumeration that matches the passed property, or null
     *         if not found.
     */
    public static ConfigurationProperties lookup(String property) {
        if (property != null) {
            for (ConfigurationProperties confProp : values())
                if (confProp.getProperty().equalsIgnoreCase(property))
                    return confProp;
        }
        return null;
    }

    /**
     * Return the name of property.
     *
     * @return the name of property.
     */
    public String getProperty() {
        return this.property;
    }

    /**
     * Return if property is mandatory
     *
     * @return true if mandatory else false.
     */
    public boolean isMandatory() {
        return this.mandatory;
    }

    /**
     * Return the value of the property.
     *
     * @return the value of the property.
     */
    public String getPropValue() {
        return this.propValue;
    }

    /**
     * Test for mandatory properties, complete with default values when
     * missing, and avoid unknown properties.
     */
    public static void validateProperties(Properties properties) {

        List<String> errors = new ArrayList<String>();

        // Test for mandatory and complete properties with default values when
        // missing
        for (ConfigurationProperties confProps : ConfigurationProperties.values()) {
            String propName = confProps.getProperty();
            String propValue = confProps.getPropValue();
            Object value = properties.get(propName);
            if (confProps.isMandatory()) {
                if (value == null) {
                    errors.add("The mandatory Property '"
                            + confProps.getProperty() + "' is not set.");
                }
            } else if (value == null && propValue != null) {
                properties.setProperty(propName, propValue);
            }
        }

        // Test for unknown properties set in configuration
        for (Object key : properties.keySet()) {
            if (ConfigurationProperties.lookup((String) key) == null) {
                errors.add("The Property '" + key
                        + "' is unknown");
            }
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Error validating configuration : \n" + properties + "\n" +  errors.toString());
        }
    }

    /**
     * Loads and validate a properties file.
     *
     * @param configDir
     * @param name
     * @return
     */
    public static Properties loadProperties(String name) {
        if (name == null)
            return null;

        Properties properties = null;
        File file = new File(name);

        if (file.exists()) {
            FileInputStream fis = null;
            try {
                properties = new Properties();
                fis = new FileInputStream(file);
                properties.load(fis);
            } catch (Exception e) {
                properties = null;
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // Empty on purpose
                    }
                }
            }
        }

        return properties;
    }
}