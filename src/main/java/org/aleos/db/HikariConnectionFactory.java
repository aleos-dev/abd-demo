package org.aleos.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionFactory extends ConnectionFactory {

    private final HikariDataSource dataSource;

    public HikariConnectionFactory(String filePath) {
        HikariConfig hikariConfig = new HikariConfig(filePath);
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
