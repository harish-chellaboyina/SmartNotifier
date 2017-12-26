package starter;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

import DTO.NotificationConfigDTO;
import appContext.ApplicationContext;
import commandConfig.CommandConfigHolder;
import configReader.ConfigReader;
import constants.Constants;
import logger.Logger;
import looper.Looper;
import taskRunners.MailTaskRunner;

public class Starter {
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		String uniqueKey = null, commandConfigPath = null;
		int interval = 0;

		if (args.length == 0) {
			Scanner scanner = new Scanner(System.in);

			System.out.print("Enter unique key for notifier to listen on : ");
			uniqueKey = scanner.nextLine().trim();

			System.out.print("Enter path for command config file : ");
			commandConfigPath = scanner.nextLine().trim();

			if (!new File(commandConfigPath).isFile()) {
				System.out.println("Please enter proper file path and run again");
				return;
			}

			System.out.print("Enter listener interval time (in minutes) : ");
			interval = scanner.nextInt();

		}

		ConfigReader cr = new ConfigReader();
		CommandConfigHolder commandConfigHolderObj = cr.parseConfig(commandConfigPath);

		NotificationConfigDTO notificationConfig = new NotificationConfigDTO();

		notificationConfig.setCommandConfigHolderObj(commandConfigHolderObj);
		notificationConfig.setInterval(interval);
		notificationConfig.setUniqueNotificationListenerKey(uniqueKey);

		ApplicationContext.getInstance().setNotificationConfigObj(notificationConfig);

		ApplicationContext.getInstance().setLastVerifiedDate(new Date());
		ApplicationContext.getInstance().initialiseThreadPool(Constants.THREAD_POOL_COUNT);

		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		Looper lp = new Looper();
		lp.startLoop(interval, new MailTaskRunner());

		Logger.debug("Listener started !!!");

	}
}
