package appContext;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DTO.NotificationConfigDTO;

public class ApplicationContext {

	private ApplicationContext() {

	}

	private NotificationConfigDTO notificationConfigObj;
	private Date lastVerifiedDate;
	ExecutorService executor;

	private static ApplicationContext instanceObj = null;

	public void initialiseThreadPool(int threadCount) {
		executor = Executors.newFixedThreadPool(threadCount);
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public Date getLastVerifiedDate() {
		return lastVerifiedDate;
	}

	public void setLastVerifiedDate(Date lastVerifiedDate) {
		this.lastVerifiedDate = lastVerifiedDate;
	}

	public static ApplicationContext getInstance() {
		if (instanceObj == null)
			instanceObj = new ApplicationContext();
		return instanceObj;
	}

	public NotificationConfigDTO getNotificationConfigObj() {
		return notificationConfigObj;
	}

	public void setNotificationConfigObj(NotificationConfigDTO notificationConfigObj) {
		this.notificationConfigObj = notificationConfigObj;
	}

}
