package org.apache.hupa.server.service;

import javax.servlet.http.HttpSession;

import org.apache.hupa.server.domain.User;
import org.apache.hupa.server.utils.SessionUtils;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public class UserDao {
	
	private HttpSession session;

	public User findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	User save(User user){
		System.out.println("+++++++");
		session = RequestFactoryServlet.getThreadLocalRequest().getSession();

        SessionUtils.cleanSessionAttributes(session);
        return user;
	}

}
