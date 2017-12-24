package appContext;

import java.util.Date;

import notificationConfig.NotificationConfigDTO;

public class ApplicationContext {

	private ApplicationContext() {

	}

	private NotificationConfigDTO notificationConfigObj;
	private Date lastVerifiedDate;

	private static ApplicationContext instanceObj = null;

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
