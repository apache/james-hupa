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

package org.apache.hupa.shared.data;

import java.io.Serializable;
import java.util.Date;

import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;

/**
 * User which will get used for login to the IMAP and SMTP account
 *
 *
 */
public class UserImpl implements User, Serializable {

    private static final long serialVersionUID = 7172612434659286225L;

    private String id;


    private String name;
    private String password;
    private Date loginDate;
    private boolean auth;
    private Settings settings;

    public String getId() {
        return id != null ? id : name;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * The name of the User
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get name of the User
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Password of the User
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the Password of the User
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if (object instanceof UserImpl) {
            if (((UserImpl) object).getName().equals(getName())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return getName().hashCode();
    }
    /**
     * Get the Date on which the User was logged in the last time
     *
     * @return loginDate
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * Set if the User was successful authenticated
     *
     * @param auth
     */
    public void setAuthenticated(boolean auth) {
        this.auth = auth;
        if (auth) {
            loginDate = new Date();
        }
    }

    /**
     * Get if the User was successful authenticated
     *
     * @return auth
     */
    public boolean getAuthenticated() {
        return auth;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }
}
