package com.jared.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class LogUtils {

    private LogUtils(){}

    /**
     * Returns a {@link Logger} object with the name of the calling class set automatically.
     *
     * @return {@link Logger} object.
     */
    public static Logger getLoggerForClass() {
        StackTraceElement callingClass = Thread.currentThread().getStackTrace()[2]; // third stack trace element is name of calling class
        return LogManager.getLogger(callingClass.getClassName());
    }
    
    public static void setConsoleLoggingLevel(String level) {
        setLoggingLevel("Console", level);
    }

    /**
     * Set the logging level for a specific Logger, used to specify logging levels at run time.
     *
     * @param loggerName   String name of Logger whose logging level is being set.
     * @param loggingLevel String logging level to set the Logger to.
     */
    private static void setLoggingLevel(String loggerName, String loggingLevel) {
        Level levelObject = Level.valueOf(loggingLevel); //convert String to Log4j Level object

        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Configuration loggerConfig = loggerContext.getConfiguration(); //get log4j logging configurations

        LoggerConfig consoleLoggerConfig = loggerConfig.getLoggerConfig(loggerName); //get log4j logging configurations for specific Logger
        consoleLoggerConfig.setLevel(levelObject);
        loggerContext.updateLoggers();

        System.out.println("Logging level: " + levelObject.toString());
    }
}
