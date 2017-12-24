package taskRunners;

import java.util.Date;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;

import org.json.JSONObject;

import appContext.ApplicationContext;
import commandUtility.CommandExecutor;
import constants.Constants;
import interfaces.TaskRunner;
import logger.Logger;
import mailUtility.Mailer;

public class MailTaskRunner implements TaskRunner {

	private static void invokeCommand(String commandName, JSONObject commandInfo) {
		if (commandInfo != null) {
			if (!commandInfo.has(Constants.PATH_KEY)) {
				Logger.debug("Path key not found for " + commandName + " in command config");
				return;
			}

			if (!commandInfo.has(Constants.COMMAND_KEY)) {
				Logger.debug("Command key not found for " + commandName + " in command config");
				return;
			}
			String path = commandInfo.getString(Constants.PATH_KEY);
			String command = commandInfo.getString(Constants.COMMAND_KEY);

			boolean status = CommandExecutor.executeCommand(path, command.split(" "));

			if (status == true) {
				Logger.debug("Successfully executed the command - " + command);
			} else {
				Logger.debug("Execution of command failed - " + command);
			}
		}
	}

	private static List<String> getCommandsFromMail(Date lastVerifiedDate) {
		Store store = null;
		Folder inbox = null;
		try {
			store = Mailer.authenticate(Constants.GMAIL_USERNAME, Constants.GMAIL_PASSWORD);
			inbox = store.getFolder(Constants.GMAIL_INBOX_FOLDER);
			Message[] mails = Mailer.getMailAfterDate(lastVerifiedDate, store, inbox);
			return Mailer.filterMails(mails, Constants.SUBJECT_PREFIC_KEY + "_"
					+ ApplicationContext.getInstance().getNotificationConfigObj().getUniqueNotificationListenerKey());
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
			List<String> commands = getCommandsFromMail(lastVerifiedDate);

			if (commands.size() == 0) {
				Logger.debug("No triggers found after - " + lastVerifiedDate);
				return;
			}

			if (commands.size() > 1) {
				Logger.debug("More than one triggers received. Executing only first one");
			}

			// TODO: Has to execute all commands sequentially

			JSONObject commandInfo = ApplicationContext.getInstance().getNotificationConfigObj()
					.getCommandConfigHolderObj().getCommandInfoForKey(commands.get(0));

			invokeCommand(commands.get(0), commandInfo);

			ApplicationContext.getInstance().setLastVerifiedDate(new Date());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
