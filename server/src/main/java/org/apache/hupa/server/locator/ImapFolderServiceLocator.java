package org.apache.hupa.server.locator;

import org.apache.hupa.server.service.ImapFolderServiceImpl;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class ImapFolderServiceLocator implements ServiceLocator {

	@Override
	public Object getInstance(Class<?> clazz) {
		return new ImapFolderServiceImpl();
	}

}
