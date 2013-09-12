package org.apache.hupa.client.rf;

import java.util.List;

<<<<<<< HEAD
<<<<<<< HEAD:client/src/main/java/org/apache/hupa/client/rf/IMAPFolderRequestContext.java
import org.apache.hupa.server.service.IMAPFolderService;
<<<<<<< HEAD
<<<<<<< HEAD
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
=======
>>>>>>> As the FetchFolders RequestFactory, but can not run correctly.
=======
import org.apache.hupa.shared.proxy.IMAPFolderProxy;
>>>>>>> Aim to make the front end view work after the server side's IMAPFolder services RF being working, but there are issues on RF's find* method, I think.
=======
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.shared.proxy.ImapFolder;
>>>>>>> Make the ValueProxy(ImapFolder) work with Manolo's patch. Hupa can display folders in west view with RequestFactory now.:client/src/main/java/org/apache/hupa/client/rf/ImapFolderRequestContext.java
=======
import org.apache.hupa.server.locator.ImapFolderServiceLocator;
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.shared.domain.ImapFolder;
>>>>>>> Allow client can use the domain entity interface.

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=ImapFolderService.class, locator=ImapFolderServiceLocator.class)

public interface ImapFolderRequestContext extends RequestContext {
	Request<List<ImapFolder>> requestFolders();
}
