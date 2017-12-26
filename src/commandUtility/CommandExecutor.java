package commandUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.*;

import logger.Logger;

public class CommandExecutor {

	public List<String> executeCommand(String path, String[] command) {
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.directory(new File(path));
			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			LinkedList<String> list = new LinkedList<String>();

			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
				if (list.size() == 10)
					list.removeFirst();
				list.addLast(line);
			}
			Logger.debug("Successfully executed the command - " + String.join(" ", command) + " at path - " + path);
		    return new ArrayList<String>(list);


		} catch (IOException e) {
			Logger.debug("Failed to execute the command - " + String.join(" ", command) + " at path - " + path);
			e.printStackTrace();
			return null;
		}

	}

}
