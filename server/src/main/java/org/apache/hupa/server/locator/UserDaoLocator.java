package org.apache.hupa.server.locator;

import org.apache.hupa.server.service.UserDao;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class UserDaoLocator implements ServiceLocator {

	@Override
	public Object getInstance(Class<?> clazz) {
		return new UserDao();
	}

}
