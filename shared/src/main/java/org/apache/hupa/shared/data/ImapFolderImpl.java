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

import java.util.ArrayList;
import java.util.List;

import org.apache.hupa.shared.domain.ImapFolder;

public class ImapFolderImpl implements ImapFolder {

    private List<ImapFolder> children = new ArrayList<ImapFolder>();
    private String name;
    private String fullName;
    private String delimiter;
    private int messageCount;
    private int unseenMessageCount;
    private boolean subscribed = false;
    private boolean hasChildren = false;

    public ImapFolderImpl() {
    }

    public ImapFolderImpl(ImapFolder folder){
        this.delimiter = folder.getDelimiter();
        this.children = folder.getChildren();
        this.fullName = folder.getFullName();
        this.messageCount = folder.getMessageCount();
        this.name = folder.getName();
        this.subscribed = folder.getSubscribed();
        this.unseenMessageCount = folder.getUnseenMessageCount();
    }

    public ImapFolderImpl(String fullName) {
        setFullName(fullName);
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean getSubscribed() {
        return subscribed;
    }

    /**
     * Get the name of the folder
     *
     * @return name
     */
    public String getName() {
        if (delimiter != null) {
            String fParts[] = getFullName().split("\\" + delimiter);
            if (fParts != null && fParts.length > 0) {
                name = fParts[fParts.length - 1];
                return name;
            }
        }
        name = fullName;
        return name;
    }

    /**
     * Set the child folders
     *
     * @param children
     */
    public void setChildren(List<ImapFolder> children) {
        this.children = children;
    }

    /**
     * Return the childs of this folder
     *
     * @return childs
     */
    public List<ImapFolder> getChildren() {
        return children;
    }

    /**
     * Return the full name of the folder. This include the full path
     * @return Full name of the folder
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Set the full name of the folder
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Set the delimiter which is used to seperate folders
     *
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Return the delimiter
     *
     * @return delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Return the total message count of the messages that exists within this folder
     *
     * @return msgCount
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * Set total message count
     *
     * @param msgCount
     */
    public void setMessageCount(int msgCount) {
        this.messageCount = msgCount;
    }

    /**
     * Set the count of all unseen messages within this folder
     *
     * @param unseenMsgCount
     */
    public void setUnseenMessageCount(int unseenMsgCount) {
        this.unseenMessageCount = unseenMsgCount;
    }

    /**
     * Return the unseen message count
     *
     * @return unseenMsgCount
     */
    public int getUnseenMessageCount() {
        return unseenMessageCount;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImapFolder) {
            if (((ImapFolder) o).getFullName().equals(getFullName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getFullName().hashCode();
    }

    // FIXME Could not locate setter for property name in type ImapFolderImpl
    @Override
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void setFolderTo(ImapFolder folder) {
        folder.setChildren(this.children);
        folder.setDelimiter(this.delimiter);
        folder.setFullName(this.fullName);
        folder.setMessageCount(this.messageCount);
        folder.setName(this.name);
        folder.setSubscribed(this.subscribed);
        folder.setUnseenMessageCount(this.unseenMessageCount);
    }

    @Override
    public boolean getHasChildren() {
        return hasChildren;
    }

    @Override
    public void setHasChildren(boolean hasChildren){
        this.hasChildren = hasChildren;
    }
}
