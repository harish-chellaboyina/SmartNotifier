package mailUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

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

	public static List<String> filterMails(Message[] mails, String subject) {

		List<String> commandList = new ArrayList<String>();

		for (int i = 0; i < mails.length; i++) {
			Message mail = mails[i];

			try {
				Object multipart = mail.getContent();

				if (!(multipart instanceof String)) {
					if (mail.getSubject().equals(subject)) {
						BodyPart bp = ((Multipart) multipart).getBodyPart(0);
						String mailContent = (String) bp.getContent();
						commandList.add(mailContent.trim());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return commandList;
	}

}
