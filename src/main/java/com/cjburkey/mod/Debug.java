package com.cjburkey.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Debug {
    
    private static final Logger logger = LogManager.getLogger("MoD");
    
    public static void info(Object msg) {
        logger.info(sanitize(msg));
    }
    
    public static void info(Object msg, Object... args) {
        logger.info(sanitize(msg), args);
    }
    
    public static void warn(Object msg) {
        logger.warn(sanitize(msg));
    }
    
    public static void warn(Object msg, Object... args) {
        logger.warn(sanitize(msg), args);
    }
    
    public static void error(Object msg) {
        logger.error(sanitize(msg));
    }
    
    public static void error(Object msg, Object... args) {
        logger.error(sanitize(msg), args);
    }
    
    public static void exception(Throwable error) {
        error("An exception occurred!");
        error("A full report follows");
        while (error != null) {
            printStackTrace(error);
            error = error.getCause();
        }
    }
    
    private static void printStackTrace(Throwable error) {
        error("  {}", error.getMessage());
        for (StackTraceElement e : error.getStackTrace()) {
            error("    {}",  e);
        }
    }
    
    private static String sanitize(Object input) {
        String out = (input == null) ? "null" : input.toString();
        return (out == null) ? "null" : out;
    }
    
}