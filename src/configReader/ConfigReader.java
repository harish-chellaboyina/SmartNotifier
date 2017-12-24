package configReader;

import java.io.IOException;
import org.json.JSONObject;

import commandConfig.CommandConfigHolder;
import utils.Utils;

public class ConfigReader {

	public CommandConfigHolder parseConfig(String configFilePath) {

		String configContent;
		try {
			configContent = Utils.readFile(configFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		JSONObject commandConfigJSON = new JSONObject(configContent);

		CommandConfigHolder cch = new CommandConfigHolder();
		cch.setCommandConfig(commandConfigJSON);
		return cch;
	}

}
