/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.hupa.server.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Flags.Flag;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.hupa.server.DemoModeSMTPTransport;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.shared.data.MessageAttachment;
import org.apache.hupa.shared.data.SMTPMessage;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.SendMessage;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * Handle sending of email messages
 * 
 */
public abstract class AbstractSendMessageHandler<A extends SendMessage> extends AbstractSessionHandler<A,GenericResult> {

	private final FileItemRegistry registry;
	private final Properties props = new Properties();
	private final boolean auth;
	private final String address;
	private final int port;
	private boolean useSSL = false;

	@Inject
	public AbstractSendMessageHandler(Log logger, FileItemRegistry registry,IMAPStoreCache store, Provider<HttpSession> provider, @Named("SMTPServerAddress") String address, @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL) {
		super(store,logger,provider);
		this.registry = registry;
		this.auth = auth;
		this.address = address;
		this.port = port;
		this.useSSL  = useSSL;
		props.put("mail.smtp.auth", auth);
	}


	/**
	 * Create basic Message which contains all headers. No body is filled
	 * 
	 * @param session the Session
	 * @param action the action
	 * @return message
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws ActionException
	 */
	protected abstract Message createMessage(Session session, A action) throws AddressException, MessagingException,ActionException;
	
	/**
	 * Fill the body of the given message with data which the given action contain
	 * 
	 * @param message the message
	 * @param action the action
	 * @return filledMessage
	 * @throws MessagingException
	 * @throws ActionException
	 */
	protected Message fillBody(Message message, A action) throws MessagingException, ActionException {

		SMTPMessage m = action.getMessage();
		ArrayList<MessageAttachment> attachments = m.getMessageAttachments();
		// check if there are any attachments to include
		if (attachments == null || attachments.isEmpty()) {
			message.setText(m.getText());
		} else {
			// create the message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// fill message
			messageBodyPart.setText(m.getText());

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			multipart = handleAttachments(multipart, attachments);
			
			// Put parts in message
			message.setContent(multipart);

		}
		// save message 
		message.saveChanges();
		return message;
	}

	protected void resetAttachments(A action) throws MessagingException, ActionException {
		SMTPMessage m = action.getMessage();
		ArrayList<MessageAttachment> attachments = m.getMessageAttachments();
		if (attachments != null && ! attachments.isEmpty()) {
			for(MessageAttachment attach : attachments) 
				registry.remove(attach.getName());
		}
	}
	
	/**
	 * Construct the multipart for the given attachments and return it
	 * 
	 * @param multipart 
	 * @param attachments
	 * @return multipart
	 * @throws MessagingException
	 */
	protected Multipart handleAttachments(Multipart multipart, ArrayList<MessageAttachment> attachments) throws MessagingException {
		if (attachments != null) {
			for(MessageAttachment attachment: attachments) {
				// get the attachment from the registry
				FileItem fItem = registry.get(attachment.getName());
				if (fItem == null)
					continue;
				
				// Part two is attachment
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				DataSource source = new FileItemDataStore(fItem);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(source.getName());
				multipart.addBodyPart(messageBodyPart);
			}
		}
		return multipart;
	}

	protected void sendMessage(User user, Session session, Message message) throws MessagingException {
		Transport transport;
	
		if (InMemoryIMAPStoreCache.DEMO_MODE.equals(address)) {
			transport = new DemoModeSMTPTransport(session);
		} else if (useSSL) {
			transport = session.getTransport("smtps");
		} else {
			transport = session.getTransport("smtp");
		}

		if (auth) {
			logger.debug("Use auth for smtp connection");
			transport.connect(address,port,user.getName(), user.getPassword());
		} else {
			transport.connect(address, port, null,null);
		}
		
		logger.info("Send message from " + message.getFrom()[0].toString()+ " to " + message.getRecipients(RecipientType.TO).toString());
		transport.sendMessage(message, message.getAllRecipients());
	}

	protected void saveSentMessage(User user, Message message) throws MessagingException {
	    IMAPStore iStore = cache.get(user);
	    IMAPFolder folder = (IMAPFolder) iStore.getFolder(user.getSettings().getSentFolderName());
	    
	    boolean exists = false;
	    if (folder.exists() == false) {
	    	exists = folder.create(IMAPFolder.READ_WRITE);
	    } else {
	    	exists = true;
	    }
	    if (exists) {
	    	if (folder.isOpen() == false) {
	    		folder.open(Folder.READ_WRITE);
	    	}
	    	message.setFlag(Flag.SEEN, true);
	    	folder.appendMessages(new Message[] {message});
	    	
	    	try {
	    		folder.close(false);
	    	} catch (MessagingException e) {
	    		// we don't care on close
	    	}

	    }
    }
	
	

	/**
	 * DataStore which wrap a FileItem
	 * 
	 */
	private class FileItemDataStore implements DataSource {

		private FileItem item;

		public FileItemDataStore(FileItem item) {
			this.item = item;
		}

		/*
		 * (non-Javadoc)
		 * @see javax.activation.DataSource#getContentType()
		 */
		public String getContentType() {
			return item.getContentType();
		}

		/*
		 * (non-Javadoc)
		 * @see javax.activation.DataSource#getInputStream()
		 */
		public InputStream getInputStream() throws IOException {
			return item.getInputStream();
		}

		/*
		 * (non-Javadoc)
		 * @see javax.activation.DataSource#getName()
		 */
		public String getName() {
			String fullName = item.getName();
			
			// Strip path from file
			int index = fullName.lastIndexOf(File.separator);
			if (index == -1) {
				return fullName;
			} else {
				return fullName.substring(index +1 ,fullName.length());
			}
		}

		/*
		 * (non-Javadoc)
		 * @see javax.activation.DataSource#getOutputStream()
		 */
		public OutputStream getOutputStream() throws IOException {
			return null;
		}

	}



	@Override
	protected GenericResult executeInternal(A action, ExecutionContext context)
			throws ActionException {
		GenericResult result = new GenericResult();
		try {
			Session session = Session.getDefaultInstance(props);

			Message message = createMessage(session, action);
			message = fillBody(message,action);

			sendMessage(getUser(),session, message);
			saveSentMessage(getUser(), message);
			resetAttachments(action);
		
			// TODO: notify the user more accurately where is the error
			// if the message was sent and the storage in the sent folder failed, etc.
		} catch (AddressException e) {
			result.setError("Error while parsing recipient: " + e.getMessage());
			logger.error("Error while parsing recipient", e);
		} catch (AuthenticationFailedException e) {
			result.setError("Error while sending message: SMTP Authentication error.");
			logger.error("SMTP Authentication error", e);
		} catch (MessagingException e) {
			result.setError("Error while sending message: " + e.getMessage());
			logger.error("Error while sending message", e);
		} catch (Exception e) {
			result.setError("Unexpected exception while sendig message: " + e.getMessage());
			logger.error("Unexpected exception while sendig message: ", e);
		}
		return result;
	}

	/**
	 * Get a Address array for the given ArrayList 
	 * 
	 * @param recipients
	 * @return addressArray
	 * @throws AddressException
	 */
	protected Address[] getRecipients(ArrayList<String> recipients) throws AddressException {
		if (recipients == null) {
			return new InternetAddress[]{};
		}
		Address[] array = new Address[recipients.size()];
		for (int i = 0; i < recipients.size(); i++) {
			array[i] = new InternetAddress(recipients.get(i));
		}
		return array;

	}

}
