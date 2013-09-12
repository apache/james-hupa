package org.apache.hupa.server.locator;

import org.apache.hupa.server.domain.User;
import org.apache.hupa.server.service.UserDao;

import com.google.web.bindery.requestfactory.shared.Locator;

public class UserLocator extends Locator<User, Long>{

	@Override
	public User create(Class<? extends User> clazz) {
		return new User();
	}

	@Override
	public User find(Class<? extends User> clazz, Long id) {
		return new UserDao().findById(id);
	}

	@Override
	public Class<User> getDomainType() {
		return User.class;
	}

	@Override
	public Long getId(User domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Object getVersion(User domainObject) {
		return domainObject.getVersion();
	}

}
