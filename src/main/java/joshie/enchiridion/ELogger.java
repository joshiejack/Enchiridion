package joshie.enchiridion;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ELogger {
    private static final Logger logger = LogManager.getLogger("Enchiridion");

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
