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

package org.apache.hupa.client.bundles;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.TreeImages;

public interface IMAPTreeImages extends TreeImages {

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.TreeImages#treeOpen()
     */
    @Resource("folder_sub.png")
    AbstractImagePrototype treeOpen();

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.TreeImages#treeClosed()
     */
    @Resource("folder_add.png")
    AbstractImagePrototype treeClosed();

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.TreeImages#treeLeaf()
     */
    @Resource("folder.png")
    AbstractImagePrototype treeLeaf();

}
