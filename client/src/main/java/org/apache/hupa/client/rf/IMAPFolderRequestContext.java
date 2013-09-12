package org.apache.hupa.client.rf;

import java.util.List;

import org.apache.hupa.server.service.IMAPFolderService;
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(IMAPFolderService.class)
public interface IMAPFolderRequestContext extends RequestContext {
	Request<List<IMAPFolderProxy>> requestFolders();
	Request<String> echo(String s);
}
