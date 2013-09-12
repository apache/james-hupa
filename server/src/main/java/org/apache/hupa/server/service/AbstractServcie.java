package org.apache.hupa.server.service;

import javax.servlet.http.HttpSession;

import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.User;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public abstract class AbstractServcie {

	protected User getUser() {
		return (User) getHttpSession().getAttribute(SConsts.USER_SESS_ATTR);
	}

	protected HttpSession getHttpSession() {
		return RequestFactoryServlet.getThreadLocalRequest().getSession();
	}
}
