package org.apache.hupa.client.rf;

import java.util.List;

import org.apache.hupa.server.service.IMAPFolderService;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(IMAPFolderService.class)
public interface IMAPFolderRequestContext extends RequestContext {
	Request<List<IMAPFolderProxy>> requestFolders();
	Request<String> echo(String s);
}
