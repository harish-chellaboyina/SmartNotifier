package DTO;

import commandConfig.CommandConfigHolder;

public class NotificationConfigDTO {

	private int interval;
	private String uniqueNotificationListenerKey;
	private CommandConfigHolder commandConfigHolderObj;

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getUniqueNotificationListenerKey() {
		return uniqueNotificationListenerKey;
	}

	public void setUniqueNotificationListenerKey(String uniqueNotificationListenerKey) {
		this.uniqueNotificationListenerKey = uniqueNotificationListenerKey;
	}

	public CommandConfigHolder getCommandConfigHolderObj() {
		return commandConfigHolderObj;
	}

	public void setCommandConfigHolderObj(CommandConfigHolder commandConfigHolderObj) {
		this.commandConfigHolderObj = commandConfigHolderObj;
	}

}
