package commandConfig;

import org.json.JSONObject;

import logger.Logger;

public class CommandConfigHolder {

	private JSONObject commandConfig = null;

	public JSONObject getCommandConfig() {
		return commandConfig;
	}

	public void setCommandConfig(JSONObject commandConfig) {
		this.commandConfig = commandConfig;
	}

	public JSONObject getCommandInfoForKey(String key) {

		if (commandConfig == null) {
			Logger.debug("Command config not found");
			return null;
		}

		if (commandConfig.has(key))
			return commandConfig.getJSONObject((key));
		else {
			Logger.debug("Cannot find command for the key - " + key);
		}
		return null;
	}

}
