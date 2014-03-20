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

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.FetchMessagesAction;

public class FetchMessagesActionImpl implements FetchMessagesAction {

    private ImapFolder folder;
    private int start;
    private int offset;
    private String searchString;

    public FetchMessagesActionImpl() {
    }

    @Override
    public String toString() {
        return "[" + folder.getFullName() + "," + start + "," + offset + "," + searchString + "]";
    }

    public FetchMessagesActionImpl(ImapFolder folder, int start, int offset, String searchString) {
        this.folder = folder;
        this.start = start;
        this.offset = offset;
        this.searchString = searchString;
    }

    @Override
    public ImapFolder getFolder() {
        return folder;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public String getSearchString() {
        return searchString;
    }

    @Override
    public void setFolder(ImapFolder folder) {
        this.folder = folder;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

}
