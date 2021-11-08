package seedu.kolinux.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** Represents the logger for this application to log user activity. */
public class KolinuxLogger {

    private static final String LOGGER_FILE_PATH = "./data/logger.log";
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Initializes the global logger for Kolinux by starting the ConsoleHandler and FileHandler.
     * The ConsoleHandler only logs messages that are at the SEVERE level while the FileHandler
     * logs messages at ALL levels to logger.log. Any class can log relevant messages if it
     * has a logger attribute with the global name.
     */
    public void initLogger() {

        LogManager.getLogManager().reset();
        logger.setLevel(Level.INFO);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.SEVERE);
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler(LOGGER_FILE_PATH);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "File logger encountered errors.", exception);
        }

        logger.log(Level.INFO, "User started Kolinux");
    }
}
