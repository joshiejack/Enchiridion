package joshie.enchiridion;

import joshie.enchiridion.lib.EInfo;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ELogger {
    private static final Logger logger = LogManager.getLogger(EInfo.MODNAME);

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}