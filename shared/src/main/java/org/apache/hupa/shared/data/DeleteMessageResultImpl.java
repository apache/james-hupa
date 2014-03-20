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

import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;

public class DeleteMessageResultImpl implements DeleteMessageResult {

    private User user;
    private ImapFolder folder;
    private int deleteCount;

    public DeleteMessageResultImpl() {
    }

    public DeleteMessageResultImpl(User user, ImapFolder folder, int deleteCount) {
        this.user = user;
        this.folder = folder;
        this.deleteCount = deleteCount;
    }
    @Override
    public int getCount() {
        return deleteCount;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public ImapFolder getFolder() {
        return folder;
    }

}
