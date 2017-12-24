package commandUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import logger.Logger;

public class CommandExecutor {

	public static boolean executeCommand(String path, String[] command) {
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.directory(new File(path));
			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
				Logger.debug(line);
			}
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

}
