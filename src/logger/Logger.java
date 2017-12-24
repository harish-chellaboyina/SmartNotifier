package logger;

import java.util.Date;

public class Logger {

	public static void debug(String message) {
		Date d = new Date();
		System.out.println("[" + d + "] DEBUG: " + message);
	}

	public static void info(String message) {
		Date d = new Date();
		System.out.println("[" + d + "] INFO: " + message);
	}

}
