package com.wen.tools.pool.entry;

import java.sql.Connection;

public class DataSource {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DataSource(Connection connection) {
        this.connection = connection;
    }
}
