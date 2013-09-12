package org.apache.hupa.client.ui;

import org.apache.hupa.shared.domain.ImapFolder;

public class LabelNode implements Comparable<LabelNode> {
<<<<<<< HEAD
	
	public static LabelNode ROOT = new LabelNode("---"); 
	
	private ImapFolder folder;
	private String name;
	private String nameForDisplay;
	private String path;
	private LabelNode parent;
	
	public LabelNode(){}
	public LabelNode(String name){this.name = name;}

	
	public String getNameForDisplay() {
		return nameForDisplay;
	}
	public void setNameForDisplay(String nameForDisplay) {
		this.nameForDisplay = nameForDisplay;
	}
=======
	private ImapFolder folder;
	private String name;
	private String path;
	private LabelNode parent;

>>>>>>> add rename RF to label setting feature
	public ImapFolder getFolder() {
		return folder;
	}
	public void setFolder(ImapFolder folder) {
		this.folder = folder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public LabelNode getParent() {
		return parent;
	}
	public void setParent(LabelNode parent) {
		this.parent = parent;
	}
	@Override
<<<<<<< HEAD
	public int compareTo(LabelNode o) {
		if(name == null) return -1;
		if(o == null) return 1;
		return name.compareTo(o.name);
=======
	public int compareTo(LabelNode arg0) {
		// TODO Auto-generated method stub
		return 0;
>>>>>>> add rename RF to label setting feature
	}

}
