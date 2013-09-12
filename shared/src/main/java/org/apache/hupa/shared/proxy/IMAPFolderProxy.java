package org.apache.hupa.shared.proxy;

import java.util.List;

import org.apache.hupa.shared.data.IMAPFolder;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=IMAPFolder.class)
public interface IMAPFolderProxy extends AbstractEntityProxy {

	int getUnseenMessageCount();

	String getName();

	String getFullName();

	void setFullName(String oldFullName);

	List<IMAPFolderProxy> getChildren();
	void setChildren(List<IMAPFolderProxy> children);

	void setUnseenMessageCount(int count);

	void setMessageCount(int realCount);

	String getDelimiter();

	void setDelimiter(String delimiter);

	void setSubscribed(boolean subscribed);
	
}
