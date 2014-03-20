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

package org.apache.hupa.shared.domain;

import java.util.List;

import org.apache.hupa.shared.data.HasFullName;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = ImapFolder.class)
public interface ImapFolder extends ValueProxy, HasName, HasFullName {

    int getUnseenMessageCount();
    List<ImapFolder> getChildren();
    void setChildren(List<ImapFolder> children);
    void setUnseenMessageCount(int count);
    void setMessageCount(int realCount);
    int getMessageCount();
    String getDelimiter();
    void setDelimiter(String delimiter);
    void setSubscribed(boolean subscribed);
    boolean getSubscribed();

    boolean getHasChildren();
    void setHasChildren(boolean hasChildren);

    /**
     * use this to proxy the dumping method, or an alternative to clone, for the ValueProxy's must be set/get-ter
     * @param folder to be dumped
     */
    void setFolderTo(ImapFolder folder);
}
