package org.apache.hupa.server.service;

import java.util.List;

import org.apache.hupa.shared.domain.ImapFolder;

public interface ImapFolderService {
	List<ImapFolder> requestFolders() throws Exception;
}
