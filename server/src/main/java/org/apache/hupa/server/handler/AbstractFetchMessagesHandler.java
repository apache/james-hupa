package org.apache.hupa.server.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Tag;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.data.Message.IMAPFlag;
import org.apache.hupa.shared.rpc.FetchMessagesResult;
import org.apache.hupa.shared.rpc.FetchMessages;

import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

public abstract class AbstractFetchMessagesHandler <A extends FetchMessages> extends AbstractSessionHandler<A, FetchMessagesResult>{

	public AbstractFetchMessagesHandler(IMAPStoreCache cache, Log logger,
			Provider<HttpSession> sessionProvider) {
		super(cache, logger, sessionProvider);
	}

	@Override
	protected FetchMessagesResult executeInternal(A action,
			ExecutionContext context) throws ActionException {
		User user = getUser(action.getSessionId());
		IMAPFolder folder = action.getFolder();
		try {
			IMAPStore store = cache.get(user);
			int start = action.getStart();
			int offset = action.getOffset();
			
			com.sun.mail.imap.IMAPFolder f =  (com.sun.mail.imap.IMAPFolder)store.getFolder(folder.getFullName());

			// check if the folder is open, if not open it read only
			 if (f.isOpen() == false) {
	             f.open(com.sun.mail.imap.IMAPFolder.READ_ONLY);
	         }

			int exists = f.getMessageCount();
			
			// if the folder is empty we have no need to process 
			if (exists == 0) {
				return new FetchMessagesResult(new ArrayList<org.apache.hupa.shared.data.Message>(),start,offset,exists);
			}		
			
			Message[] messages = getMessagesToConvert(f,action);
			
			return new FetchMessagesResult(convert(action, f, messages),start,offset,exists);
			
		} catch (Exception e) {
			logger.error("Error while fetching headers for user " + user.getName() + " in folder " + folder,e);
			throw new ActionException(
					"Error while fetching headers for user " + user.getName() + " in folder " + folder);
		
		}
	}
	
	protected abstract Message[] getMessagesToConvert(com.sun.mail.imap.IMAPFolder f, A action) throws MessagingException;
	
	protected ArrayList<org.apache.hupa.shared.data.Message> convert(FetchMessages action, com.sun.mail.imap.IMAPFolder f, Message[] messages) throws MessagingException {
		ArrayList<org.apache.hupa.shared.data.Message> mList = new ArrayList<org.apache.hupa.shared.data.Message>();
		// Setup fetchprofile to limit the stuff which is fetched 
		FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(FetchProfile.Item.FLAGS);
        fp.add(FetchProfile.Item.CONTENT_INFO);
        f.fetch(messages, fp);

        // loop over the fetched messages
		for (int i = 0; i < messages.length; i++) {
			org.apache.hupa.shared.data.Message msg = new org.apache.hupa.shared.data.Message();
			Message m = messages[i];				
			String from = null;
			if (m.getFrom() != null && m.getFrom().length >0 ) {
				try {
					from = MimeUtility.decodeText(m.getFrom()[0].toString().trim());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			msg.setFrom(from);
			
			ArrayList<String> to = new ArrayList<String>();
			// Add to addresses
			Address[] toArray = m.getRecipients(RecipientType.TO);
			if (toArray != null) {
				for (int b =0; b < toArray.length;b++) {
					to.add(toArray[b].toString());
				}
			}
			msg.setTo(to);
			
			// Check if a subject exist and if so decode it
			String subject = m.getSubject();
			if (subject != null) {
				try {
					subject = MimeUtility.decodeText(subject);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			msg.setSubject(subject);
			
			// Add cc addresses
			Address[] ccArray = m.getRecipients(RecipientType.CC);
			ArrayList<String> cc = new ArrayList<String>();

			if (ccArray != null) {
				for (int b =0; b < ccArray.length;b++) {
					cc.add(ccArray[b].toString());
				}
			}
			msg.setCc(cc);

			msg.setReceivedDate(m.getReceivedDate());

			// Add flags
			ArrayList<IMAPFlag> iFlags = new ArrayList<IMAPFlag>();
			Flags flags = m.getFlags();
			Flag[] systemFlags = flags.getSystemFlags();
			for (int a = 0; a < systemFlags.length;a++) {
				Flag flag = systemFlags[a];
				if (flag == Flag.DELETED) {
					iFlags.add(IMAPFlag.DELETED);
				}
				if (flag == Flag.SEEN) {
					iFlags.add(IMAPFlag.SEEN);
				}
				if (flag == Flag.RECENT) {
					iFlags.add(IMAPFlag.RECENT);

				}
			}
		  
			ArrayList<Tag> tags = new ArrayList<Tag>();
			String[] userFlags = flags.getUserFlags();
			for (int a = 0; a < userFlags.length;a++) {
				String flag = userFlags[a];
				if (flag.startsWith(Tag.PREFIX)) {
					tags.add(new Tag(flag.substring(Tag.PREFIX.length())));
				}
			}
			
			msg.setUid(f.getUID(m));
			msg.setFlags(iFlags);
			msg.setTags(tags);
			msg.setHasAttachments(hasAttachment(m));
			
			mList.add(0, msg);
			if (i > action.getOffset()) {
				break;
			}
		}
		f.close(false);
		return mList;
	}
	
	private boolean hasAttachment(Message message) throws MessagingException {
		if (message.getContentType().startsWith("multipart/")) {
			try {
				Object content;

				content = message.getContent();

				if (content instanceof Multipart) {
					Multipart mp = (Multipart) content;
					if (mp.getCount() > 1) {
						for (int i = 0; i < mp.getCount(); i++) {
							String disp = mp.getBodyPart(i).getDisposition();
							if (disp != null
									&& disp.equalsIgnoreCase(Part.ATTACHMENT)) {
								return true;
							}
						}
					}

				}
			} catch (IOException e) {
				logger.error("Error while get content of message " + message.getMessageNumber());
			}
			
		}
		return false;
	}
}
