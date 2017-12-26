package workerThread;

import java.util.List;

import DTO.MailInfoDTO;
import commandUtility.CommandExecutor;
import constants.Constants;
import mailUtility.Mailer;

public class WorkerThread implements Runnable {

	private final String[] command;
	private final String path;
	private final MailInfoDTO mailInfo;

	public WorkerThread(String path, String[] command, MailInfoDTO mailInfo) {
		this.path = path;
		this.command = command;
		this.mailInfo = mailInfo;
	}

	public String[] getCommand() {
		return command;
	}

	@Override
	public void run() {
		CommandExecutor ce = new CommandExecutor();
		List<String> list = ce.executeCommand(path, command);
		String logs = String.join("\n", list);
		Mailer.sendMail(Constants.GMAIL_USERNAME, Constants.GMAIL_PASSWORD, mailInfo.getFrom(), Constants.COMMAND_UPDATE_MAIL_SUBJECT, logs);
		System.out.println(logs);
	}

}
