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

package org.apache.hupa.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;

import com.google.inject.Inject;

public class FileItemRegistry {

    public Map<String,FileItem> map = new HashMap<String, FileItem>();
    private Log logger;
    static int idCounter = 0;
    int registryId;

    public String toString() {
        return "registryId=" + registryId +
               " nItems=" + map.size();
    }

    @Inject
    public FileItemRegistry(Log logger) {
        this.logger = logger;
        registryId  = idCounter++;
    }

    public void add(FileItem item) {
        logger.debug("Store item " + item.getName() + " with name " + item.getFieldName());
        map.put(item.getFieldName(), item);
    }

    public void remove(String name) {
        remove(get(name));
    }

    public void remove(FileItem item) {
        if (item != null) {
            logger.debug("Remove item " + item.getName() + " with name " + item.getFieldName());
            map.remove(item.getFieldName());
            // Remove temporary stuff
            item.delete();
        }
    }

    public void clear() {
        for (Entry<String,FileItem> e: map.entrySet())
            remove(e.getValue());
    }

    public FileItem get(String name) {
        logger.debug("Retrieve item " + name + " isNull=" + (map.get(name) == null));
        return map.get(name);
    }

    public int size() {
        return map.size();
    }
}
