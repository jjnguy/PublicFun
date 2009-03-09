package edu.cs319.server;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import edu.cs319.util.Util;

/**
 * 
 * @author The Squirrels
 */
public class ServerLog {
	public static final String LOG_FILE = "log.txt";

	private static final Level DEBUG_LEVEL = Level.ALL;

	/** All server code can use this for logging */
	public static final Logger log = Logger.getLogger("server");

	static {
		try {
			File f = new File(LOG_FILE);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}

			FileHandler fileHandler = new FileHandler(LOG_FILE, true);
			fileHandler.setFormatter(new SimpleFormatter());
			log.addHandler(fileHandler);
			fileHandler.setLevel(Level.WARNING);
			log.setLevel(Level.FINEST);

			// Root logger->Console handler->set level
			if (Util.DEBUG) log.getParent().getHandlers()[0].setLevel(DEBUG_LEVEL);
		} catch (Exception e) {
			// :(
			System.err.println("Error occured while creating logger, exiting");
			if (Util.DEBUG) e.printStackTrace();
			System.exit(1);
		}
	}
}
