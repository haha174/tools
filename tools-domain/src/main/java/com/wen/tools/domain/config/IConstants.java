package com.wen.tools.domain.config;

public interface IConstants {
    interface JDBC {
        String JDBC_DRIVER = "jdbc.driver";
        String JDBC_URL = "jdbc.url";
        String JDBC_USER = "jdbc.username";
        String JDBC_PASSWORD = "jdbc.password";
        String JDBC_DATASOURCE_SIZE = "jdbc.datasource.size";
        String JDBC_DATASOURCE_MAX_SIZE = "jdbc.datasource.max.size";
        String JDBC_DATASOURCE_RETEY_TIME = "jdbc.datasource.retry.time";
    }

    interface CONFIG {
        String   CONFIG_PROPERTIES="pool.properties";
    }

    interface PATTERN {
        String PATTERN_CHECK_NUMBER = "^\\s*[0-9]+\\s*$";
    }

}
