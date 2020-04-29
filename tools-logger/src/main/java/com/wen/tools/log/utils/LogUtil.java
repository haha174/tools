package com.wen.tools.log.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class LogUtil {
    private static Logger coreLog;

    static {
        init();
    }

    public static void init() {
        coreLog = LogManager.getLogger("com.wen.tools.log");
    }

    public static Logger getCoreLog(Class T) {
        return LogManager.getLogger(T);
    }

    public static Logger getCoreLog() {
        return coreLog;
    }

    public static void modifyLevel(Logger logger, Level level) {
        LoggerContext context = (LoggerContext)LogManager.getContext(false);
        LoggerConfig conf = context.getConfiguration().getLoggerConfig(logger.getName());
        conf.setLevel(level);
        context.updateLoggers();
    }
}
