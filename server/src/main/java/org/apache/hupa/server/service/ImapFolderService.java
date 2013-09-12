package org.apache.hupa.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.proxy.ImapFolder;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.sun.mail.imap.IMAPStore;

public class ImapFolderService {

	private static boolean useSSL = true;
	
	public static String echo(String s){
		return s;
	}
	
	public static List<ImapFolder> requestFolders() throws MessagingException, ActionException{
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		Session mailSession = Session.getDefaultInstance(new Properties(), null);
		IMAPStore store = (IMAPStore)mailSession.getStore(useSSL ? "imaps" : "imap");

        User user = (User) session.getAttribute(SConsts.USER_SESS_ATTR);
		store.connect("imap.gmail.com", 993, user.getName(), user.getPassword());
		

        com.sun.mail.imap.IMAPFolder folder = (com.sun.mail.imap.IMAPFolder) store.getDefaultFolder();

        // List of mail 'root' imap folders
        List<ImapFolder> imapFolders = new ArrayList<ImapFolder>();

        // Create IMAPFolder tree list
        for (Folder f : folder.list()) {
        	ImapFolder imapFolder = createIMAPFolder(f);
            imapFolders.add(imapFolder);
            walkFolders(f, imapFolder);
        }
		return imapFolders;
	}

    /**
     * Walk through the folder's sub-folders and add sub-folders to current imapFolder
     *   
     * @param folder Folder to walk
     * @param imapFolder Current IMAPFolder
     * @throws ActionException If an error occurs
     * @throws MessagingException If an error occurs
     */
    private static void walkFolders(Folder folder, ImapFolder imapFolder) throws  MessagingException{
        for (Folder f : folder.list()) {
        	ImapFolder iFolder = createIMAPFolder(f);
            imapFolder.getChildren().add(iFolder);
            walkFolders(f, iFolder);
        }
    }

    /**
     * Create a new IMAPFolder from the given Folder
     * 
     * @param folder Current folder
     * @return imapFolder Created IMAPFolder
     * @throws ActionException If an error occurs
     * @throws MessagingException If an error occurs
     */
    private static ImapFolder createIMAPFolder(Folder folder){

        String fullName = folder.getFullName();
        String delimiter;
        ImapFolder iFolder = null;
        
        try {
            System.out.println("Creating folder: " + fullName + " for user: ");
            delimiter = String.valueOf(folder.getSeparator());
            iFolder = (ImapFolder)new ImapFolderImpl(fullName);
            iFolder.setDelimiter(delimiter);
            if("[Gmail]".equals(folder.getFullName()))
                return iFolder;
            iFolder.setMessageCount(folder.getMessageCount());
            iFolder.setSubscribed(folder.isSubscribed());
            iFolder.setUnseenMessageCount(folder.getUnreadMessageCount());
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
        
        return iFolder;
    }
}
