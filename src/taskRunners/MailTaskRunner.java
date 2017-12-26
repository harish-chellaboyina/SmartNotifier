package taskRunners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Store;

import org.json.JSONObject;

import DTO.MailInfoDTO;
import appContext.ApplicationContext;
import constants.Constants;
import interfaces.TaskRunner;
import logger.Logger;
import mailUtility.Mailer;
import workerThread.WorkerThread;

public class MailTaskRunner implements TaskRunner {

	private static void invokeCommand(MailInfoDTO mailInfo, JSONObject commandInfo) {
		if (commandInfo != null) {
			if (!commandInfo.has(Constants.PATH_KEY)) {
				Logger.debug("Path key not found for " + mailInfo.getCommand() + " in command config");
				return;
			}

			if (!commandInfo.has(Constants.COMMAND_KEY)) {
				Logger.debug("Command key not found for " + mailInfo.getCommand() + " in command config");
				return;
			}
			String path = commandInfo.getString(Constants.PATH_KEY);
			String command = commandInfo.getString(Constants.COMMAND_KEY);

			WorkerThread wt = new WorkerThread(path, command.split(" "), mailInfo);
			ApplicationContext.getInstance().getExecutor().execute(wt);
			
			
		}
	}
	
	private static List<MailInfoDTO> createMailInfoDTOFromMessages(List<Message> mails) {
		List<MailInfoDTO> list = new ArrayList<MailInfoDTO>();
		for (int i = 0; i < mails.size(); i++) {
			Message mail = mails.get(i);

			try {
				Object multipart = mail.getContent();
				BodyPart bp = ((Multipart) multipart).getBodyPart(0);
				String mailContent = (String) bp.getContent();
				list.add(new MailInfoDTO(mail.getFrom()[0].toString(), mailContent.trim()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;

	}

	private static List<MailInfoDTO> getCommandsFromMail(Date lastVerifiedDate) {
		Store store = null;
		Folder inbox = null;
		try {
			store = Mailer.authenticate(Constants.GMAIL_USERNAME, Constants.GMAIL_PASSWORD);
			inbox = store.getFolder(Constants.GMAIL_INBOX_FOLDER);
			Message[] mails = Mailer.getMailAfterDate(lastVerifiedDate, store, inbox);
			List<Message> filteredMails = Mailer.filterMailsBySubject(mails, Constants.SUBJECT_PREFIC_KEY + "_"
					+ ApplicationContext.getInstance().getNotificationConfigObj().getUniqueNotificationListenerKey());
			return createMailInfoDTOFromMessages(filteredMails);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (store.isConnected())
					store.close();
				if (inbox.isOpen())
					inbox.close(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void execute() {
		try {

			Date lastVerifiedDate = ApplicationContext.getInstance().getLastVerifiedDate();
			List<MailInfoDTO> mailInfoDTOList = getCommandsFromMail(lastVerifiedDate);

			if (mailInfoDTOList.size() == 0) {
				Logger.debug("No triggers found after - " + lastVerifiedDate);
				return;
			}

			if (mailInfoDTOList.size() > 1) {
				Logger.debug("More than one triggers received. Executing only first one");
			}

			// TODO: Has to execute all commands sequentially

			JSONObject commandInfo = ApplicationContext.getInstance().getNotificationConfigObj()
					.getCommandConfigHolderObj().getCommandInfoForKey(mailInfoDTOList.get(0).getCommand());

			invokeCommand(mailInfoDTOList.get(0), commandInfo);

			ApplicationContext.getInstance().setLastVerifiedDate(new Date());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
