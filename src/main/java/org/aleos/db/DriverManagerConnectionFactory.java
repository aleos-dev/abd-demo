package org.aleos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerConnectionFactory extends ConnectionFactory {

    private final String jdbcUrl;

    public DriverManagerConnectionFactory(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }
}
