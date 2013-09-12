package org.apache.hupa.client.rf;

import org.apache.hupa.server.locator.UserDaoLocator;
import org.apache.hupa.server.service.UserDao;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = UserDao.class, locator=UserDaoLocator.class)
public interface UserRequest extends RequestContext {
	Request<UserProxy> findById(Long id);
	Request<UserProxy> save(UserProxy user);
	
}
