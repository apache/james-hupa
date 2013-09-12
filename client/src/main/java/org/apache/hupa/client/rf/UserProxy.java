package org.apache.hupa.client.rf;

import org.apache.hupa.server.domain.User;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = User.class)
public interface UserProxy extends EntityProxy {

}
