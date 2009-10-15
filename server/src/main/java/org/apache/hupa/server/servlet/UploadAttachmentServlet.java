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

package org.apache.hupa.server.servlet;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.hupa.server.FileItemRegistry;

import com.google.inject.Inject;

/**
 * Servlet which handle uploads. The uploaded files will get added to a temporary registry to get looked-up
 * later.
 * 
 *
 */
public class UploadAttachmentServlet extends UploadAction{

	private static final long serialVersionUID = 4936687307133529124L;
	private FileItemRegistry registry;
	
	@Inject
	public UploadAttachmentServlet(FileItemRegistry registry) {
		this.registry = registry;
	}
	
	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		logger.info("Executing Action, files in session: " + sessionFiles.size() + " files in registry: " + registry.size());
		// save file items in the registry
		for(FileItem item: sessionFiles) 
        	registry.add(item);
		
		// remove items from session but not remove the data from disk or memory
        removeSessionFileItems(request, false);
        return null;
	}
	
	@Override
	public void removeItem(HttpServletRequest request, FileItem item)  throws UploadActionException {
	   registry.remove(item);
	}
}
