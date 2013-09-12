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

import java.io.Serializable;
import java.util.ArrayList;
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.List;

<<<<<<< HEAD:shared/src/main/java/org/apache/hupa/shared/data/IMAPFolder.java
import org.apache.hupa.shared.rf.EntityBase;
=======
>>>>>>> first commit
=======
import java.util.List;

<<<<<<< HEAD
import javax.servlet.http.HttpSession;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
=======
import org.apache.hupa.shared.rf.EntityBase;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.:shared/src/main/java/org/apache/hupa/shared/data/ImapFolderImpl.java

/**
 * IMAPFolder
 * 
 */
<<<<<<< HEAD:shared/src/main/java/org/apache/hupa/shared/data/IMAPFolder.java
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
public class IMAPFolder extends EntityBase implements Serializable {
	
	private Long id;
	private Long version;
	

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	
=======
public class IMAPFolder implements Serializable {
>>>>>>> 

	/**
=======
public class IMAPFolder implements Serializable {
<<<<<<< HEAD

    /**
>>>>>>> first commit
=======
=======
public class IMAPFolder extends EntityBase implements Serializable {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
	
	private Long id;
	private Long version;
	

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	
=======
public class ImapFolderImpl implements ImapFolder {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.:shared/src/main/java/org/apache/hupa/shared/data/ImapFolderImpl.java

	/**
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
     * 
     */
    private static final long serialVersionUID = 2084188092060266479L;

<<<<<<< HEAD:shared/src/main/java/org/apache/hupa/shared/data/IMAPFolder.java
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    private List<IMAPFolder> childs = new ArrayList<IMAPFolder>();
=======
    private ArrayList<IMAPFolder> childs = new ArrayList<IMAPFolder>();
>>>>>>> first commit
=======
    private List<IMAPFolder> childs = new ArrayList<IMAPFolder>();
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
    private List<IMAPFolder> children = new ArrayList<IMAPFolder>();
>>>>>>> 
=======
    private List<ImapFolder> children = new ArrayList<ImapFolder>();
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.:shared/src/main/java/org/apache/hupa/shared/data/ImapFolderImpl.java
    private String fullName;
    private String delimiter;
    private int messageCount;
    private int unseenMessageCount;
    private boolean subscribed = false;

    public ImapFolderImpl() {
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
                return fParts[fParts.length - 1];
            }
        }
        return fullName;
    }

    /**
     * Set the child folders 
     * 
     * @param children
     */
<<<<<<< HEAD:shared/src/main/java/org/apache/hupa/shared/data/IMAPFolder.java
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public void setChildIMAPFolders(List<IMAPFolder> childs) {
=======
    public void setChildIMAPFolders(ArrayList<IMAPFolder> childs) {
>>>>>>> first commit
=======
    public void setChildIMAPFolders(List<IMAPFolder> childs) {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
        this.childs = childs;
=======
    public void setChildren(List<IMAPFolder> children) {
=======
    public void setChildren(List<ImapFolder> children) {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.:shared/src/main/java/org/apache/hupa/shared/data/ImapFolderImpl.java
        this.children = children;
>>>>>>> 
    }

    /**
     * Return the childs of this folder
     * 
     * @return childs
     */
<<<<<<< HEAD:shared/src/main/java/org/apache/hupa/shared/data/IMAPFolder.java
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    public List<IMAPFolder> getChildIMAPFolders() {
=======
    public ArrayList<IMAPFolder> getChildIMAPFolders() {
>>>>>>> first commit
=======
    public List<IMAPFolder> getChildIMAPFolders() {
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
        return childs;
=======
    public List<IMAPFolder> getChildren() {
=======
    public List<ImapFolder> getChildren() {
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.:shared/src/main/java/org/apache/hupa/shared/data/ImapFolderImpl.java
        return children;
>>>>>>> 
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
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> first commit
=======

>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
    
}
