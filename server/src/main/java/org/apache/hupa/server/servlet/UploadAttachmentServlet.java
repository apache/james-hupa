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

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.hupa.server.FileItemRegistry;

import com.google.inject.Inject;

import gwtupload.server.UploadServlet;

/**
 * Servlet which handle uploads. The uploaded files will get added to a temporary registry to get looked-up
 * later.
 * 
 *
 */
public class UploadAttachmentServlet extends UploadServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4936687307133529124L;
	private FileItemRegistry registry;
	
	@Inject
	public UploadAttachmentServlet(FileItemRegistry registry) {
		this.registry = registry;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
        Vector<FileItem> sessionFiles = (Vector<FileItem>) request.getSession().getAttribute(ATTR_FILES);
        
        // add fileItems to the registry
        for (int i =0; i < sessionFiles.size(); i++) {
        	registry.add(sessionFiles.get(i));
        	
        }
	}

	

}
