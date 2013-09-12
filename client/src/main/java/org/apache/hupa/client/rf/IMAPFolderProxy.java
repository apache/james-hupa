package org.apache.hupa.client.rf;

import java.util.List;

import org.apache.hupa.shared.data.IMAPFolder;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(IMAPFolder.class)
public interface IMAPFolderProxy extends EntityProxy {
//	List<IMAPFolder> requestFolders();
	Long getId();
	Long getVersion();
}
