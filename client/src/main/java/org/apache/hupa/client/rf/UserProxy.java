package org.apache.hupa.client.rf;



import org.apache.hupa.server.domain.User;
import org.apache.hupa.server.locator.UserLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = User.class, locator=UserLocator.class)
public interface UserProxy extends EntityProxy {
	public Long getId();
	public String getName();
	public void setName(String name);
	
	public String getPassword();
	public void setPassword(String password);
	
}
