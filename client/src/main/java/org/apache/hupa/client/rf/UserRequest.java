package org.apache.hupa.client.rf;

import org.apache.hupa.server.domain.User;

import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(User.class)
public interface UserRequest extends RequestContext {

}
