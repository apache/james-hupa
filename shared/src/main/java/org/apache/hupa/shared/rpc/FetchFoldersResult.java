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
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
import java.util.ArrayList;
=======
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
import java.util.List;

import net.customware.gwt.dispatch.shared.Result;
>>>>>>> first commit

import org.apache.hupa.shared.proxy.IMAPFolderProxy;

public class FetchFoldersResult implements Result, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6215610133650989605L;
<<<<<<< HEAD
<<<<<<< HEAD
    private List<IMAPFolderProxy> folders;

    public FetchFoldersResult(List<IMAPFolderProxy> folders) {
=======
    private List<IMAPFolder> folders;

    public FetchFoldersResult(List<IMAPFolder> folders) {
>>>>>>> first commit
=======
    private List<IMAPFolderProxy> folders;

    public FetchFoldersResult(List<IMAPFolderProxy> folders) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
        this.folders=folders;
    }
    
    @SuppressWarnings("unused")
    private FetchFoldersResult() {
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
    public List<IMAPFolderProxy> getFolders() {
=======
    public List<IMAPFolder> getFolders() {
>>>>>>> first commit
=======
    public List<IMAPFolderProxy> getFolders() {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
        return folders;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer("");
<<<<<<< HEAD
<<<<<<< HEAD
        for (IMAPFolderProxy folder : folders) {
            ret.append(folder.getFullName()).append("\n");
            for (IMAPFolderProxy f : folder.getChildIMAPFolders()) {
=======
        for (IMAPFolder folder : folders) {
            ret.append(folder.getFullName()).append("\n");
            for (IMAPFolder f : folder.getChildIMAPFolders()) {
>>>>>>> first commit
=======
        for (IMAPFolderProxy folder : folders) {
            ret.append(folder.getFullName()).append("\n");
<<<<<<< HEAD
            for (IMAPFolderProxy f : folder.getChildIMAPFolders()) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
            for (IMAPFolderProxy f : folder.getChildren()) {
>>>>>>> 
                childFolder(f, ret);
            }
        }
        return ret.toString();
    }
    
<<<<<<< HEAD
<<<<<<< HEAD
    private void childFolder(IMAPFolderProxy child, StringBuffer ret) {
        ret.append(child.getFullName()).append("\n");
        for (IMAPFolderProxy folder : child.getChildIMAPFolders()) {
=======
    private void childFolder(IMAPFolder child, StringBuffer ret) {
        ret.append(child.getFullName()).append("\n");
        for (IMAPFolder folder : child.getChildIMAPFolders()) {
>>>>>>> first commit
=======
    private void childFolder(IMAPFolderProxy child, StringBuffer ret) {
        ret.append(child.getFullName()).append("\n");
<<<<<<< HEAD
        for (IMAPFolderProxy folder : child.getChildIMAPFolders()) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
        for (IMAPFolderProxy folder : child.getChildren()) {
>>>>>>> 
            childFolder(folder, ret);
        }
    }
    
}
