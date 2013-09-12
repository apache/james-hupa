package org.apache.hupa.shared.domain;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = ImapFolder.class)
public interface ImapFolder extends ValueProxy {
	int getUnseenMessageCount();
	String getName();
	void setName(String name);
	String getFullName();
	void setFullName(String oldFullName);
	List<ImapFolder> getChildren();
	void setChildren(List<ImapFolder> children);
	void setUnseenMessageCount(int count);
	void setMessageCount(int realCount);
	int getMessageCount();
	String getDelimiter();
	void setDelimiter(String delimiter);
	void setSubscribed(boolean subscribed);
	boolean getSubscribed();
}
