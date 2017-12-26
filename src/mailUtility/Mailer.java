package mailUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import logger.Logger;

public class Mailer {

	public static Store authenticate(String username, String password) {
		Properties props = new Properties();

		props.setProperty("mail.store.protocol", "imaps");
		Store store = null;
		try {
			Session session = Session.getInstance(props, null);
			store = session.getStore();
			store.connect("imap.gmail.com", username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return store;
	}

	public static Message[] getAllMails(Store store, Folder folder) throws MessagingException {
		folder.open(Folder.READ_ONLY);
		return folder.getMessages();
	}

	public static Message[] getMailAfterDate(Date lastDate, Store store, Folder folder) {
		try {
			Message[] mails = getAllMails(store, folder);

			int i;
			for (i = mails.length - 1; i >= 0; i--) {
				Message mail = mails[i];
				if (mail.getReceivedDate().before(lastDate)) {
					break;
				}
			}

			if (i == mails.length - 1) {
				Logger.debug("No recent mails found after " + lastDate);
				final Message[] NO_FILES = {};
				return NO_FILES;

			}
			return Arrays.copyOfRange(mails, i + 1, mails.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Message> filterMailsBySubject(Message[] mails, String subject) {

		List<Message> filteredMails = new ArrayList<Message>();

		for (int i = 0; i < mails.length; i++) {
			Message mail = mails[i];

			try {
				Object multipart = mail.getContent();

				if (!(multipart instanceof String)) {
					if (mail.getSubject().equals(subject)) {
						filteredMails.add(mail);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return filteredMails;
	}
	
	public static synchronized boolean sendMail(String username, String password, String to, String subject, String body) {
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);


		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return false;
		
	}
	

}
