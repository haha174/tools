package com.wen.tools.pool.domain;

public interface ToolsPoolIConstants {
    interface CONFIG{
        String JDBC_POOL_CONFIG_GILE = "pool.properties";
    }
    interface JDBC {
        String JDBC_DRIVER = "jdbc.driver";
        String AUTO_COMMIT = "jdbc.auto_commit";
        String JDBC_URL = "jdbc.url";
        String JDBC_USER = "jdbc.username";
        String JDBC_PASSWORD = "jdbc.password";
        String JDBC_DATASOURCE_SIZE = "jdbc.datasource.size";
        String JDBC_DATASOURCE_MAX_SIZE = "jdbc.datasource.max.size";
        String JDBC_DATASOURCE_GET_CONNECTION_MAX_RETEY_TIMES = "jdbc.datasource.get.connection.max.retry.times";
        String JDBC_DATASOURCE_GET_CONNECTION_RETEY_SLEEP_TIMES = "jdbc.datasource.get.connection.retry.sleep.time";
    }
    interface DYNAMIC_SQL{
        // ? mean ${table_name}
        String GET_COLUMNS_SQL="SELECT column_name FROM information_schema.columns WHERE table_name=?";
        // ? mean ${table_name}
        String GET_PRIMARY_KEY_SQL="SELECT column_name FROM INFORMATION_SCHEMA.`KEY_COLUMN_USAGE` WHERE table_name=? AND constraint_name='PRIMARY'";
        // ? mean ${table_name}
        String SELECT_TABLE_ALL_SQL="SELECT * FROM ?";
    }

    interface PATTERN {
        String PATTERN_CHECK_NUMBER = "^\\s*[0-9]+\\s*$";
    }

}
