package org.apache.hupa.server.service;

import java.util.List;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.shared.domain.ImapFolder;

public interface ImapFolderService {
	List<ImapFolder> requestFolders() throws ActionException;
}
