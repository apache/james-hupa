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

package org.apache.hupa.shared.rpc;

import java.io.Serializable;
import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Action;

import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.proxy.IMAPFolderProxy;

public class SetFlag implements Action<GenericResult>, Serializable {

    private static final long serialVersionUID = 662741801793895357L;
    private IMAPFlag flag;
    private ArrayList<Long> uids;
    private IMAPFolderProxy folder;
    private boolean value;
    
    public SetFlag(IMAPFolderProxy folder, IMAPFlag flag, boolean value, ArrayList<Long> uids) {
        this.flag = flag;
        this.value = value;
        this.uids = uids;
        this.folder = folder;
    }
    
    protected SetFlag() {
    }
    
    public IMAPFolderProxy getFolder() {
        return folder;
    }
    
    public boolean getValue() {
        return value;
    }
    public IMAPFlag getFlag() {
        return flag;
    }
    
    public ArrayList<Long> getUids() {
        return uids;
    }
}
